package com.socioFix.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.socioFix.Dto.AreaDto;
import com.socioFix.Dto.FollowingUserDto;
import com.socioFix.Dto.OrganizationDto;
import com.socioFix.Dto.TalukaDto;
import com.socioFix.PK.UserFollowingOrganizationPK;
import com.socioFix.model.Location;
import com.socioFix.model.Organization;
import com.socioFix.model.Sector;
import com.socioFix.model.User;
import com.socioFix.model.UserFollowingOrganization;
import com.socioFix.model.Notification.Notification;
import com.socioFix.model.geoLocations.Area;
import com.socioFix.model.geoLocations.Taluka;
import com.socioFix.repository.AreaRepository;
import com.socioFix.repository.DriveRepository;
import com.socioFix.repository.NotificationRepository;
import com.socioFix.repository.OrganizationRepository;
import com.socioFix.repository.PostRepository;
import com.socioFix.repository.SectorRepository;
import com.socioFix.repository.TalukaRepository;
import com.socioFix.repository.UserFollowingOrganizationRepository;
import com.socioFix.repository.UserRepository;

@Service
public class OrganizationService {

	@Autowired
	OrganizationRepository organizationRepository;
	
	@Autowired
	SectorRepository sectorRepository;
	
	@Autowired
	DriveRepository driveRepository;
	
	@Autowired
	PostRepository postRepository;
	
	@Autowired
	UserRepository userRepository;
	

	@Autowired
	TalukaRepository talukaRepository;
	
	@Autowired
	AreaRepository areaRepository;
	
	@Autowired 
	NotificationRepository notificationRepository;
	
	@Autowired
	UserFollowingOrganizationRepository userFollowingOrganizationRepository;
	
	
	public static ModelMapper modelMapper = new ModelMapper();
	
	
	public OrganizationDto saveOrganization(OrganizationDto organizationDto) {
		
		System.out.println(organizationDto.getSectors());
	Organization organization = modelMapper.map(organizationDto, Organization.class);
	
	System.out.println("after mapper "+ organizationDto.getSectors()); 
	
//	for (Sector sector : organization.getSectors()) {
//		
//		sector= sectorRepository.findById(sector.getSectorId()).orElse(null);
//		//System.out.println(sector.toString());
//		
//	}
//
//	for(Taluka taluka: organization.getServingTalukas()) {
//		
//		taluka= talukaRepository.findById(taluka.getTalukaId()).orElse(null);
//	}
//	
//	for(Area area: organization.getServingAreas()) {
//		
//		area= areaRepository.findById(area.getAreaId()) .orElse(null);
//	}
	
	HashSet<Sector> sectors = new HashSet<>();
	HashSet<Taluka> talukas = new HashSet<>();
	HashSet<Area> areas = new HashSet<>();
	
	
	for (Sector sector : organization.getSectors()) {
		
		sectors.add(sectorRepository.findById(sector.getSectorId()).orElse(null));
	
		
	}

	for(Taluka taluka: organization.getServingTalukas()) {
		
		talukas.add(talukaRepository.findById(taluka.getTalukaId()).orElse(null));
	}
	
	for(AreaDto areaDto: organizationDto.getServingAreas()) {
		
		Area areaNew = areaRepository.findById(areaDto.getAreaId()) .orElse(null);
		
		if(areaNew==null) {
			String areaId = areaDto.getAreaId();
			areaDto.setAreaId(null);
			Area area = modelMapper.map(areaDto, Area.class);
			area.setAreaId(areaId);
			areaNew=areaRepository.save(area);
		}
		areas.add(areaNew);
	}
	organization.setSectors(sectors);
	organization.setServingAreas(areas);
	organization.setServingTalukas(talukas);
	organization.setUserOrOrganization("Organization");
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	organization.setCreatedAt(timestamp);
	organization = organizationRepository.save(organization);
	return modelMapper.map(organization, OrganizationDto.class);
	
	}


