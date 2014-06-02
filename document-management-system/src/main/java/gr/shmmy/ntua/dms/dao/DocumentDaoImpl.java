package gr.shmmy.ntua.dms.dao;

import gr.shmmy.ntua.dms.domain.Folder;
import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.domain.Projects;
import gr.shmmy.ntua.dms.domain.PublicData;
import gr.shmmy.ntua.dms.domain.User;
import gr.shmmy.ntua.dms.utils.Constants;
import gr.shmmy.ntua.dms.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.web.multipart.MultipartFile;

public class DocumentDaoImpl implements DocumentDao {

	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	SessionFactory sessionFactory;

	@Value("${doc.repo.path}")
	private String fileStorePath;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setFileStorePath(String fileStorePath) {
		this.fileStorePath = fileStorePath;
	}

	// @Override
	public int saveDocument(Metadata metadata) {
		log.info("Entering saveDocument method in DAO");
		Session session = sessionFactory.openSession();
		@SuppressWarnings("unused")
		Long metadataId = (Long) session.save(metadata);
		// session.save(document);
		log.info("Leaving saveDocument method in DAO");
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Metadata> getDocuments(int count, String username,
			Collection<GrantedAuthority> roles,Long foldId) {

		List<Metadata> lst =null;// new ArrayList<Metadata>();

		log.info("Entering getDocuments method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Metadata.class);
		//crit.setMaxResults(count);
		crit.addOrder(Order.desc("createdDate"));// asc desc  auksousa fthinousa

		Conjunction conjunction = Restrictions.conjunction();
		
	/*	GrantedAuthority admin = new GrantedAuthorityImpl(Constants.ROLE_ADMIN);
		GrantedAuthority superAdmin = new GrantedAuthorityImpl(
				Constants.ROLE_SUPER_ADMIN);
*/
		/*
		 * if user is not an admin or super admin then he sees only his and
		 * public docs
		 */
		if ((username != null && !username.trim().equals(""))
				) { //&& !(roles.contains(admin) || roles.contains(superAdmin)

		/*	crit.add(Restrictions.or(
					Restrictions.ilike("createUser", username), Restrictions
							.ilike("owner", Constants.DOC_TYPE_PUBLIC)));*/
			Criterion byUser = Restrictions.like("createUser", username);
			Criterion byParrent_Id = Restrictions.eq("parrentId", new Long(foldId));
			Criterion enabled = Restrictions.eq("enabled", 1);
			conjunction.add(byUser);
			conjunction.add(byParrent_Id);
			conjunction.add(enabled);
			crit.add(conjunction);

		}

		lst = (List<Metadata>) crit.list();
		log.info("Leaving getDocuments method in DAO");
		return lst;
	}

	@SuppressWarnings("unchecked")
	public List<Metadata> getDocuments2(Long foldId) {

		List<Metadata> lst =null;// new ArrayList<Metadata>();

		log.info("Entering getDocuments method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Metadata.class);
		//crit.setMaxResults(count);
		crit.addOrder(Order.desc("createdDate"));// asc desc  auksousa fthinousa

		Conjunction conjunction = Restrictions.conjunction();
		
		//	Criterion byUser = Restrictions.like("createUser", username);
			Criterion byParrent_Id = Restrictions.eq("parrentId", new Long(foldId));
		//	conjunction.add(byUser);
			Criterion enabled = Restrictions.eq("enabled", 1);
			conjunction.add(byParrent_Id);
			conjunction.add(enabled);
			crit.add(conjunction);

		

		lst = (List<Metadata>) crit.list();
		log.info("Leaving getDocuments method in DAO");
		return lst;
	}

	
	
	@Override
	public Metadata getDocumentById(Long docId) {
		log.info("Entering getDocumentById method in DAO");
		Session session = sessionFactory.openSession();
		Metadata meta = (Metadata) session.get(Metadata.class, docId);
		return meta;
	}
	@Override
	public PublicData getPublicDocumentById(Long docId) {
		log.info("Entering getPublicDocumentById method in DAO");
		Session session = sessionFactory.openSession();
		PublicData publicdata = (PublicData) session.get(PublicData.class, docId);
		return publicdata;
	}

	@Override
	public List<Metadata> getDocumentsBySearchQuery(String searchQuery) {

		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Metadata.class);
		crit.add(Restrictions.disjunction().add(
				Restrictions.ilike("documentFileName", searchQuery,
						MatchMode.ANYWHERE))
				.add(
						
						Restrictions.ilike("createUser", searchQuery,
								MatchMode.ANYWHERE)).add(
						Restrictions.ilike("updatedUser", searchQuery,
								MatchMode.ANYWHERE)).add(
						Restrictions.ilike("subject", searchQuery,
								MatchMode.ANYWHERE)).add(
						Restrictions.ilike("comments", searchQuery,
								MatchMode.ANYWHERE)));
		crit.setMaxResults(100);
		crit.addOrder(Order.desc("createdDate"));
		@SuppressWarnings("unchecked")
		List<Metadata> lst = crit.list();
		int len = lst.size();
		log.info(":::: Search returned " + len + " items.");
		return lst;
	}

	/**
	 * Deletes a document
	 */

	@Override
	public void deleteDocument(Long documentId) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Metadata meta = (Metadata) session.get(Metadata.class, documentId);

		// delete file from file system
		StringBuilder fullPath = new StringBuilder();
		fullPath.append(fileStorePath + File.separatorChar);
		if (meta.getOwner().equals(Constants.DOC_TYPE_PUBLIC)) {
			fullPath.append(Constants.PUBLIC_FOLDER);
		} else {
			fullPath.append(meta.getOwner());
		}
		fullPath.append(File.separatorChar + meta.getDocumentFileName());
		log.info("Final document download path is : " + fullPath);
		File file = new File(fullPath.toString());
		if (!file.exists()) {
			log.error("File does not exist! (" + meta.getDocumentFileName()
					+ ")");
		} else if (file.isDirectory()) {
			log.error("File is a directory! CAN NOT DELETE DIRECTORY. ("
					+ meta.getDocumentFileName() + ")");
		}
		file.delete();
		session.delete(meta);
		log.info("Is transaction active? = "
				+ session.getTransaction().isActive());
		session.getTransaction().commit();

		log.info(">>>>>>>>>>>>>>document Deleted.<<<<<<<<<<<<");
	}

	//not used
	@Override
	public void deleteByParrentId(Long parrentId) {
		// TODO Auto-generated method stub
		System.out.println("Eimai sto delete by parrent");
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		System.out.println("Eimai sto delete by parrent2");

		Query query = session.createSQLQuery("delete from METADATA where PARRENT_ID=:parrentId");
		System.out.println("Eimai sto delete by parrent 3");

		query.setParameter("parrentId", parrentId);
		System.out.println("Eimai sto delete by parrent4");

	    query.executeUpdate();
		System.out.println("Eimai sto delete by parrent5");

	    session.beginTransaction().commit();
		System.out.println("Eimai sto delete by parrent6");

	}
	
	@Override
	public void deletePublicDocument(Long documentId) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		//Metadata meta = (Metadata) session.get(Metadata.class, documentId);
		PublicData publicdata = (PublicData) session.get(PublicData.class, documentId);
		// delete file from file system
		StringBuilder fullPath = new StringBuilder();
		fullPath.append(fileStorePath + File.separatorChar);
		if (publicdata.getOwner().equals(Constants.DOC_TYPE_PUBLIC)) {
			fullPath.append(Constants.PUBLIC_FOLDER);
		} else {
			fullPath.append(publicdata.getOwner());
		}
		fullPath.append(File.separatorChar + publicdata.getDocumentFileName());
		log.info("Final document download path is : " + fullPath);
		File file = new File(fullPath.toString());
		if (!file.exists()) {
			log.error("File does not exist! (" +publicdata.getDocumentFileName()
					+ ")");
		} else if (file.isDirectory()) {
			log.error("File is a directory! CAN NOT DELETE DIRECTORY. ("
					+ publicdata.getDocumentFileName() + ")");
		}
		file.delete();
		session.delete(publicdata);
		log.info("Is transaction active? = "
				+ session.getTransaction().isActive());
		session.getTransaction().commit();

		log.info(">>>>>>>>>>>>>>public document Deleted.<<<<<<<<<<<<");
		
	}
	@Override
	public Long getAllDocumentCount() {
		Session session = sessionFactory.openSession();
		Long count = (Long) session.createCriteria(Metadata.class)
				.setProjection(Projections.rowCount()).uniqueResult();

		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveFileToRepo(MultipartFile multipartFile,
			String fileStorePath, Metadata metadata) throws IOException {

		log.info("************************************************");
		String fileName = multipartFile.getOriginalFilename();
		File dir = new File(fileStorePath);
		
		
		if (!dir.exists()) {
			log.info("User's dir did not existed, may be because "
					+ "user is uploading file for the first time. "
					+ "Creating user's dir at " + fileStorePath);

			dir.mkdirs();
		}
	//	System.out.println("\n eimai sto daolmpl\n");
		if (Utils.fileAlreadyExists(fileStorePath, fileName)) {
			//throw new IOException("File with similar name already exists.");
		}

		if (!dir.isDirectory()) {
			throw new IOException(dir.getAbsolutePath()
					+ " is not a directory.");
		}

		File path = new File(fileStorePath + "/" + fileName);
		//FileOutputStream fos = new FileOutputStream(path);
		//fos.write(multipartFile.getBytes());

		
		Blob blob = Hibernate.getLobCreator(sessionFactory.getCurrentSession()).createBlob(multipartFile.getInputStream(),
				metadata.getDocumentSize());
		metadata.setContent(blob);
		
		// save metadata.
		log.info("Saving metadata");
		Session session = sessionFactory.openSession();
		
		//check if file already exist in db with current path
		
		
		//if file already exist that mean we need to update status end block previous version (send enabled=0)
		
		//end of check for uniqe or not file in db with current path(current "futher" id)
		
		Long metadataId = (Long) session.save(metadata);
		log.info("Metadata saved with id : " + metadataId);
		log.info("Leaving saveFileToRepo method in DAO");

	}

	@Override
	public void savePublicFile(MultipartFile multipartFile, PublicData publicdata)
			throws IOException {
		// TODO Auto-generated method stub
		Blob blob = Hibernate.getLobCreator(sessionFactory.getCurrentSession()).createBlob(multipartFile.getInputStream(),
				publicdata.getDocumentSize());
		publicdata.setContent(blob);
		log.info("Entering saveDocument method in DAO");
		Session session = sessionFactory.openSession();
		@SuppressWarnings("unused")
		Long metadataId = (Long) session.save(publicdata);
		 //session.save(document);
		log.info("Leaving savePublicFile method in DAO");
		//return 1;
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PublicData> getPublicFiles(String username) {
		// TODO Auto-generated method stub
		List<PublicData> lst = new ArrayList<PublicData>();
		log.info("Entering getPublicData method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(PublicData.class);
		//crit.setMaxResults(count);
		//crit.addOrder(Order.desc("createdDate"));
		crit.add(Restrictions.like("createUser", username));
		//crit.add(Restrictions.);
		
		lst = (List<PublicData>) crit.list();
		log.info("Leaving getPublicFiles method in DAO");
		return lst;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Metadata> getDocByDocNameAndParrentId(String documentFileName, Long parrentId) {
		Session session = sessionFactory.openSession();
		
		//check if file already exist in db with current path
		List<Metadata> lst = null;
		Criteria crit = session.createCriteria(Metadata.class);
		Conjunction conjunction = Restrictions.conjunction();
		Criterion byParrent_Id = Restrictions.eq("parrentId", parrentId);
		Criterion byName = Restrictions.like("documentFileName", documentFileName);
		conjunction.add(byParrent_Id);
		conjunction.add(byName);
		crit.add(conjunction);
		lst =  (List<Metadata>)crit.list();
		return lst;
	}

	@Override
	public void setEnabled(int enabled, Long metadataId) {
		
		Metadata metadata = new Metadata();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
				//metadataId
		try{
			tx=session.beginTransaction();
			metadata = (Metadata)session.get(Metadata.class, metadataId);
			//session.update("enabled", enabled);
			metadata.setEnabled(enabled);
			
			session.merge(metadata);

			tx.commit();

		}catch(HibernateException e){
			e.printStackTrace();
		}finally {
	         session.close(); 
	      }
		
		
	}

	@Override
	public void setUpdtoPrevious(Long metadataId) {
		Metadata metadata = new Metadata();
		Session session = sessionFactory.openSession();
		
		Transaction tx = null;
		
		//session.beginTransaction().commit();
		
		try{
			
		tx=session.beginTransaction();
		metadata = (Metadata)session.get(Metadata.class, metadataId);
		
		Date date = new Date();
		date=metadata.getUpdatedDate();
		metadata.setCreatedDate(date);
		metadata.setUpdatedDate(null);
		
		session.merge(metadata);
		tx.commit();
	}catch(HibernateException e){
		e.printStackTrace();
	}finally {
         session.close(); 
      }
		
	}

	@Override
	public Metadata documentById(Long documentId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Metadata metadata = (Metadata) session.get(Metadata.class, documentId);
		return metadata;
	}

	@Override
	public void editBlob(String bytes, Long documentId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Metadata metadata = (Metadata)session.get(Metadata.class, documentId);
		Metadata data = new Metadata();
		List<Metadata> lst = getDocByDocNameAndParrentId(metadata.getDocumentFileName(),metadata.getParrentId());
		
		byte[] bit = bytes.getBytes();
		Blob blob = session.getLobHelper().createBlob(bit);
		
		if(lst.size()==1){
			setEnabled(0, metadata.getMetadataId());//set enabled id 	
			data.setComments(metadata.getComments());
			data.setContent(blob);
			data.setCreatedDate(null);
			data.setUpdatedDate(new Date());
			data.setCreateUser(metadata.getCreateUser());
			data.setDocumentFileName(metadata.getDocumentFileName());
			data.setDocumentSize(metadata.getDocumentSize());
			data.setDocumentType(metadata.getDocumentType());
			data.setEnabled(1);
			data.setParrentId(metadata.getParrentId());
			data.setSubject(metadata.getSubject());
			data.setOwner(metadata.getOwner());

			session.save(data);
		}
		else if(lst.size()==2){
			for(Metadata ls: lst){
				if(ls.getEnabled()==0){
					Long id =	ls.getMetadataId();
					deleteDocument(id);
				}
				if(ls.getEnabled()==1){
					System.out.println("get enabled einai 1");
				Long docId =ls.getMetadataId();
				setEnabled(0, docId);
				setUpdtoPrevious(docId);
				//setUpdtoPrevious(docId);
				data.setComments(metadata.getComments());
				data.setContent(blob);
				data.setCreatedDate(null);
				data.setUpdatedDate(new Date());
				data.setCreateUser(metadata.getCreateUser());
				data.setDocumentFileName(metadata.getDocumentFileName());
				data.setDocumentSize(metadata.getDocumentSize());
				data.setDocumentType(metadata.getDocumentType());
				data.setEnabled(1);
				data.setParrentId(metadata.getParrentId());
				data.setSubject(metadata.getSubject());
				data.setOwner(metadata.getOwner());
				session.save(data);
				
				}
				
				
			}
			
		}
		
		
		
		
		
	//	System.out.println("the string for change is "+bytes);

		/*try{
		
		tx=session.beginTransaction();
		//Blob blob = Hibernate.getLobCreator(sessionFactory.getCurrentSession()).createBlob(bit);
		//metadata.setContent(blob);
		
		session.merge(metadata);
		tx.commit();
		
	}catch(HibernateException e){
		e.printStackTrace();
	}finally {
         session.close(); 
      }*/
		
/*
 * Blob blob = Hibernate.getLobCreator(sessionFactory.getCurrentSession()).createBlob(multipartFile.getInputStream(),
				metadata.getDocumentSize());
 * */
	}



	



	

	


}
