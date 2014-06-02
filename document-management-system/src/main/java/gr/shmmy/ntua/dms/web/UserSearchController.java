package gr.shmmy.ntua.dms.web;

import gr.shmmy.ntua.dms.domain.User;
import gr.shmmy.ntua.dms.service.UserService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class UserSearchController {
	@Autowired
	private UserService userservice;
	
	public void setUserService(UserService userservice){
		this.userservice=userservice;
	}

	@RequestMapping(value = "/searchuser", method = RequestMethod.POST)
	public ModelAndView searchUser(@RequestParam("q") String searchUserQuery){
		
		ModelAndView mav = new ModelAndView();
		System.out.println("tsa!");

		//UsersListController.upbean.setUsername(searchUserQuery);
		UsersListController.usernamePath=searchUserQuery;
		System.out.println("username"+UsersListController.usernamePath);

		mav.setViewName("redirect:searchuser");
		return mav;
	}
	
@RequestMapping(value = "/searchuser", method = RequestMethod.GET)
public ModelAndView searchUserGet(){
		
		ModelAndView mav = new ModelAndView();
		
		System.out.println("username"+UsersListController.usernamePath);
		List<User> usq = userservice.getUserBySearchQuery(UsersListController.usernamePath);
		
		mav.addObject("usq", usq);
		mav.addObject("q",UsersListController.usernamePath);
		mav.setViewName("userSearch");
		return mav;
	}
	
	
	
	
	
	
}
