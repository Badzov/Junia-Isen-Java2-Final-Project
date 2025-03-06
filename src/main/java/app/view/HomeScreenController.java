package app.view;


import javafx.scene.text.Text;
import java.io.IOException;
import app.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeScreenController {
	
	@FXML
	public Button launchButton;
	@FXML
	public Text text;
	
	int i = 0;
	
	public void handleLaunchButton() throws IOException {
		if (i == 0) {
			text.setText("Are you sure???");
			launchButton.setText("I'm sure, just do it");
			i++;
		}
		else if (i == 1) {
			text.setText("I'm warning you");
			launchButton.setText("Please just launch the app already");
			launchButton.setPrefWidth(211);
			launchButton.setLayoutX(250);
			i++;
		}
		else if (i == 2) {
			text.setText("The answer to life, the universe... and what was it again.. it's been a while since I read that book..");
			launchButton.setText("42");
			launchButton.setPrefWidth(120);
			launchButton.setLayoutX(295);
			i++;
		}
		else if (i == 3) {
			text.setText("Oops");
			launchButton.setText("Launch");
			launchButton.setLayoutX(174);
			launchButton.setLayoutY(270);
			i++;
		}
		else if (i == 4) {
			text.setText("This isn't funny anymore is it");
			launchButton.setLayoutX(537);
			launchButton.setLayoutY(372);
			i++;
		}
		else if (i == 5) {
			text.setText("Fine, you win.");
			launchButton.setPrefWidth(435);
			launchButton.setPrefHeight(230);
			launchButton.setLayoutX(138);
			launchButton.setLayoutY(161);
			i++;
		}
		else if (i == 6) {
			App.showView("ContactsView");
		}
		
	}
}
