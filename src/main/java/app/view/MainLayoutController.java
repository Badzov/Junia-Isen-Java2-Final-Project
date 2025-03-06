package app.view;

import app.App;
import javafx.application.Platform;

public class MainLayoutController {

	public void closeApplication() {
		Platform.exit();
	}

	public void gotoHome() {
		App.showView("HomeScreen");
	}
	
	public void gotoContactsView() {
		App.showView("ContactsView");
	}

}
