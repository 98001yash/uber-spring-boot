package com.company.project.uber.uperApp.services;

import com.company.project.uber.uperApp.entities.User;
import com.company.project.uber.uperApp.exceptions.ResourceNotFoundException;
import com.company.project.uber.uperApp.repositories.UserRepositories;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public final class UserService implements UserDetailsService {

    private final UserRepositories userRepositories;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepositories.findByEmail(username).orElse(null);
    }

    public User getUserById(Long id) {
        return userRepositories.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
    }
}