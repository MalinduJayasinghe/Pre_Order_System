package lk.ijse.preordersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteMenuItemDTO {

    private long favoriteId;
    private long userId;
    private long itemId;
    private String itemName;
    private double price;
}
