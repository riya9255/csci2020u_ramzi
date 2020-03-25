
/*
* 										CSCI 2020 FinalProject
* 										Group13
* 											
*/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;

public class Agent extends Application {

	// creating Window Components
	// creating GUI Components
	// this will contain the window
	VBox container = new VBox();
	// contains nameLabel,AgentNameField and save button
	GridPane upperSide = new GridPane();
	// this is create scrollbar for the textarea
	ScrollPane chatBar = new ScrollPane();
	TextArea chatText = new TextArea();
	// this will contain the reply button, messageBody
	GridPane lowerSide = new GridPane();
	// create UI controls
	// this will create reply button which triggers the response function.
	Button replyBtn = new Button("Reply");
	// this button will save the chat text into a txt file.
	Button saveBtn = new Button("Save Chat");
	Label nameLbl = new Label(" Agent Name:");
	// reply Body
	TextField messageBody = new TextField();
	// this will add the user name from the textfield
	TextField AgentNameField = new TextField();
	File loadFile;
	// this will create reply button which triggers the response function.
	Button loadBtn = new Button("Products");
	// Target file to save the conversation.
	File saveAsFile;
	//this will create a file chooser to load data from a txt file
	FileChooser fil_chooser = new FileChooser(); 
	// File Chooser to save the file
	FileChooser fil2_chooser = new FileChooser();

