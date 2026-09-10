package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.FavoriteMenuItemDTO;
import lk.ijse.preordersystem.entity.FavoriteMenuItem;
import lk.ijse.preordersystem.entity.MenuItem;
import lk.ijse.preordersystem.entity.User;
import lk.ijse.preordersystem.repository.FavoriteMenuItemRepository;
import lk.ijse.preordersystem.repository.MenuItemRepository;
import lk.ijse.preordersystem.repository.UserRepository;
import lk.ijse.preordersystem.service.FavoriteMenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteMenuItemServiceImpl implements FavoriteMenuItemService {

    private final FavoriteMenuItemRepository favoriteMenuItemRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public FavoriteMenuItemDTO addFavorite(long userId, long itemId) {

        log.info("Execute method addFavorite");

        try {

            Optional<FavoriteMenuItem> existing = favoriteMenuItemRepository.findByUser_UserIdAndMenuItem_ItemId(userId, itemId);
            if (existing.isPresent()) {
                return mapToDto(existing.get());
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            MenuItem menuItem = menuItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            FavoriteMenuItem favorite = new FavoriteMenuItem();
            favorite.setUser(user);
            favorite.setMenuItem(menuItem);
            favorite.setAddedAt(LocalDateTime.now());

            FavoriteMenuItem savedFavorite = favoriteMenuItemRepository.save(favorite);

            log.info("Favorite added successfully");
            return mapToDto(savedFavorite);

        }catch (Exception e){
            log.error("Error in method addFavorite" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeFavorite(long userId, long itemId) {

        log.info("Execute method removeFavorite");

        try {

            Optional<FavoriteMenuItem> existing = favoriteMenuItemRepository.findByUser_UserIdAndMenuItem_ItemId(userId, itemId);
            if (existing.isPresent()) {
                favoriteMenuItemRepository.delete(existing.get());
            }

            log.info("Favorite removed successfully");

        }catch (Exception e){
            log.error("Error in method removeFavorite" + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<FavoriteMenuItemDTO> getFavoritesForUser(long userId) {

        log.info("Execute method getFavoritesForUser");

        try {

            List<FavoriteMenuItemDTO> responseList = new ArrayList<>();
            List<FavoriteMenuItem> favoriteList = favoriteMenuItemRepository.findByUser_UserId(userId);

            for (FavoriteMenuItem favorite : favoriteList) {
                responseList.add(mapToDto(favorite));
            }

            log.info("Favorites retrieved successfully");
            return responseList;

        }catch (Exception e){
            log.error("Error in method getFavoritesForUser" + e.getMessage());
            throw e;
        }
    }

    private FavoriteMenuItemDTO mapToDto(FavoriteMenuItem favorite) {

        FavoriteMenuItemDTO dto = new FavoriteMenuItemDTO();
        dto.setFavoriteId(favorite.getFavoriteId());
        dto.setUserId(favorite.getUser().getUserId());
        dto.setItemId(favorite.getMenuItem().getItemId());
        dto.setItemName(favorite.getMenuItem().getName());
        dto.setPrice(favorite.getMenuItem().getPrice());

        return dto;
    }
}
