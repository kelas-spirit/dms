package gr.shmmy.ntua.dms.web;

import gr.shmmy.ntua.dms.service.DocumentService;
import gr.shmmy.ntua.dms.web.formBean.DocumentPostFormBean;
import gr.shmmy.ntua.dms.web.formBean.FolderPostFormBean;

import java.io.IOException;

import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Blob;

@Controller
@RequestMapping(value = "/")
public class DocumentFormController {

	SessionFactory sessionfactory;
	
	//protected FolderPostFormBean formbean ;
	
	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private DocumentService documentService;
   
	
	
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public ModelAndView prepareDocumentPostForm() {

		DocumentPostFormBean formBean = new DocumentPostFormBean();
		
		//System.out.println("EIMAI EDW STO get 1 : "+DocumentController.GlobalId);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("docForm", formBean);
		mav.setViewName("uploadForm");
		return mav;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ModelAndView processDocumentPostForm(
			@Valid DocumentPostFormBean formBean,
			BindingResult bindingResult,
			@RequestParam(value = "document", required = true) MultipartFile document,
			Model model) throws IOException {
		
		log.info(">>>>>>>>>> " + document);
		log.info(">>>>>>>>>> " + formBean);
		log.info(">>>>>>>>>> " + bindingResult);

		ModelAndView mav = new ModelAndView();

		//Blob blob = Hibernate.createBlob(document.getInputStream());
		//Blob blob = Hibernate.getLobCreator(sessionfactory.getCurrentSession()).createBlob(document.getInputStream(), 0);
		//Blob blob = Hibernate.getLobCreator(Session session).createBlob();
		if (bindingResult.hasErrors() || document.isEmpty()) {

			mav.setViewName("uploadForm");
			mav.addObject("docForm", formBean);

			return mav;
		}
	//	System.out.println("EIMAI EDW STO POST 1 : ");
		
		
		
		formBean.setParrentId(DocumentController.GlobalId);
		System.out.println("EIMAI EDW STO POST 2 : ");
		documentService.saveFileToRepo(document, formBean);

		mav.setViewName("redirect:docs/"+DocumentController.GlobalPath);

		return mav;
	}

}
