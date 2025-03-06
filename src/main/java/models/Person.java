package models;

import java.time.LocalDate;

public class Person {

	private int idperson;
	private String lastname;
	private String firstname;
	private String nickname;
	private String phone_number;
	private String adress;
	private String email_adress;
	private LocalDate birth_date;
	private String picture_path;
	
	public Person() {
	}
	
	public Person(int idperson, String lastname, String firstname, String nickname, String phone_number, String adress,
			String email_adress, LocalDate birth_date, String picture_path) {
		super();
		this.idperson = idperson;
		this.lastname = lastname;
		this.firstname = firstname;
		this.nickname = nickname;
		this.phone_number = phone_number;
		this.adress = adress;
		this.email_adress = email_adress;
		this.birth_date = birth_date;
		this.picture_path = picture_path;
	}
	
	public Person(String lastname, String firstname, String nickname, String phone_number, String adress,
			String email_adress, LocalDate birth_date, String picture_path) {
		super();
		this.lastname = lastname;
		this.firstname = firstname;
		this.nickname = nickname;
		this.phone_number = phone_number;
		this.adress = adress;
		this.email_adress = email_adress;
		this.birth_date = birth_date;
		this.picture_path = picture_path;
	}

	public int getIdperson() {
		return idperson;
	}

	public void setIdperson(int idperson) {
		this.idperson = idperson;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getEmail_adress() {
		return email_adress;
	}

	public void setEmail_adress(String email_adress) {
		this.email_adress = email_adress;
	}

	public LocalDate getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(LocalDate birth_date) {
		this.birth_date = birth_date;
	}
	
	public String getPicture_path() {
		return picture_path;
	}
	
	public void setPicture_path(String picture_path) {
		this.picture_path = picture_path;
	}
}
