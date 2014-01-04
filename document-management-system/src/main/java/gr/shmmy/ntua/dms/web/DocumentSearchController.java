package gr.shmmy.ntua.dms.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.service.DocumentService;

@Controller
public class DocumentSearchController {

	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private DocumentService documentService;

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam("q") String searchQuery, ModelMap model,
			HttpServletResponse res) throws Exception {

		log.info("::: Search Query ::: " + searchQuery);
		ModelAndView mav = new ModelAndView();
		List<Metadata> lst = documentService
				.getDocumentsBySearchQuery(searchQuery);
		mav.addObject("lst", lst);
		mav.addObject("q",searchQuery);
		mav.setViewName("listDocs");
		return mav;

	}

}
