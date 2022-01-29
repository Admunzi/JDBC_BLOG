package dao.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.DAOException;
import dao.PostDAO;
import modelo.Post;

public class PostMysqlDAO implements PostDAO{


	final String INSERTCATEGORY = "INSERT INTO category(nameCategory, recommendedAge) VALUES(?, ?)";
	final String INSERT = "INSERT INTO post(title, postCreateDate, idUser, nameCategory, content) VALUES(?,?,?,?,?)";
	final String UPDATE = "UPDATE post SET title = ?, postCreateDate = ?, idUser = ?, nameCategory = ?, content = ? WHERE idPost = ?";
	final String DELETE = "DELETE FROM post WHERE idPost = ?";
	final String GETALL = "SELECT idPost, title, postCreateDate, idUser, nameCategory, content FROM post";
	final String GETONE = "SELECT idPost, title, postCreateDate, idUser, nameCategory, content  FROM post WHERE idPost = ?";
	final String GETONECATEGORY = "SELECT nameCategory, recommendedAge FROM category WHERE nameCategory = ?";
	
	private Connection conn;
	
	public PostMysqlDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insertar(Post p) throws DAOException {
		//Comprobamos si existe la categoria
		try (PreparedStatement stat = conn.prepareStatement(GETONECATEGORY);){
			conn.setAutoCommit(false);

			stat.setString(1, p.getNameCategory());
			
			ResultSet rs = stat.executeQuery();
			rs = stat.executeQuery();
			int row;
			if (!rs.next()) {
				//Si no existe la creamos
				PreparedStatement pstInsert = conn.prepareStatement(INSERTCATEGORY);
				
				pstInsert.setString(1, p.getNameCategory());
				pstInsert.setLong(2, 0);
				row = pstInsert.executeUpdate(); //Aquí tenemos transaccion
			}else {
				//Si existe seguimos
				row = 1;
			}
			
			if(row == 1) {
				//Creamos el post
				PreparedStatement pstAssignment = conn.prepareStatement(INSERT);
                
				pstAssignment.setString(1, p.getTitle());
				pstAssignment.setDate(2, new java.sql.Date(p.getPostCreateDate().getTime()));
				pstAssignment.setLong(3, p.getIdUser());
				pstAssignment.setString(4, p.getNameCategory());
				pstAssignment.setString(5, p.getContent());
				
				pstAssignment.executeUpdate();
				conn.commit();
			}else {
            	conn.rollback();
            }
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a insertar", ex);
		}
	}

	@Override
	public void modificar(Post p) throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(GETONECATEGORY);){
			conn.setAutoCommit(false);

			stat.setString(1, p.getNameCategory());
			
			ResultSet rs = stat.executeQuery();
			rs = stat.executeQuery();
			int row;
			if (!rs.next()) {
				//Si no existe la creamos
				PreparedStatement pstInsert = conn.prepareStatement(INSERTCATEGORY);
				
				pstInsert.setString(1, p.getNameCategory());
				pstInsert.setLong(2, 0);
				row = pstInsert.executeUpdate(); //Aquí tenemos transaccion
			}else {
				//Si existe seguimos
				row = 1;
			}
			
			
			if(row == 1) {
				//Modificamos el post
				PreparedStatement pstAssignment = conn.prepareStatement(UPDATE);
                
				pstAssignment.setString(1, p.getTitle());
				pstAssignment.setDate(2, new java.sql.Date(p.getPostCreateDate().getTime()));
				pstAssignment.setLong(3, p.getIdUser());
				pstAssignment.setString(4, p.getNameCategory());
				pstAssignment.setString(5, p.getContent());
				pstAssignment.setLong(6, p.getIdPost());
				
				pstAssignment.executeUpdate();
				conn.commit();
			}else {
            	conn.rollback();
            }
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a modificar", ex);
		}
	}

	@Override
	public void eliminar(Post p) throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(DELETE);){
			stat.setLong(1, p.getIdPost());
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a modificar", ex);
		}		
	}

	@Override
	public List<Post> obtenerTodos() throws DAOException {
		List<Post> posts = new ArrayList<>();
		try (PreparedStatement stat = conn.prepareStatement(GETALL);){

			ResultSet rs = stat.executeQuery(GETALL);
			rs = stat.executeQuery();
			while (rs.next()) {
				posts.add(convertir(rs));				
			}			
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a obtener", ex);
		}
		return posts;
	}

	@Override
	public Post obtener(Long id) throws DAOException {
		Post p = null;
		try (PreparedStatement stat = conn.prepareStatement(GETONE);){
			stat.setLong(1, id);
			
			ResultSet rs = stat.executeQuery();
			rs = stat.executeQuery();
			if (rs.next()) {
				p = convertir(rs);
			}else {
				throw new DAOException("No se ha encontrado ese post");
			}
			
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a obtener", ex);
		}
		return p;
	}

	private Post convertir(ResultSet rs) throws DAOException {
		Post post = null;
		try {
			String title = rs.getString("title");
			Date postCreateDate = rs.getDate("postCreateDate");
			Long idUser = rs.getLong("idUser");
			String nameCategory = rs.getString("nameCategory");
			String content = rs.getString("content");
			
			post = new Post(title, postCreateDate, idUser, nameCategory, content);
			post.setIdPost(rs.getLong("idPost"));
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando se convierte", ex);
		}
		return post;
	}
	
}
