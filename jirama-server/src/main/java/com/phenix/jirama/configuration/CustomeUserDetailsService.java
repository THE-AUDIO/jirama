package com.phenix.jirama.configuration;

import com.phenix.jirama.Repository.UserRepository;
import com.phenix.jirama.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class CustomeUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User nowUser = this.userRepository.findByUsername(username).orElseThrow();
        return new org.springframework.security.core.userdetails.User(nowUser.getUsername(), nowUser.getPassword(),getGrantedAuthority(nowUser.getRole()));

    }

    private List<GrantedAuthority> getGrantedAuthority(String role){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
        return authorities;
    }
}
