package com.alenut.mpi.config;

import com.alenut.mpi.auxiliary.MD5Encryption;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MPIAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.getByEmail(email);
        try {
            if (user != null && MD5Encryption.computeMD5(password).equals(user.getPassword())) {
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
                return new UsernamePasswordAuthenticationToken(email, password, grantedAuths);
            } else {
                return null;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
