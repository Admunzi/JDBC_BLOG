package dao.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dao.CategoryDAO;
import dao.CommentDAO;
import dao.DAOException;
import dao.DAOManager;
import dao.PostDAO;
import dao.UserDAO;

public class ManagerMysqlDAO implements DAOManager{
	
	private Connection conn;
	
	//Quitado null
	private CategoryDAO category;
	private CommentDAO comment;
	private PostDAO post;
	private UserDAO user;
	
	final String IMPORT_DB = 
			"DROP TABLE comment;"
			+ "DROP TABLE post;"
			+ "DROP TABLE user;"
			+ "DROP TABLE category;"
						
			+ "CREATE TABLE category ("
			+ "  nameCategory varchar(255) NOT NULL,"
			+ "  recommendedAge int(2) NOT NULL"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"			
			
			+ "INSERT INTO category (nameCategory, recommendedAge) VALUES"
			+ "('Comida', 18),"
			+ "('General', 18),"
			+ "('Medicina', 18),"
			+ "('Peliculas', 25),"
			+ "('Salud', 20);"
			
			+ "CREATE TABLE comment ("
			+ "  idComment int(11) NOT NULL,"
			+ "  name varchar(255) NOT NULL,"
			+ "  commentContent text NOT NULL,"
			+ "  commentCreatedDate date NOT NULL,"
			+ "  score float NOT NULL,"
			+ "  idPost int(11) NOT NULL"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
			
			+ "INSERT INTO comment (idComment, name, commentContent, commentCreatedDate, score, idPost) VALUES"
			+ "(12, 'Joselito', 'Buen post', '2021-12-18', 7.8, 25),"
			+ "(13, 'Maripili', 'Osu, nene que bonico.', '2021-12-12', 9.9, 25),"
			+ "(14, 'Anastasia', 'Pos mu bien', '2021-11-18', 8.8, 26),"
			+ "(15, 'Bugs bunny', 'Brrrrrrr brrrrr', '2023-12-16', 4.4, 26);"
			
			+ "CREATE TABLE post ("
			+ "  idPost int(11) NOT NULL,"
			+ "  title varchar(255) NOT NULL,"
			+ "  postCreateDate date NOT NULL,"
			+ "  idUser int(11) NOT NULL,"
			+ "  nameCategory varchar(255) NOT NULL,"
			+ "  content text NOT NULL"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
			
			+ "INSERT INTO post (idPost, title, postCreateDate, idUser, nameCategory, content) VALUES"
			+ "(24, 'Vacaciones de verano', '2021-12-24', 1, 'General', 'Lorem Rajoy Ipsum viva el vino un vaso es un vaso y un plato es un plato A veces moverse es bueno, otras veces no mejor para mí el suyo, beneficio político. No es cosa menor, dicho de otra manera, es cosa mayor un vaso es un vaso y un plato es un plato exportar es positivo porque vendes lo que produces somos sentimientos y tenemos seres humanos A veces moverse es bueno, otras veces no una cosa es ser solidario y otra es serlo a cambio de nada.'),"
			+ "(25, 'Comida de familia', '2020-12-10', 5, 'Comida', 'Lorem Rajoy Ipsum viva el vino un vaso es un vaso y un plato es un plato A veces moverse es bueno, otras veces no mejor para mí el suyo, beneficio político. No es cosa menor, dicho de otra manera, es cosa mayor un vaso es un vaso y un plato es un plato exportar es positivo porque vendes lo que produces somos sentimientos y tenemos seres humanos A veces moverse es bueno, otras veces no una cosa es ser solidario y otra es serlo a cambio de nada.'),"
			+ "(26, 'Medicina peligrosa', '2021-10-15', 7, 'Medicina', 'Lorem Rajoy Ipsum viva el vino un vaso es un vaso y un plato es un plato A veces moverse es bueno, otras veces no mejor para mí el suyo, beneficio político. No es cosa menor, dicho de otra manera, es cosa mayor un vaso es un vaso y un plato es un plato exportar es positivo porque vendes lo que produces somos sentimientos y tenemos seres humanos A veces moverse es bueno, otras veces no una cosa es ser solidario y otra es serlo a cambio de nada.');"
			
