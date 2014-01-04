package gr.shmmy.ntua.dms.service;

import gr.shmmy.ntua.dms.dao.DocumentDao;
import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.utils.Constants;
import gr.shmmy.ntua.dms.web.formBean.DocumentPostFormBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public class DocumentService {

	private final Logger log = Logger.getLogger(this.getClass());

	@Value("${doc.repo.path}")
	private String fileStorePath;

	@Autowired
	private DocumentDao documentDao;

	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}

	public void setFileStorePath(String fileStorePath) {
		this.fileStorePath = fileStorePath;
	}

	@Transactional
	public List<Metadata> getUsersDocuments() {
		List<Metadata> lst = new ArrayList<Metadata>();

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();
		
		Collection<GrantedAuthority> roles = user.getAuthorities();
		
		lst = (List<Metadata>) documentDao.getDocuments(50, user.getUsername(),
				roles);

		return lst;
	}

	@Transactional
	public Metadata getDocumentById(Long docId) {
		//1. Get document's metadata
		Metadata meta = new Metadata();
		meta = documentDao.getDocumentById(docId);
		//2. Get document from file system.
		
		return meta;
	}

	@Transactional
	public List<Metadata> getDocumentsBySearchQuery(String searchQuery) {
		List<Metadata> lst = new ArrayList<Metadata>();

		lst = documentDao.getDocumentsBySearchQuery(searchQuery);
		return lst;
	}

	@Transactional
	public void deleteDocument(Long documentId) {
		log.info("Inside service delete method.<<<<<<<<<<<<<<<<<<");
		documentDao.deleteDocument(documentId);
	}

	public Long getAllDocumentCount() {
		return documentDao.getAllDocumentCount();
	}

	/**
	 * Saves file to file system
	 * 
	 * @param multipartFile
	 */

	@Transactional
	public void saveFileToRepo(MultipartFile multipartFile, DocumentPostFormBean formBean) {
		log.info("Entering saveFileToRepo in DAO");
		String finalFileStorePath = "";
		/*
		 * Full path for logged-in users documents will be the username appended
		 * by base path of repo.
		 */
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();
		String username = user.getUsername();
		if (username == null || username == "") {
			throw new RuntimeException(
					"Unable to get Logged-in user's name. Document can not be saved.");
		}
		System.out.println("\n eimai sto documentService\n");
		Metadata metadata = new Metadata();
		metadata.setComments(formBean.getComments());
		metadata.setCreatedDate(new Date());
		metadata.setCreateUser(user.getUsername());
		metadata.setKeywords(formBean.getKeywords());
		metadata.setSubject(formBean.getSubject());
		metadata.setDocumentType(multipartFile.getContentType());
		metadata.setDocumentFileName(multipartFile.getOriginalFilename());
		metadata.setDocumentSize(multipartFile.getSize());

		log.info("Content Type :" + multipartFile.getContentType());
		log.info("Name :" + multipartFile.getName());
		log.info("Original File Name :"
				+ multipartFile.getOriginalFilename());
		log.info("Size :" + multipartFile.getSize());
		log.info("Is Empty :" + multipartFile.isEmpty());
		
		if(formBean.getIsPublic()){
			finalFileStorePath = fileStorePath + "\\" + Constants.PUBLIC_FOLDER;
			metadata.setOwner(Constants.DOC_TYPE_PUBLIC);
		}else{
			finalFileStorePath = fileStorePath + "\\" + username;
			metadata.setOwner(user.getUsername()); 
		}
		
		log.info("Entering saveFileToRepo method, File will be saved to "
				+ fileStorePath);
		try {
			documentDao.saveFileToRepo(multipartFile, finalFileStorePath, metadata);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public List<gr.shmmy.ntua.dms.domain.User> getAllUsers() {
		return documentDao.getAllUsers();
	}
	
}
