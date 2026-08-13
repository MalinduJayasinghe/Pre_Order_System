package lk.ijse.preordersystem.dto;

import lk.ijse.preordersystem.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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
    private Set<Ingredient> ingredients;
}
