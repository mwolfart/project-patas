package model;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userPatas")
public class User {
	
	//TODO: DO WE NEED ID?
	
	@Id 
	@GeneratedValue
	private Long id;
	private String username;
	private byte[] passwordHash;
	private byte[] salt;
	private Integer userType;
	private String fullName;
	
	public User() {}
	
	public User(Long id, String username, byte[] passwordHash, byte[] salt, Integer userType, String fullName) {
		super();
		this.id = id;
		this.username = username;
		this.passwordHash = passwordHash;
		this.salt = salt;
		this.userType = userType;
		this.fullName = fullName;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public byte[] getPasswordHash() {
		return passwordHash;
	}
	
	public void setPasswordHash(byte[] passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", passwordHash="
				+ Arrays.toString(passwordHash) + ", salt="
				+ Arrays.toString(salt) + ", userType=" + userType +
				", fullName=" + fullName + "]";
	}
}
