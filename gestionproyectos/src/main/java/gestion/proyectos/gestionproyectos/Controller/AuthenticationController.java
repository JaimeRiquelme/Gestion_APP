package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.DTOs.AuthResponse;
import gestion.proyectos.gestionproyectos.DTOs.LoginRequest;
import gestion.proyectos.gestionproyectos.DTOs.RegisterRequest;
import gestion.proyectos.gestionproyectos.DTOs.TokenRefreshRequest;
import gestion.proyectos.gestionproyectos.Entity.RefreshToken;
import gestion.proyectos.gestionproyectos.Service.AuthenticationService;
import gestion.proyectos.gestionproyectos.Service.RefreshTokenService;
import gestion.proyectos.gestionproyectos.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody TokenRefreshRequest request
    ) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    return ResponseEntity.ok(AuthResponse.builder()
                            .accessToken(token)
                            .refreshToken(requestRefreshToken)
                            .userId(user.getIdUsuario())
                            .email(user.getEmail())
                            .names(user.getNames())
                            .secondNames(user.getSecondNames())
                            .phoneNumber(user.getPhoneNumber())
                            .build());
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(refreshToken)
                .map(token -> {
                    refreshTokenService.deleteByUserId(token.getUser().getIdUsuario());
                    return ResponseEntity.ok("Log out successful!");
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}