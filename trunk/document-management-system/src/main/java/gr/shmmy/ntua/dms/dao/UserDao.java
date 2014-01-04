/**
 * 
 */
package gr.shmmy.ntua.dms.dao;

import gr.shmmy.ntua.dms.domain.User;

/**
 * @author npapadopoulos
 *
 */
public interface UserDao {
	public User findUserByUsernameAndPassword(String username, String password);
}
