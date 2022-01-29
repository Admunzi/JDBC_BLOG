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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import modelo.User;

public class UserPanelController {

	private User user;
	private boolean editable;
	private List<User> listUsers = new ArrayList<>();
	
    @FXML private ImageView borrar;
    @FXML private ImageView cancelar;
    @FXML private ImageView editar;
    @FXML private ImageView guardar;
    @FXML private ImageView nuevo;
    
    @FXML private TableView<User> table;

    @FXML private TableColumn<User, LocalDate> colDate;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, Long> colId;
    @FXML private TableColumn<User, String> colLastname;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colPassword;
    @FXML private TableColumn<User, String> colUser;
    
    @FXML private TextField inputApellidos;
    @FXML private TextField inputCorreo;
    @FXML private DatePicker inputFecha;
    @FXML private TextField inputNombre;
    @FXML private TextField inputPassword;
    @FXML private TextField inputUsuario;

	@FXML
    public void initialize() throws SQLException, DAOException {
		ManagerMysqlDAO conecc = new ManagerMysqlDAO("localhost", "root", "", "blog");
		desactivarBarra();
		showTableWithData(conecc);
		createListener(conecc);
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
		listUsers = conecc.getUserDAO().obtenerTodos();		
		ObservableList<User> listObser = FXCollections.observableArrayList(listUsers);

		this.colId.setCellValueFactory(new PropertyValueFactory<User,Long>("idUser"));
		this.colName.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
		this.colLastname.setCellValueFactory(new PropertyValueFactory<User,String>("lastname"));
		this.colUser.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
		this.colPassword.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
		this.colEmail.setCellValueFactory(new PropertyValueFactory<User,String>("mail"));
		this.colDate.setCellValueFactory(new PropertyValueFactory<User,LocalDate>("userCreatedDate"));
		
		table.setItems(listObser);
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
					User user = getUserSelected(conecc);
					setUser(user);;
					setEditable(false);
					loadData();
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
		    	 setUser(null);
				setEditable(true);
				loadData();
				table.getSelectionModel().clearSelection();
				desactivarBarra();
				
				event.consume();
		     }
		});
		
		nuevo.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setUser(null);
				loadData();
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
					saveData();
					User use = getUser();
					if (use.getIdUser() == null) {
						if (comprobarCampos(use)) {
							//Hay campos vacios
							JOptionPane.showMessageDialog(null, "No puede haber campos vacios","Error",JOptionPane.ERROR_MESSAGE);
						}else {
							conecc.getUserDAO().insertar(use);
						}
					}else {
						if (comprobarCampos(use)) {
							//Hay campos vacios
							JOptionPane.showMessageDialog(null, "No puede haber campos vacios","Error",JOptionPane.ERROR_MESSAGE);
						}else {
							conecc.getUserDAO().modificar(use);
						}
						
					}
					setUser(null);
					setEditable(true);
					loadData();
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
					if (JOptionPane.showConfirmDialog(null, "¿Seguro que quieres borrar este usuario?", "Borrar usuario", 
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						User user = getUserSelected(conecc);
						conecc.getUserDAO().eliminar(user);
						showTableWithData(conecc);
					}
					
					event.consume();
				} catch (DAOException | SQLException e) {
					e.printStackTrace();
				}
		     }
		});
	}
	
	public boolean comprobarCampos(User use) {
		if (use.getLastname() == "" || use.getMail() == "" || use.getName() == "" || use.getPassword() == "" || use.getUsername() == "") {
			return true;
		}
		return false;
	}
	
	private User getUserSelected(ManagerMysqlDAO conecc) throws DAOException {
		Long id = table.getSelectionModel().getSelectedItem().getIdUser();
		return conecc.getUserDAO().obtener(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		inputNombre.setDisable(editable);
		inputApellidos.setDisable(editable);
		inputCorreo.setDisable(editable);
		inputFecha.setDisable(editable);
		inputPassword.setDisable(editable);
		inputUsuario.setDisable(editable);
	}
	
	public void loadData() {
		if (user != null) {
			inputNombre.setText(user.getName());
			inputApellidos.setText(user.getLastname());
			inputUsuario.setText(user.getUsername());
			inputPassword.setText(user.getPassword());
			inputCorreo.setText(user.getMail());
			inputFecha.setValue(new java.sql.Date(user.getUserCreatedDate().getTime()).toLocalDate());
		}else {
			inputNombre.setText("");
			inputApellidos.setText("");
			inputUsuario.setText("");
			inputPassword.setText("");
			inputCorreo.setText("");
			inputFecha.setValue(null);
		}
		inputNombre.requestFocus();
	}

	public void saveData() {
		if (user == null) {
			user = new User();
		}
		user.setName(inputNombre.getText());
		user.setLastname(inputApellidos.getText());
		user.setUsername(inputUsuario.getText());
		user.setPassword(inputPassword.getText());
		user.setMail(inputCorreo.getText());
		if (inputFecha.getValue() != null) {
			user.setUserCreatedDate(Date.from(inputFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		}
		
	}
}
