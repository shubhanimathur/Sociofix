package com.socioFix.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.socioFix.model.User;

public interface UserRepository extends JpaRepository<User,String>{

	 @Query("SELECT u.userId FROM User u WHERE u.userId = :userId")
	    String findUserTypeById(@Param("userId") String userId);
	 
}
