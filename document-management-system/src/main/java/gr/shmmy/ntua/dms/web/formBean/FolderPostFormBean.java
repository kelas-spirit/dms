package gr.shmmy.ntua.dms.web.formBean;

public class FolderPostFormBean {

	private String name;
	private Long id;
	private String path;
	private Long parrentId;
	private String pathId;
	
	public FolderPostFormBean(){
		
	}
	
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id=id;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setPath(String path){
		this.path=path;
	}
	
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		 this.name=name;
	}
	
	public Long getParrentId() {
		return parrentId;
	}

	public void setParrentId(Long parrentId) {
		this.parrentId = parrentId;
	}
	
	public String getPathId() {
		return pathId;
	}

	public void setPathId(String pathId) {
		this.pathId = pathId;
	}
	
}
