package com.alenut.mpi.service.impl;

import com.alenut.mpi.config.MPIAuthenticationProvider;
import com.alenut.mpi.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class AutoLoginService {

    @Autowired
    private MPIAuthenticationProvider provider;

    public void autoLogin(User user, String initialPassword, HttpServletRequest request) {
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
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), initialPassword, grantedAuths);
        authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = provider.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
