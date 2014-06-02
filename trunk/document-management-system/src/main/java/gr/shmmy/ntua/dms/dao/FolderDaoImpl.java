package gr.shmmy.ntua.dms.dao;


import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import gr.shmmy.ntua.dms.domain.Folder;
import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.domain.Projects;
//import gr.shmmy.ntua.dms.utils.Constants;


import org.hibernate.*;
import org.hibernate.criterion.*;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;



public class FolderDaoImpl implements FolderDao {
	private final Logger log = Logger.getLogger(this.getClass());
	
	@Value("${doc.repo.path}")
	private String fileStorePath;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setFileStorePath(String fileStorePath) {
		this.fileStorePath = fileStorePath;
	}
	
	public void reEditPath(String pathId,Long id){
		
		Folder fld = new Folder();
		Session session = sessionFactory.openSession();
		//System.out.println("mpika 1");
		fld = (Folder)session.get(Folder.class, id);
		String str = fld.getPathId()+" "+pathId;

	//	System.out.println("mpika 2");
		//System.out.println("str: "+str);
		//fld.setPathId(fld.getPathId()+" "+pathId);
		fld.setPathId(str);
	//	System.out.println("mpika 3");

		session.merge(fld);
	//	System.out.println("this is Long.valueOf(st.nextToken(): "+id);

		session.beginTransaction().commit();
		session.close();
		
	}
	
	public void saveFolderToRepo(String path, Folder folder){
		System.out.println("mpika sto dao ");
		System.out.println(path);
		log.info("****************FOLDER*******************************");
		File dir = new File(path);
		dir.mkdir();
		
	//	Long id;
	//	String pathId;
		log.info("Saving folder");
		Session session = sessionFactory.openSession();
		Long folderId = (Long) session.save(folder);
		log.info("Metadata saved with id : " + folderId);
		//String pathId= String.valueOf(folderId);
		//reEditPath(pathId,folderId);
		
		/*
		 * edit paths to tree
		 * 
		 * */
	//	String str = folder.getPathId();
		//StringTokenizer st = new StringTokenizer(str);
		
	/*
		while (st.hasMoreTokens()){
			id=Long.valueOf(st.nextToken());
			System.out.println("this is Long.valueOf(st.nextToken(): "+id);
			
			pathId= String.valueOf(folderId);
			System.out.println("this is pathId: "+pathId);
			if((id>0)&&(pathId!=null)){
		//	reEditPath(pathId,id);
			}
		}
		*/
		
		
		log.info("Leaving saveFolderToRepo method in DAO");
	}
	
