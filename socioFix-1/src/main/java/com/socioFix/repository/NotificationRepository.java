package com.socioFix.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.socioFix.model.Post;
import com.socioFix.model.Notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	
	   @Query("SELECT n FROM Notification n WHERE  n.toUser.userId =:user_id ORDER BY n.createdAt DESC")
	   Page<Notification> getNotifications(@Param("user_id") String user_id, Pageable pageable);
	   
	   @Query("SELECT n FROM Notification n WHERE n.toUser.userId = :user_id AND n.notificationType IN ('solved', 'accepted', 'contribute') ORDER BY n.createdAt DESC")
	   Page<Notification> getNotificationsUserHelp(@Param("user_id") String user_id, Pageable pageable);
	   
	   @Query("SELECT n FROM Notification n WHERE n.toUser.userId = :user_id AND n.notificationType NOT IN ('solved', 'accepted', 'contribute','toBeReminded') ORDER BY n.createdAt DESC")
	   Page<Notification> getNotificationsUserActivity(@Param("user_id") String user_id, Pageable pageable);
	   
	   @Query("SELECT n FROM Notification n WHERE n.toUser.userId = :user_id AND n.notificationType IN ('toAccept', 'accepted', 'contribute') ORDER BY n.createdAt DESC")
	   Page<Notification> getNotificationsOrganizationHelp(@Param("user_id") String user_id, Pageable pageable);
	   
	   @Query("SELECT n FROM Notification n WHERE n.toUser.userId = :user_id AND n.notificationType NOT IN ('toAccept', 'accepted', 'contribute','toBeReminded') ORDER BY n.createdAt DESC")
	   Page<Notification> getNotificationsOrganizationActivity(@Param("user_id") String user_id, Pageable pageable);

	   @Query("SELECT n FROM Notification n WHERE n.notificationType = :notificationType")
	   List<Notification> findByNotificationType(@Param("notificationType") String notificationType);
	   
	   
	   
	   
}