	// Main start function
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("whats up ");
		// This will start the chat server, see near bottom of program for server code
		startChat();
		// this will create and configure User Window
		primaryStage.setResizable(false);
		setupGUI(primaryStage);

	}

	private void setupGUI(Stage primaryStage) {
		// this method will configure the upperSide GridPane
		// contains nameLabel,AgentNameField and save button
		setupUpperSide(primaryStage);
		// this method will configure the lowerSide GridPane
		// contains replyBtn,messageBody
		setupLowerSide();
		// this method will configure the ScrollBar and the TextArea
		setupChat();
		// adding children to Vbox
		setupContainer();
		// show the scene
		setupStage(primaryStage);
	}

	// this function will setup the stage settings
	private void setupStage(Stage primaryStage) {
		Scene scene = new Scene(container);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Agent");
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
		// Set width
		chatText.setMaxWidth(1000);

		// this will disable the textarea using SetEditable();
		chatText.setEditable(false);
		// this will add the textarea to the scrollbar
		chatBar.setContent(chatText);
		// this will change the default font color to black

	}

	// this function will setup the chat components
	private void setupLowerSide() {
		// Set number of cols to organize elements
		int numCols = 4;
		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPrefWidth(110);
			if (i == 0) {
				colConst.setPrefWidth(104);
			}
			lowerSide.getColumnConstraints().add(colConst);
		}
		// this will add the reply button and the messageBody to the LowerPane
		messageBody.setMaxWidth(2000);
		lowerSide.add(replyBtn, 4, 0);
		lowerSide.add(messageBody, 0, 0, 4, 1);
		replyBtn.setOnAction(
				// setup the reply by adding the text to chat area and write it to the Agent.

				e -> {// check if any of the fields is empty and stop the process
					if (messageBody.getText().isEmpty() == false & AgentNameField.getText().isEmpty() == false) {
						//this will reply to the client as well as loading the products files
						sendChat();
						// will stop the Agent from changing name
						AgentNameField.setDisable(true);
					}

				else {// this code will stop the user from sending empty replies it uses alert Box
						setupErrorBox();
					}
					// reset the reply text
					messageBody.setText("");
					// send the response to the Agent
					startChat();
					// -----

				});

	}

	private void sendChat() {
		chatText.appendText(
				"\nAgent (" + AgentNameField.getText() + "):" + "    " + messageBody.getText());
		// Send message to Client
		sendMessage("\nAgent (" + AgentNameField.getText() + "):" + "    " + messageBody.getText());
	}

	// this code will stop the user from sending empty replies it uses alert Box
	private void setupErrorBox() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Box");
		alert.setContentText("Reply or Name is empty.");
		alert.showAndWait();
	}

	// this function will setup the name components
	private void setupUpperSide(Stage primaryStage) {
		// Set number of cols to organize elements
		int numCols = 4;
		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			if (i == 0) {
				colConst.setPrefWidth(80);
			} else if (i == 2) {
				colConst.setPrefWidth(130);
			} else {
				colConst.setPrefWidth(100);
			}
			upperSide.getColumnConstraints().add(colConst);
		}
		AgentNameField.setMaxWidth(1000);
		upperSide.add(nameLbl, 0, 0);
		upperSide.add(AgentNameField, 1, 0, 2, 1);
		upperSide.add(saveBtn, 4, 0);
		upperSide.add(loadBtn, 5, 0);
		loadBtn.setOnAction(e->{
			try {
				loadFile=fil_chooser.showOpenDialog(primaryStage);
				loadProduct();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		saveBtn.setOnAction(r -> {
			//this will add filters when saving files it will save it as txt file
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text File", "*.txt");
		    fil2_chooser.getExtensionFilters().add(extFilter);
			saveAsFile = fil2_chooser.showSaveDialog(primaryStage);
			saveAs();
		});

	}

	private void loadProduct() throws Exception {

		try {//this code will readinputstream(file), add the content to messageBody and will show a confirmation window
			Alert alert = new Alert(AlertType.CONFIRMATION);
			//thiwill read a file, add the streaminput into into reply area and finally send the reply to the client
			Scanner inputStream = new Scanner(loadFile);
			while (inputStream.hasNext()) {
				String data = inputStream.nextLine();
				messageBody.appendText(data);
				sendChat();
				//this will reset the reply text after data finish transfering
				messageBody.setText("");
			}
			//this will show a conformation that file has been loaded successfully
			alert.setTitle("Confirmation Box");
			alert.setContentText("File has been successfully loaded!");
			alert.showAndWait();
		} catch (FileNotFoundException e) {
			System.out.println("Something went wrong while reading the file");
		}
		
		
		
	}

	// this will write conversation to the file using writer and bufferedwriter
	private void saveAs() {
		// creates buffer writer, get the textarea content, write to the file, then it
		// closes the connection
		try (Writer writer = new BufferedWriter(new FileWriter(saveAsFile))) {
			String chatContent = chatText.getText();
			writer.write(chatContent);
			writer.close();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Box");
			alert.setContentText("Conversation has been successfully saved!");
			alert.showAndWait();

		} catch (Exception e) {
			System.out.println("Problem while writing the file.");
			e.printStackTrace();
		}

	}

	/*********************************************
	 * SERVER CODE
	 *********************************************/
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private String message = "";
	private Socket connection;

	// Function to start server and chat
	public void startChat() {
		// Create another thread for server to run on
		Runnable serverTask = new Runnable() {
			@Override
			public void run() {

				try {
					// Establish server socket and max number of backlogged connected users
					server = new ServerSocket(3000, 50);
					while (true) {
						try {
							// Wait for connection and run input and output stream functions to retrieve and
							// send messages
							waitingToConnect();
							setupStreams();
							connectedChat();
						} catch (EOFException eofException) {
							// If server connection interrupted print error message
							showMessage("\n ERROR: Connection interrupted. ");
						} finally {
							// Close connection when done
							closeConnection();
						}
					}
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		};
		// Start server thread
		Thread serverThread = new Thread(serverTask);
		serverThread.start();
	}

	// Function to connect to server and client
	private void waitingToConnect() throws IOException {
		// Display waiting message then connection info when connected
		showMessage(" Waiting for a client to connect... \n");
		connection = server.accept();
		showMessage("\n Now connected to: " + connection.getInetAddress().getHostName());
	}

	// Function to setup streams
	private void setupStreams() throws IOException {
		// Setup input and output streams
		input = new ObjectInputStream(connection.getInputStream());
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		// Print confirmation message
		showMessage("\n Client is ready to chat. \n");
	}

	// Function to handle messages while chatting
	private void connectedChat() throws IOException {
		do {
			try {
				// Append incoming user's message
				message = (String) input.readObject();
				showMessage(message);
			} catch (ClassNotFoundException classNotFoundException) {
				// If message sent is not valid, print error message
				showMessage("ERROR: User has sent an invalid object!");
			}
		} while (!message.equals("END CHAT"));

	}

	// Function to close the connections
	public void closeConnection() {
		showMessage("\n Closing Connections... \n");
		try {
			// Close path to and from client, as well as connection
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Send message to the client
	private void sendMessage(String message) {
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException ioException) {
			chatText.appendText("\n ERROR: message not sent, please retry.");
		}
	}

	// Update chat windows with message
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Show message in text box
				chatText.appendText(text);
			}
		});
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}