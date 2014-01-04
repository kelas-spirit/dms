package gr.shmmy.ntua.dms.web;

import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.service.DocumentService;
import gr.shmmy.ntua.dms.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DocumentController {

	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private DocumentService documentService;

	@Value("${doc.repo.path}")
	private String fileStorePath;
	
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public void setFileStorePath(String fileStorePath) {
		this.fileStorePath = fileStorePath;
	}

	/**
	 * Lists most recent documents
	 * 
	 * @return
	 */
	@RequestMapping(value = "/docs", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listDocuments() {
		String msg = "Recent Documents";
		List<Metadata> lst = documentService.getUsersDocuments();
		Long docCount = documentService.getAllDocumentCount();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("listDocs");
		mav.addObject("msg", msg);
		mav.addObject("lst", lst);
		mav.addObject("docCount", docCount);
		return mav;
	}

	/**
	 * download document.
	 * 
	 * @param docId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public void downloadDocumentById(@PathVariable(value = "id") Long docId,
			HttpServletRequest request, HttpServletResponse response,
			Authentication auth) {

		Metadata meta = documentService.getDocumentById(docId);

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ meta.getDocumentFileName());

		try {
			ServletOutputStream out = response.getOutputStream();
			StringBuilder fullPath = new StringBuilder();
			fullPath.append(fileStorePath + File.separatorChar);
			if(meta.getOwner().equals(Constants.DOC_TYPE_PUBLIC))
			{
				fullPath.append(Constants.PUBLIC_FOLDER);
			}else{
				fullPath.append(meta.getOwner());
			}
			fullPath.append(File.separatorChar+meta.getDocumentFileName());
			log.info("Final document download path is : " + fullPath);
			File f = new File(fullPath.toString());
			if(f.isFile())
			{
				log.info("Document is a file ...");
			}
			try {
				final int BUF_SIZE = 1024;
				byte[] buffer = new byte[BUF_SIZE];
				FileInputStream fis = new FileInputStream(f);
				int count = 0;
				do{
					count = fis.read(buffer);
					if(count == -1){
						break;
					}
					out.write(buffer, 0, count);
				}while(true);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/docs/delete", method = RequestMethod.GET)
	public ModelAndView deleteDocument(@RequestParam("id") Long id,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) {
		log.info("::::: " + id);
		log.info("::::: url : " + request.getRequestURI());

		documentService.deleteDocument(id);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/docs");

		return mav;
	}

	@RequestMapping(value = "/docs/count", method = RequestMethod.GET)
	public ModelAndView getAllDocumentCount(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {

		Long count = documentService.getAllDocumentCount();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/docs");

		return mav;
	}
}