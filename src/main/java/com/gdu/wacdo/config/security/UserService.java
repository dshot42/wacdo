package com.gdu.wacdo.config.security;

import com.gdu.wacdo.model.Employee;
import com.gdu.wacdo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private EmployeeRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Employee user = userRepository.findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isAdmin()) {
            throw new UsernameNotFoundException("User is not an admin");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .password(user.getPassword()) // hashed password
                .roles("ADMIN")       // Spring adds ROLE_ prefix automatically
                .build();
    }
}