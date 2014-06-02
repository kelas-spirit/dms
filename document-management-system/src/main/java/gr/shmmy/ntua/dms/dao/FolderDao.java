package gr.shmmy.ntua.dms.dao;

import gr.shmmy.ntua.dms.domain.Folder;

import java.io.IOException;
import java.util.List;

public interface FolderDao {
	
	public void saveFolderToRepo(String path,Folder folder)throws IOException ;

	public List<Folder> getFoldersForDelete(String username,Long folderId);
	public List<Folder> getFolders(String username, Long PARRENT_ID);
	public List<Folder> getFolders2( Long PARRENT_ID);
	public List<Folder> getFolders3( Long folderId,Long parentId);
	public List<Folder> getFolders4(String folderName, Long PARRENT_ID);
   
	
	public List<Folder> getFolderById(Long folderId);
	//return list with criteria folder id
	public List<Folder> folderExistInSystem(Long folderId);
	public void deleteFolder(Long folderId);
	public String owner(Long folderId);

}
