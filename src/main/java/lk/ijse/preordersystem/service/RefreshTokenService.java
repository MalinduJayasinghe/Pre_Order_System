package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken issueRefreshToken(long userId);
    Optional<RefreshToken> validateRefreshToken(String token);
    void revokeRefreshToken(String token);
    void revokeAllForUser(long userId);
}
