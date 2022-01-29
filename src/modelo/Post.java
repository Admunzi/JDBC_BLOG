package modelo;

import java.util.Date;
import java.util.Objects;

public class Post {
	
	private Long idPost;
	private String title;
	private Date postCreateDate;
	private String content;
	private Long idUser;
	private String nameCategory;
	
	public Post(String title, Date postCreateDate, Long idUser, String nameCategory, String content) {
		this.title = title;
		this.postCreateDate = postCreateDate;
		this.idUser = idUser;
		this.nameCategory = nameCategory;
		this.content = content;
	}

	public Post() {
		this.title = "";
		this.postCreateDate = (Date) new java.util.Date();
		this.content = "";
		this.idUser = null;
		this.nameCategory = "";
	}

	public Long getIdPost() {
		return idPost;
	}

	public void setIdPost(Long idPost) {
		this.idPost = idPost;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPostCreateDate() {
		return postCreateDate;
	}

	public void setPostCreateDate(Date postCreateDate) {
		this.postCreateDate = postCreateDate;
	}
	
	public String getNameCategory() {
		return nameCategory;
	}

	public void setNameCategory(String nameCategory) {
		this.nameCategory = nameCategory;
	}
	
	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, idPost, idUser, nameCategory, postCreateDate, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		return Objects.equals(content, other.content) && Objects.equals(idPost, other.idPost)
				&& Objects.equals(idUser, other.idUser) && Objects.equals(nameCategory, other.nameCategory)
				&& Objects.equals(postCreateDate, other.postCreateDate) && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Post [idPost=" + idPost + ", title=" + title + ", postCreateDate=" + postCreateDate + ", content="
				+ content + ", idUser=" + idUser + ", nameCategory=" + nameCategory + "]";
	}
}
