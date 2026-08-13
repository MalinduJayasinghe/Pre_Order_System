package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.MenuItemDTO;
import lk.ijse.preordersystem.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "v1/menu")
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    private final MenuService menuService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllMenuItems(@RequestBody MenuItemDTO menuItemDTO) {

        log.info("getAllMenuItems API was called");
        List<MenuItemDTO> allMenuItems = menuService.getAllMenuItems(menuItemDTO);

        log.info("getAllMenuItems API successful");
        return new CommonResponse(0, allMenuItems, "MenuItems called");
    }

    @GetMapping(value = "/filter",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterMenuItems(@RequestParam(required = false) List<String> excludedIngredients) {

        log.info("filterMenuItems API was called");
        List<MenuItemDTO> allMenuItems = menuService.getMenuItemsExcludingIngredients(excludedIngredients);

        log.info("filterMenuItems API successful");
        return new CommonResponse(0, allMenuItems, "Excluded MenuItems filtered");
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addMenuItem(@RequestBody MenuItemDTO menuItemDTO) {

        log.info("addMenuItem API was called");
        menuService.saveMenuItem(menuItemDTO);

        log.info("addMenuItem API successful");
        return new CommonResponse(0, "Menu Item Added", "Item added successfully");
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateMenuItem(@RequestBody MenuItemDTO menuItemDTO) {

        log.info("updateMenuItem API was called");
        menuService.updateMenuItem(menuItemDTO);

        log.info("updateMenuItem API successful");
        return new CommonResponse(0, "Menu Item Updated", "Item updated successfully");
    }

    @DeleteMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteMenuItem(@PathVariable Long itemId) {

        log.info("deleteMenuItem API was called");
        menuService.deleteMenuItem(itemId);

        log.info("deleteMenuItem API successful");
        return new CommonResponse(0, "Menu Item Deleted", "Item deleted successfully");
    }

    @PostMapping(value = "/{itemId}/image",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addMenuItemImage(@PathVariable Long itemId, @RequestParam("file") MultipartFile file){

        log.info("addMenuItemImage API was called");
        try {
            String fileName = menuService.saveMenuItemImage(itemId, file);
            log.info("addMenuItemImage API successful");
            return new CommonResponse(0, fileName, "Image added successfully");

        }catch (Exception e){

            log.error("addMenuItemImage API failed" + e.getMessage());
            return new CommonResponse(1, e.getMessage(), "Image upload failed");
        }
    }
}
