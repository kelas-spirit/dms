package gr.shmmy.ntua.dms.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PROJECTS")

public class Projects implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -7010339357162891527L;
/**
	 * 
	 */
private Long projectId;
private Long folderId;
private String username;
private String authority;
private Long userId;
private Date createdDate;



@Id
@GeneratedValue(strategy = GenerationType.AUTO)
@Column(name = "PROJECTS_ID")
public Long getProjectId() {
	return projectId;
}

public void setProjectId(Long projectId) {
	this.projectId=projectId;
}

@Column(name = "FOLDER_ID")
public Long getFolderId() {
	return folderId;
}

public void setFolderId(Long folderId) {
	this.folderId =folderId;
}

@Column(name = "USERNAME")
public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

@Column(name = "AUTHORITY", length = 100)
public String getAuthority() {
	return authority;
}

public void setAuthority(String authority) {
	this.authority = authority;
}

@Temporal(value = TemporalType.TIMESTAMP)
@Column(name = "CREATED_DATE")
public Date getCreatedDate() {
	return createdDate;
}

public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}
/*
@Column(name = "USER_ID")
public Long getUserId() {
	return userId;
}

public void setUserId(Long userId) {
	this.userId = userId;
}*/




}
