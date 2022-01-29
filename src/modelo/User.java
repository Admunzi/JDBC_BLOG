package modelo;

import java.util.Date;
import java.util.Objects;

public class User {

	private Long idUser;
	private String name;
	private String lastname;
	private String username;
	private String mail;
	private String password;
	private Date userCreatedDate;
	
	public User(String name, String lastname, String username, String mail, String password,
			Date userCreatedDate) {
		this.name = name;
		this.lastname = lastname;
		this.username = username;
		this.mail = mail;
		this.password = password;
		this.userCreatedDate = userCreatedDate;
	}
	
	public User() {
		this.name = "";
		this.lastname = "";
		this.username =  "";
		this.mail =  "";
		this.password =  "";
		this.userCreatedDate = (Date) new java.util.Date();
	}
	
	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getUserCreatedDate() {
		return userCreatedDate;
	}
	public void setUserCreatedDate(Date date) {
		this.userCreatedDate = date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idUser, lastname, mail, name, password, userCreatedDate, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return idUser == other.idUser && Objects.equals(lastname, other.lastname) && Objects.equals(mail, other.mail)
				&& Objects.equals(name, other.name) && Objects.equals(password, other.password)
				&& Objects.equals(userCreatedDate, other.userCreatedDate) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "User [idUser=" + idUser + ", name=" + name + ", lastname=" + lastname + ", username=" + username
				+ ", mail=" + mail + ", password=" + password + ", userCreatedDate=" + userCreatedDate + "]";
	}

	
}
