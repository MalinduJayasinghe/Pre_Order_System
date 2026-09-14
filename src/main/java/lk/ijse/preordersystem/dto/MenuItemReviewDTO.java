package lk.ijse.preordersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemReviewDTO {

    private Long reviewId;
    private Long itemId;
    private Long userId;
    private String username;
    private int rating;
    private String comment;
    private LocalDateTime reviewedAt;
}
