package gr.shmmy.ntua.dms.web.formBean;

public class ProjectPostFormBean {

		private Long projectId;
		private Long folderId;
		//private String username;
		private String authority;
		private Long userId;
	
	public ProjectPostFormBean(){
		
	}
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId =projectId;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId =folderId;
	}
	/*
	@Column(name = "USERNAME", length = 100, unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}*/

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	
}
