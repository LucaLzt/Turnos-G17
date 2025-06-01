package com.oo2.grupo17.services.implementation;

import java.text.MessageFormat;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oo2.grupo17.repositories.IUserRepository;

import lombok.Builder;

@Service("userService") @Builder
public class UserService implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(MessageFormat.format("User with username {0} not found", username))
        );
    }
    
}
