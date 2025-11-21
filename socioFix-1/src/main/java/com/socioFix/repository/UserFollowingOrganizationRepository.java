package com.socioFix.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.socioFix.PK.UserFollowingOrganizationPK;
import com.socioFix.model.Drive;
import com.socioFix.model.Organization;
import com.socioFix.model.User;
import com.socioFix.model.UserFollowingOrganization;

public interface UserFollowingOrganizationRepository extends JpaRepository<UserFollowingOrganization,UserFollowingOrganizationPK> 
{

	 @Query("SELECT ufo.organization FROM UserFollowingOrganization ufo WHERE ufo.user.userId = :userId")
	 Page<Organization> findFollowingOrganizationsByUserId(@Param("userId") String userId,Pageable pageable);

	
	
	 @Query("SELECT ufo.user FROM UserFollowingOrganization ufo WHERE ufo.organization.userId = :userId")
	 Page<User> findFollowingUsersByOrganizationId(@Param("userId") String userId,Pageable pageable);
	 
	 
	 @Query("SELECT d FROM Drive d WHERE d.organization.userId IN "
		       + "(SELECT ufo.organization.userId FROM UserFollowingOrganization ufo WHERE ufo.user.id = :userId) "
		       + "ORDER BY d.createdAt DESC")
		Page<Drive> getDrivesForFollowedOrganizations(@Param("userId") String userId, Pageable pageable);
	
}
