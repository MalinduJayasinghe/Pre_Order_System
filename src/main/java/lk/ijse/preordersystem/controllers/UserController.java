package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.UserDTO;
import lk.ijse.preordersystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllUsers() {

        log.info("getAllUsers API was called");
        List<UserDTO> allUsers = userService.getAllUsers();

        log.info("getAllUsers API successful");
        return new CommonResponse(0, allUsers, "Users called");
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addUser(@RequestBody UserDTO userDTO) {

        log.info("addUser API was called");
        userService.saveUser(userDTO);

        log.info("addUser API successful");
        return new CommonResponse(0, "Account Added", "Account added successfully");
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateUser(@RequestBody UserDTO userDTO) {

        log.info("updateUser API was called");
        userService.updateUser(userDTO);

        log.info("updateUser API successful");
        return new CommonResponse(0, "Account Updated", "Account updated successfully");
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteUser(@PathVariable Long userId) {

        log.info("deleteUser API was called");
        userService.deleteUser(userId);

        log.info("deleteUser API successful");
        return new CommonResponse(0, "Account Deleted", "Account deleted successfully");
    }
}