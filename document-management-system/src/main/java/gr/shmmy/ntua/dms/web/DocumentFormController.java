package gr.shmmy.ntua.dms.web;

import gr.shmmy.ntua.dms.service.DocumentService;
import gr.shmmy.ntua.dms.web.formBean.DocumentPostFormBean;

import java.io.IOException;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class DocumentFormController {

	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private DocumentService documentService;

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public ModelAndView prepareDocumentPostForm() {

		DocumentPostFormBean formBean = new DocumentPostFormBean();

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

		if (bindingResult.hasErrors() || document.isEmpty()) {

			mav.setViewName("uploadForm");
			mav.addObject("docForm", formBean);

			return mav;
		}
	
		// save docs.
		documentService.saveFileToRepo(document, formBean);

		mav.setViewName("redirect:docs");

		return mav;
	}

}
