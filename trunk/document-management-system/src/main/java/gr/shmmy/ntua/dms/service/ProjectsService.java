package gr.shmmy.ntua.dms.service;

import gr.shmmy.ntua.dms.dao.ProjectsDao;
import gr.shmmy.ntua.dms.domain.Projects;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class ProjectsService {
    
	@Autowired
	private ProjectsDao projectsDao;
	
	@Transactional
	public List<Projects> myProjectListByFolderId(Long folderId,String role) {
		List<Projects> usq = new ArrayList<Projects>();

		usq = projectsDao.myProjectListByFolderId(folderId, role);
		return usq;
	}
	@Transactional
	public List<Projects> myProjectListByFolderId(Long folderId) {
		List<Projects> usq = new ArrayList<Projects>();

		usq = projectsDao.myProjectListByFolderId(folderId);
		return usq;
	}
	
	public void wpleader(String username, Long folderId,String authority){
		projectsDao.wpleader(username, folderId, authority);
	}
	public void deleteFromProjects(Long projectId){
		projectsDao.deleteFromProjects(projectId);
	}
	
	
	public boolean exist (String username, Long folderId,String role){
	return	projectsDao.exist(username, folderId, role);
	}
	public boolean exist( Long folderId,String role){
		List<Projects> usq = new ArrayList<Projects>();

		usq=projectsDao.myProjectListByFolderId(folderId, role);
		if(usq.size()>0){
			return true;
		}
		else{
		return false;
		}
	}
	@Transactional
	public List<Projects> myProjectsView(String username,String role){
		List<Projects>lst = new ArrayList<Projects>();
		lst = projectsDao.myProjectList(username,role);
		
		return lst;
		
	}
	
	@Transactional
	public List<Projects> myProjectsView(String role){
		List<Projects>lst = new ArrayList<Projects>();
		lst = projectsDao.myProjectList(role);
		
		return lst;
		
	}
	public void updateWPuser(Long projectId,String username){
		projectsDao.updateWPuser(projectId, username);
		
	}
	
}
