package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.DTOs.AuthResponse;
import gestion.proyectos.gestionproyectos.DTOs.LoginRequest;
import gestion.proyectos.gestionproyectos.DTOs.RegisterRequest;
import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import gestion.proyectos.gestionproyectos.security.AuthenticationException;
import gestion.proyectos.gestionproyectos.security.JwtService;
import gestion.proyectos.gestionproyectos.security.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final LoginAttemptService loginAttemptService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        validateRegisterRequest(request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationException("Ya existe una cuenta asociada a este correo electrónico");
        }

        try {
            var user = User.builder()
                    .names(request.getNames())
                    .secondNames(request.getSecondNames())
                    .email(request.getEmail().toLowerCase().trim())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phoneNumber(request.getPhoneNumber())
                    .active(true)
                    .build();

            user = userRepository.save(user);
            return buildAuthResponse(user, null, null);

        } catch (Exception e) {
            throw new AuthenticationException("No se pudo completar el registro. Por favor, inténtalo más tarde");
        }
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        validateLoginRequest(request);

        String email = request.getEmail().toLowerCase().trim();

        if (loginAttemptService.isBlocked(email)) {
            throw new LockedException("Tu cuenta está temporalmente bloqueada por múltiples intentos fallidos. " +
                    "Por favor, espera 30 minutos antes de intentar nuevamente.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("No existe una cuenta asociada a este correo electrónico"));

            if (!user.isEnabled()) {
                throw new DisabledException("Tu cuenta está desactivada. Por favor, contacta con soporte.");
            }

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = refreshTokenService.createRefreshToken(user.getIdUsuario());

            loginAttemptService.loginSucceeded(email);
            return buildAuthResponse(user, jwtToken, refreshToken.getToken());

        } catch (BadCredentialsException e) {
            loginAttemptService.loginFailed(email);
            int remainingAttempts = loginAttemptService.getRemainingAttempts(email);
            String message = remainingAttempts > 0
                    ? String.format("Credenciales incorrectas. Te quedan %d intentos.", remainingAttempts)
                    : "Credenciales incorrectas. Tu cuenta será bloqueada después de este intento.";
            throw new BadCredentialsException(message);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (LockedException | DisabledException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Error al procesar la solicitud. Por favor, inténtalo más tarde.");
        }
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud de registro no puede ser nula");
        }
        if (!StringUtils.hasText(request.getEmail()) || !isValidEmail(request.getEmail())) {
            throw new AuthenticationException("El correo electrónico es inválido");
        }
        if (!StringUtils.hasText(request.getPassword()) || request.getPassword().length() < 8) {
            throw new AuthenticationException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!StringUtils.hasText(request.getNames())) {
            throw new AuthenticationException("El nombre es requerido");
        }
    }

    private void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud de login no puede ser nula");
        }
        if (!StringUtils.hasText(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico es requerido");
        }
        if (!StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }
        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("El formato del correo electrónico no es válido");
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private AuthResponse buildAuthResponse(User user, String jwtToken, String refreshToken) {
        return AuthResponse.builder()
                .userId(user.getIdUsuario())
                .email(user.getEmail())
                .names(user.getNames())
                .secondNames(user.getSecondNames())
                .phoneNumber(user.getPhoneNumber())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}