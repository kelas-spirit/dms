package gr.shmmy.ntua.dms.web;

import gr.shmmy.ntua.dms.service.DocumentService;
import gr.shmmy.ntua.dms.service.FolderService;
import gr.shmmy.ntua.dms.service.UserService;
import gr.shmmy.ntua.dms.web.formBean.ProjectPostFormBean;
import gr.shmmy.ntua.dms.web.formBean.UserPostBean;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import gr.shmmy.ntua.dms.domain.*;

@Controller

public class UsersListController {
	protected static Logger logger = Logger.getLogger("controller");
	
	//String for error message
	
	
	
	@Autowired
	private UserService userService;
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private FolderService folderService;
	
	public static String usernamePath;
	//private DocumentService documentService;
	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;
	
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
	}
	
	@RequestMapping(value="/users", method = RequestMethod.GET)
	public ModelAndView processUserListView(){
		
		
		
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("users", userService.getAllUsers());
		mav.setViewName("UsersList");
		return mav;
		
	}
	@RequestMapping(value="/tmlist", method = RequestMethod.GET)
	public ModelAndView processUserListViewTM(){
		
		
		
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("users", userService.getAllUsers());
		mav.setViewName("ListForTM");
		return mav;
		
	}
	
	@RequestMapping(value="/tmlistpost", method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView processUserListViewTMPostGet(
			@RequestParam("username") String  username,
			@RequestParam("email") String  email){
		
		
		
		
		ModelAndView mav = new ModelAndView();
		//mav.addObject("users", userService.getAllUsers());
		mav.addObject("username", username);
		mav.addObject("email", email);
		mav.setViewName("MailForm");
		return mav;
		
	}
	@RequestMapping(value="/tmlistpostq", method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView processUserListViewTMPsearch(
			@RequestParam("q") String  username
			){
		
		Long id = userService.userId(username);
		User user = userService.findUserById(id);
		
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("username", user.getUsername());
		mav.addObject("email", user.getEmail());
		mav.setViewName("MailForm");
		return mav;
		
	}
	
	@RequestMapping(value="/public/registration", method = RequestMethod.GET)
	public ModelAndView publicUserRegForm(){
		ModelAndView mav = new ModelAndView();
		System.out.println("I'm Here");
		
		mav.setViewName("Registration");
		return mav;
	}
	
	@RequestMapping(value="/public/registration", method = RequestMethod.POST)
	public ModelAndView publicUserRegFormPost(
			@RequestParam("Name")String firstname,
			@RequestParam("LastName")String lastname,
			@RequestParam("User")String username,
			@RequestParam("Email")String email,
			@RequestParam("Password")String password
			){
		ModelAndView mav = new ModelAndView();
		UserPostBean userbean= new UserPostBean();
		userbean.setFirstname(firstname);
		userbean.setLastname(lastname);
		userbean.setUsername(username);
		userbean.setEmail(email);
		userbean.setPassword(password);
		
		boolean userExist= userService.userExist(username);
		if(userExist==true){
			mav.setViewName("RegistrationError");
		}else{
		
		
		userService.inserFromPublicReg(userbean);
		
		mav.setViewName("redirect:/docs");
		}
		return mav;
	}
	
	
	@RequestMapping(value="/chat", method = RequestMethod.GET)
	public ModelAndView processUserPublicChat(){
		
		//Authentication auth = SecurityContextHolder.getContext()
		//		.getAuthentication();
		//User username = (User) auth.getPrincipal();
		
		ModelAndView mav = new ModelAndView();
	//	mav.addObject("username", username.getUsername());
		mav.setViewName("Chat");
		return mav;
		
	}
	
	@RequestMapping(value="/usersonline", method = RequestMethod.GET)
	public ModelAndView onlineUserListView(){
		
		List<Object> principals = 
			sessionRegistry.getAllPrincipals();
		

		List<User> lst = new ArrayList<User>();
		
		if((principals.size()==0)){
			System.out.println("is empty");
		}else{
			System.out.println("is Not empty");

		}
		String[] array = new String[principals.size()];
		System.out.println("size:"+array.length);

		List<String> usersNamesList = new ArrayList<String>();
		List<String> usersList = new ArrayList<String>();
		int index = 0;
		//lst = <User>principals.get(0);
		for (Object principal: principals) {
			
		    if (principal instanceof User) {
		        usersNamesList.add(((User) principal).getUsername());
		       // System.out.println("vrethike o user: "+((User) principal).getUsername());
		    }else{
		    	usersNamesList.add(index, principal.toString());
		    	System.out.println("vrethike o user:"+usersNamesList.get(index));
		    //	lst.get(index).setUsername(stringToUser(principal.toString()));
		    	usersList.add(index, stringToUser(principal.toString()));
		    	System.out.println("added to user"+usersList.get(index));
		    	index++;
		    }
			
			
		}
		
		  //  System.out.println( usersNamesList.get(0).toString());
		
		
		//System.out.println("meta");
		ModelAndView mav = new ModelAndView();
		mav.addObject("usersNamesList", usersList);
		mav.setViewName("UsersOnline");
		return mav;
	}
	
	@RequestMapping(value = "/setnewrole", method = RequestMethod.POST)
	public ModelAndView setUserRole(@RequestParam("role") List<String> roles,
			@RequestParam("userId") Long userId,@RequestParam("username")String username, HttpServletRequest request){
			
		System.out.println("userId: "+userId);
		for(int i=0;i<roles.size();i++){
			//
			if(!(userService.roleExist(userId, roles.get(i)))){
			userService.saveUserRole(userId, roles.get(i));
			}
		}
		
		
		usernamePath=username;
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("redirect:searchuser");
		
		
		return mav;
	}
	
	//delete roles from admin
	@RequestMapping(value = "/deleterole", method = RequestMethod.GET)
	public ModelAndView DeleteRoleById(@RequestParam("id") Long id){
		ModelAndView mav = new ModelAndView();
		userService.DeleteRoleById(id);
	//	System.out.println("to id: "+id);
		//System.out.println("to username: ");
		mav.setViewName("redirect:searchuser");		
		return mav;
		
	}
	
	
	public String stringToUser(String path){
		String username="";
		boolean flag=false;
		StringTokenizer st = new StringTokenizer(path);
		System.out.println("mpika re :)");

		
		while (st.hasMoreTokens()){
			if(flag==true){
				username=st.nextToken();
				System.out.println("username:)"+username);
				flag=false;
			}
			
			
			if(st.nextToken().equals("Username:")){
				flag=true;
			}
		}
		
		
		return username.replace(";", "");
	}
	
	
	

	
	
	
	
}
