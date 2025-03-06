package app.view;

import java.io.File;
import java.time.LocalDate;
import app.service.PersonService;
import app.service.FileService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import models.Person;
import utils.PersonValueFactory;

public class ContactsViewController {

	@FXML
	public TableView<Person> personsTable;
	@FXML
	public TableColumn<Person,String> personColumn;
	@FXML
	public TextField textfieldFirstName;
	@FXML
	public TextField textfieldLastName;
	@FXML
	public TextField textfieldNickname;
	@FXML
	public TextField textfieldPhoneNumber;
	@FXML
	public TextField textfieldAdress;
	@FXML
	public TextField textfieldEmail;
	@FXML
	public TextField textfieldDateofBirth;
	@FXML
	public ToggleButton toggleButtonEdit;
	@FXML
	public AnchorPane formPane;
	@FXML
	public Button buttonAdd;
	@FXML
	public Button buttonSave;
	@FXML
	public Button buttonDelete;
	@FXML
	public Button buttonSearch;
	@FXML
	public TextField textfieldSearch;
	@FXML
	public Button buttonUpload;
	@FXML
	public ImageView imageviewPicture;
	
	private Person currentPerson;
	private Person previouslySelected;
	private File selectedImageFile;
	
	@FXML
	private void initialize() {
		//Here we bind the column width to the table width so that it resizes with the table but subtract 2 to avoid a horizontal scrollbar
		personColumn.prefWidthProperty().bind(Bindings.subtract(personsTable.widthProperty(), 2));
		//Sets the display member of Persons
		personColumn.setCellValueFactory(new PersonValueFactory());
		repopulateList();
		//A basic listener to show the details of the selected person
		personsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
		exitEditMode();
	}
	
	//Here we implemented the method to handle the deselecting of a person which is not implemented in the TableView code
	//It was a needed feature mostly for the Add new button, but is a good feature to have in general
	//It turned to be a bit of a headscratcher but it ended up being very elegant
	@FXML
	private void handlePersonClicked(MouseEvent event) {
        if (currentPerson != null) {
        	selectedImageFile = null;
            if (currentPerson.equals(previouslySelected)) {
                // If the clicked item is already selected, deselect it
            	personsTable.getSelectionModel().clearSelection();
                previouslySelected = null;
                currentPerson = null;// Reset the previously selected item and current item
            } else {
                // Otherwise, update the previously selected item
                previouslySelected = currentPerson;
            }
        } else {
            // If no item is selected, reset the previously selected item
            previouslySelected = null;
        }
	}
	
	@FXML
	private void handleEditButton() {
		if (toggleButtonEdit.isSelected()) {
			enterEditMode();
		} else {
			exitEditMode();
		}
	}
	
	@FXML
	private void handleDeleteButton() {
		if (currentPerson == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Information");
	        alert.setHeaderText("No person selected");
	        alert.setContentText("Please select a person to delete");
	        alert.showAndWait();
			return;
		}
		Alert alert1 = new Alert(AlertType.CONFIRMATION);
		alert1.setTitle("Confirmation");
		alert1.setContentText("Are you sure you want to delete this person?");
		alert1.showAndWait();
		if (!alert1.getResult().getText().equals("OK")) {
			return;
		}
		PersonService.getInstance().deletePerson(currentPerson);
		repopulateList();
		Alert alert2 = new Alert(AlertType.INFORMATION);
        alert2.setTitle("Information");
        alert2.setHeaderText("Person deleted successfully");
        alert2.showAndWait();
	}
	
	@FXML
	private void handleSaveButton() {
		if (currentPerson == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Information");
	        alert.setHeaderText("No person selected");
	        alert.setContentText("Please select a person to edit");
	        alert.showAndWait();
			return;
		}
		if (!validateFields()) {
			return;
		} 
		//If we uploaded a picture, we copy it to a proper path and set the persons picture_path to the newly created path in pictures
		if (selectedImageFile != null) {
            String relativePath = FileService.getInstance().uploadImage(selectedImageFile);
            if (relativePath != null) {
                currentPerson.setPicture_path(relativePath);
            }
            selectedImageFile = null; // Clear the temporary file field
        }
		currentPerson.setFirstname(textfieldFirstName.getText());
		currentPerson.setLastname(textfieldLastName.getText());
		currentPerson.setNickname(textfieldNickname.getText());
		currentPerson.setPhone_number(textfieldPhoneNumber.getText());
		currentPerson.setAdress(textfieldAdress.getText());
		currentPerson.setEmail_adress(textfieldEmail.getText());
		currentPerson.setBirth_date(LocalDate.parse(textfieldDateofBirth.getText()));
		PersonService.getInstance().updatePerson(currentPerson);
		repopulateList();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Infromation");
        alert.setHeaderText("Edit saved successfully");
        alert.showAndWait();
	}
	