			+ "CREATE TABLE user ("
			+ "  idUser int(11) NOT NULL,"
			+ "  name varchar(255) NOT NULL,"
			+ "  lastname varchar(255) NOT NULL,"
			+ "  username varchar(50) NOT NULL,"
			+ "  mail varchar(320) NOT NULL,"
			+ "  password varchar(255) NOT NULL,"
			+ "  userCreatedDate date NOT NULL"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
			
			+ "INSERT INTO user (idUser, name, lastname, username, mail, password, userCreatedDate) VALUES"
			+ "(1, 'Dani', 'Ayala', 'Admunzi', 'daniaya@gmail.com', 'dani123', '2021-11-09'),"
			+ "(5, 'Pepe', 'El tuercas', 'pepon', 'pepe@gmail.com', 'pepe123', '2021-12-06'),"
			+ "(6, 'Maria', 'Del monte', 'mariiia', 'maria@gmail.com', 'maria123', '2021-12-19'),"
			+ "(7, 'David', 'Bisbal', 'davisito', 'buleria@gmail.com', 'david123', '2022-01-02');"
			
			+ "ALTER TABLE category"
			+ "  ADD PRIMARY KEY (nameCategory);"
			
			+ "ALTER TABLE comment"
			+ "  ADD PRIMARY KEY (idComment),"
			+ "  ADD KEY id_post (idPost);"
			
			+ "ALTER TABLE post"
			+ "  ADD PRIMARY KEY (idPost),"
			+ "  ADD KEY id_user (idUser),"
			+ "  ADD KEY fk_foreign_nameCategory_name (nameCategory);"
			
			+ "ALTER TABLE user"
			+ "  ADD PRIMARY KEY (idUser);"
			
			+ "ALTER TABLE comment"
			+ "  MODIFY idComment int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;"
			
			+ "ALTER TABLE post"
			+ "  MODIFY idPost int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;"
			
			+ "ALTER TABLE user"
			+ "  MODIFY idUser int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;"
			
			+ "ALTER TABLE comment"
			+ "  ADD CONSTRAINT comment_ibfk_1 FOREIGN KEY (idPost) REFERENCES post (idPost) ON DELETE CASCADE;"

			+ "ALTER TABLE post"
			+ "  ADD CONSTRAINT fk_foreign_nameCategory_name FOREIGN KEY (nameCategory) REFERENCES category (nameCategory) ON DELETE CASCADE,"
			+ "  ADD CONSTRAINT post_ibfk_1 FOREIGN KEY (idUser) REFERENCES user (idUser) ON DELETE CASCADE;";
	
	public ManagerMysqlDAO(String host, String username, String password, String database) throws SQLException {
		conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?allowMultiQueries=true", username, password);
	}
	
	@Override
	public CategoryDAO getCategoryDAO() {
		if (category == null) {
			category = new CategoryMysqlDAO(conn);
		}
		return category;
	}

	@Override
	public CommentDAO getCommentDAO() {
		if (comment == null) {
			comment = new CommentMysqlDAO(conn);
		};
		return comment;
	}

	@Override
	public PostDAO getPostDAO() {
		if (post == null) {
			post = new PostMysqlDAO(conn);
		};
		return post;
	}

	@Override
	public UserDAO getUserDAO() {
		if (user == null) {
			user = new UserMysqlDAO(conn);
		};
		return user;
	}
	
	public void insertarBD() throws DAOException {
		try (PreparedStatement stat = conn.prepareStatement(IMPORT_DB);){
			
			stat.execute();
		}catch (SQLException ex) {
			throw new DAOException("Error en SQL, cuando vas a insertar", ex);
		}
	}
	
	
}
