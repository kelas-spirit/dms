package gr.shmmy.ntua.dms.service;

import gr.shmmy.ntua.dms.dao.DocumentDao;
import gr.shmmy.ntua.dms.dao.UserDao;
import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.domain.Projects;
import gr.shmmy.ntua.dms.domain.UserRole;
//import gr.shmmy.ntua.dms.utils.Constants;
import gr.shmmy.ntua.dms.web.formBean.DocumentPostFormBean;
import gr.shmmy.ntua.dms.web.formBean.UserPostBean;
import gr.shmmy.ntua.dms.web.formBean.UserRegFormBean;
import gr.shmmy.ntua.dms.domain.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class UserService {
	
	//private static final int Set = 0;
	
	private int userid;
	@Autowired
	private UserDao userDao;
	
	@Transactional
	public void insertUser( UserRegFormBean formBean){
		
		//User user = new User(formBean.getUsername(),formBean.getPassword(),formBean.getFirstname(),formBean.getLastname(),1);
		
		User user = new User();
		//Set<UserRole> set = new HashSet<UserRole>();
		//Collections.addAll(set, formBean.getRoles());
	//	user.setUserId(6L);
		System.out.println("edw1");
		user.setEnabled(1);
		System.out.println("edw2");
		user.setFirstname(formBean.getFirstname());
		System.out.println("edw3 "+formBean.getFirstname());
		user.setLastname(formBean.getLastname());
		System.out.println("edw4 "+formBean.getLastname());
		user.setUsername(formBean.getUsername());
		System.out.println("edw5 "+formBean.getUsername());
		user.setPassword(formBean.getPassword());
		System.out.println("edw6 "+formBean.getPassword()); 
		//user.setEmail(email);
		try {
			userDao.insertUser(user,formBean.getRoles());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//User user = new User(formBean.getUsername(),formBean.getPassword(),formBean.getFirstname(),formBean.getLastname(),1);
	//	user.setUserRoles(formBean.getRoles());
		
		
	}
	
	@Transactional
	public void inserFromPublicReg(UserPostBean userbean){
		User user = new User();
		user.setEmail(userbean.getEmail());
		user.setEnabled(1);
		user.setFirstname(userbean.getFirstname());
		user.setLastname(userbean.getLastname());
		user.setUsername(userbean.getUsername());
		user.setPassword(userbean.getPassword());
		try {
			userDao.insertUser(user,"ROLE_USER");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	public String usernameById(Long userId){
		String username = userDao.usernameById(userId);
		return username;	
	}
	
	public void saveUserRole(Long id, String role){
		userDao.insertUserRole(id, role);
		
	}
	
	@Transactional
	public List<User> getUserBySearchQuery(String searchQuery) {
		List<User> usq = new ArrayList<User>();

		usq = userDao.getUserBySearchQuery(searchQuery);
		return usq;
	}
	
	
	
	public List<gr.shmmy.ntua.dms.domain.User> getAllUsers() {
		return userDao.getAllUsers();
	}
	
	public void DeleteRoleById(Long id){
		userDao.DeleteRoleById(id);
	}
	

	//exist folder and role
	public gr.shmmy.ntua.dms.domain.User findUserById(Long id){
		gr.shmmy.ntua.dms.domain.User user = userDao.userById(id);
		return user;
	}
	
	public boolean userExist (String username){
		return	userDao.userExist(username);
	}
	public Long userId(String username){
		return userDao.userId(username);
		
	}
	public boolean roleExist(Long userId, String authority){
		return userDao.roleExist(userId, authority);
	}
	
	

}
