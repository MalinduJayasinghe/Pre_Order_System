package lk.ijse.preordersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDTO {

    private long discountId;
    private String code;
    private double percentage;
    private boolean active;
}
