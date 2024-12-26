package gestion.proyectos.gestionproyectos.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    // Cache para almacenar los intentos por usuario (email)
    private final LoadingCache<String, Integer> attemptsCache;
    // Número máximo de intentos permitidos
    private static final int MAX_ATTEMPTS = 5;

    public LoginAttemptService() {
        // Configuración del cache
        attemptsCache = CacheBuilder.newBuilder()
                // El cache expira después de 30 minutos de inactividad
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        // Valor inicial para nuevas entradas
                        return 0;
                    }
                });
    }

    // Se llama cuando el login es exitoso
    public void loginSucceeded(String email) {
        // Elimina los intentos fallidos cuando el login es exitoso
        attemptsCache.invalidate(email);
    }

    // Se llama cuando el login falla
    public void loginFailed(String email) {
        int attempts;
        try {
            // Incrementa el contador de intentos fallidos
            attempts = attemptsCache.get(email) + 1;
        } catch (ExecutionException e) {
            attempts = 1;
        }
        attemptsCache.put(email, attempts);
    }

    // Verifica si una cuenta está bloqueada
    public boolean isBlocked(String email) {
        try {
            // Retorna true si los intentos superan el máximo permitido
            return attemptsCache.get(email) >= MAX_ATTEMPTS;
        } catch (ExecutionException e) {
            return false;
        }
    }

    public int getAttempts(String email) {
        try {
            return attemptsCache.get(email);
        } catch (ExecutionException e) {
            return 0;
        }
    }

    // Método para obtener intentos restantes
    public int getRemainingAttempts(String email) {
        return MAX_ATTEMPTS - getAttempts(email);
    }
}