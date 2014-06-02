package gr.shmmy.ntua.dms.dao;

import gr.shmmy.ntua.dms.domain.Folder;
import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.domain.Projects;
import gr.shmmy.ntua.dms.domain.PublicData;
import gr.shmmy.ntua.dms.domain.User;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentDao {

	public List<Metadata> getDocuments(int count, String username, Collection<GrantedAuthority> roles,Long foldId);
	public List<Metadata> getDocuments2(Long foldId);
	public List<Metadata>getDocByDocNameAndParrentId(String documentFileName, Long parrentId);

	//public List<PublicData>getPublicDocuments(String username);
	public void setEnabled(int enabled, Long metadataId);
	public void setUpdtoPrevious(Long metadataId);
	public Metadata getDocumentById(Long docId);
	
	//public void setCreatedDayAfterUpdate
	
	public PublicData getPublicDocumentById(Long docId);
	
	public List<Metadata> getDocumentsBySearchQuery(String searchQuery);
	
	public List<PublicData> getPublicFiles(String username);
	
	public void deleteByParrentId(Long parrentId);
	
	public void deleteDocument(Long documentId);
	
	public void deletePublicDocument(Long documentId);
	
	public Long getAllDocumentCount();
	
	public void saveFileToRepo(MultipartFile multipartFile, String fileStorePath, Metadata metadata) throws IOException;
	
	//save every file when it's public
	public void savePublicFile(MultipartFile multipartFile, PublicData publicdata)throws IOException;
	
	public Metadata documentById(Long documentId);
	
	public void editBlob(String bytes,Long documentId);
	
	//public String owner(Long folderId);
	
	//public List<Projects> myProjectListByFolderId(Long folderId, String role);

	//public List<User> getAllUsers();
}
