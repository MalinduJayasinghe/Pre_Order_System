package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.DiscountDTO;
import lk.ijse.preordersystem.service.DiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/discount")
@RequiredArgsConstructor
@Slf4j
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllDiscounts() {

        log.info("getAllDiscounts API was called");
        List<DiscountDTO> allDiscounts = discountService.getAllDiscounts();

        log.info("getAllDiscounts API successful");
        return new CommonResponse(0, allDiscounts, "Discounts called");
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addDiscount(@RequestBody DiscountDTO discountDTO) {

        log.info("addDiscount API was called");
        DiscountDTO savedDiscount = discountService.saveDiscount(discountDTO);

        log.info("addDiscount API successful");
        return new CommonResponse(0, savedDiscount, "Discount added successfully");
    }

    @DeleteMapping(value = "/{discountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteDiscount(@PathVariable Long discountId) {

        log.info("deleteDiscount API was called");
        discountService.deleteDiscount(discountId);

        log.info("deleteDiscount API successful");
        return new CommonResponse(0, "Discount Deleted", "Discount deleted successfully");
    }

    @GetMapping(value = "/validate/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse validateDiscountCode(@PathVariable String code) {

        log.info("validateDiscountCode API was called");
        DiscountDTO discountDTO = discountService.validateDiscountCode(code);

        log.info("validateDiscountCode API successful");
        return new CommonResponse(0, discountDTO, "Discount code is valid");
    }
}
