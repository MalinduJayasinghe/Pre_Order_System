package lk.ijse.preordersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDTO {

    private long itemId;
    private String name;
    private String category;
    private double price;
    private boolean available;
    private String imageFileName;
    private List<String> ingredients;
}