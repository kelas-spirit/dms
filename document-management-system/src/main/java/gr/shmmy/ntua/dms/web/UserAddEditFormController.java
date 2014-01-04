package gr.shmmy.ntua.dms.web;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gr.shmmy.ntua.dms.service.DocumentService;
import gr.shmmy.ntua.dms.utils.Constants;
import gr.shmmy.ntua.dms.web.formBean.UserRegFormBean;

@Controller
@RequestMapping("/user")
public class UserAddEditFormController{

	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private DocumentService documentService;
		
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	
	@RequestMapping(value="/reg", method = RequestMethod.GET)
	public ModelAndView prepareUserRegForm(){
		ModelAndView mav = new ModelAndView();
		
		UserRegFormBean formBean = new UserRegFormBean();
		formBean.setRoles(new String[]{Constants.ROLE_USER});
		mav.addObject("formBean", formBean);
		mav.addObject("users", documentService.getAllUsers());
		mav.setViewName("userAddEditForm");
		return mav;
	}
	
	@RequestMapping(value="/reg", method = RequestMethod.POST)
	public ModelAndView processUserRegForm(@ModelAttribute("formBean") @Valid UserRegFormBean formBean,
			BindingResult bindingResult){
		ModelAndView mav = new ModelAndView();
		mav.addObject("users", documentService.getAllUsers());
		
		log.info(formBean);
		
		// check if both passwords match
		if(formBean.getPassword() == null || formBean.getPassword().equals(formBean.getRePassword()))
		{
			bindingResult.addError(new ObjectError("password", "Passwords do not match"));
		}
		
		if(bindingResult.hasErrors()){
			log.info("************* form has errors : "+ bindingResult.getErrorCount());
			
			for(ObjectError e : bindingResult.getAllErrors()){
				log.info(e.getObjectName()+" :::: "+ e.getDefaultMessage());
			}
			
			log.info(bindingResult.getAllErrors().toString());
			/* form bean */
			mav.addObject("formBean", formBean);
			/* view */
			mav.setViewName("userAddEditForm");
			mav.addObject(bindingResult);
			return mav;
		}
		
		//formBean.setRoles(new String[]{Constants.ROLE_USER});
		mav.addObject("formBean", formBean);
	
		mav.setViewName("userAddEditForm");
		return mav;
	}
}