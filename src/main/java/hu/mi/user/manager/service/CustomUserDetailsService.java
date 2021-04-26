package hu.mi.user.manager.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import hu.mi.user.properties.entity.User;
import hu.mi.user.properties.repository.UserRepo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    
    @Autowired
    private UserRepo userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> agnosUser = userRepo.findById(username);
        if (agnosUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(agnosUser.get());
    }


}
