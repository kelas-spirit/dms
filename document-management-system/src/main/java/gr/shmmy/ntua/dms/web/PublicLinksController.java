package gr.shmmy.ntua.dms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PublicLinksController {

	@RequestMapping(value="public/disclaimer", method = RequestMethod.GET)
	public String showDisclaimer(){
		return "disclaimer";
	}
}
