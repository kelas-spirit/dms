package gr.shmmy.ntua.dms.dao;

import gr.shmmy.ntua.dms.domain.Projects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectsDaoImpl implements ProjectsDao {
	
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Projects> myProjectListByFolderId(Long folderId) {
		// TODO Auto-generated method stub
		List<Projects> lst = new ArrayList<Projects>();
		log.info("Entering myProjectList method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Projects.class);
		
		crit.add(Restrictions.like("folderId", folderId));
		lst = (List<Projects>) crit.list();

		
		return lst;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Projects> myProjectListByFolderId(Long folderId, String role) {
		// TODO Auto-generated method stub
		List<Projects> lst = new ArrayList<Projects>();
		log.info("Entering myProjectList method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Projects.class);
		Conjunction conjunction = Restrictions.conjunction();
		Criterion folderID = Restrictions.like("folderId", folderId);
		Criterion userRole = Restrictions.like("authority", role);
		
		
		conjunction.add(folderID);
		conjunction.add(userRole);
		crit.add(conjunction);
		
		
		lst = (List<Projects>) crit.list();
		
		return lst;
	}
	
	@Override
	public void wpleader(String username, Long folderId,String authority) {
		// TODO Auto-generated method stub
		Projects prj = new Projects();
		Session session = sessionFactory.openSession();
		prj.setFolderId(folderId);
		//prj.setUserId(userId);
		prj.setUsername(username);
		prj.setAuthority(authority);
		prj.setCreatedDate(new Date());
		Long prjId = (Long) session.save(prj);
		log.info("prjId saved with id : " + prjId);
		
	}
	
	@Override
	public void deleteFromProjects(Long projectId) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Projects projectID = (Projects)session.get(Projects.class, projectId);
		session.delete(projectID);
		session.getTransaction().commit();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean exist(String username, Long folderID, String role) {
		// TODO Auto-generated method stub
	//	Projects prj = new Projects();
		List<Projects> usq = null;
		//System.out.println("ena");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Projects.class);

		Conjunction conjunction = Restrictions.conjunction();
		Criterion userId = Restrictions.like("username", username);
		Criterion folderId = Restrictions.eq("folderId", folderID);
		Criterion userRole = Restrictions.like("authority", role);

		conjunction.add(userId);
		conjunction.add(folderId);
		conjunction.add(userRole);
		crit.add(conjunction);
		usq =  (List<Projects>)crit.list();
		if(usq.size()>0){
			return true;
		}
		else{
		
		return false;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Projects> myProjectList(String username,String role) {
		// TODO Auto-generated method stub
		List<Projects> lst = new ArrayList<Projects>();
		log.info("Entering myProjectList method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Projects.class);
		Conjunction conjunction = Restrictions.conjunction();
		Criterion user = Restrictions.like("username", username);
		Criterion userRole = Restrictions.like("authority", role);
		conjunction.add(user);
		conjunction.add(userRole);
		crit.add(conjunction);
		
		
		lst = (List<Projects>) crit.list();

		log.info("Leaving myProjectList method in DAO");

		return lst;
	}




	@SuppressWarnings("unchecked")
	@Override
	public List<Projects> myProjectList(String role) {
		// TODO Auto-generated method stub
		
		List<Projects> lst = new ArrayList<Projects>();
		log.info("Entering myProjectList method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Projects.class);
		crit.add(Restrictions.like("authority", role));
		
		lst = (List<Projects>) crit.list();

		
		return lst;
	}




	@Override
	public void updateWPuser(Long projectId,String username) {
		// TODO Auto-generated method stub
		Projects projects = new Projects();
		Session session = sessionFactory.openSession();
		projects = (Projects)session.get(Projects.class, projectId);
		projects.setUsername(username);
		session.merge(projects);
	//	session.getTransaction().commit();	
		session.beginTransaction().commit();
		session.close();

	}
	
}
