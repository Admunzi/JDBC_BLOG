package dao.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.DAOException;
import dao.UserDAO;
import modelo.User;

public class UserMysqlDAO implements UserDAO{
	
	final String INSERT = "INSERT INTO user(name, lastname, username, mail, password, userCreatedDate) VALUES(?,?,?,?,?,?)";
	final String UPDATE = "UPDATE user SET name = ?, lastname = ?, username = ?, mail = ?, password = ?, userCreatedDate = ? WHERE idUser = ?";
	final String DELETE = "DELETE FROM user WHERE idUser = ?";
	final String GETALL = "SELECT idUser, name, lastname, username, mail, password, userCreatedDate FROM user";
	final String GETONE = "SELECT idUser, name, lastname, username, mail, password, userCreatedDate FROM user WHERE idUser = ?";

	private Connection conn;
	
	public UserMysqlDAO(Connection conn) {
		this.conn = conn;
	}	
	
//	public static void main(String[] args) throws SQLException {
//		ManagerMysqlDAO conecc = new ManagerMysqlDAO("localhost", "root", "", "blog");
//		try {
//			System.out.println(conecc.getUserDAO().obtenerTodos()); 
//		} catch (DAOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	@Override
	public void insertar(User a) throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(INSERT);){
			stat.setString(1, a.getName());
			stat.setString(2, a.getLastname());
			stat.setString(3, a.getUsername());
			stat.setString(4, a.getMail());
			stat.setString(5, a.getPassword());
			stat.setDate(6, new java.sql.Date(a.getUserCreatedDate().getTime()));
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a insertar", ex);
		}
	}

	@Override
	public void modificar(User a) throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(UPDATE);){
			stat.setString(1, a.getName());
			stat.setString(2, a.getLastname());
			stat.setString(3, a.getUsername());
			stat.setString(4, a.getMail());
			stat.setString(5, a.getPassword());
			stat.setDate(6, new Date(a.getUserCreatedDate().getTime()));
			stat.setLong(7, a.getIdUser());
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a modificar", ex);
		}
	}

	@Override
	public void eliminar(User a) throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(DELETE);){
			stat.setLong(1, a.getIdUser());
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a modificar", ex);
		}
	}

	@Override
	public List<User> obtenerTodos() throws DAOException {
		List<User> users = new ArrayList<>();
		try (PreparedStatement stat = conn.prepareStatement(GETALL);){

			ResultSet rs = stat.executeQuery(GETALL);
			rs = stat.executeQuery();
			while (rs.next()) {
				users.add(convertir(rs));				
			}			
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a obtener", ex);
		}
		return users;
	}


	@Override
	public User obtener(Long id) throws DAOException {
		User u = null;
		try (PreparedStatement stat = conn.prepareStatement(GETONE);){
			stat.setLong(1, id);	
			
			ResultSet rs = stat.executeQuery();
			rs = stat.executeQuery();
			if (rs.next()) {
				u = convertir(rs);
			}else {
				throw new DAOException("No se ha encontrado ese usuario");
			}
			
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a obtener", ex);
		}
		return u;
	}
	
	private User convertir(ResultSet rs) throws DAOException {
		User user = null;
		try {
			String name = rs.getString("name");
			String lastname = rs.getString("lastname");
			String username = rs.getString("username");
			String mail = rs.getString("mail");
			String password = rs.getString("password");
			Date userCreatedDate = rs.getDate("userCreatedDate");
			
			user = new User(name, lastname, username, mail, password, userCreatedDate);
			user.setIdUser(rs.getLong("idUser"));
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando se convierte", ex);
		}
		return user;
	}

}
