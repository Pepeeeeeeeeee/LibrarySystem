package be.kdg.librarysystem.security;

import be.kdg.librarysystem.service.UserService;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(
            UserService userService
    ) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        var user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new CustomUserDetails(user);
    }
}