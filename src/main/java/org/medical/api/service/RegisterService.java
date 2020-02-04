package org.medical.api.service;

import org.medical.libs.OrdinaryUser;
import org.medical.repositories.OrdinaryUserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class RegisterService {
    private final OrdinaryUserRepo userRepo;
    private final PasswordEncoder encoder;
    public RegisterService(OrdinaryUserRepo userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public Optional<OrdinaryUser> register(OrdinaryUser user) {
        Optional<OrdinaryUser> byEmail = userRepo.findByEmail(user.getEmail());
        if(!byEmail.isPresent()) return Optional.empty();
        user.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.mm.yyyy")));
        user.setRoles("USER");
        user.setPassword(encoder.encode(user.getPassword()));
        return Optional.of(userRepo.save(user));
    }

}
