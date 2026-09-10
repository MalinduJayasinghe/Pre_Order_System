package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.DiscountDTO;
import lk.ijse.preordersystem.entity.Discount;
import lk.ijse.preordersystem.repository.DiscountRepository;
import lk.ijse.preordersystem.service.DiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Override
    public List<DiscountDTO> getAllDiscounts() {

        log.info("Execute method getAllDiscounts");

        try {

            List<DiscountDTO> responseList = new ArrayList<>();
            List<Discount> discountList = discountRepository.findAll();

            for (Discount discount : discountList) {
                responseList.add(mapToDto(discount));
            }

            log.info("Discounts retrieved successfully");
            return responseList;

        }catch (Exception e){
            log.error("Error in method getAllDiscounts" + e.getMessage());
            throw e;
        }
    }

    @Override
    public DiscountDTO saveDiscount(DiscountDTO discountDTO) {

        log.info("Execute method saveDiscount");

        try {

            Discount discount = new Discount();
            discount.setCode(discountDTO.getCode().trim().toUpperCase());
            discount.setPercentage(discountDTO.getPercentage());
            discount.setActive(discountDTO.isActive());

            Discount savedDiscount = discountRepository.save(discount);

            log.info("Discount saved successfully");
            return mapToDto(savedDiscount);

        }catch (Exception e){
            log.error("Error in method saveDiscount" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteDiscount(Long discountId) {

        log.info("Execute method deleteDiscount");

        try {
            discountRepository.deleteById(discountId);
            log.info("Discount deleted successfully");

        }catch (Exception e){
            log.error("Error in method deleteDiscount" + e.getMessage());
            throw e;
        }
    }

    @Override
    public DiscountDTO validateDiscountCode(String code) {

        log.info("Execute method validateDiscountCode");

        try {

            Optional<Discount> optionalDiscount = discountRepository.findByCodeIgnoreCase(code);

            if (optionalDiscount.isEmpty() || !optionalDiscount.get().isActive()) {
                throw new RuntimeException("Invalid or inactive discount code");
            }

            log.info("Discount code validated successfully");
            return mapToDto(optionalDiscount.get());

        }catch (Exception e){
            log.error("Error in method validateDiscountCode" + e.getMessage());
            throw e;
        }
    }

    private DiscountDTO mapToDto(Discount discount) {

        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountId(discount.getDiscountId());
        discountDTO.setCode(discount.getCode());
        discountDTO.setPercentage(discount.getPercentage());
        discountDTO.setActive(discount.isActive());

        return discountDTO;
    }
}
