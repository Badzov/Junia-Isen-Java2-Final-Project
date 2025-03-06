package app;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.daos.DataSourceFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

//Application made by: BADZOV and PEDRAZA :)

public class App extends Application {

    private static Scene scene;
    private static BorderPane mainlayout;
    
    public static void main(String[] args) {
        launch();
    }

    public void init() throws Exception {
        ensurePicturesDirectory();
        try {
            initDb();
        } catch (SQLException e) {
            e.printStackTrace();
            throw (e);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Contact App");
        mainlayout = loadFXML("MainLayout");
        scene = new Scene(mainlayout);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        App.showView("HomeScreen");
    }

    private static <T> T loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/app/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void showView(String rootElement) {
        try {
            mainlayout.setCenter(loadFXML(rootElement));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void initDb() throws Exception {
        Connection connection = DataSourceFactory.getConnection();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS person (" +
                "  idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  lastname VARCHAR(45) NOT NULL," +
                "  firstname VARCHAR(45) NOT NULL," +
                "  nickname VARCHAR(45) NOT NULL," +
                "  phone_number VARCHAR(15) NULL," +
                "  adress VARCHAR(200) NULL," +
                "  email_adress VARCHAR(150) NULL," +
                "  birth_date DATE NULL," +
                "  picture_path TEXT NULL" +
                ");");
        //These next lines were used during the testing so that the DataBase resets itself every run.
        //Also the JUnit tests alter the database so these lines can be uncommented after the tests to have some fresh data
//        stmt.executeUpdate("DELETE FROM person");
//        stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='person'");
//        stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, adress, email_adress, birth_date, picture_path) "
//                + "VALUES (1, 'Soprano', 'Tony', 'Tone', '0605544701', '14 Aspen Drive, New Jersey', 'tony.soprano@example.com', '1959-08-22 00:00:00.000', 'pictures/tony soprano.png')");
//        stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, adress, email_adress, birth_date, picture_path) "
//                + "VALUES (2, 'Moltisanti', 'Christopher', 'Chris', '0604251700', '667 Bergen Avenue, New Jersey', 'christ.moltisanti@example.com', '1969-07-22 00:00:00.000', 'pictures/christopher moltisanti.png')");
        stmt.close();
        connection.close();
    }

    private void ensurePicturesDirectory() {
        File picturesDir = new File("pictures");
        if (!picturesDir.exists()) {
            picturesDir.mkdirs();
        }
    }
}
