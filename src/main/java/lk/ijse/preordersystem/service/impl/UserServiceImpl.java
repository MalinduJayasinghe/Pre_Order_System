package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.UserDTO;
import lk.ijse.preordersystem.entity.User;
import lk.ijse.preordersystem.repository.UserRepository;
import lk.ijse.preordersystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO getUserDetails(String username, String password, String userRoles) {

        log.info("Execute method getUserDetails");

        try {

            Optional<User> optionalUser = userRepository.findByUserNameAndPassword(username,password);

            if(optionalUser.isEmpty()) {
                throw new RuntimeException("Sorry no user");
            }

            User user = optionalUser.get();
            UserDTO responseData = new UserDTO();

            responseData.setUserId(user.getUserId());
            responseData.setUsername(user.getUserName());
            responseData.setUserRoles(user.getUserRoles());
            responseData.setPassword(user.getPassword());

            log.info("UserDetails retrieved successfully");
            return responseData;

        }catch (Exception e){
            log.error("Error in method getUserDetails" + e.getMessage());
            throw e;
        }
    }

    @Override
   public void saveUser(UserDTO userDTO) {

        log.info("Execute method saveUser");

        try {
            User user = new User();
            user.setUserName(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setUserRoles(userDTO.getUserRoles());

            userRepository.save(user);
            log.info("User saved successfully");

        }catch (Exception e){
            log.error("Error in method saveUser" + e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {

        try {
            log.info("Execute method getAllUsers");
            List<UserDTO> allUsers = userRepository.getAllUsers();

            log.info("All users retrieved successfully");
            return allUsers;

        }catch (Exception e){
            log.error("Error in method getAllUsers" + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<UserDTO> searchUserByUsername(String username) {

        try {
            log.info("Execute method searchUserByUsername");
            return userRepository.searchByUserName(username);

        }catch (Exception e){
            log.error("Error in method searchUserByUsername" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteUser(Long userId) {

        log.info("Execute method deleteUser");

        try {
            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);
                log.info("User deleted successfully");
            } else {
                throw new RuntimeException("User not found with ID: " + userId);
            }

        }catch (Exception e){
            log.error("Error in method deleteUser" + e.getMessage());
        }
    }

    @Override
    public void updateUser(UserDTO userDTO) {

        log.info("Execute method updateUser");

        try {
            Optional<User> optionalUser = userRepository.findById(userDTO.getUserId());

            if (optionalUser.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            User user = optionalUser.get();

            user.setUserName(userDTO.getUsername());
            user.setUserRoles(userDTO.getUserRoles());

            userRepository.save(user);
            log.info("User updated successfully");

        }catch (Exception e){
            log.error("Error in method updateUser" + e.getMessage());
        }
    }
}
