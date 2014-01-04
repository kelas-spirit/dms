package gr.shmmy.ntua.dms.web.formBean;

public class DocumentPostFormBean {

	private String subject;
	private Integer category;
	private String keywords;
	private String comments;
	private boolean isPublic;

	public DocumentPostFormBean() {
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean getIsPublic() {
		return isPublic;
	}
	
	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	@Override
	public String toString() {
		return "DocumentPostFormBean [category=" + category + ", comments="
				+ comments + ", isPublic=" + isPublic + ", keywords="
				+ keywords + ", subject=" + subject + "]";
	}

}
