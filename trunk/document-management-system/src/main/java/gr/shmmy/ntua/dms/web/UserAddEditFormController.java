package gr.shmmy.ntua.dms.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
//import javax.validation.constraints.
import javax.persistence.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException; 
import org.hibernate.Query;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;

import gr.shmmy.ntua.dms.service.DocumentService;
import gr.shmmy.ntua.dms.service.UserService;
//import gr.shmmy.ntua.dms.utils.Constants;
import gr.shmmy.ntua.dms.web.formBean.UserRegFormBean;
import gr.shmmy.ntua.dms.domain.*;

@Controller
@RequestMapping("/user")
public class UserAddEditFormController{

	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private DocumentService documentService;
	private User user;
	private Session session;
	private SessionFactory sessionFactory;
	//private UserService userService;
	
	@Autowired
	private UserService userservice;
		
	public void setUserService(UserService userservice) {
		this.userservice = userservice;
	}
	
	
	
	
	
	@RequestMapping(value="/reg", method = RequestMethod.GET)
	public ModelAndView prepareUserRegForm(){
		 System.out.println("eimaste sto get");
		
		ModelAndView mav = new ModelAndView();
		
		UserRegFormBean formBean = new UserRegFormBean();
		formBean.setRoles(new String("ROLE_USER"));
		mav.addObject("formBean", formBean);
		mav.addObject("users", userservice.getAllUsers());
		mav.setViewName("userAddEditForm");
		return mav;
	}
	
	@RequestMapping(value="/reg", method = RequestMethod.POST)
	
	public ModelAndView processUserRegForm(@ModelAttribute("formBean") @Valid UserRegFormBean formBean,
			BindingResult bindingResult,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.addObject("users", userservice.getAllUsers());
		String userErr = " ";
		String RePassErr = " ";
		log.info(formBean);
		//System.out.println("eimaste sto post1");
		// check if both passwords match
		if(formBean.getPassword() == null || !formBean.getPassword().equals(formBean.getRePassword()))
		{
			bindingResult.addError(new ObjectError("password", "Passwords do not match"));
			RePassErr = "Passwords do not match";
		}
		if(FindUser(formBean.getUsername())){
			 System.out.println("true-uparxei idi o user");
			 bindingResult.addError(new ObjectError("username", "username exist"));
			 userErr = "username already exist";
			 
		}
		else 
			if(!FindUser(formBean.getUsername())){
			//	 System.out.println("false- den uparxei,opote kane ta magika s :P");
				 userErr = " ";
				 
			}
		
		if(bindingResult.hasErrors()){
			log.info("************* form has errors : "+ bindingResult.getErrorCount());
			
			for(ObjectError e : bindingResult.getAllErrors()){
				log.info(e.getObjectName()+" :::: "+ e.getDefaultMessage());
			}
			
			log.info(bindingResult.getAllErrors().toString());
			request.setAttribute("userErr", userErr);
			request.setAttribute("RePassErr", RePassErr);
			
			/* form bean */
			mav.addObject("formBean", formBean);
			/* view */
			mav.setViewName("userAddEditForm");
			mav.addObject(bindingResult);
			return mav;
		}
		
		
		mav.addObject("formBean", formBean);	
		if(!bindingResult.hasErrors()){
			System.out.println("mpika k edw1 :P");
			userservice.insertUser(formBean);
			//mav.addObject("hdsjk",userservice.insertUser(formBean));
			System.out.println("mpika k edw2 :P");
		}
		
		
		
		//System.out.println(ls.get(ls.size()-1));
		mav.setViewName("userAddEditForm");
		

		return mav;
	}

	public boolean FindUser (String username){
		boolean flag=false;
		List<User> list= userservice.getAllUsers();
		
		for (int i=0; i<list.size();i++)
		    if (list.get(i).getUsername().equals(username)){
		    	flag=true;
		    }
		return flag;
		
	}
	
	
	
	
}