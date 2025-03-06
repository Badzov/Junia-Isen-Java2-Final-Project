package app.service;

import java.util.List;

import db.daos.PersonDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Person;

//Same singleton pattern as in PersonDao (explained there)

public class PersonService {
	
	private final PersonDao personDao;
	private ObservableList<Person> persons;
	private static volatile PersonService instance;
	
	private PersonService() {
		this.personDao  = PersonDao.getInstance();
		List<Person> personsList = personDao.getPersons();
		this.persons = FXCollections.observableArrayList(personsList);
	}
	
    public static PersonService getInstance() {
        if (instance == null) {
            synchronized (PersonDao.class) {
                if (instance == null) {
                    instance = new PersonService();
                }
            }
        }
        return instance;
    }
    
    public ObservableList<Person> getPersons() {
		return persons;
	}
    
    public void addPerson(Person person) {
		personDao.addPerson(person);
		refreshPersons();
	}
	
	public void updatePerson(Person person) {
		personDao.updatePerson(person);
		refreshPersons();
	}
	
	public void deletePerson(Person person) {
		personDao.deletePerson(person);
		refreshPersons();
	}
	
	public ObservableList<Person> searchPersons(String searchText) {
        ObservableList<Person> filteredList = FXCollections.observableArrayList();
        for (Person person : persons) {
            if (person.getFirstname().toLowerCase().contains(searchText.toLowerCase()) ||
                person.getLastname().toLowerCase().contains(searchText.toLowerCase()) ||
                person.getFirstname().concat(" ").concat(person.getLastname()).toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(person);
            }
        }
        return filteredList;
    }
	
	public void refreshPersons() {
		List<Person> personsList = personDao.getPersons();
		persons.clear();
		persons.addAll(personsList);
	}
	
}
