package com.alexander.probes.entity;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;
	
	@ManyToMany
    @JoinTable(name = "role_user", 
      joinColumns = { @JoinColumn(name = "user_id")}, 
      inverseJoinColumns = { @JoinColumn(name = "role_id")})
	private List<Role> roles;
	
	@Column(name="token")
	private String  token = "";
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public User() {}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public User(String name,String email, String password,
			List<Role> roles, String token) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.token = token;
	}
	
	public User(User user) {
		// TODO Auto-generated constructor stub
		this.id = user.id;
		this.name = user.name;
		this.email = user.email;
		this.password = user.password;
		this.roles = user.roles;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", roles=" + roles
				+ ", token=" + token + "]";
	}
	
}
