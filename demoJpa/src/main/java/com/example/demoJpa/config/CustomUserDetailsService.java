package com.example.demoJpa.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demoJpa.entity.Role;
import com.example.demoJpa.entity.User;
import com.example.demoJpa.repository.RoleRepository;
import com.example.demoJpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
       String name = user.getRole();
        Role userRole = roleRepository.findByName(name);
        if (userRole == null) {
            throw new UsernameNotFoundException("Role not found with name: " + name);
        }
        List<GrantedAuthority> authorities = new ArrayList<>(userRole.getAuthorities());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

}
