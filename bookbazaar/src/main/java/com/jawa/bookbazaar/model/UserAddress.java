package com.jawa.bookbazaar.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFilter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

//V
@Entity
@Table(name = "user_address")
@JsonFilter("AddressFilter")
public class UserAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	@NotEmpty(message = "address cannot be empty")
	@NotNull(message = "address cannot be empty")
	private String address;

	@Column
	@NotEmpty(message = "city cannot be empty")
	@NotNull(message = "city cannot be empty")
	private String city;

	@Column
	@NotEmpty(message = "state cannot be empty")
	@NotNull(message = "state cannot be empty")
	private String state;

	@Column
	@NotEmpty(message = "country cannot be empty")
	@NotNull(message = "country cannot be empty")
	private String country;

	@Column
	@Min(value = 100000, message = "invalid pin")
	@Max(value = 999999, message = "invalid pin")
	private int pin;

	@Column
	@NotEmpty(message = "landmark cannot be empty")
	@NotNull(message = "landmark cannot be empty")
	private String landmark;

	@Column(name = "mobile_no")
	@Min(value = 5000000000l, message = "Enter valid mobile number")
	@Max(value = 9999999999l, message = "Enter valid mobile number")
	@JsonAlias("mobile")
	private long mobile;

	public UserAddress() {

	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "UserAddress [id=" + id + ", address=" + address + ", city=" + city + ", state=" + state + ", country="
				+ country + ", pin=" + pin + ", landmark=" + landmark + ", mobile=" + mobile + "]";
	}

}
