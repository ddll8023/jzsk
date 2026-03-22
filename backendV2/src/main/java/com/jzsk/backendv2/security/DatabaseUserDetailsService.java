package com.jzsk.backendv2.security;

import com.jzsk.backendv2.mapper.auth.LegacyAuthMapper;
import com.jzsk.backendv2.pojo.entity.auth.LegacyAuthUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

    private final LegacyAuthMapper legacyAuthMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LegacyAuthUserEntity user = legacyAuthMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Bad credentials");
        }

        List<String> authorityCodes = new ArrayList<>();
        authorityCodes.addAll(legacyAuthMapper.selectRoleCodesByUserId(user.getId()));
        authorityCodes.addAll(legacyAuthMapper.selectAuthorityCodesByUserId(user.getId()));

        List<SimpleGrantedAuthority> authorities = authorityCodes.stream()
                .filter(StringUtils::hasText)
                .distinct()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String displayName = StringUtils.hasText(user.getName()) ? user.getName() : user.getUsername();

        return new LoginUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                displayName,
                true,
                authorities
        );
    }
}
