package lk.ijse.preordersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDTO {

    private Long discountId;
    private String code;
    private double percentage;
    private boolean active;
    private LocalDate discountDate;
    private List<Long> applicableItemIds;
    private List<String> applicableItemNames;
}