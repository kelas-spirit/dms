package gr.shmmy.ntua.dms.domain;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "PUBLIC_DATA")
public class PublicData {
	private Long publicId;
	private String createUser;
	private String documentFileName;
	private String documentType;
	private String owner;
	private Long documentSize;
	private Blob content;
	
	public PublicData(){
		
	}
	
	
	@Id
	@GeneratedValue
	@Column(name = "PUBLIC_DATA_ID")
	public Long getPublicId() {
		return publicId;
	}

	public void setPublicId(Long publicId) {
		this.publicId = publicId;
	}
	
	@Column(name = "CREATED_USER")
	public String getCreateUser() {
		return createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "DOCUMENT_FILE_NAME", length = 1000)
	public String getDocumentFileName() {
		return documentFileName;
	}

	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}
	
	@Column(name = "DOCUMENT_TYPE", length = 200)
	public String getDocumentType() {
		return documentType;
	}
	
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	@Column(name="OWNER", length=100, nullable = false)
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@Column(name = "SIZE")
	public Long getDocumentSize() {
		return documentSize;
	}
	
	public void setDocumentSize(Long documentSize) {
		this.documentSize = documentSize;
	}
	
	public Blob getContent(){
		return content;
	}
	
	public void setContent(Blob content){
		this.content=content;
	}
}
