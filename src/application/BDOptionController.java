package application;

import java.io.IOException;
import java.sql.SQLException;

import dao.DAOException;
import dao.mysql.ManagerMysqlDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BDOptionController {
	
	@FXML
	private void importarBD(ActionEvent event) throws SQLException, DAOException {
		ManagerMysqlDAO conecc = new ManagerMysqlDAO("localhost", "root", "", "blog");
		conecc.insertarBD();
		abrirVentana();
	}
	
	@FXML
	private void usarLocal(ActionEvent event) {
		abrirVentana();
	}
	
	private void abrirVentana() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
			
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.setTitle("Principal menu");
			stage.setResizable(false);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
