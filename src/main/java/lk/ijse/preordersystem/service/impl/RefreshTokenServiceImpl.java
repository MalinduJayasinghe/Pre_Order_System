package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.entity.RefreshToken;
import lk.ijse.preordersystem.entity.User;
import lk.ijse.preordersystem.repository.RefreshTokenRepository;
import lk.ijse.preordersystem.repository.UserRepository;
import lk.ijse.preordersystem.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final long REFRESH_TOKEN_VALID_DAYS = 7;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public RefreshToken issueRefreshToken(long userId) {

        log.info("Execute method issueRefreshToken");

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_VALID_DAYS));
            refreshToken.setRevoked(false);

            RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

            log.info("Refresh token issued successfully");
            return savedToken;

        }catch (Exception e){
            log.error("Error in method issueRefreshToken" + e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<RefreshToken> validateRefreshToken(String token) {

        log.info("Execute method validateRefreshToken");

        try {

            Optional<RefreshToken> optionalToken = refreshTokenRepository.findByToken(token);

            if (optionalToken.isEmpty()) {
                return Optional.empty();
            }

            RefreshToken refreshToken = optionalToken.get();

            if (refreshToken.isRevoked()) {
                return Optional.empty();
            }

            if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                return Optional.empty();
            }

            if (!refreshToken.getUser().isEnabled()) {
                return Optional.empty();
            }

            return Optional.of(refreshToken);

        }catch (Exception e){
            log.error("Error in method validateRefreshToken" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void revokeRefreshToken(String token) {

        log.info("Execute method revokeRefreshToken");

        try {

            Optional<RefreshToken> optionalToken = refreshTokenRepository.findByToken(token);
            if (optionalToken.isPresent()) {
                RefreshToken refreshToken = optionalToken.get();
                refreshToken.setRevoked(true);
                refreshTokenRepository.save(refreshToken);
            }

            log.info("Refresh token revoked successfully");

        }catch (Exception e){
            log.error("Error in method revokeRefreshToken" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void revokeAllForUser(long userId) {

        log.info("Execute method revokeAllForUser");

        try {

            List<RefreshToken> tokens = refreshTokenRepository.findByUser_UserId(userId);

            for (RefreshToken refreshToken : tokens) {
                refreshToken.setRevoked(true);
                refreshTokenRepository.save(refreshToken);
            }

            log.info("All refresh tokens revoked for user");

        }catch (Exception e){
            log.error("Error in method revokeAllForUser" + e.getMessage());
            throw e;
        }
    }
}
