package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dao.DAOException;
import dao.mysql.ManagerMysqlDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import modelo.Category;

public class CategoryPanelController {
	
	//private CategoryDAO lista = manager.getCategoryDAO();
	//private CategoryTableModel model;
	
	private Category category;
	private boolean editable;
	private List<Category> listCategory = new ArrayList<>();
	
	@FXML private TableView<Category> table;
	
    @FXML private TableColumn<Category, Integer> colAge;
    @FXML private TableColumn<Category, String> colName;
	
    @FXML private TextField inputNombre;
    @FXML private TextField inputEdad;
    
    @FXML private ImageView borrar;
    @FXML private ImageView cancelar;
    @FXML private ImageView editar;
    @FXML private ImageView guardar;
    @FXML private ImageView nuevo;
    
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
					Category category = getCategorySelected(conecc);
					setCategory(category);
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
				setCategory(null);
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
				setCategory(null);
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
					Category cat = getCategory();
					
					if (comprobarCampos(cat)) {
						//Hay campos vacios
						JOptionPane.showMessageDialog(null, "No puede haber campos vacios","Error",JOptionPane.ERROR_MESSAGE);
					}else {
						if (conecc.getCategoryDAO().obtener(cat.getNameCategory()) == null) {
							conecc.getCategoryDAO().insertar(cat);
						}else {
							conecc.getCategoryDAO().modificar(cat);
						}
					}

					setCategory(null);
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
					if (JOptionPane.showConfirmDialog(null, "¿Seguro que quieres borrar esta categoría?", "Borrar categoría", 
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						Category category = getCategorySelected(conecc);
						conecc.getCategoryDAO().eliminar(category);
						showTableWithData(conecc);
					}
					
					event.consume();
				} catch (DAOException | SQLException e) {
					e.printStackTrace();
				}
		     }
		});
	}
	
	public boolean comprobarCampos(Category cat) {
		if (cat.getNameCategory() == "") {
			return true;
		}
		return false;
	}

	private Category getCategorySelected(ManagerMysqlDAO conecc) throws DAOException {
		String name = table.getSelectionModel().getSelectedItem().getNameCategory();
		return conecc.getCategoryDAO().obtener(name);
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
		listCategory = conecc.getCategoryDAO().obtenerTodos();		
		ObservableList<Category> listObser = FXCollections.observableArrayList(listCategory);
		
		this.colName.setCellValueFactory(new PropertyValueFactory<Category,String>("nameCategory"));
		this.colAge.setCellValueFactory(new PropertyValueFactory<Category,Integer>("recommendedAge"));
		table.setItems(listObser);
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		inputNombre.setDisable(editable);
		inputEdad.setDisable(editable);
	}
	
	public void loadData() {
		if (category != null) {
			inputNombre.setText(category.getNameCategory());
			inputEdad.setText(Integer.toString(category.getRecommendedAge()));
		}else {
			inputNombre.setText("");
			inputEdad.setText("");
		}
		inputNombre.requestFocus();
	}
	
	public void saveData() {
		if (category == null) {
			category = new Category();
		}
		category.setNameCategory(inputNombre.getText());
		category.setRecommendedAge(Integer.parseInt(inputEdad.getText()));
	}
}
	