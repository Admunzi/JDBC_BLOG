package modelo;

import java.util.Date;
import java.util.Objects;

public class Comment {
	
	private Long idComment;
	private String name;
	private String commentContent;
	private Date commentCreatedDate;
	private Float score;
	private Long idPost;
	
	public Comment(String name, String commentContent, Date commentCreatedDate,
			Float score, Long idPost) {
		this.name = name;
		this.commentContent = commentContent;
		this.commentCreatedDate = commentCreatedDate;
		this.score = score;
		this.idPost = idPost;
	}
	
	public Comment() {
		this.name = "";
		this.commentContent = "";
		this.commentCreatedDate = (Date) new java.util.Date();;
		this.score = null;
		this.idPost = null;
	}

	public Long getIdComment() {
		return idComment;
	}

	public void setIdComment(Long idComment) {
		this.idComment = idComment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = (name.length() <= 30) ? name:name.substring(0,30);
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = (score >= 0 && score <= 10) ? score:5;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Date getCommentCreatedDate() {
		return commentCreatedDate;
	}

	public void setCommentCreatedDate(Date commentCreatedDate) {
		this.commentCreatedDate = commentCreatedDate;
	}

	public Long getIdPost() {
		return idPost;
	}

	public void setIdPost(Long idPost) {
		this.idPost = idPost;
	}

	@Override
	public int hashCode() {
		return Objects.hash(commentContent, commentCreatedDate, idComment, idPost, name, score);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		return Objects.equals(commentContent, other.commentContent)
				&& Objects.equals(commentCreatedDate, other.commentCreatedDate)
				&& Objects.equals(idComment, other.idComment) && Objects.equals(idPost, other.idPost)
				&& Objects.equals(name, other.name) && Float.floatToIntBits(score) == Float.floatToIntBits(other.score);
	}

	@Override
	public String toString() {
		return "Comment [idComment=" + idComment + ", name=" + name + ", commentContent=" + commentContent
				+ ", commentCreatedDate=" + commentCreatedDate + ", score=" + score + ", idPost=" + idPost + "]";
	}
	

	
}
