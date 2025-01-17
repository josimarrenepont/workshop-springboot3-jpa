package com.educandoweb.course.entities;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "tb_user")
public class User implements UserDetails, Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "user_name", unique = true)
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "account_non_expired")
	private Boolean accountNonExpired;
	@Column(name = "account_non_locked")
	private Boolean accountNonLocked;

	@Column(name = "credentials_non_expired")
	private Boolean credentialsNonExpired;

	@Column(name = "enabled")
	private Boolean enabled;

	private String email;
	private String phone;


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_permission",
			joinColumns = @JoinColumn(name = "permission_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Set<Permission> permissions;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Order> orders = new ArrayList<>();

    public User() {}

    public User(long l, String user, String mail, String number, String password) {
	}

	public User(Long id, String username, String password, Boolean accountNonExpired,
				Boolean accountNonLocked, Boolean credentialsNonExpired, Boolean enabled,
				String email, String phone, Set<Permission> permissions) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.email = email;
		this.phone = phone;
		this.permissions = permissions;
	}

    public List<String> getRoles(){
		List<String> roles = new ArrayList<>();
		for(Permission permission : permissions){
			roles.add(permission.getDescription());
		}
		return roles;
	}

	public List<Order> getOrders() {
		return orders;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.permissions;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Boolean getCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getUserName(),
				user.getUserName()) && Objects.equals(getPassword(), user.getPassword())
				&& Objects.equals(isAccountNonExpired(), user.isAccountNonExpired()) && Objects.equals(isAccountNonLocked(), user.isAccountNonLocked())
				&& Objects.equals(isCredentialsNonExpired(), user.isCredentialsNonExpired())
				&& Objects.equals(isEnabled(), user.isEnabled()) && Objects.equals(getEmail(),
				user.getEmail()) && Objects.equals(getPhone(), user.getPhone())
				&& Objects.equals(getPermissions(), user.getPermissions())
				&& Objects.equals(getOrders(), user.getOrders());
	}
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getUserName(), getPassword(),
				isAccountNonExpired(), isAccountNonLocked(), isCredentialsNonExpired(),
				isEnabled(), getEmail(), getPhone(), getPermissions(), getOrders());
	}
}
