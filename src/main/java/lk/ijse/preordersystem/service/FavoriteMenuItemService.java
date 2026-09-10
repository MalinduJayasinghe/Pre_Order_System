package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.dto.FavoriteMenuItemDTO;

import java.util.List;

public interface FavoriteMenuItemService {

    FavoriteMenuItemDTO addFavorite(long userId, long itemId);
    void removeFavorite(long userId, long itemId);
    List<FavoriteMenuItemDTO> getFavoritesForUser(long userId);
}
