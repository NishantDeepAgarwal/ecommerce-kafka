package com.order.service.security;

import com.order.service.dto.Role;
import com.order.service.entity.Users;
import com.order.service.repository.UserDetailsRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    public CustomUserDetailsService(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user=userDetailsRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found- " +username));

        Role role = user.getRole();

        Set<GrantedAuthority> authorities = new java.util.HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        role.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .forEach(authorities::add);
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
