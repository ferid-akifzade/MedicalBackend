package org.medical.security.seclib;

import org.medical.libs.OrdinaryUser;
import org.medical.repositories.OrdinaryUserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrdinaryUserDetailsService implements UserDetailsService {

    private final OrdinaryUserRepo userRepo;

    public OrdinaryUserDetailsService(OrdinaryUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public static UserDetails mapper(OrdinaryUser user){
        return new OrdinaryUserDetails(user.getId(),user.getEmail(),user.getPassword(),user.getRoles());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .map(OrdinaryUserDetailsService::mapper)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User email = %s not found",username)));
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        return userRepo.findById(id)
                .map(OrdinaryUserDetailsService::mapper)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User id = %d not found",id)));
    }
}
