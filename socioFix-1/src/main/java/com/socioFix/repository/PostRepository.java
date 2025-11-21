package com.socioFix.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.socioFix.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

	
	   @Query("SELECT p FROM Post p WHERE (p.sector.sectorId IN (:sectors) AND ( p.location.areaId IN (:areas) OR p.location.taluka IN (:talukas))) ORDER BY p.upvotes DESC")
	   Page<Post> getByLocationAndSectorMostPopular(@Param("sectors") List<String> sectors,
               @Param("talukas") List<String> talukas, @Param("areas") List<String> areas,Pageable pageable );
	   
	   @Query("SELECT p FROM Post p WHERE (p.sector.sectorId IN (:sectors) AND ( p.location.areaId IN (:areas) OR p.location.taluka IN (:talukas))) ORDER BY p.createdAt DESC")
	   Page<Post> getByLocationAndSectorMostRecent(@Param("sectors") List<String> sectors,
               @Param("talukas") List<String> talukas, @Param("areas") List<String> areas,Pageable pageable );

	   @Query("SELECT p FROM Post p WHERE p.sector.sectorId IN (:sectors) ORDER BY p.createdAt DESC")
	   Page<Post> getBySectorMostRecent(@Param("sectors") List<String> sectors,Pageable pageable);

	   @Query("SELECT p FROM Post p WHERE p.sector.sectorId IN (:sectors) ORDER BY p.upvotes DESC")
	   Page<Post> getBySectorMostPopular(@Param("sectors") List<String> sectors,Pageable pageable);

	   @Query("SELECT p FROM Post p WHERE  p.location.areaId IN (:areas) OR p.location.taluka IN (:talukas) ORDER BY p.createdAt DESC")
	   Page<Post> getByLocationMostRecent( @Param("talukas") List<String> talukas, @Param("areas") List<String> areas,Pageable pageable);

	   @Query("SELECT p FROM Post p WHERE  p.location.areaId IN (:areas) OR p.location.taluka IN (:talukas)  ORDER BY p.upvotes DESC")
	   Page<Post> getByLocationMostPopular(@Param("talukas") List<String> talukas, @Param("areas") List<String> areas,Pageable pageable);

	   @Query("SELECT p FROM Post p WHERE  p.user.userId =:user_id  ORDER BY p.createdAt DESC")
	   Page<Post> getUserPosts(@Param("user_id") String user_id, Pageable pageable);
	   
//	   @Query("SELECT p FROM Post p WHERE  p.user.userId =:user_id AND (SELECT u from User u WHERE u.userId =:user_id AND u IN (:p.savedPostUsers) )  ORDER BY p.createdAt DESC")
//	   Page<Post> getUserUpvotedPosts(@Param("user_id") String user_id, Pageable pageable);
	  
	   @Query("SELECT p FROM Post p JOIN p.upvotedPostUsers u WHERE u.id = :user_id")
	    Page<Post> findAllByUpvotedUser(@Param("user_id") String userId, Pageable pageable);
	   
	   @Query("SELECT p FROM Post p JOIN p.savedPostUsers u WHERE u.id = :user_id")
	    Page<Post> findAllBySavedUser(@Param("user_id") String userId, Pageable pageable);
	   
	   @Query("SELECT p FROM Post p WHERE  p.organization.userId =:user_id AND p.status =:status ORDER BY p.createdAt DESC")
	   Page<Post> getUserAcceptedPosts(@Param("user_id") String user_id, @Param("status") String status, Pageable pageable);
	   
	   @Query("SELECT p FROM Post p WHERE p.organization.userId =:user_id AND p.status =:status ORDER BY p.createdAt DESC")
	   Page<Post> getUserSolvedPosts(@Param("user_id") String user_id, @Param("status") String status, Pageable pageable);
	   

	   @Query("SELECT p FROM Post p ORDER BY p.upvotes DESC")  
	   Page<Post> getByDefaultMostPopular(Pageable pageable);
	   
	   
	   @Query("SELECT COUNT(p) FROM Post p WHERE p.organization.userId = :user_id AND p.status = :status")
	   Long countUserPostsbyStatus(@Param("user_id") String user_id, @Param("status") String status);

	
	   Optional<Post> findTopByUserUserIdOrderByCreatedAtDesc(String userId);
	   
	   @Query("SELECT p.organization.userId as organizationId, p.organization.name as name, COUNT(p.postId) as count " +
               "FROM Post p " +
               "WHERE p.status = 'Solved' " +
               "GROUP BY p.organization.userId, p.organization.name " +
               "ORDER BY count DESC LIMIT 10")
	   List<Object[]> findTop10OrganizationsWithSolvedPosts();
	   
	   @Query("SELECT p.user.userId as userId, p.user.name as name, COUNT(p.postId) as count FROM Post p WHERE p.status IN ('Accepted', 'Solved') AND p.createdAt >= :oneMonthAgo GROUP BY p.user.userId, p.user.name ORDER BY count DESC LIMIT 10")
	    List<Object[]> findTopTenUsersWithAcceptedOrSolvedPostsAfterDate(@Param("oneMonthAgo") LocalDateTime oneMonthAgo);

	
}