	public OrganizationDto getOrganization(String organization_id) {
		
		Organization organization = organizationRepository.findById(organization_id).orElseThrow(() -> new NullPointerException("organization not found"));
		OrganizationDto organizationDto = modelMapper.map(organization, OrganizationDto.class);
		if(organization.getDrives()==null)
			organizationDto.setDriveNo(0);
		else
			organizationDto.setDriveNo(organization.getDrives().size());
		if(organization.getFollowingUsers()==null)
			organizationDto.setFollowingUserNo(0);
		else
			organizationDto.setFollowingUserNo(organization.getFollowingUsers().size());
		

		Long solvedPostsNo = postRepository.countUserPostsbyStatus(organization_id, "Solved");
		Long acceptedPostsNo = postRepository.countUserPostsbyStatus(organization_id, "Accepted");

		organizationDto.setSolvedPostsNo( solvedPostsNo.intValue());
		organizationDto.setAcceptedPostsNo( acceptedPostsNo.intValue());
		for(TalukaDto talukaDto : organizationDto.getServingTalukas()) {
			talukaDto.setTalukaPolygon(null);
		}
		return organizationDto;

	}


	public String organizationFollow(String organization_id, String user_id) {
		
		Organization organization = organizationRepository.findById(organization_id).orElseThrow(() -> new NullPointerException("organization not found"));
		User user = userRepository.findById(user_id).orElseThrow(() -> new NullPointerException("user not found"));
		if(organization.getUserFollowingOrganizations()==null) {
			
			organization.setUserFollowingOrganizations(new HashSet<UserFollowingOrganization>());
			
		}
			
			if(organization.getFollowingUsers().contains(user)) {
				 

				
				UserFollowingOrganizationPK userFollowingOrganizationPK = new UserFollowingOrganizationPK();
				userFollowingOrganizationPK.setUserId(user_id);
				userFollowingOrganizationPK.setOrganizationId(organization_id);
				UserFollowingOrganization userFollowingOrganization = new UserFollowingOrganization();
				userFollowingOrganization.setId(userFollowingOrganizationPK);
				userFollowingOrganization.setOrganization(organization);
				userFollowingOrganization.setUser(user);
				organization.getUserFollowingOrganizations().remove(userFollowingOrganization);
				organization.getFollowingUsers().remove(user);
				user.getFollowingOrganizations().remove(organization);
				 organizationRepository.save(organization);
//				System.out.println("before");
//				for(UserFollowingOrganization usert: organization.getUserFollowingOrganizations()) {
//					
//					System.out.println(usert.getUser().getUserId());
//				}
//				
//				
//				
//				System.out.println("after remove");
//				for(User usert: organization.getFollowingUsers()) {
//					
//					System.out.println(usert.getUserId());
//				}
//				
//				//organizationRepository.save(organization);
//			
//				System.out.println("after save");
//				for(User usert: organization.getFollowingUsers()) {
//					
//					System.out.println(usert.getUserId());
//				}
				return "Unfollowed";
			}
			else {
				UserFollowingOrganizationPK userFollowingOrganizationPK = new UserFollowingOrganizationPK();
				userFollowingOrganizationPK.setUserId(user_id);
				userFollowingOrganizationPK.setOrganizationId(organization_id);
				UserFollowingOrganization userFollowingOrganization = new UserFollowingOrganization();
				userFollowingOrganization.setId(userFollowingOrganizationPK);
				userFollowingOrganization.setOrganization(organization);
				userFollowingOrganization.setUser(user);
				organization.getUserFollowingOrganizations().add(userFollowingOrganization);
				userFollowingOrganizationRepository.save(userFollowingOrganization);
				 organizationRepository.save(organization);
				 
				 
				 Notification notification = new Notification();
				 notification.setToUser(organization);
				 
				 if(user.getUserOrOrganization().equals("User")) {
					 
					 notification.setNotificationType("userFollowing");
					 
				 }else {
					 
					 notification.setNotificationType("orgFollowing");
					 
				 }
				 
				 notification.setByUser(user);
				
				 notification.setDescription(user.getName()+" started following you");
				 // Set the created_at field to the current timestamp
				 notification.setCreatedAt( LocalDateTime.now());
				 //notification.setPost(post);
				 //notiAl.add(notification);
				 notificationRepository.save(notification);
				 
				 return "Followed";
				
			}
			
		
		
		
		
		
		
		
	}