	//@SuppressWarnings( { "unchecked", "deprecation" } )
	@SuppressWarnings("unchecked")
	public List<Folder> getFolders(String username, Long PARRENT_ID){
	//	List<Folder> lst = new ArrayList<Folder>(); 
		List<Folder> lst = null;
		log.info("Entering getFolders method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Folder.class);
		crit.addOrder(Order.asc("createdDate"));

		
		Conjunction conjunction = Restrictions.conjunction();
		
		if ((username != null && !username.trim().equals(""))) {
			
			Criterion byUser = Restrictions.like("createUser", username);
			Criterion byParrent_Id = Restrictions.eq("parrentId", new Long(PARRENT_ID));
			conjunction.add(byUser);
			conjunction.add(byParrent_Id);
			crit.add(conjunction);
			
		//	System.out.println("I'm here in folder with criteria");
			
		}

	   lst =  (List<Folder>)crit.list();
		//System.out.println("Gooood");
		log.info("Leaving getDocuments method in DAO");

		
		return lst;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> getFolderById(Long folderId) {
		// TODO Auto-generated method stub
		//System.out.println("ok5");
		List<Folder> lst = new ArrayList<Folder>();
		//System.out.println("ok1");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Folder.class);
		//Conjunction conjunction = Restrictions.conjunction();

		crit.add(Restrictions.eq("folderId", folderId));
		lst = (List<Folder>) crit.list();
		System.out.println("ok2");
		return lst;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> getFolders2( Long PARRENT_ID) {
		//List<Folder> lst = new ArrayList<Folder>(); 
		List<Folder> lst = null;
		log.info("Entering getFolders method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Folder.class);
		crit.addOrder(Order.asc("createdDate"));
		Conjunction conjunction = Restrictions.conjunction();
		Criterion byParrent_Id = Restrictions.eq("parrentId", new Long(PARRENT_ID));
		conjunction.add(byParrent_Id);
		crit.add(conjunction);
		

	   lst =  (List<Folder>)crit.list();
		//System.out.println("Gooood");
		log.info("Leaving getDocuments method in DAO");

		
		return lst;
	}
/*
 * TO DO...
 * */
	@Override
	public List<Folder> folderExistInSystem(Long folderId) {
		List<Folder> lst = null;
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Folder.class);
		
		return lst;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> getFolders3(Long folderId, Long parentId) {
		List<Folder> lst = null;
		log.info("Entering getFolders method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Folder.class);
		crit.addOrder(Order.asc("createdDate"));

		
		Conjunction conjunction = Restrictions.conjunction();
		
			
			Criterion byUser = Restrictions.eq("createUser", folderId);
			Criterion byParrent_Id = Restrictions.eq("parrentId", parentId);
			conjunction.add(byUser);
			conjunction.add(byParrent_Id);
			crit.add(conjunction);
			
		//	System.out.println("I'm here in folder with criteria");
			
		

	   lst =  (List<Folder>)crit.list();
		//System.out.println("Gooood");
		log.info("Leaving getDocuments method in DAO");

		
		return lst;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> getFolders4(String folderName, Long parrentId) {
		//	List<Folder> lst = new ArrayList<Folder>(); 
		List<Folder> lst = null;
		log.info("Entering getFolders method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Folder.class);
	//	crit.addOrder(Order.asc("createdDate"));

		
		Conjunction conjunction = Restrictions.conjunction();
		
			
			Criterion byUser = Restrictions.like("folderName", folderName);
			Criterion byParrent_Id = Restrictions.eq("parrentId", parrentId);
			conjunction.add(byUser);
			conjunction.add(byParrent_Id);
			crit.add(conjunction);
			
		//	System.out.println("I'm here in folder with criteria");
			
		

	   lst =  (List<Folder>)crit.list();
		//System.out.println("Gooood");
		log.info("Leaving getDocuments method in DAO");
		if(lst.size()>0){
			System.out.println("list not null ");
		}
		
		return lst;
	}

	@Override
	public void deleteFolder(Long folderId) {
		// TODO Auto-generated method stub
		System.out.println("tha ginei delete tou  "+folderId);

		System.out.println("1");
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		
		Folder folder = (Folder) session.get(Folder.class, folderId);
		System.out.println("2");
		try{
			tx=session.beginTransaction();
		session.delete(folder);
		System.out.println("3");
		tx.commit();
		System.out.println("4");
		}catch(HibernateException e){
			e.printStackTrace();
		}finally {
	         session.close(); 
	      }
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> getFoldersForDelete(String username, Long folderId) {
		System.out.println("ok5");
		List<Folder> lst = new ArrayList<Folder>();
		System.out.println("ok1");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Folder.class);
		Conjunction conjunction = Restrictions.conjunction();
		Criterion idSort = Restrictions.ge("folderId", folderId);
		Criterion byUser = Restrictions.like("createUser", username);
		conjunction.add(idSort);
		conjunction.add(byUser);
		crit.add(conjunction);
		//crit.add(Restrictions.eq("folderId", folderId));
		lst = (List<Folder>) crit.list();
		System.out.println("ok2");
		return lst;
	}

	@Override
	public String owner(Long folderId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Folder fold = (Folder) session.get(Folder.class, folderId);
		
		return fold.getCreateUser();
	}

	
	
	
	

}
