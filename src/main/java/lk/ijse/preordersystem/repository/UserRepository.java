package lk.ijse.preordersystem.repository;

import lk.ijse.preordersystem.dto.UserDTO;
import lk.ijse.preordersystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserNameAndPassword(String username, String password);

    Optional<User> findByUserName(String username);

    @Query(value = "SELECT new com.example.spring_security_test.dto.UserDTO(u.userId, u.userName, u.userRoles) FROM User u")
    List<UserDTO> getAllUsers();

    @Query(value = "SELECT new com.example.spring_security_test.dto.UserDTO(u.userId, u.userName, u.userRoles) FROM User u WHERE u.userName LIKE %:username%")
    List<UserDTO> searchByUserName(String username);
}