	public String organizationFollowStatus(String organization_id, String user_id) {
		
		Organization organization = organizationRepository.findById(organization_id).orElseThrow(() -> new NullPointerException("organization not found"));
		User user = userRepository.findById(user_id).orElseThrow(() -> new NullPointerException("user not found"));
		if(organization.getUserFollowingOrganizations()==null) {
			
			organization.setUserFollowingOrganizations(new HashSet<UserFollowingOrganization>());
			
		}
			
			if(organization.getFollowingUsers().contains(user)) {
				 

				return "Following";
			}
			else {
				
				 return "Not Following";
				
			}
			
	}


	public List<FollowingUserDto> getFollowingUsers(String user_id, Integer page, Integer size) {
		
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<User> users = userFollowingOrganizationRepository.findFollowingUsersByOrganizationId(user_id,pageable).getContent();
		List<FollowingUserDto> followingUserDtos = new ArrayList<FollowingUserDto>();
		FollowingUserDto followingUserDto ;
		for(User user:users) {
		followingUserDto = new FollowingUserDto(user.getUserId(),user.getName(),user.getProfileImg(),1,user.getUserOrOrganization());
		followingUserDtos.add(followingUserDto);
		}
		return followingUserDtos;

		
	}


	public String getAcceptedPostsNo(String user_id) {
		Organization organization = organizationRepository.findById(user_id).orElseThrow(() -> new NullPointerException("organization not found"));
	
		Long acceptedPostsNo = postRepository.countUserPostsbyStatus(user_id, "Accepted");

		

		return acceptedPostsNo.toString();
	}

	public List<FollowingUserDto> getOrganizationsByLocationAndSector(List<String> sectors, List<String> talukas,
			List<String> areas,Integer page,Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Organization> organizations= organizationRepository.findDistinctBySectorsAndTalukasAndAreas(sectors,talukas,areas,pageable).getContent();
		List<FollowingUserDto> followingUserDtos = new ArrayList<FollowingUserDto>();
		FollowingUserDto followingUserDto ;
		for(Organization organization: organizations) {
			followingUserDto = new FollowingUserDto(organization.getUserId(),organization.getName(),organization.getProfileImg(),1,organization.getUserOrOrganization());
			followingUserDtos.add( followingUserDto);
		}
		return followingUserDtos;
	}
	public List<FollowingUserDto> getOrganizationsByLocation(List<String> talukas, List<String> areas,Integer page,Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Organization> organizations= organizationRepository.findDistinctByLocation(talukas,areas,pageable).getContent();
		List<FollowingUserDto> followingUserDtos = new ArrayList<FollowingUserDto>();
		FollowingUserDto followingUserDto ;
		for(Organization organization: organizations) {
			followingUserDto = new FollowingUserDto(organization.getUserId(),organization.getName(),organization.getProfileImg(),1,organization.getUserOrOrganization());
			followingUserDtos.add( followingUserDto);
		}
		return followingUserDtos;
	}
	public List<FollowingUserDto> getOrganizationsBySector(List<String> sectors,Integer page,Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Organization> organizations= organizationRepository.findDistinctBySectors(sectors,pageable).getContent();
		List<FollowingUserDto> followingUserDtos = new ArrayList<FollowingUserDto>();
		FollowingUserDto followingUserDto ;
		for(Organization organization: organizations) {
			followingUserDto = new FollowingUserDto(organization.getUserId(),organization.getName(),organization.getProfileImg(),1,organization.getUserOrOrganization());
			followingUserDtos.add( followingUserDto);
		}
		return followingUserDtos;
	}


