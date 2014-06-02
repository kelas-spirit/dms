package gr.shmmy.ntua.dms.web;

import gr.shmmy.ntua.dms.domain.Folder;
import gr.shmmy.ntua.dms.domain.Metadata;
import gr.shmmy.ntua.dms.domain.Projects;
import gr.shmmy.ntua.dms.domain.PublicData;
import gr.shmmy.ntua.dms.domain.User;
import gr.shmmy.ntua.dms.service.DocumentService;
import gr.shmmy.ntua.dms.service.FolderService;
import gr.shmmy.ntua.dms.service.ProjectsService;
import gr.shmmy.ntua.dms.service.UserService;
//import gr.shmmy.ntua.dms.utils.Constants;
import gr.shmmy.ntua.dms.web.formBean.FolderPostFormBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	@Autowired
	private UserService userservice;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private ProjectsService projectService;
	
	private String errorMsg="";
	
	public FolderPostFormBean formbean =new FolderPostFormBean() ;
	public FolderPostFormBean rolebean =new FolderPostFormBean() ;
	
	//this for upload file,we need to know id folder where we put to file,mazafaka!!!
	public static Long GlobalId;
	public static String GlobalPath;

	@Value("${doc.repo.path}")
	private String fileStorePath;
	
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public void setUserService(UserService userservice) {
		this.userservice = userservice;
	}
	
	public void setFileStorePath(String fileStorePath) {
		this.fileStorePath = fileStorePath;
	}

	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
	}
	/**
	 * Lists most recent documents
	 * 
	 * @return
	 */
	
	
	@RequestMapping(value = "/docs", method = RequestMethod.GET)
	public ModelAndView prepareFolderPost(Authentication auth){
		String msg = "Recent Documents";
		System.out.println("auth.getname():"+auth.getName());

		Long docCount = documentService.getAllDocumentCount();
		
		formbean.setParrentId(0L);
		formbean.setPathId("0");
		//formbean = new FolderPostFormBean();
		formbean.setId((long) 0); // /because docs have ever value 0, docs/somthing_else have another value
		formbean.setName(" ");
		GlobalId = formbean.getId();
		GlobalPath = formbean.getName(); // must will change for get absolute path
		List<Metadata> lst = documentService.getUsersDocuments((long) 0);
		List<Folder> fold = folderService.getUsersFolder((long) 0);
		String owner = folderService.owner();
		
		
		FolderPostFormBean formBean = new FolderPostFormBean();
		
		
	//	System.out.println("EIMAI STO GET");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("listDocs");
		mav.addObject("formBean",formBean);
		mav.addObject("msg", msg);
		mav.addObject("owner", owner);
		mav.addObject("lst", lst);
		mav.addObject("fold", fold);
		mav.addObject("docCount", docCount);
		//mav.setViewName("listDocs");
		//ModelAndView mav = new ModelAndView();
		return mav;
	}
	

	@RequestMapping(value = {"/create_folder/{path}","/create_folder"}, method = RequestMethod.POST)
	public ModelAndView processFolderPostPath(@ModelAttribute("formBean") FolderPostFormBean formBean, BindingResult bindingResult,
			@RequestParam("food")String name,HttpServletRequest request)throws IOException {
		System.out.println("EIMAI STO POST");
		 errorMsg="";
		
		//if folder already exist
		boolean folderAlreadyExist = folderService.getUsersFolderExist(name, formbean.getId());
		System.out.println("EIMAI STO POST2");

		
		System.out.println("formbean.getId()"+formbean.getId());
		if(folderAlreadyExist==true){	
			errorMsg="Folder already exist";
			System.out.println("Folder already exist");
			
		}
		
		else if(folderAlreadyExist==false){
			System.out.println("Folder not exist, ok!");

			folderService.saveFolderToRepo(name,formbean);
			errorMsg="";
		}
	//	System.out.println("string tou textbox:"+name);
		ModelAndView mav = new ModelAndView();
		
		
		mav.setViewName("redirect:/docs/"+formbean.getName());
		return mav;
		
		
	}
	
	
	
	
	@RequestMapping(value = "/docs/{path}", method = RequestMethod.GET)
	public ModelAndView prepareFolderPostLongPath()throws Exception{
	//	System.out.println("EIMAI STO id GET");
		ModelAndView mav = new ModelAndView();
		System.out.println("tsa lol");
		List<Metadata> lst = documentService.getUsersDocuments2(formbean.getId());
		//System.out.println("tsa lol");
		List<Folder> fold = folderService.getUsersFolder2( formbean.getId());
		String owner = folderService.owner(formbean.getId());
	//	System.out.println("tsa lol");
		
		//List<String> taskname = new ArrayList<String>();
		//String wpleadername="";
		if(rolebean.getId()!=null){
			System.out.println("not equal with 0");
		List<Projects>pr = projectService.myProjectListByFolderId(rolebean.getId(), "TASK_PARTICIPANT");
		List<Projects>prw = projectService.myProjectListByFolderId(rolebean.getId(), "ROLE_WORKPACKAGE_LEADER");
		mav.addObject("pr", pr);
		mav.addObject("prw", prw);
		}else if(rolebean.getId()==null){
			//System.out.println("null -> as user");

		}
		//System.out.println("tsa lol4");
		
	
	//	mav.addObject("errorMsg", errorMsg);
		mav.addObject("owner", owner);
		mav.addObject("lst", lst);
		mav.addObject("fold", fold);
		mav.setViewName("listDocs");
		return mav;
	}
	
	@RequestMapping(value = "/fold/{path}", method = RequestMethod.POST)
	public ModelAndView processFolderPostLongPath(@PathVariable  String path,@ModelAttribute("formBean") FolderPostFormBean formBean, BindingResult bindingResult,
			@RequestParam("folderName") String folderName,
			@RequestParam("folderPath") String folderPath,
			@RequestParam("folderId") Long foldId,
			@RequestParam("parrentId") Long parrentId,
			@RequestParam("pathId") String pathId,
			
			HttpServletRequest request)throws IOException {
		errorMsg="";
		
		
		System.out.println("EIMAI STO id POST");
			System.out.println("folderName"+folderName);
		System.out.println("folderPath"+folderPath);
		System.out.println("foldId"+foldId);
		System.out.println("request path: "+path);
		System.out.println("parrentId path: "+parrentId);
		
		// formbean = new FolderPostFormBean();
		formbean.setId(foldId);
		formbean.setName(folderName);
		formbean.setPath(folderPath);
		formbean.setParrentId(parrentId);
		formbean.setPathId(pathId);
		GlobalId = formbean.getId();
		GlobalPath = formbean.getName();
	//	System.out.println("path: "+path);@PathVariable  String path,
		System.out.println("eftasa k edw");
	//	List<Folder> fold = folderService.getUsersFolder( foldId);
		
		//@RequestParam("food")String name,
		
		
		ModelAndView mav = new ModelAndView();
		//mav.addObject("fold", fold);
	
		mav.setViewName("redirect:/docs/"+formbean.getName());
		return mav;
		
		
	}
	
	@RequestMapping(value = "/fold/delete", method = RequestMethod.GET)
	public  ModelAndView  folderDelete(@RequestParam("id") Long folderId){
		 ModelAndView mav = new  ModelAndView ();
		 
		 List<Folder>folder = folderService.deleteFolderPrepare(folderId);
				System.out.println("i lista einai etoimi");
		if(folder.size()>0){
			System.out.println("kai den einai adeia");

		
		 for(Folder fld: folder){
				System.out.println("sto dokument controller me id: "+folderId);

			 folderService.deleteFolder(fld.getPathId(), folderId, fld.getFolderId());
		 }
		}
		folderService.deleteOneFolder(folderId);
		
			mav.setViewName("redirect:/docs/"+formbean.getName());
			List<Metadata> lst=documentService.getUsersDocuments2(folderId);
			if(lst.size()>0){
				
				for(Metadata ls:lst){
					//documentDao.deleteDocument(ls.getMetadataId());
					System.out.println("teleutaio:diagrafi arxeiou me id  "+ls.getMetadataId());

					documentService.deleteDocument(ls.getMetadataId());
				}
				
			}
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

		
			
			//na paiksw me inline
			try {
				response.setHeader("Content-Disposition", "attachment; filename=\""
						+ meta.getDocumentFileName());
				OutputStream out = response.getOutputStream();
				//PrintWriter out = response.getWriter();
				response.setContentType(meta.getDocumentType());
				IOUtils.copy(meta.getContent().getBinaryStream(), out);
				out.flush();
	           out.close();
	            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
		
		
	//	response.setContentType("application/octet-stream");
		
/*
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
		}*/
	}
	
	//download from public folder
	@RequestMapping(value = "/pbdownload/{id}", method = RequestMethod.GET)
	public void downloadPublicDocumentById(@PathVariable(value = "id") Long docId,
			HttpServletRequest request, HttpServletResponse rs,
			Authentication auth) {

		
		PublicData publicdata = documentService.getPublicDocumentById(docId);

		
		
		//na paiksw me inline
		try {
			rs.setHeader("Content-Disposition", "attachment; filename=\""
					+ publicdata.getDocumentFileName());
			OutputStream out = rs.getOutputStream();
			//PrintWriter out = rs.getWriter();
			rs.setContentType(publicdata.getDocumentType());
			IOUtils.copy(publicdata.getContent().getBinaryStream(), out);
			out.flush();
            out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		/*
		//Metadata meta = documentService.getDocumentById(docId);
		PublicData publicdata = documentService.getPublicDocumentById(docId);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ publicdata.getDocumentFileName());

		try {
			ServletOutputStream out = response.getOutputStream();
			StringBuilder fullPath = new StringBuilder();
			fullPath.append(fileStorePath + File.separatorChar);
			if(publicdata.getOwner().equals(Constants.DOC_TYPE_PUBLIC))
			{
				fullPath.append(Constants.PUBLIC_FOLDER);
			}else{
				fullPath.append(publicdata.getOwner());
			}
			fullPath.append(File.separatorChar+publicdata.getDocumentFileName());
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
		}*/
	}
	
	@RequestMapping(value = "/docs/deleteall", method = RequestMethod.GET)
	public ModelAndView deleteDocumentAll(@RequestParam("parrentId") Long parrentId, @RequestParam("documentFileName") String documentFileName){
		ModelAndView mav = new ModelAndView();
		documentService.deleteDocument(parrentId,documentFileName);
		System.out.println("parrentId:"+parrentId);
		System.out.println("documentFileName:"+documentFileName);

		mav.setViewName("redirect:/docs/"+formbean.getName());

		return mav;
	}

	@RequestMapping(value = "/docs/delete", method = RequestMethod.GET)
	public ModelAndView deleteDocument(@RequestParam("id") Long id,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) {
		log.info("::::: " + id);
		log.info("::::: url : " + request.getRequestURI());

		documentService.deleteDocument(id);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/docs/"+formbean.getName());

		return mav;
	}
	//delete public file
	@RequestMapping(value = "/pbdocs/delete", method = RequestMethod.GET)
	public ModelAndView deletePublicDocument(@RequestParam("id") Long id,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) {
		log.info("::::: " + id);
		log.info("::::: url : " + request.getRequestURI());

		documentService.deletePublicDocument(id);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/public_folder");

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
	
	@RequestMapping(value = "/public_folder", method = RequestMethod.GET)
	public ModelAndView getPublicDocuments(){
		
		List<PublicData> plst = documentService.getPublicDocuments();
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("plst",plst);
		mav.setViewName("publicListDocs");
		return mav;
	}
	
	@RequestMapping(value = "/public/download/{owner}/{id}", method = RequestMethod.GET)
	public ModelAndView getPublicDocumentsLink(@PathVariable(value="owner") String owner,
			@PathVariable(value="id") Long id){
		PublicData plst = documentService.getPublicDocumentById(id);
		
		
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("plst",plst);
		mav.setViewName("publicFileDownload");
		return mav;
	}
	
	
	
	@RequestMapping(value = "/prepareforuserfolders", method = RequestMethod.POST)
	public ModelAndView getProjectView(@RequestParam("folderId") Long folderId){
		System.out.println("mpika...");

		ModelAndView mav = new ModelAndView();
		formbean.setId(folderId);
		rolebean.setId(folderId);
		List<Folder>folder = folderService.getFolderById(folderId);
		for(Folder fld: folder){
			formbean.setName(fld.getFolderName());
		}
		System.out.println("folder name : "+formbean.getName());
		mav.setViewName("redirect:/docs/"+formbean.getName());
		return mav;
	}
	
	//Go to previous version
	@RequestMapping(value = "/previsousversion", method = RequestMethod.POST)
	public ModelAndView goToPreviousVersion(@RequestParam("parrentId") Long parrentId,
			@RequestParam("documentFileName") String documentFileName){
		ModelAndView mav = new ModelAndView();
		documentService.previousVersion(documentFileName, parrentId);
		mav.setViewName("redirect:/docs/"+formbean.getName());

		
		return mav;
	}
	@RequestMapping(value = "/blob/view", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView fromBlobToView(@RequestParam("documentId") Long documentId){
		ModelAndView mav = new ModelAndView();
		Metadata metadata = documentService.documentById(documentId);
		Blob blob = metadata.getContent();
		try {
			byte[] blobAsBytes = blob.getBytes(1, (int) blob.length());
			//byte[] encoded=Base64.encodeBase64(blobAsBytes);
			String encodedString = new String(blobAsBytes);
			mav.addObject("encodedString", encodedString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.setViewName("fileview");

		return mav;
	}
	
	@RequestMapping(value = "/blob/editfile", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView fromBlobToViewEdit(@RequestParam("documentId") Long documentId){
		ModelAndView mav = new ModelAndView();
		Metadata metadata = documentService.documentById(documentId);
		Blob blob = metadata.getContent();
		try {
			byte[] blobAsBytes = blob.getBytes(1, (int) blob.length());
			//byte[] encoded=Base64.encodeBase64(blobAsBytes);
			String encodedString = new String(blobAsBytes);
			mav.addObject("documentId", metadata.getMetadataId());
			mav.addObject("encodedString", encodedString);
			mav.addObject("username", metadata.getCreateUser());
			mav.addObject("documentFileName", metadata.getDocumentFileName());
			
		//	System.out.println("user created"+metadata.getCreateUser());
			//mav.addObject("DocumentFileName", metadata.get);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.setViewName("editfile");

		return mav;
	}
	
	@RequestMapping(value = "/blob/editblob", method =  RequestMethod.POST)
	public ModelAndView reEditBlob(@RequestParam("documentId") Long documentId,
			@RequestParam("food")String name,
			@RequestParam("username")String username,
			@RequestParam("documentFileName")String documentFileName
			//documentFileName
			){
		ModelAndView mav = new ModelAndView();
		
		documentService.editBlob(name, documentId, username,documentFileName);
		
		
		
		mav.setViewName("redirect:/docs/"+formbean.getName());

		return mav;
	}
	
	
}