/**
 * 
 */
package gr.shmmy.ntua.dms.dao;

import java.io.IOException;
import java.util.List;

import gr.shmmy.ntua.dms.domain.Projects;
import gr.shmmy.ntua.dms.domain.User;

/**
 * @author kelas
 *
 */
public interface UserDao {
	public User findUserByUsernameAndPassword(String username, String password);
	public void insertUser( User user, String roles) throws IOException;
	public List<User> getAllUsers();
	public List<User> getUserBySearchQuery(String searchQuery);
	public void insertUserRole(Long id, String role);
	public Long idFromUsername(String user);
	public String usernameById(Long userId);
	//roles for technical_manager or wp_leader, not for task_p
	public void DeleteRoleById(Long id);
	//delete  main id from projects table-> delete some project
	//delete all package with criteria folderId
	public void deleteByIdFolder(Long folderId);//not needed
	//exist is if exist in project table with  role
	public boolean userExist(String user);//no
	public boolean roleExist(Long userId, String authority);//no
//	public boolean roleExist(String username, String authority);//no

	public Long userId(String username);//no
	
	public User userById(Long userId);

	//public List<User> UserList(String username);

}
