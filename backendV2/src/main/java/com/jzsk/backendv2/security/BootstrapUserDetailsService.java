package com.jzsk.backendv2.security;

import com.jzsk.backendv2.config.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BootstrapUserDetailsService implements UserDetailsService {

    private final SecurityProperties securityProperties;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityProperties.BootstrapUser bootstrapUser = securityProperties.getBootstrapUser();
        if (!bootstrapUser.getUsername().equals(username)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<SimpleGrantedAuthority> authorities = bootstrapUser.getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new LoginUser(
                bootstrapUser.getUserId(),
                bootstrapUser.getUsername(),
                passwordEncoder.encode(bootstrapUser.getPassword()),
                bootstrapUser.getDisplayName(),
                authorities
        );
    }
}
