package application;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import dao.DAOException;
import dao.mysql.ManagerMysqlDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import modelo.Post;

public class PostPanelController {

	private Post post;
	private boolean editable;
	private List<Post> listPosts = new ArrayList<>();
	
    @FXML private ImageView borrar;
    @FXML private ImageView cancelar;
    @FXML private ImageView editar;
    @FXML private ImageView guardar;
    @FXML private ImageView nuevo;

    @FXML private TableView<Post> table;
    
    @FXML private TableColumn<Post, String> colContenido;
    @FXML private TableColumn<Post, LocalDate> colFecha;
    @FXML private TableColumn<Post, Long> colId;
    @FXML private TableColumn<Post, String> colNombreCategoria;
    @FXML private TableColumn<Post, Long> colIdUser;
    @FXML private TableColumn<Post, String> colTitulo;

    @FXML private TextField inputCategoria;
    @FXML private TextArea inputContenido;
    @FXML private DatePicker inputFecha;
    @FXML private TextField inputTitulo;
    @FXML private TextField inputUser;
    
	@FXML
    public void initialize() throws SQLException, DAOException {
		ManagerMysqlDAO conecc = new ManagerMysqlDAO("localhost", "root", "", "blog");
		desactivarBarra();
		showTableWithData(conecc);
		createListener(conecc);
	}
	
	private void createListener(ManagerMysqlDAO conecc) {
		table.getSelectionModel().selectedItemProperty().addListener(e ->{
			editar.setDisable(false);
			editar.setOpacity(1);
			borrar.setDisable(false);
			borrar.setOpacity(1);
		});
		
		editar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		         try {
					Post post = getPostSelected(conecc);
					setPost(post);
					setEditable(false);
					loadData(conecc);
					guardar.setDisable(false);
					guardar.setOpacity(1);
					cancelar.setDisable(false);
					cancelar.setOpacity(1);
				} catch (DAOException e) {
					e.printStackTrace();
				}finally {
					event.consume();
				}
		     }
		});
		
		cancelar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		    	setPost(null);
				setEditable(true);
				loadData(conecc);
				table.getSelectionModel().clearSelection();
				desactivarBarra();
				
				event.consume();
		     }
		});
		
		nuevo.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setPost(null);
				loadData(conecc);
				setEditable(false);
				guardar.setDisable(false);
				guardar.setOpacity(1);
				cancelar.setDisable(false);
				cancelar.setOpacity(1);
				borrar.setDisable(true);
				borrar.setOpacity(0.2);
				editar.setDisable(true);
				editar.setOpacity(0.2);
				 
				 event.consume();
			}
		});
		
		guardar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
				try {
					saveData(conecc);
					Post pos = getPost();
					if (comprobarCampos(pos)) {
						if (pos.getIdPost() == null) {
							//Crea nuevo post
							conecc.getPostDAO().insertar(pos);
						}else {
							//Modifica el post
							conecc.getPostDAO().modificar(pos);
						}
					}else {
						JOptionPane.showMessageDialog(null, "No puede haber campos vacios","Error",JOptionPane.ERROR_MESSAGE);
					}
					
					setPost(null);
					setEditable(true);
					loadData(conecc);
					table.getSelectionModel().clearSelection();
					desactivarBarra();
					showTableWithData(conecc);
					
					event.consume();
				} catch (DAOException | SQLException e) {
					e.printStackTrace();
				}
		     }
		});
		
		borrar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
				try {
					if (JOptionPane.showConfirmDialog(null, "¿Seguro que quieres borrar este post?", "Borrar post", 
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						Post post = getPostSelected(conecc);
						conecc.getPostDAO().eliminar(post);
						showTableWithData(conecc);
					}
					
					event.consume();
				} catch (DAOException | SQLException e) {
					e.printStackTrace();
				}
		     }
		});
	}
	
	public boolean comprobarCampos(Post post) {
		if (post.getContent() == "" || post.getIdUser() == null || post.getNameCategory() == "" || post.getTitle() == "" ){
			return false;
		}
		return true;
	}
	
	private void desactivarBarra() {
		editar.setDisable(true);
		editar.setOpacity(0.2);
		cancelar.setDisable(true);
		cancelar.setOpacity(0.2);
		borrar.setDisable(true);
		borrar.setOpacity(0.2);
		guardar.setDisable(true);
		guardar.setOpacity(0.2);
		setEditable(true);
	}
	
	private void showTableWithData(ManagerMysqlDAO conecc) throws DAOException, SQLException {
		listPosts = conecc.getPostDAO().obtenerTodos();		
		ObservableList<Post> listObser = FXCollections.observableArrayList(listPosts);

		this.colId.setCellValueFactory(new PropertyValueFactory<Post,Long>("idPost"));
		this.colTitulo.setCellValueFactory(new PropertyValueFactory<Post,String>("title"));
		this.colFecha.setCellValueFactory(new PropertyValueFactory<Post,LocalDate>("postCreateDate"));
		this.colIdUser.setCellValueFactory(new PropertyValueFactory<Post,Long>("idUser"));
		this.colNombreCategoria.setCellValueFactory(new PropertyValueFactory<Post,String>("nameCategory"));
		this.colContenido.setCellValueFactory(new PropertyValueFactory<Post,String>("content"));

		table.setItems(listObser);
	}

	private Post getPostSelected(ManagerMysqlDAO conecc) throws DAOException {
		Long id = table.getSelectionModel().getSelectedItem().getIdPost();
		return conecc.getPostDAO().obtener(id);
	}
	
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		inputTitulo.setDisable(editable);
		inputFecha.setDisable(editable);
		inputUser.setDisable(editable);
		inputCategoria.setDisable(editable);
		inputContenido.setDisable(editable);
	}
	
	public void loadData(ManagerMysqlDAO conecc) {
		if (post != null) {
			inputTitulo.setText(post.getTitle());
			inputFecha.setValue(new java.sql.Date(post.getPostCreateDate().getTime()).toLocalDate());
			inputUser.setText(post.getIdUser().toString());
			inputCategoria.setText(post.getNameCategory());
			inputContenido.setText(post.getContent());
		}else {
			inputTitulo.setText("");
			inputFecha.setValue(null);
			inputUser.setText("");
			inputCategoria.setText("");
			inputContenido.setText("");
		}
		inputTitulo.requestFocus();
	}
	
	public void saveData(ManagerMysqlDAO conecc) {
		if (post == null) {
			post = new Post();
		}
		post.setTitle(inputTitulo.getText());
		if (inputFecha.getValue() != null) {
			post.setPostCreateDate(Date.from(inputFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		}
		if (inputUser.getText() != "") {
			post.setIdUser(Long.parseLong(inputUser.getText()));
		}else {
			post.setIdUser(null);
		}
		post.setNameCategory(inputCategoria.getText());		
		post.setContent(inputContenido.getText());
	}

}
