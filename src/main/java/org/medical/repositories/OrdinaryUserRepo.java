package org.medical.repositories;

import org.medical.libs.OrdinaryUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdinaryUserRepo extends CrudRepository<OrdinaryUser, Long> {
    Optional<OrdinaryUser> findByEmail(String email);
}
