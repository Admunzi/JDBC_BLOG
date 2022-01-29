package dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.CategoryDAO;
import dao.DAOException;
import modelo.Category;

public class CategoryMysqlDAO implements CategoryDAO{

	final String INSERT = "INSERT INTO category(nameCategory, recommendedAge) VALUES(?, ?)";
	final String UPDATE = "UPDATE category SET nameCategory = ?, recommendedAge = ? WHERE nameCategory = ?";
	final String DELETE = "DELETE FROM category WHERE nameCategory = ?";
	final String GETALL = "SELECT nameCategory, recommendedAge FROM category";
	final String GETONE = "SELECT nameCategory, recommendedAge FROM category WHERE nameCategory = ?";

	private Connection conn;
	
	public CategoryMysqlDAO(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insertar(Category a) throws DAOException{
		try (PreparedStatement stat = conn.prepareStatement(INSERT);){
			stat.setString(1, a.getNameCategory());
			stat.setInt(2, a.getRecommendedAge());
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a insertar", ex);
		}
	}

	@Override
	public void modificar(Category a) throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(UPDATE);){
			stat.setString(1, a.getNameCategory());
			stat.setInt(2, a.getRecommendedAge());
			stat.setString(3, a.getNameCategory());
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a modificar", ex);
		}
	}

	@Override
	public void eliminar(Category a) throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(DELETE);){
			stat.setString(1, a.getNameCategory());
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a modificar", ex);
		}		
	}
	
	@Override
	public List<Category> obtenerTodos() throws DAOException {
		List<Category> categories = new ArrayList<>();
		try (PreparedStatement stat = conn.prepareStatement(GETALL);){

			ResultSet rs = stat.executeQuery(GETALL);
			rs = stat.executeQuery();
			while (rs.next()) {
				categories.add(convertir(rs));				
			}			
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a obtener", ex);
		}
		return categories;
	}

	@Override
	public Category obtener(String nameCategory) throws DAOException {
		Category c = null;
		try (PreparedStatement stat = conn.prepareStatement(GETONE);){
			stat.setString(1, nameCategory);
			
			ResultSet rs = stat.executeQuery();
			rs = stat.executeQuery();
			if (rs.next()) {
				c = convertir(rs);
			}else {
				return c;
			}
			
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a obtener", ex);
		}
		return c;
	}
	
	private Category convertir(ResultSet rs) throws DAOException {
		Category category = null;
		try {
			int recommendedAge = rs.getInt("recommendedAge");
			
			category = new Category(recommendedAge);
			category.setNameCategory(rs.getString("nameCategory"));
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando se convierte", ex);
		}
		return category;
	}

}
