package app.service;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import db.daos.PersonDao;

public class FileService {

    private static final String PICTURES_FOLDER_PATH = "pictures/";
    private static volatile FileService instance;
    
    private FileService() {
        ensurePicturesDirectory();
    }
    
    public static FileService getInstance() {
        if (instance == null) {
            synchronized (PersonDao.class) {
                if (instance == null) {
                    instance = new FileService();
                }
            }
        }
        return instance;
    }

    private void ensurePicturesDirectory() {
        File directory = new File(PICTURES_FOLDER_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String uploadImage(File file) {
        if (file == null) {
            return null;
        }
        
        try {
            // Define the destination file
            File destination = new File(PICTURES_FOLDER_PATH + file.getName());
            // Copy the file to the destination
            Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Return the relative path
            return PICTURES_FOLDER_PATH + file.getName();
            
        } catch (IOException e) {
            // Handle file copy errors
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File Copy Failed");
            alert.setContentText("An error occurred while copying the file: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
            return null;
        }
    }
}
