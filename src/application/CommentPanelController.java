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
import modelo.Comment;

public class CommentPanelController {

	private Comment comment;
	private boolean editable;
	private List<Comment> listComments = new ArrayList<>();
	
	
    @FXML private ImageView borrar;
    @FXML private ImageView cancelar;
    @FXML private ImageView editar;
    @FXML private ImageView guardar;
    @FXML private ImageView nuevo;
        
    @FXML private TableView<Comment> table;
    
    @FXML private TableColumn<Comment, String> colCommentContent;
    @FXML private TableColumn<Comment, LocalDate> colCommentDate;
    @FXML private TableColumn<Comment, Long> colId;
    @FXML private TableColumn<Comment, Long> colIdPost;
    @FXML private TableColumn<Comment, String> colName;
    @FXML private TableColumn<Comment, Float> colScore;

    @FXML private TextField inputPost;
    @FXML private TextArea inputContenido;
    @FXML private DatePicker inputDate;
    @FXML private TextField inputNombre;
    @FXML private TextField inputPuntuacion;
    
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
		        	Comment comment = getCommentSelected(conecc);
		        	setComment(comment);
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
		    	setComment(null);
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
				setComment(null);
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
					Comment comme = getComment();
					if (comprobarCampos(comme)) {
						if (comme.getIdComment() == null) {
							//Crea nuevo post
							conecc.getCommentDAO().insertar(comme);
						}else {
							//Modifica el post
							conecc.getCommentDAO().modificar(comme);
						}
					}else {
						JOptionPane.showMessageDialog(null, "No puede haber campos vacios","Error",JOptionPane.ERROR_MESSAGE);
					}
					
					setComment(null);
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
						Comment comment = getCommentSelected(conecc);
						conecc.getCommentDAO().eliminar(comment);
						showTableWithData(conecc);
					}
					
					event.consume();
				} catch (DAOException | SQLException e) {
					e.printStackTrace();
				}
		     }
		});
	}
	
	public boolean comprobarCampos(Comment comment) {
		if (comment.getCommentContent() == "" || comment.getIdPost() == null || comment.getScore() == null || comment.getName() == "" ){
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
		listComments = conecc.getCommentDAO().obtenerTodos();		
		ObservableList<Comment> listObser = FXCollections.observableArrayList(listComments);

		this.colId.setCellValueFactory(new PropertyValueFactory<Comment,Long>("idComment"));
		this.colName.setCellValueFactory(new PropertyValueFactory<Comment,String>("name"));
		this.colCommentContent.setCellValueFactory(new PropertyValueFactory<Comment,String>("commentContent"));
		this.colCommentDate.setCellValueFactory(new PropertyValueFactory<Comment,LocalDate>("commentCreatedDate"));
		this.colScore.setCellValueFactory(new PropertyValueFactory<Comment,Float>("score"));
		this.colIdPost.setCellValueFactory(new PropertyValueFactory<Comment,Long>("idPost"));

		table.setItems(listObser);
	}

	private Comment getCommentSelected(ManagerMysqlDAO conecc) throws DAOException {
		Long id = table.getSelectionModel().getSelectedItem().getIdComment();
		return conecc.getCommentDAO().obtener(id);
	}
	
	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		inputNombre.setDisable(editable);
		inputContenido.setDisable(editable);
		inputDate.setDisable(editable);
		inputPuntuacion.setDisable(editable);
		inputPost.setDisable(editable);
	}
	
	public void loadData(ManagerMysqlDAO conecc) {
		if (comment != null) {
			inputNombre.setText(comment.getName());
			inputContenido.setText(comment.getCommentContent());
			inputDate.setValue(new java.sql.Date(comment.getCommentCreatedDate().getTime()).toLocalDate());
			inputPuntuacion.setText(comment.getScore().toString());
			inputPost.setText(comment.getIdPost().toString());
		}else {
			inputNombre.setText("");
			inputContenido.setText("");
			inputDate.setValue(null);
			inputPuntuacion.setText("");
			inputPost.setText("");
		}
		inputNombre.requestFocus();
	}
	
	public void saveData(ManagerMysqlDAO conecc) {
		if (comment == null) {
			comment = new Comment();
		}
		comment.setName(inputNombre.getText());
		comment.setCommentContent(inputContenido.getText());
		if (inputDate.getValue() != null) {
			comment.setCommentCreatedDate(Date.from(inputDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		}
		if (inputPuntuacion.getText() != "") {
			comment.setScore(Float.parseFloat(inputPuntuacion.getText()));
		}else {
			comment.setScore(null);
		}
		if (inputPost.getText() != "") {
			comment.setIdPost(Long.parseLong(inputPost.getText()));
		}else {
			comment.setIdPost(null);
		}	
		
	}
}
