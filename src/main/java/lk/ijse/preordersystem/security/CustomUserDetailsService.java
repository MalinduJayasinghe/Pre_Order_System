package lk.ijse.preordersystem.security;

import lk.ijse.preordersystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<lk.ijse.preordersystem.entity.User> optionalUser = userRepository.findByUserName(username);

        if(optionalUser.isEmpty())
            throw new RuntimeException("Sorry no user");


        return User.builder()
                .username(optionalUser.get().getUserName())
                .password(optionalUser.get().getPassword())
                .roles(optionalUser.get().getUserRoles())
                .build();
    }

}
