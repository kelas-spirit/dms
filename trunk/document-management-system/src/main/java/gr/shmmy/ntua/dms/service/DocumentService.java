package gr.shmmy.ntua.dms.service;

import gr.shmmy.ntua.dms.dao.DocumentDao;
import gr.shmmy.ntua.dms.dao.UserDao;
import gr.shmmy.ntua.dms.domain.Metadata;
//import gr.shmmy.ntua.dms.utils.Constants;
import gr.shmmy.ntua.dms.web.formBean.DocumentPostFormBean;
import gr.shmmy.ntua.dms.web.formBean.UserRegFormBean;
import gr.shmmy.ntua.dms.domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

	@Autowired
	private UserDao userDao;
	
	@Autowired
    private JavaMailSender mailSender;
	
	//private <gr.shmmy.ntua.dms.domain.User> User NewUser= new User();
	
	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}

	public void setFileStorePath(String fileStorePath) {
		this.fileStorePath = fileStorePath;
	}
	
	@Transactional
	public List<Metadata> getUsersDocuments(Long foldId) {
		List<Metadata> lst = new ArrayList<Metadata>();

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();
		
		Collection<GrantedAuthority> roles = user.getAuthorities();
		
		lst = (List<Metadata>) documentDao.getDocuments(50, user.getUsername(),
				roles,foldId);

		return lst;
	}
	@Transactional
	public List<Metadata> getUsersDocuments2(Long foldId) {
		List<Metadata> lst = new ArrayList<Metadata>();

		/*Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();
		
		Collection<GrantedAuthority> roles = user.getAuthorities();*/
		
		lst = (List<Metadata>) documentDao.getDocuments2(foldId);

		return lst;
	}
	
	@Transactional
	public List<PublicData> getPublicDocuments(){
		List<PublicData> lst = new ArrayList<PublicData>();
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();
		lst = (List<PublicData>)documentDao.getPublicFiles(user.getUsername());
		
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
	public PublicData getPublicDocumentById(Long docId) {
		//1. Get document's metadata
		PublicData publicdata = new PublicData();
		publicdata = documentDao.getPublicDocumentById(docId);
		//2. Get document from file system.
		
		return publicdata;
	}
	
	@Transactional
	public List<Metadata> getDocumentsBySearchQuery(String searchQuery) {
		List<Metadata> lst = new ArrayList<Metadata>();

		lst = documentDao.getDocumentsBySearchQuery(searchQuery);
		return lst;
	}
	
	public void deleteDocument(Long parrentId, String documentFileName){
		List<Metadata> lst=documentDao.getDocByDocNameAndParrentId(documentFileName,parrentId);
		
		for(Metadata mtd: lst){
			documentDao.deleteDocument(mtd.getMetadataId());
		}
		
	}

	@Transactional
	public void deleteDocument(Long documentId) {
		log.info("Inside service delete method.<<<<<<<<<<<<<<<<<<");
		
		documentDao.deleteDocument(documentId);
	}
	@Transactional
	public void deletePublicDocument(Long documentId) {
		log.info("Inside service public delete method.<<<<<<<<<<<<<<<<<<");
		documentDao.deletePublicDocument(documentId);
	}

	public Long getAllDocumentCount() {
		return documentDao.getAllDocumentCount();
	}

	@Transactional
	public void previousVersion(String documentFileName, Long parrentId){
		List<Metadata> lst=documentDao.getDocByDocNameAndParrentId(documentFileName,parrentId);
		
		for(Metadata ls: lst){
			if(ls.getEnabled()==0){
			Long id = ls.getMetadataId();
			System.out.println("get enabled einai 0,edw to thetoume 1");
			documentDao.setEnabled(1, id);
			//documentDao.deleteDocument(id);
			}
			if(ls.getEnabled()==1){
				System.out.println("get enabled einai 1 k to diagrafoume");
			Long docId =ls.getMetadataId();
			//documentDao.setEnabled(0, docId);
			//documentDao.setUpdtoPrevious(docId);
			documentDao.deleteDocument(docId);
			
			}
			
		}
		
	}
	
	public Metadata documentById(Long documentId){
		Metadata metadata = documentDao.documentById(documentId);
		return metadata;
	}
	
	/**
	 * Saves file to file system
	 * Poihsh by kelas!
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
		PublicData publicdata = new PublicData ();
		
		
		//IF IS PUBLIC
		if(formBean.getIsPublic()){
		//	finalFileStorePath = fileStorePath + "/" + Constants.PUBLIC_FOLDER;
			//metadata.setOwner(Constants.DOC_TYPE_PUBLIC);
			
			publicdata.setCreateUser(user.getUsername());
			publicdata.setDocumentFileName(multipartFile.getOriginalFilename());
			publicdata.setDocumentType(multipartFile.getContentType());
			publicdata.setOwner(user.getUsername());
			publicdata.setDocumentSize(multipartFile.getSize());
			try {
				documentDao.savePublicFile(multipartFile, publicdata);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		//end of IF IS PUBLIC
		//else{
			finalFileStorePath = fileStorePath + "/" + username;
			metadata.setOwner(user.getUsername()); 
			
			
			metadata.setComments(formBean.getComments());
			//metadata.setCreatedDate(new Date());
			metadata.setCreateUser(user.getUsername());
		//	metadata.setKeywords(formBean.getKeywords());
			metadata.setSubject(formBean.getSubject());
			metadata.setDocumentType(multipartFile.getContentType());
			metadata.setDocumentFileName(multipartFile.getOriginalFilename());
			metadata.setDocumentSize(multipartFile.getSize());
			metadata.setParrentId(formBean.getParrentId());
			metadata.setEnabled(1);
			
			
			/*
			 * POIHSH!!!!!
			 * */
			List<Metadata> lst=documentDao.getDocByDocNameAndParrentId(metadata.getDocumentFileName(),metadata.getParrentId());
			if(lst.size()==0){
				System.out.println("Mpika gia prwti fora");
				metadata.setCreatedDate(new Date());
				metadata.setUpdatedDate(null);
				try {
					documentDao.saveFileToRepo(multipartFile, finalFileStorePath, metadata);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			if(lst.size()==1){
				System.out.println("Mpika gia deuteri fora, ara tha kanoume update to : ");
				for(Metadata ls: lst){
					System.out.println(ls.getMetadataId());
					Long id= ls.getMetadataId();
					documentDao.setEnabled(0, id);
				}
				metadata.setUpdatedDate(new Date());
				try {
					documentDao.saveFileToRepo(multipartFile, finalFileStorePath, metadata);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}
			if(lst.size()==2){
				
				for(Metadata ls: lst){
					if(ls.getEnabled()==0){
					Long id =	ls.getMetadataId();
					System.out.println("get enabled einai 0");
					
					documentDao.deleteDocument(id);
					}
					if(ls.getEnabled()==1){
						System.out.println("get enabled einai 1");
					Long docId =ls.getMetadataId();
					documentDao.setEnabled(0, docId);
					documentDao.setUpdtoPrevious(docId);
					
					}
					
				}
				metadata.setUpdatedDate(new Date());
				try {
					documentDao.saveFileToRepo(multipartFile, finalFileStorePath, metadata);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
			}
			
			
			

			log.info("Content Type :" + multipartFile.getContentType());
			log.info("Name :" + multipartFile.getName());
			log.info("Original File Name :"
					+ multipartFile.getOriginalFilename());
			log.info("Size :" + multipartFile.getSize());
			log.info("Is Empty :" + multipartFile.isEmpty());
			
			
		
		log.info("Entering saveFileToRepo method, File will be saved to "
				+ fileStorePath);
		/*	try {
			documentDao.saveFileToRepo(multipartFile, finalFileStorePath, metadata);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}*/
		
	//}
  }

	
	public void editBlob(String bytes, Long documentId,String username, String documentFileName){
		documentDao.editBlob(bytes, documentId);
		//send mail:
		
		SimpleMailMessage email = new SimpleMailMessage();
		Long id=userDao.idFromUsername(username);
		System.out.println("eftasa edw k  user id einai "+id);

		gr.shmmy.ntua.dms.domain.User user = new gr.shmmy.ntua.dms.domain.User();
		 user = userDao.userById(id);
		System.out.println("eftasa edw k  user id einai "+user.getUserId());
		
		//print email's data
		 System.out.println("To: " + user.getEmail());
	       // System.out.println("Subject: " + s);
	    //    System.out.println("Message: " + message);
		
		
	//	SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("File updated");
        email.setText("Hello,"+username+"! Your file <"+documentFileName+"> was updated!");
		
        mailSender.send(email);
	}
	
	
	/*public List<gr.shmmy.ntua.dms.domain.User> getAllUsers() {
		return documentDao.getAllUsers();
	}*/
	
}
