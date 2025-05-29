package com.phenix.jirama.Repository;

import com.phenix.jirama.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.rmi.server.UID;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, UID> {
    Optional<User> findByUsername(String username);
}
