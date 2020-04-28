package com.prsweb.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prsweb.business.Request;
import com.prsweb.business.User;

public interface RequestRepository extends JpaRepository<Request, Integer> {
	List<Request> findAllByStatusAndUserNot(String status,Optional<User> user);	
}
