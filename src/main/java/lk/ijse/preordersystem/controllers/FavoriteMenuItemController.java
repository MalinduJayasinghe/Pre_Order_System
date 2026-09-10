package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.FavoriteMenuItemDTO;
import lk.ijse.preordersystem.service.FavoriteMenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/favorite")
@RequiredArgsConstructor
@Slf4j
public class FavoriteMenuItemController {

    private final FavoriteMenuItemService favoriteMenuItemService;

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getFavoritesForUser(@PathVariable long userId) {

        log.info("getFavoritesForUser API was called");
        List<FavoriteMenuItemDTO> favorites = favoriteMenuItemService.getFavoritesForUser(userId);

        log.info("getFavoritesForUser API successful");
        return new CommonResponse(0, favorites, "Favorites called");
    }

    @PostMapping(value = "/user/{userId}/item/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addFavorite(@PathVariable long userId, @PathVariable long itemId) {

        log.info("addFavorite API was called");
        FavoriteMenuItemDTO favorite = favoriteMenuItemService.addFavorite(userId, itemId);

        log.info("addFavorite API successful");
        return new CommonResponse(0, favorite, "Favorite added successfully");
    }

    @DeleteMapping(value = "/user/{userId}/item/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse removeFavorite(@PathVariable long userId, @PathVariable long itemId) {

        log.info("removeFavorite API was called");
        favoriteMenuItemService.removeFavorite(userId, itemId);

        log.info("removeFavorite API successful");
        return new CommonResponse(0, "Favorite Removed", "Favorite removed successfully");
    }
}
