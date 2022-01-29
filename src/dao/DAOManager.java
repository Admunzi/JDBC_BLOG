package dao;

public interface DAOManager {

	CategoryDAO getCategoryDAO();
	
	CommentDAO getCommentDAO();
	
	PostDAO getPostDAO();
	
	UserDAO getUserDAO();
}
