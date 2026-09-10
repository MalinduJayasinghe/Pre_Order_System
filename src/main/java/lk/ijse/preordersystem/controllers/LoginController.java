package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.*;
import lk.ijse.preordersystem.entity.RefreshToken;
import lk.ijse.preordersystem.security.JwtUtil;
import lk.ijse.preordersystem.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse authLogin(@RequestBody AuthDTO authDTO){

        UserDTO userDetails = userService.getUserDetails(authDTO.getUserName(), authDTO.getPassword(), authDTO.getUserRoles());
        log.info("authLogin API was called");
        String token = jwtUtil.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.issueRefreshToken(userDetails.getUserId());

        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setUserId(userDetails.getUserId());
        userDataDTO.setToken(token);
        userDataDTO.setUserRoles(userDetails.getUserRoles());
        userDataDTO.setRefreshToken(refreshToken.getToken());

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

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse refreshAccessToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){

        log.info("refreshAccessToken API was called");

        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(refreshTokenRequestDTO.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token is invalid, expired, or revoked"));

        UserDTO userDTO = new UserDTO(
                refreshToken.getUser().getUserId(),
                refreshToken.getUser().getUserName(),
                refreshToken.getUser().getRole().getRoleName()
        );

        String newAccessToken = jwtUtil.generateToken(userDTO);

        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setUserId(userDTO.getUserId());
        userDataDTO.setToken(newAccessToken);
        userDataDTO.setUserRoles(userDTO.getUserRoles());
        userDataDTO.setRefreshToken(refreshToken.getToken());

        log.info("refreshAccessToken API successful");
        return new CommonResponse(0, userDataDTO, "Access token refreshed");
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse logout(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){

        log.info("logout API was called");
        refreshTokenService.revokeRefreshToken(refreshTokenRequestDTO.getRefreshToken());

        log.info("logout API successful");
        return new CommonResponse(0, "Logged Out", "Refresh token revoked");
    }
}
