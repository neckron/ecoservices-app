package com.ecoservices.app.security.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecoservices.app.model.Role;
import com.ecoservices.app.model.User;
import com.ecoservices.app.security.repository.RoleRepository;
import com.ecoservices.app.security.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user) {
        logger.debug("boss: "+user.getBossEmail());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Optional<User> boss = userRepository.findByEmail(user.getBossEmail());
        user.setBoss(boss.get());
        Role userRole = roleRepository.findByRole(user.getCreationRole());
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            List<GrantedAuthority> authorities = getUserAuthority(optionalUser.get().getRoles());
            return buildUserForAuthentication(optionalUser.get(), authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    public Optional<User> getUserByAuthenticationContext(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optionalUser = userRepository.findByEmail(auth.getName());
        if(optionalUser.isPresent()){
            return optionalUser;
        }else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    public Optional<User> findUserByResetToken(String resetToken){
       return userRepository.findByResetToken(resetToken);
    }

   public List<User> findUsersByRole(String role){
        List<User> listUsersByRole = userRepository.findAll();
        return listUsersByRole
                .stream()
                .filter(user -> "ADMIN".equals(user.getCreationRole()))
                .collect(Collectors.toList());
    }

}
