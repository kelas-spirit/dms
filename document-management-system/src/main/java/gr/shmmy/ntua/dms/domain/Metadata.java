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
@Table(name = "METADATA")
public class Metadata {

	private Long metadataId;
	private String createUser;
	private String updatedUser;
	private String subject;
	private String keywords;
	private String comments;
	private String documentFileName;
	private Date createdDate;
	private Date updatedDate;
	private String documentType;
	private Long documentSize;
	private String owner;
	
	public Metadata() {
	}

	@Id
	@GeneratedValue
	@Column(name = "METADATA_ID")
	public Long getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(Long metadataId) {
		this.metadataId = metadataId;
	}

	@Column(name = "CREATED_USER")
	public String getCreateUser() {
		return createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@Column(name = "UPDATED_USER")
	public String getUpdatedUser() {
		return updatedUser;
	}
	
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
	
	@Column(name = "SUBJECT")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "KEYWORDS")
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Column(name = "COMMENTS", length = 2000)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "DOCUMENT_FILE_NAME", length = 1000)
	public String getDocumentFileName() {
		return documentFileName;
	}

	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "DOCUMENT_TYPE", length = 200)
	public String getDocumentType() {
		return documentType;
	}
	
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	@Column(name = "SIZE")
	public Long getDocumentSize() {
		return documentSize;
	}
	
	public void setDocumentSize(Long documentSize) {
		this.documentSize = documentSize;
	}
	
	@Column(name="OWNER", length=100, nullable = false)
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}

}