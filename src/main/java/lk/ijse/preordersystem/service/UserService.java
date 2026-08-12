package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.dto.UserDTO;

import java.util.List;

public interface UserService {


    UserDTO getUserDetails(String username, String password);

    void saveUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    List<UserDTO> searchUserByUsername(String username);

    void deleteUser(Long userId);

    void updateUser(UserDTO userDTO);
}
