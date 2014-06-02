/**
 * 
 */
package gr.shmmy.ntua.dms.dao;

import gr.shmmy.ntua.dms.domain.Folder;
import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.domain.Projects;
import gr.shmmy.ntua.dms.domain.User;
import gr.shmmy.ntua.dms.domain.UserRole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kelas
 * 
 */
public class UserDaoImpl implements UserDao {
	private final Logger log = Logger.getLogger(this.getClass());
	SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public User findUserByUsernameAndPassword(String username, String password) {

		/*Session session = this.sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<User> users = session
				.createQuery(
						"from USER user where user.username=? and user.password=?")
				.setParameter(0, username).setParameter(1, password).list();
		log.info("SELECT * FROM dms.USER WHERE USERNAME=" + username
				+ " AND PASSWORD=" + password);
		if (session != null) {
			session.close();
		}
		if (users != null && !users.isEmpty()) {
			return users.get(0);
		}*/
		return null;
	}

	@Override
	//@Autowired
	public void insertUser(User user, String roles) throws IOException {
		// TODO Auto-generated method stub
		UserRole userRole = new UserRole();
		
		
	//	System.out.println("ROLE IS ..."+roles);
		
		
		
		Session session = sessionFactory.openSession();
		Long UserId = (Long) session.save(user);// save data for user
	
		// this save data for userrole with user id
		log.info("UserId saved with id : " + UserId);
		if(roles==null || (!roles.equals("ROLE_ADMIN"))){
		//	System.out.println("in user"+roles);
			userRole.setAuthority("ROLE_USER");
		}
		else 
			if(roles.equals("ROLE_ADMIN")){
				System.out.println("in admin"+roles);
				userRole.setAuthority(roles);
				
			}
		userRole.setUserId(UserId);
		Long UserRoleId = (Long) session.save(userRole);
		log.info("UserRoleId saved with id : " + UserRoleId);
		
	}

	@Override
	public List<User> getAllUsers() {
		Session session = sessionFactory.openSession();
		
		org.hibernate.Query query = (org.hibernate.Query) session.createQuery("from User");
		
		@SuppressWarnings("unchecked")
		List<User> lst = query.list();
		
		return lst;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserBySearchQuery(String searchQuery) {
		// TODO Auto-generated method stub
		List<User> usq = new ArrayList<User>();
		
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.like("username", searchQuery));
		
		usq = (List<User>) crit.list();
		
		
		return usq;
	}

	@Override
	public void insertUserRole(Long id, String role) {
		// TODO Auto-generated method stub
		UserRole userRole = new UserRole();
		Session session = sessionFactory.openSession();
		userRole.setUserId(id);
		userRole.setAuthority(role);
		Long UserRoleId = (Long) session.save(userRole);
		log.info("UserRoleId and new role saved with id : " + UserRoleId);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long idFromUsername(String user) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		List<User> usq = new ArrayList<User>();
		Long id = null;
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.like("username", user));
		usq = (List<User>) crit.list();
		for(User usr:usq){
			id = usr.getUserId();
		}
		
		return id;
	}

	@Override
	public void DeleteRoleById(Long userRoleId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		UserRole userRole = (UserRole)session.get(UserRole.class, userRoleId);
		session.delete(userRole);
		session.getTransaction().commit();
		
	}

	

	

	@SuppressWarnings("unchecked")
	@Override
	public Long userId(String username) {
		// TODO Auto-generated method stub
		List<User> usr =null;
		Long userId = null;
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.like("username",username));
		//crit.add(Restrictions.)
		usr =  (List<User>)crit.list();
		for(User us: usr){
			userId=us.getUserId();
		}
		
		
		return userId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean userExist(String username) {
		// TODO Auto-generated method stub
		List<User> usr =null;
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.like("username", username));
		usr =  (List<User>)crit.list();
		
		if(usr.size()>0){
			return true;
		}
		else{
			return false;
		}
		
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean roleExist(Long userId, String authority) {
		List<UserRole> usrRole =null;
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(UserRole.class);
		Conjunction conjunction = Restrictions.conjunction();
		Criterion usrId = Restrictions.eq("userId", userId);
		Criterion userRole = Restrictions.like("authority", authority);

		//conjunction.add(userId);
		conjunction.add(usrId);
		conjunction.add(userRole);
		crit.add(conjunction);
		usrRole =  (List<UserRole>)crit.list();
		
		if(usrRole.size()>0){
			return true;
		}
		else{
		return false;
		}
	}

	
	
	

	

	@Override
	public void deleteByIdFolder(Long folderId) {
		// TODO Auto-generated method stub
	//	Session session = sessionFactory.openSession();
		//session.beginTransaction();
				

		
	}

	@Override
	public String usernameById(Long userId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		User user = (User) session.get(User.class, userId);
		
		return user.getUsername();
	}

	@Override
	public User userById(Long userId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		User user = (User) session.get(User.class, userId);
		return user;
	}

	
	

	
	

	//@Override
	/*public List<User> UserList(String username) {
		// TODO Auto-generated method stub
		List<User> lst = new ArrayList<User>();
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.like("username", username));
		return null;
	}*/

}
