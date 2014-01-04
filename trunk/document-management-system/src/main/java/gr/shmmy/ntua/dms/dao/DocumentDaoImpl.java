package gr.shmmy.ntua.dms.dao;

import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.domain.User;
import gr.shmmy.ntua.dms.utils.Constants;
import gr.shmmy.ntua.dms.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
			Collection<GrantedAuthority> roles) {
//collection einai ena sunolo apo classes k interfaces,sumperiferetai san vivliothiki
// 	GrantedAuthority parousiazei ton eauto t eite ws string eite upostirizetai apo AccessDecisionManager
//to opoio kanei teleutaio elegxo prosvasis ..
		List<Metadata> lst = new ArrayList<Metadata>();

		log.info("Entering getDocuments method in DAO");
		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Metadata.class);
		crit.setMaxResults(count);
		crit.addOrder(Order.desc("createdDate").asc("owner"));

		GrantedAuthority admin = new GrantedAuthorityImpl(Constants.ROLE_ADMIN);
		GrantedAuthority superAdmin = new GrantedAuthorityImpl(
				Constants.ROLE_SUPER_ADMIN);

		/*
		 * if user is not an admin or super admin then he sees only his and
		 * public docs
		 */
		if ((username != null && !username.trim().equals(""))
				&& !(roles.contains(admin) || roles.contains(superAdmin))) {

			crit.add(Restrictions.or(
					Restrictions.ilike("createUser", username), Restrictions
							.ilike("owner", Constants.DOC_TYPE_PUBLIC)));

		}

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
	public List<Metadata> getDocumentsBySearchQuery(String searchQuery) {

		Session session = sessionFactory.openSession();
		Criteria crit = session.createCriteria(Metadata.class);
		crit.add(Restrictions.disjunction().add(
				Restrictions.ilike("documentFileName", searchQuery,
						MatchMode.ANYWHERE))
				.add(
						Restrictions.ilike("keywords", searchQuery,
								MatchMode.ANYWHERE)).add(
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

	@Override
	public Long getAllDocumentCount() {
		Session session = sessionFactory.openSession();
		Long count = (Long) session.createCriteria(Metadata.class)
				.setProjection(Projections.rowCount()).uniqueResult();

		return count;
	}

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
		System.out.println("\n eimai sto daolmpl\n");
		if (Utils.fileAlreadyExists(fileStorePath, fileName)) {
			throw new IOException("File with similar name already exists.");
		}

		if (!dir.isDirectory()) {
			throw new IOException(dir.getAbsolutePath()
					+ " is not a directory.");
		}

		File path = new File(fileStorePath + "\\" + fileName);
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(multipartFile.getBytes());

		// save metadata.
		log.info("Saving metadata");
		Session session = sessionFactory.openSession();
		Long metadataId = (Long) session.save(metadata);
		log.info("Metadata saved with id : " + metadataId);
		log.info("Leaving saveFileToRepo method in DAO");

	}

	@Override
	public List<User> getAllUsers() {
		Session session = sessionFactory.openSession();
		
		org.hibernate.Query query = (org.hibernate.Query) session.createQuery("from User");
		
		List<User> lst = query.list();
		
		return lst;
	}
}