	@FXML
	private void handleAddButton() {
		if (currentPerson != null) {
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Information");
	        alert.setHeaderText("Person already selected");
	        alert.setContentText("Please deselect the current person to add a new one");
	        alert.showAndWait();
			return;
		}
		if (!validateFields()) {
			return;
		}
		Person newPerson = new Person(
				textfieldLastName.getText(),
				textfieldFirstName.getText(),
				textfieldNickname.getText(),
				textfieldPhoneNumber.getText(),
				textfieldAdress.getText(),
				textfieldEmail.getText(),
				LocalDate.parse(textfieldDateofBirth.getText()),
				null);
		
	    if (selectedImageFile != null) {
	        String relativePath = FileService.getInstance().uploadImage(selectedImageFile);
	        if (relativePath != null) {
	            newPerson.setPicture_path(relativePath); 
	        }
	        selectedImageFile = null; 
	    }
		PersonService.getInstance().addPerson(newPerson);
		repopulateList();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Infromation");
		alert.setHeaderText("Person added successfully");
		alert.showAndWait();
	}
	
	@FXML
	private void handleSearchButton() {
		//This if statement might be reduntat but it prevents the change of the list reference if the search field is empty
		//Otherwise we would get the filteredList from PersonService instead of the original persons list, which would still work
		if (textfieldSearch.getText().isEmpty()) {
			repopulateList();
			return;
		}
		personsTable.setItems(PersonService.getInstance().searchPersons(textfieldSearch.getText()));
		personsTable.refresh();
	}
	
	@FXML
	public void handleUploadButton() {
	    FileChooser fileChooser = new FileChooser();
	    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
	    fileChooser.getExtensionFilters().add(filter);
	    File file = fileChooser.showOpenDialog(imageviewPicture.getScene().getWindow());
	    if (file != null) {
	        // Stores the selected file temporarily
	        selectedImageFile = file;
	        // Displays the selected image in the ImageView
	        imageviewPicture.setImage(new Image(file.toURI().toString()));
	    }
	}

	//Here we call this method everytime we make a change to the list of persons, it also comes in handy in resetting the search
	private void repopulateList() {
		personsTable.setItems(PersonService.getInstance().getPersons());
		//Here we dont clear the search field with the rest
		textfieldSearch.clear();
		refreshList();
	}
	
	private void refreshList() {
		personsTable.refresh();
		personsTable.getSelectionModel().clearSelection();
		currentPerson = null;
		previouslySelected = null;
		emptyFields();
	}
	
	private void emptyFields() {
		selectedImageFile = null;
		textfieldFirstName.clear();
		textfieldLastName.clear();
		textfieldNickname.clear();
		textfieldPhoneNumber.clear();
		textfieldAdress.clear();
		textfieldEmail.clear();
		textfieldDateofBirth.clear();
		//Here it just loads the default placeholder picture
		imageviewPicture.setImage(new Image("file:pictures/placeholder.jpg"));
	}


	private void showPersonDetails(Person person) {
		if (person == null) {
			emptyFields();
		} else {
			formPane.setVisible(true);
			currentPerson = person;
			textfieldFirstName.setText(person.getFirstname());
			textfieldLastName.setText(person.getLastname());
			textfieldNickname.setText(person.getNickname());
			textfieldPhoneNumber.setText(person.getPhone_number());
			textfieldAdress.setText(person.getAdress());
			textfieldEmail.setText(person.getEmail_adress());
			textfieldDateofBirth.setText(person.getBirth_date().toString());
			
			if (person.getPicture_path() != null && !person.getPicture_path().isEmpty()) {
                File imageFile = new File(person.getPicture_path());
                if (imageFile.exists()) {
                    imageviewPicture.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    imageviewPicture.setImage(new Image("file:pictures/placeholder.jpg"));
                }
            } else {
                imageviewPicture.setImage(new Image("file:pictures/placeholder.jpg"));
            }
		}
	}
	
	private void enterEditMode() {
		textfieldFirstName.setEditable(true);
		textfieldLastName.setEditable(true);
		textfieldNickname.setEditable(true);
		textfieldPhoneNumber.setEditable(true);
		textfieldAdress.setEditable(true);
		textfieldEmail.setEditable(true);
		textfieldDateofBirth.setEditable(true);
		buttonSave.setVisible(true);
		buttonDelete.setVisible(true);
		buttonAdd.setVisible(true);
		buttonUpload.setVisible(true);
	}
	
	private void exitEditMode() {
		textfieldFirstName.setEditable(false);
		textfieldLastName.setEditable(false);
		textfieldNickname.setEditable(false);
		textfieldPhoneNumber.setEditable(false);
		textfieldAdress.setEditable(false);
		textfieldEmail.setEditable(false);
		textfieldDateofBirth.setEditable(false);
		buttonSave.setVisible(false);
		buttonDelete.setVisible(false);
		buttonAdd.setVisible(false);
		buttonUpload.setVisible(false);
	}
	
	private boolean validateFields() {
		if (textfieldFirstName.getText().isEmpty() || textfieldLastName.getText().isEmpty() || textfieldNickname.getText().isEmpty() || textfieldPhoneNumber.getText().isEmpty() || textfieldAdress.getText().isEmpty() || textfieldEmail.getText().isEmpty() || textfieldDateofBirth.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Empty Fields");
	        alert.setContentText("Please fill all the fields");
	        alert.showAndWait();
	        return false;
		}
		try {
			LocalDate.parse(textfieldDateofBirth.getText());
		} catch (Exception e) {
			 Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setHeaderText("Parsing Error");
		        alert.setContentText("Please enter a valid date format YYYY-MM-DD");	
		        alert.showAndWait();
		        return false;
		}
		return true;
	}
}