	public List<FollowingUserDto> getAllOrganizations(Integer page, Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		List<Organization> organizations= organizationRepository.findAll(pageable).getContent();
		List<FollowingUserDto> followingUserDtos = new ArrayList<FollowingUserDto>();
		FollowingUserDto followingUserDto ;
		for(Organization organization: organizations) {
			followingUserDto = new FollowingUserDto(organization.getUserId(),organization.getName(),organization.getProfileImg(),1,organization.getUserOrOrganization());
			followingUserDtos.add( followingUserDto);
		}
		return followingUserDtos;
		
	}


	public List<FollowingUserDto> getAllOrganizationsBySearch(String search, Integer page, Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		List<Organization> organizations= organizationRepository.findByUserIdOrNameContainingIgnoreCase(search,search,pageable).getContent();
		List<FollowingUserDto> followingUserDtos = new ArrayList<FollowingUserDto>();
		FollowingUserDto followingUserDto ;
		for(Organization organization: organizations) {
			followingUserDto = new FollowingUserDto(organization.getUserId(),organization.getName(),organization.getProfileImg(),1,organization.getUserOrOrganization());
			followingUserDtos.add( followingUserDto);
		}
		return followingUserDtos;
		
	}

	
	
//	public List<OrganizationDto> getOrganizationsByLocationAndSector(List<String> sectors, List<String> talukas,
//			List<String> areas,Integer page,Integer size) {
//		
//		Pageable pageable = PageRequest.of(page, size);
//		
//		List<Organization> organizations= organizationRepository.findDistinctBySectorsAndTalukasAndAreas(sectors,talukas,areas,pageable).getContent();
//		List<OrganizationDto> organizationDtos = new ArrayList<OrganizationDto>();
//		for(Organization organization: organizations) {
//			OrganizationDto organizationDto =modelMapper.map(organization, OrganizationDto.class);
//			for(TalukaDto talukaDto : organizationDto.getServingTalukas()) {
//				talukaDto.setTalukaPolygon(null);
//			}
//			organizationDtos.add( organizationDto);
//		}
//		
//		return organizationDtos;
//	}
//
//
//	public List<OrganizationDto> getOrganizationsByLocation(List<String> talukas, List<String> areas,Integer page,Integer size) {
//		
//		Pageable pageable = PageRequest.of(page, size);
//		
//		List<Organization> organizations= organizationRepository.findDistinctByLocation(talukas,areas,pageable).getContent();
//		List<OrganizationDto> organizationDtos = new ArrayList<OrganizationDto>();
//		for(Organization organization: organizations) {
//			OrganizationDto organizationDto =modelMapper.map(organization, OrganizationDto.class);
//			for(TalukaDto talukaDto : organizationDto.getServingTalukas()) {
//				talukaDto.setTalukaPolygon(null);
//			}
//			organizationDtos.add( organizationDto);
//		}
//		return organizationDtos;
//		
//	}
//
//
//	public List<OrganizationDto> getOrganizationsBySector(List<String> sectors,Integer page,Integer size) {
//		
//		Pageable pageable = PageRequest.of(page, size);
//		
//		List<Organization> organizations= organizationRepository.findDistinctBySectors(sectors,pageable).getContent();
//		List<OrganizationDto> organizationDtos = new ArrayList<OrganizationDto>();
//		for(Organization organization: organizations) {
//			OrganizationDto organizationDto =modelMapper.map(organization, OrganizationDto.class);
//			for(TalukaDto talukaDto : organizationDto.getServingTalukas()) {
//				talukaDto.setTalukaPolygon(null);
//			}
//			organizationDtos.add( organizationDto);
//		}
//		
//		return organizationDtos;
//	}
	
	

	
}
