package gr.shmmy.ntua.dms.web.formBean;

public class DocumentSearchFormBean {

	private String searchQuery;
	
	public DocumentSearchFormBean() {
	}
	
	public String getSearchQuery() {
		return searchQuery;
	}
	
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	@Override
	public String toString() {
		return "DocumentSearchFormBean [searchQuery=" + searchQuery + "]";
	}
}
