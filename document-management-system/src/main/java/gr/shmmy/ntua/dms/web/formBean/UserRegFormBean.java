package gr.shmmy.ntua.dms.web.formBean;

import java.util.Arrays;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.*;

public class UserRegFormBean {

	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private String rePassword;
	private String roles;
	
	
	public UserRegFormBean() {
	}

	@NotNull(message="First Name can't be empty")
	@Pattern(regexp="^[a-zA-Z\\ \\-]+$", message="invalid characters in First Name")
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	@NotNull(message="First Name can't be empty")
	@Pattern(regexp="^[a-zA-Z\\ \\-]+$", message="invalid characters in Last Name")
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	
	@NotNull
	@Size(min=5, message="Username should be atleast 5 digit long")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NotNull
	@Size(min=6, message="Password should be atleast 6 digit long")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotNull
	@Size(min=6, message="Re - Password should be atleast 6 digit long" )
	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	@Override
	public String toString() {
		return "UserRegFormBean [password=" + password + ", rePassword="
				+ rePassword + ", roles=" +roles
				+ ", username=" + username + "]";
	}

}
