package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.DTOs.AuthResponse;
import gestion.proyectos.gestionproyectos.DTOs.LoginRequest;
import gestion.proyectos.gestionproyectos.DTOs.RegisterRequest;
import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import gestion.proyectos.gestionproyectos.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .names(request.getNames())
                .secondNames(request.getSecondNames())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .build();

        user = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getIdUsuario());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .userId(user.getIdUsuario())
                .email(user.getEmail())
                .names(user.getNames())
                .secondNames(user.getSecondNames())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getIdUsuario());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .userId(user.getIdUsuario())
                .email(user.getEmail())
                .names(user.getNames())
                .secondNames(user.getSecondNames())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}