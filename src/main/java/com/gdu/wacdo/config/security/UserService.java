package com.gdu.wacdo.config.security;

import com.gdu.wacdo.model.Employee;
import com.gdu.wacdo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private EmployeeRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

        if (mail.equals("superAdmin") )
            return org.springframework.security.core.userdetails.User.builder()
                    .username("superAdmin")
                    .password(new BCryptPasswordEncoder().encode("123")) // hashed password
                    .roles("ADMIN")
                    .build();

        Employee user = userRepository.findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isAdmin()) {
            throw new UsernameNotFoundException("User is not an admin");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles("ADMIN")
                .build();
    }


}