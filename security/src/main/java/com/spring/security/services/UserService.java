package com.spring.security.services;

import com.spring.security.entities.User;
import com.spring.security.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user){
        userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User fetchedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!!!"));
        log.info("USER FOUND with role : {}",fetchedUser.getAuthorities());
        return fetchedUser;
    }
}
