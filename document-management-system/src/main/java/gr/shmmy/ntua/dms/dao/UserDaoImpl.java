/**
 * 
 */
package gr.shmmy.ntua.dms.dao;

import gr.shmmy.ntua.dms.domain.User;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author npapadopoulos
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

		Session session = this.sessionFactory.openSession();
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
		}
		return null;
	}
}
