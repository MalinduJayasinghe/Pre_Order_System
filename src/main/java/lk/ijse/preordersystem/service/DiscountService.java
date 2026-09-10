package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.dto.DiscountDTO;

import java.util.List;

public interface DiscountService {

    List<DiscountDTO> getAllDiscounts();
    DiscountDTO saveDiscount(DiscountDTO discountDTO);
    void deleteDiscount(Long discountId);
    DiscountDTO validateDiscountCode(String code);
}
