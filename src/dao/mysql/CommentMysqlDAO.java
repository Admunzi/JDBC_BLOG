package dao.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.CommentDAO;
import dao.DAOException;
import modelo.Comment;

public class CommentMysqlDAO implements CommentDAO{

	final String INSERT = "INSERT INTO comment(name, commentContent, commentCreatedDate, score, idPost) VALUES(?,?,?,?,?)";
	final String UPDATE = "UPDATE comment SET name = ?, commentContent = ?, commentCreatedDate = ?, score = ?, idPost = ? WHERE idComment = ?";
	final String DELETE = "DELETE FROM comment WHERE idComment = ?";
	final String GETALL = "SELECT idComment, name, commentContent, commentCreatedDate, score, idPost FROM comment";
	final String GETONE = "SELECT idComment, name, commentContent, commentCreatedDate, score, idPost FROM comment WHERE idComment = ?";

	private Connection conn;
	
	public CommentMysqlDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insertar(Comment a) throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(INSERT);){
			stat.setString(1, a.getName());
			stat.setString(2, a.getCommentContent());
			stat.setDate(3, new java.sql.Date(a.getCommentCreatedDate().getTime()));
			stat.setFloat(4, a.getScore());
			stat.setLong(5, a.getIdPost());
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a insertar", ex);
		}
	}

	@Override
	public void modificar(Comment a) throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(UPDATE);){
			stat.setString(1, a.getName());
			stat.setString(2, a.getCommentContent());
			stat.setDate(3, new java.sql.Date(a.getCommentCreatedDate().getTime()));
			stat.setFloat(4, a.getScore());
			stat.setLong(5, a.getIdPost());
			stat.setLong(6, a.getIdComment());
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a modificar", ex);
		}
	}

	@Override
	public void eliminar(Comment a) throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(DELETE);){
			stat.setLong(1, a.getIdComment());
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a modificar", ex);
		}
	}

	@Override
	public List<Comment> obtenerTodos() throws DAOException {
		List<Comment> comments = new ArrayList<>();
		try (PreparedStatement stat = conn.prepareStatement(GETALL);){

			ResultSet rs = stat.executeQuery(GETALL);
			rs = stat.executeQuery();
			while (rs.next()) {
				comments.add(convertir(rs));				
			}			
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a obtener", ex);
		}
		return comments;
	}

	@Override
	public Comment obtener(Long id) throws DAOException {
		Comment c = null;
		try (PreparedStatement stat = conn.prepareStatement(GETONE);){
			stat.setLong(1, id);	
			
			ResultSet rs = stat.executeQuery();
			rs = stat.executeQuery();
			if (rs.next()) {
				c = convertir(rs);
			}else {
				throw new DAOException("No se ha encontrado ese usuario");
			}
			
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a obtener", ex);
		}
		return c;
	}
	
	private Comment convertir(ResultSet rs) throws DAOException {
		Comment coment = null;
		try {
			String name = rs.getString("name");
			String commentContent = rs.getString("commentContent");
			Date commentCreatedDate = rs.getDate("commentCreatedDate");
			float score = rs.getFloat("score");
			Long idPost = rs.getLong("idPost");
			
			coment = new Comment(name, commentContent, commentCreatedDate, score, idPost);
			coment.setIdComment(rs.getLong("idComment"));
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando se convierte", ex);
		}
		return coment;
	}

}
