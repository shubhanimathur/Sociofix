package com.socioFix.repository;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.socioFix.model.Drive;

public interface DriveRepository extends JpaRepository<Drive, Integer> {
	
	@Query("SELECT d FROM Drive d WHERE (d.sector.sectorId IN (:sectors) AND ( d.location.areaId IN (:areas) OR d.location.taluka IN (:talukas))) ORDER BY d.upvotes DESC")
	Page<Drive> getByLocationAndSectorMostPopular(@Param("sectors") List<String> sectors,
			@Param("talukas") List<String> talukas, @Param("areas") List<String> areas,Pageable pageable );
	
	@Query("SELECT d FROM Drive d WHERE (d.sector.sectorId IN (:sectors) AND ( d.location.areaId IN (:areas) OR d.location.taluka IN (:talukas))) ORDER BY d.createdAt DESC")
	Page<Drive> getByLocationAndSectorMostRecent(@Param("sectors") List<String> sectors,
			@Param("talukas") List<String> talukas, @Param("areas") List<String> areas,Pageable pageable );
	
	@Query("SELECT d FROM Drive d WHERE d.sector.sectorId IN (:sectors) ORDER BY d.createdAt DESC")
	Page<Drive> getBySectorMostRecent(@Param("sectors") List<String> sectors,Pageable pageable);
	
	
	@Query("SELECT d FROM Drive d WHERE d.sector.sectorId IN (:sectors) ORDER BY d.upvotes DESC")
	Page<Drive> getBySectorMostPopular(@Param("sectors") List<String> sectors,Pageable pageable);
	
	
	@Query("SELECT d FROM Drive d WHERE d.location.areaId IN (:areas) OR d.location.taluka IN (:talukas) ORDER BY d.createdAt DESC")
	Page<Drive> getByLocationMostRecent( @Param("talukas") List<String> talukas, @Param("areas") List<String> areas,Pageable pageable);
	
	
	@Query("SELECT d FROM Drive d WHERE d.location.areaId IN (:areas) OR d.location.taluka IN (:talukas) ORDER BY d.upvotes DESC")
	Page<Drive> getByLocationMostPopular(@Param("talukas") List<String> talukas, @Param("areas") List<String> areas,Pageable pageable);
	
	
	@Query("SELECT d FROM Drive d WHERE d.organization.userId =:user_id ORDER BY d.createdAt DESC")
	Page<Drive> getUserDrives(@Param("user_id") String user_id, Pageable pageable);
	// @Query("SELECT d FROM Drive d WHERE d.user.userId =:user_id AND (SELECT u from User u WHERE u.userId =:user_id AND u IN (:d.savedDriveUsers) ) ORDER BY d.createdAt DESC")
	// Page<Drive> getUserUpvotedDrives(@Param("user_id") String user_id, Pageable pageable);
	
	
	@Query("SELECT d FROM Drive d JOIN d.upvotedDrivesUsers u  WHERE u.userId = :user_id")
	Page<Drive> findAllByUpvotedUser(@Param("user_id") String userId, Pageable pageable);
	
	
	@Query("SELECT d FROM Drive d JOIN d.savedDrivesUsers u WHERE u.userId = :user_id")
	Page<Drive> findAllBySavedUser(@Param("user_id") String userId, Pageable pageable);
	
	@Query("SELECT d FROM Drive d JOIN d.volunteeredDrivesUsers u WHERE u.userId = :user_id")
	Page<Drive> findAllByVolunteeredUser(@Param("user_id") String userId, Pageable pageable);

	
	@Query("SELECT d FROM Drive d ORDER BY d.upvotes DESC")
	Page<Drive> getByDeafultMostPopular(Pageable pageable);
	
	

}

