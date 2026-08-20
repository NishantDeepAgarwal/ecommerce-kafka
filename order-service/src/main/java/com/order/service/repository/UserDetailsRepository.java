package com.order.service.repository;

import com.order.service.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByUsername(String username);

//    Optional<Users> findByUserName(String username);

//    boolean existsByUsername(String username);

//    boolean existsByEmail(String email);
}
