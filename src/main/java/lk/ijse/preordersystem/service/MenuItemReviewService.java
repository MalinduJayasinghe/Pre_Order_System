package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.dto.MenuItemReviewDTO;

import java.util.List;

public interface MenuItemReviewService {

    MenuItemReviewDTO addReview(MenuItemReviewDTO menuItemReviewDTO);
    List<MenuItemReviewDTO> getReviewsForItem(long itemId);
}
