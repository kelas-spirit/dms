package gr.shmmy.ntua.dms.dao;

import gr.shmmy.ntua.dms.domain.Projects;

import java.util.List;

public interface ProjectsDao {
	//list of projects by id and role
	public List<Projects> myProjectListByFolderId(Long folderId, String role);
//list of projects by id
	public List<Projects> myProjectListByFolderId(Long folderId);
	//if project exist
	public boolean exist (String username,Long id2,String role);
	//save wp by name and id
	public void wpleader(String username, Long folderId,String authority);
	//delete project by id
	public void deleteFromProjects(Long id);
	//lista apo prjects
	public List<Projects> myProjectList(String username, String role);

	public List<Projects> myProjectList( String role);

	public void updateWPuser(Long projectId,String username);

}
