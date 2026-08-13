package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.AuthDTO;
import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.UserDTO;
import lk.ijse.preordersystem.dto.UserDataDTO;
import lk.ijse.preordersystem.security.JwtUtil;
import lk.ijse.preordersystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/login")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse authLogin(@RequestBody AuthDTO authDTO){

        UserDTO userDetails = userService.getUserDetails(authDTO.getUserName(), authDTO.getPassword(), authDTO.getUserRoles());
        log.info("authLogin API was called");
        String token = jwtUtil.generateToken(userDetails);

        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setUserId(userDetails.getUserId());
        userDataDTO.setToken(token);
        userDataDTO.setUserRoles(userDetails.getUserRoles());

        log.info("authLogin API successful");
        return new CommonResponse(0, userDataDTO, "JWT Token");
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveCustomer(@RequestBody UserDTO userDTO){

        log.info("saveCustomer API was called");
        userService.saveUser(userDTO);

        log.info("saveCustomer API successful");
        return new CommonResponse(0, userDTO, "Customer saved successfully");
    }

}
