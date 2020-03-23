/*
* 										FinalProject
* 										Group:
* 											
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Client extends Application {

	// creating Window Components
	// creating GUI Components
	// this will contain the window
	VBox container = new VBox();
	// contains nameLabel,ClientNameField and save button
	GridPane upperSide = new GridPane();
	// this is create scrollbar for the textarea
	ScrollPane chatBar = new ScrollPane();
	TextArea chatText = new TextArea();
	// this will contain the replybutton, messageBody
	GridPane lowerSide = new GridPane();
	// create UI controls
	// this will create reply button which triggers the response function.
	Button replyBtn = new Button("Reply");
	// this button will save the chat text into a txt file.
	Button saveBtn = new Button("Save");
	Label nameLbl = new Label("Client Name");
	//reply Body
	TextField messageBody = new TextField();
	// this will add the user name from the textfield
	TextField ClientNameField = new TextField();
	// Target file to save the conversation.
	File saveAsFile;
	// File Chooser to save the file
	FileChooser fil2_chooser = new FileChooser();

	@Override
	public void start(Stage primaryStage) throws Exception {
		// this will create and configure User Window
		setupGUI(primaryStage);

	}

	private void setupGUI(Stage primaryStage) {
		// this method will configure the upperSide GridPane
		// contains nameLabel,ClientNameField and save button
		setupUpperside(primaryStage);
		// this method will configure the lowerSide GridPane 
		//contains replyBtn,messageBody
		setupLowerSide();
		// this method will configure the ScrollBar and the TextArea
		setupChat();
		// adding childern to Vbox
		setupContainer();
		// show the scene
		setupStage(primaryStage);
	}

	// this function will setup the stage settings
	private void setupStage(Stage primaryStage) {
		Scene scene = new Scene(container);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Client");
		primaryStage.show();
	}

	// this function will setup the container area
	private void setupContainer() {
		// add components in order to fill the window
		container.getChildren().addAll(upperSide, chatBar, lowerSide);
	}

	/*
	 * this function will setup the the chat scroll bar it will add the textarea to
	 * it
	 */
	private void setupChat() {
		// this will disable the textarea using SetEditable();
		chatText.setEditable(false);
		// this will add the textarea to the scrollbar
		chatBar.setContent(chatText);
		// this will change the default font color to black

	}

	// this function will setup the chat components
	private void setupLowerSide() {
		// this will add the reply button and the messageBody to the LowerPane
		lowerSide.add(replyBtn, 0, 0);
		lowerSide.add(messageBody, 1, 0);
		replyBtn.setOnAction(
				// setup the reply by adding the text to chat area and write it to the client.
				
				e -> {//check if any of the fields is empty and stop the process
					if (messageBody.getText().isEmpty() == false & ClientNameField.getText().isEmpty()==false)
						{chatText.appendText(
								"Client(" + ClientNameField.getText() + "):" + "    " + messageBody.getText() + "\n");
						// will stop the client from changing name
						ClientNameField.setDisable(true);
						}
					
					else {// this code will stop the user from sending empty replies it uses alert Box
						setupErrorBox();
					}
					// reset the reply text
					messageBody.setText("");
					// send the response to the client
					// -----
					
				});

	}

	// this code will stop the user from sending empty replies it uses alert Box
	private void setupErrorBox() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Box");
		alert.setContentText("Reply or Name is empty");
		alert.showAndWait();
	}

	// this function will setup the name components
	private void setupUpperside(Stage primaryStage) {
		upperSide.add(nameLbl, 0, 0);
		upperSide.add(ClientNameField, 1, 0);
		upperSide.add(saveBtn, 2, 0);
		saveBtn.setOnAction(r -> {
			saveAsFile = fil2_chooser.showSaveDialog(primaryStage);
			saveAs();
		});

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	private void saveAs() {
//creates buffer writer, get the textarea content, write to the file, then it closes the connection
		try (Writer writer = new BufferedWriter(new FileWriter(saveAsFile))) {
			String chatContent = chatText.getText();
			writer.write(chatContent);
			writer.close();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Conformation Box");
			alert.setContentText("Conversation has been successfully saved");
			alert.showAndWait();
			
		} catch (Exception e) {
			System.out.println("Problem while writing the file");
			e.printStackTrace();
		}

	}

}
