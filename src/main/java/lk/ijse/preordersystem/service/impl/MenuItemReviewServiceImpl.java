package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.MenuItemReviewDTO;
import lk.ijse.preordersystem.entity.MenuItem;
import lk.ijse.preordersystem.entity.MenuItemReview;
import lk.ijse.preordersystem.entity.User;
import lk.ijse.preordersystem.repository.MenuItemRepository;
import lk.ijse.preordersystem.repository.MenuItemReviewRepository;
import lk.ijse.preordersystem.repository.UserRepository;
import lk.ijse.preordersystem.service.MenuItemReviewService;
import lk.ijse.preordersystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuItemReviewServiceImpl implements MenuItemReviewService {

    private static final int LOW_RATING_THRESHOLD = 2;

    private final MenuItemReviewRepository menuItemReviewRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public MenuItemReviewDTO addReview(MenuItemReviewDTO menuItemReviewDTO) {

        log.info("Execute method addReview");

        try {

            if (menuItemReviewDTO.getComment() == null || menuItemReviewDTO.getComment().trim().isEmpty()) {
                throw new RuntimeException("A comment is required to leave a review");
            }

            MenuItem menuItem = menuItemRepository.findById(menuItemReviewDTO.getItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            User user = userRepository.findById(menuItemReviewDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            MenuItemReview review = new MenuItemReview();
            review.setMenuItem(menuItem);
            review.setUser(user);
            review.setRating(menuItemReviewDTO.getRating());
            review.setComment(menuItemReviewDTO.getComment());
            review.setReviewedAt(LocalDateTime.now());

            MenuItemReview savedReview = menuItemReviewRepository.save(review);

            if (savedReview.getRating() <= LOW_RATING_THRESHOLD) {
                notifyAdminsOfLowRating(savedReview);
            }

            log.info("Review saved successfully");
            return mapToDto(savedReview);

        }catch (Exception e){
            log.error("Error in method addReview" + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<MenuItemReviewDTO> getReviewsForItem(long itemId) {

        log.info("Execute method getReviewsForItem");

        try {

            List<MenuItemReviewDTO> responseList = new ArrayList<>();
            List<MenuItemReview> reviewList = menuItemReviewRepository.findByMenuItem_ItemId(itemId);

            for (MenuItemReview review : reviewList) {
                responseList.add(mapToDto(review));
            }

            log.info("Reviews retrieved successfully");
            return responseList;

        }catch (Exception e){
            log.error("Error in method getReviewsForItem" + e.getMessage());
            throw e;
        }
    }

    private void notifyAdminsOfLowRating(MenuItemReview review) {

        List<User> admins = userRepository.findByRole_RoleName("ADMIN");

        String message = "Low rating (" + review.getRating() + "\u2605) on " + review.getMenuItem().getName()
                + " from " + review.getUser().getUserName()
                + (review.getComment() != null && !review.getComment().isEmpty() ? ": \"" + review.getComment() + "\"" : "");

        for (User admin : admins) {
            notificationService.createNotification(admin.getUserId(), message);
        }
    }

    private MenuItemReviewDTO mapToDto(MenuItemReview review) {

        MenuItemReviewDTO dto = new MenuItemReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setItemId(review.getMenuItem().getItemId());
        dto.setUserId(review.getUser().getUserId());
        dto.setUsername(review.getUser().getUserName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setReviewedAt(review.getReviewedAt());

        return dto;
    }
}
