package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController {
	
	@FXML
	private void postButtom(ActionEvent event) {
		abrirVentana("PostPanel.fxml");
	}
	
	@FXML
	private void categoryButtom(ActionEvent event) {
		abrirVentana("CategoryPanel.fxml");
	}
	
	@FXML
	private void userButtom(ActionEvent event) {
		abrirVentana("UserPanel.fxml");
	}
	
	@FXML
	private void commentButtom(ActionEvent event) {
		abrirVentana("CommentPanel.fxml");
	}
	
	private void abrirVentana(String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Configuration panel");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
