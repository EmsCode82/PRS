package com.prsweb.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prsweb.business.User;

public interface UserRepository extends JpaRepository<User, Integer> {	
	Optional<User> findByUserNameAndPassword(String userName, String password);
}
