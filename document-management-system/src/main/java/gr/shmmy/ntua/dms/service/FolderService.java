package gr.shmmy.ntua.dms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import gr.shmmy.ntua.dms.dao.DocumentDao;
import gr.shmmy.ntua.dms.dao.FolderDao;
import gr.shmmy.ntua.dms.domain.Folder;
import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.web.formBean.FolderPostFormBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

public class FolderService {
	
	
	
	
	@Value("${doc.repo.path}")
	private String fileStorePath;
	
	@Autowired
	private FolderDao folderDao;
	@Autowired
	private DocumentDao documentDao;
	public void setFileStorePath(String fileStorePath) {
		this.fileStorePath = fileStorePath;
	}
	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}
	//save folder on local system
	@Transactional
	public void saveFolderToRepo(String name, FolderPostFormBean formbean){
		System.out.println("mpika sto saveFolderToRepo");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();
		String username = user.getUsername();
		if (username == null || username == "") {
			throw new RuntimeException(
					"Unable to get Logged-in user's name. Document can not be saved.");
		}
		String pathLong = String.valueOf(formbean.getId());
		String folderPath = "/"+formbean.getName();
		//String finalFileStorePath = "";
		System.out.println("eimai sto service k to id: "+formbean.getId());
		System.out.println("this my path"+folderPath);
		Folder folder = new Folder();
		folder.setParrentId(formbean.getId());
		folder.setFolderName(name);
		folder.setCreatedDate(new Date());
		folder.setCreateUser(user.getUsername());
		folder.setFolderPath(folderPath);
		folder.setPathId(formbean.getPathId()+" "+pathLong);
		//todo set folder parrent path
		
		try {
			System.out.println("sto try catch na dw an paizei kamia poustia");
			folderDao.saveFolderToRepo(folderPath, folder);
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
	}
	
	@Transactional
	public List<Folder> getUsersFolder(Long PARRENT_ID){
		
		List<Folder> lst = new ArrayList<Folder>();

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();
		
		lst = (List<Folder>) folderDao.getFolders(user.getUsername(), PARRENT_ID);
		
		return lst;
	}
	
	@Transactional
	public List<Folder> getUsersFolder2(Long PARRENT_ID){
		
		List<Folder> lst = new ArrayList<Folder>();

		/*Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();*/
		
		lst = (List<Folder>) folderDao.getFolders2( PARRENT_ID);
		
		return lst;
	}
	
	public boolean getUsersFolderExist(String folderName, Long parentId){
		
		List<Folder> lst = new ArrayList<Folder>();

		/*Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();*/
		
		lst = (List<Folder>) folderDao.getFolders4( folderName,parentId);
		if(lst.size()>0){
			return true;
		}else {
		return false;
		}
	}
	
	public List<Folder> getFolderById(Long folderId){
		List<Folder> lst = new ArrayList<Folder>();
		lst= folderDao.getFolderById(folderId);
		return lst;
	}
	
	public boolean findFolder(Long folderId){
		List<Folder> lst = null;
		lst= folderDao.getFolderById(folderId);
		if(lst.size()>0){
			return true;
		}
		else{
			return false;
		}
		
		
	}
	
	@Transactional
	public List<Folder>deleteFolderPrepare(Long folderId){
		List<Folder> lst = null;
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();
		//System.out.println("upopsifio pros delete: "+user.getUsername());

    	lst=folderDao.getFoldersForDelete(user.getUsername(), folderId);

		return lst;
	}
	@Transactional	
	public void deleteOneFolder(Long folderId){
		folderDao.deleteFolder(folderId);
		//documentDao.deleteByParrentId(folderId);
		//documentDao.deleteDocument(folderId);
	}
	
	@Transactional
    public void deleteFolder(String path,Long id,Long folderId){
    	Long foldId;
    	//Long min=id;
    	List<Metadata> lst=new ArrayList<Metadata>();
		System.out.println("tha ginei i diagrafi tou: "+id);
		boolean flag=false;
		StringTokenizer st = new StringTokenizer(path);
		while (st.hasMoreTokens()){
			foldId= Long.valueOf(st.nextToken());
			System.out.println("upopsifio pros delete: "+foldId);

			if((foldId.equals(id))){ //long is object,not primitive
				System.out.println("kai twra i diagrafi tou: "+foldId);
				flag=true;
				folderDao.deleteFolder(folderId);
				//documentDao.deleteDocument(id);
				
			}
		}
		
		//here delete all folder files contain
		if(flag==true){
			System.out.println("flag is true!");
			lst=documentDao.getDocuments2(folderId);
			if(lst.size()>0){
				
				for(Metadata ls:lst){
					System.out.println("diagrafete arxeio me id : "+ls.getMetadataId());

					documentDao.deleteDocument(ls.getMetadataId());
				}
				
			}
		}
		
		//folderDao.deleteFolder(id);

    	
    }
	
	public String owner(Long folderId){
		return folderDao.owner(folderId);
	}
	
	public String owner(){
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		User user = (User) auth.getPrincipal();
		return user.getUsername();
	}

}
