package gr.shmmy.ntua.dms.web;

import gr.shmmy.ntua.dms.domain.Folder;
import gr.shmmy.ntua.dms.domain.Projects;
import gr.shmmy.ntua.dms.service.DocumentService;
import gr.shmmy.ntua.dms.service.FolderService;
import gr.shmmy.ntua.dms.service.ProjectsService;
import gr.shmmy.ntua.dms.service.UserService;
import gr.shmmy.ntua.dms.web.formBean.ProjectPostFormBean;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProjectsController {
	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ProjectsService projectsService;
	@Autowired
	private FolderService folderService;
	@Autowired
	private UserService userService;
	
	private ProjectPostFormBean ppfbean;


	private String userNotExist="";
	private String task_partExist="";
	private String userName;
	private String errorString="";
	public static String usernamePath;

	
	
	
	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
	}
	
	public void setProjectsService(ProjectsService projectsService) {
		this.projectsService = projectsService;
	}
	
	/*dffwpl: delete folder from wp list
	 * 
	 * */
	@RequestMapping(value = "/dffwpl", method = RequestMethod.GET)
	public ModelAndView deleteAllProjectByFolderId(@RequestParam("id") Long folderId){
		ModelAndView mav = new ModelAndView();
		List<Projects>prj = projectsService.myProjectListByFolderId(folderId);
		
		for(Projects pr: prj){
			projectsService.deleteFromProjects(pr.getProjectId());
		}
		List<Projects>findUser = projectsService.myProjectListByFolderId(folderId,"ROLE_WORKPACKAGE_LEADER");
		//find username
		for(Projects find: findUser){
			userName=find.getUsername();
		}
		//userName=findUser.get(2).toString(); //userName is localPath gia na pigenei sto wpleader fold list
///searchuser/wpleaderlist
		System.out.println("userName: "+userName);
		mav.setViewName("redirect:/searchuser/wpleaderlist");		
		return mav;
		
	}
	
	//this for delete wp_leader folder
		@RequestMapping(value = "/wpprojectdelete", method = RequestMethod.GET)
		public ModelAndView wpdeleteFromProjects(@RequestParam("id") Long id){
			ModelAndView mav = new ModelAndView();
			projectsService.deleteFromProjects(id);
			System.out.println("to id: "+id);
			mav.setViewName("redirect:/projects");		
			return mav;
			
		}
	
	@RequestMapping(value = "/taskprojectdelete", method = RequestMethod.GET)
	public ModelAndView deleteFromProjects(@RequestParam("id") Long id){
		ModelAndView mav = new ModelAndView();
		projectsService.deleteFromProjects(id);
		//System.out.println("to id: "+id);
		mav.setViewName("redirect:/projects");		
		return mav;
		
	}
	//this for delete task_p folder
		@RequestMapping(value = "/projectdelete", method = RequestMethod.GET)
		public ModelAndView deleteFromProjectsTaskList(@RequestParam("id") Long id){
			ModelAndView mav = new ModelAndView();
			projectsService.deleteFromProjects(id);
			//System.out.println("to id: "+id);
			mav.setViewName("redirect:/projects/taskparticipantlist");		
			return mav;
			
		}
	
	
	/*
	 *  list from users project (wp or task)
	 * */
	
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public ModelAndView getProjectView(Authentication auth){
		
		ModelAndView mav = new ModelAndView();
		//List<Projects>lst= documentService.myProjectsView(userId);
		//List<User> usq = userservice.getUserBySearchQuery(auth.getName());
		//list when user is wp leader
		List<Projects>fold= projectsService.myProjectsView(auth.getName(),"ROLE_WORKPACKAGE_LEADER");
		//list when user is task 
		List<Projects>task= projectsService.myProjectsView(auth.getName(),"TASK_PARTICIPANT");
		mav.addObject("fold",fold);
		mav.addObject("task",task);
		mav.setViewName("Projects");
		
		
		return mav;
	}
	
	
	
	/*
	 * show list with wpleader folders
	 * 
	 * 
	 * */
	@RequestMapping(value = "/forsearchwpfold", method = RequestMethod.POST)
	public ModelAndView wpleaderList(@RequestParam("username")String username){
		ModelAndView mav = new ModelAndView();
		userName= username;

		mav.setViewName("redirect:searchuser/wpleaderlist");

		return mav;
	}
	/*
	 * Here the list of folders by wp leader
	 * */
	
	@RequestMapping(value = "/searchuser/wpleaderlist", method = RequestMethod.GET)
	public ModelAndView wpleaderList(){
		ModelAndView mav = new ModelAndView();
		//List<Projects>lst= documentService.myProjectsView(userId);
		//List<User> usq = userservice.getUserBySearchQuery(auth.getName());
		//list when user is wp leader

		List<Projects>fold= projectsService.myProjectsView(userName,"ROLE_WORKPACKAGE_LEADER");
		System.out.println("sto get2");

		//list when user is task 
		mav.addObject("username", userName);
		mav.addObject("errorString", errorString);
		mav.addObject("fold",fold);
		mav.setViewName("WPList");
		return mav;
	
	}
	
	
	/*
	 * apo /projets stelnoume kapoia dedomena gia na to steiloume sto /projects/fortaskparticipantlist
	 * method post
	 * */
	@RequestMapping(value = "/projects/fortaskparticipantlist", method = RequestMethod.POST)
	public ModelAndView prepareTaskParticipantList(@RequestParam("folderId")Long folderId){
		
		ppfbean=new ProjectPostFormBean();
		ppfbean.setFolderId(folderId);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/projects/taskparticipantlist");
		return mav;
		
	}
	
	/*
	 * epistrefoume tin lista me tous users pou einai task participant gia kapoio sugkekrimeno folder
	 * method get
	 * */
	@RequestMapping(value = "/projects/taskparticipantlist", method = RequestMethod.GET)
	public ModelAndView prepareTaskParticipantList(){
		
		ModelAndView mav = new ModelAndView();
		System.out.println("get folder id"+ppfbean.getFolderId());
		List<Projects>prjtp = projectsService.myProjectListByFolderId(ppfbean.getFolderId(), "TASK_PARTICIPANT");
		mav.addObject("userNotExist", userNotExist);//string userNotExist
		mav.addObject("task_partExist", task_partExist); //string wp_leaderExist
		mav.addObject("prjtp", prjtp);
		mav.addObject("folderId", ppfbean.getFolderId());
		
		mav.setViewName("TaskParticipantList");
		return mav;
	}
	
	/*
	 * Edw pairnoume to username tou xristi k to folder id tou project sto opoio tha einai
	 * wp leader
	 * */
	@RequestMapping(value = "/setwpleadere", method = RequestMethod.POST)
	public ModelAndView prepareWPleader(@RequestParam("userId")Long userId,@RequestParam("username")String username,
			@RequestParam("folderId")Long folderId, @RequestParam("authority")String authority,
			HttpServletRequest request){
		/*System.out.println("to username: "+username);
		System.out.println("to id: "+userId);
		System.out.println("to idf: "+folderId);
		System.out.println("to auth: "+authority);*/
		ModelAndView mav = new ModelAndView();
		usernamePath=username;		
		userName=username;
		String str = "The folder is missing OR  wp-leader for current folder is already exist";
		boolean folderExistInSystem = folderService.findFolder(folderId);
	//	boolean flag=userService.exist(username, folderId,"ROLE_WORKPACKAGE_LEADER");
		boolean foldANDRoleExist= projectsService.exist(folderId, "ROLE_WORKPACKAGE_LEADER");
		if((foldANDRoleExist==true)||(folderExistInSystem==false)){
			System.out.println("true!");
			errorString="The folder is missing OR  wp-leader for current folder is already exist";
		
		}
		else{
			System.out.println("false");
			str=" ";
			errorString="";
		//	request.setAttribute("str", str);
		projectsService.wpleader(username, folderId, authority);
		}
		
		//to do: redirect sto user search
		mav.setViewName("redirect:/searchuser/wpleaderlist");
		
		
		
		return mav;
	}
	/*
	 * edw thetoume task participant k id panw sto opoio tha doulevei
	 * exoume arketa if-else logo tou oti theloume sto GET na emfanizoume swsta ta errors
	 * */
	@RequestMapping(value = "/settaskpr", method = RequestMethod.POST)
	public ModelAndView prepareWPleader(@RequestParam("username")String username,@RequestParam("folderId")Long folderId,
			HttpServletRequest request){
		Long id;
		boolean projectroleflag=true;
		boolean flag=userService.userExist(username);
	//	boolean roleflag=false;
		if(flag==false){
			System.out.println("user not exist");
			userNotExist = "Username is not exist";
			task_partExist=" ";

			//request.setAttribute("str", str);
			//projectroleflag=true;
		}else
		if(flag==true){
			userNotExist=" ";
			projectroleflag=projectsService.exist(username, folderId, "TASK_PARTICIPANT");
		}
		if((projectroleflag==true)&&(flag==true)){
			//request.setAttribute("str2", str2);
			task_partExist = "The  current TASK PARTICIPANT is already exist";
		}
		else if(projectroleflag==false){
			task_partExist=" ";
			//request.setAttribute("str2", str2);
			id = userService.userId(username);
		boolean	roleflag=userService.roleExist(id, "TASK_PARTICIPANT");
			//System.out.println("to id mas"+id);
			if(roleflag==false){
			userService.saveUserRole(id, "TASK_PARTICIPANT");
			}
			System.out.println("folderId"+folderId);
			projectsService.wpleader(username, folderId, "TASK_PARTICIPANT");

		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:projects/taskparticipantlist");
		return mav;
		
	}
	
	
	
	/*
	 * WP FOLDERS
	 * 
	 * 
	 * */
	
	@RequestMapping(value = "/wpfolders", method = RequestMethod.GET)
	public ModelAndView wpfoldersList(){
		
		ModelAndView mav = new ModelAndView();
		List<Projects>lst = projectsService.myProjectsView("ROLE_WORKPACKAGE_LEADER");
		mav.addObject("lst", lst);
		mav.setViewName("WPfolders");

		return mav;
	}
	
	@RequestMapping(value = "/wpfolderdelete", method = RequestMethod.GET)
	public ModelAndView wpfolderDelete(@RequestParam("id")Long folderId){
		
		ModelAndView mav = new ModelAndView();
		List<Projects>prj = projectsService.myProjectListByFolderId(folderId);
		
		for(Projects pr: prj){
			projectsService.deleteFromProjects(pr.getProjectId());
		}
		mav.setViewName("redirect:wpfolders");

		return mav;
	}
	
	@RequestMapping(value = "/searchfolder", method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView wpfolderSearch(@RequestParam("q") Long folderId){
		
		ModelAndView mav = new ModelAndView();
		List<Projects>lst = projectsService.myProjectListByFolderId(folderId, "ROLE_WORKPACKAGE_LEADER");
		
		
		mav.addObject("lst", lst);
		mav.setViewName("WPfoldersSearch");

		return mav;
	}
	@RequestMapping(value = "/changewp", method = RequestMethod.POST)
	public ModelAndView wpfolderChange(@RequestParam("username")String username,@RequestParam("folderId") Long folderId,
			@RequestParam("projectId") Long projectId,HttpServletRequest request){
		
		ModelAndView mav = new ModelAndView();
		boolean alredyExist = projectsService.exist(username, folderId, "ROLE_WORKPACKAGE_LEADER");
	

		boolean userExist = userService.userExist(username);
		boolean roleExist = userService.roleExist(userService.userId(username), "ROLE_WORKPACKAGE_LEADER");
		
	    if((alredyExist==true)||(!userExist)||(roleExist==false)){
	    //	mav.setViewName("redirect:"+referer);
	    	mav.setViewName("redirect:errors");
	    }
		
	    else{
	    	projectsService.updateWPuser(projectId, username);
		mav.setViewName("redirect:wpfolders");
	    }
		return mav;
	}
	@RequestMapping(value = "/errors", method = {RequestMethod.GET})
	public ModelAndView  errors(){
		ModelAndView  mav = new ModelAndView ();
    	mav.setViewName("Error");
		return mav;
	}

}
