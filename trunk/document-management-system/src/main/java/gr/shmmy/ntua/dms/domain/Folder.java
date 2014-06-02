package gr.shmmy.ntua.dms.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FOLDER")
public class Folder {
 
	private Long folderId;
	private String createUser;
	private Date createdDate;
	private String folderName;
	private String folderPath;
	private Long parrentId;
	private String pathId;
	
	public Folder(){
		
	}
	
	@Id
	@GeneratedValue
	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId =folderId;
	}
	@Column(name = "CREATED_USER")
	public String getCreateUser() {
		return createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	@Column(name = "FOLDER_FILE_NAME", length = 20)
	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	@Column(name = "FOLDER_PATH")
	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	
	
	
	@Column(name = "PARRENT_ID")
	public Long getParrentId() {
		return parrentId;
	}

	public void setParrentId(Long parrentId) {
		this.parrentId = parrentId;
	}
	@Column(name = "PATH_ID")
	public String getPathId() {
		return pathId;
	}

	public void setPathId(String pathId) {
		this.pathId = pathId;
	}
	
	@Override
	public String toString() {
		return "Inside in Folder column we find this: [folderId=" + folderId + ", createUser=" + createUser
				+ ", folderName=" + folderName + ",parrentId=" + parrentId + "]";
	}
	
}
