package gr.shmmy.ntua.dms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	@RequestMapping("test")
	public String test(){
		
		return "siteTemplate";
	}
	
	@RequestMapping("test1")
	public String test1(){
		
		return "fullSiteTemplate";
	}
}
