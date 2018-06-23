package com.alenut.mpi.config;

import com.alenut.mpi.entities.User;
import com.alenut.mpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MPIAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String emailOrUsername = authentication.getName();

        String password = authentication.getCredentials().toString();
        User user = null;
        if (userService.getByEmail(emailOrUsername) != null) {
            user = userService.getByEmail(emailOrUsername);
        } else {
            user = userService.getByUsername(emailOrUsername);
        }

        if (user != null && BCrypt.checkpw(password, user.getPassword()) && user.getConfirmed() == 1) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            String role = "";
            switch (user.getRole()) {
                case 1: {
                    role = "USER";
                    break;
                }
                case 0: {
                    role = "ADMIN";
                    break;
                }
            }
            grantedAuths.add(new SimpleGrantedAuthority(role));
            return new UsernamePasswordAuthenticationToken(emailOrUsername, password, grantedAuths);
        } else {
            return null;
        }

//        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
