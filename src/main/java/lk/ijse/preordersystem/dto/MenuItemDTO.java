package lk.ijse.preordersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDTO {

    private Long itemId;
    private String name;
    private String category;
    private double price;
    private boolean available;
    private String imageFileName;
}