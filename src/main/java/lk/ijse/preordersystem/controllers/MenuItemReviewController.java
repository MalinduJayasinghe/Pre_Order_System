package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.MenuItemReviewDTO;
import lk.ijse.preordersystem.service.MenuItemReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/review")
@RequiredArgsConstructor
@Slf4j
public class MenuItemReviewController {

    private final MenuItemReviewService menuItemReviewService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addReview(@RequestBody MenuItemReviewDTO menuItemReviewDTO) {

        log.info("addReview API was called");
        MenuItemReviewDTO savedReview = menuItemReviewService.addReview(menuItemReviewDTO);

        log.info("addReview API successful");
        return new CommonResponse(0, savedReview, "Review added successfully");
    }

    @GetMapping(value = "/item/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getReviewsForItem(@PathVariable long itemId) {

        log.info("getReviewsForItem API was called");
        List<MenuItemReviewDTO> reviews = menuItemReviewService.getReviewsForItem(itemId);

        log.info("getReviewsForItem API successful");
        return new CommonResponse(0, reviews, "Reviews called");
    }
}
