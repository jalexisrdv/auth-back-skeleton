package com.jardvcode.user.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
	
	@Id
    @Column(name = "username")
    private String username;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    @Column(name = "verification_code")
    private String verificationCode;
    
    @Column(name = "verification_code_expiration_date")
    private LocalDateTime verificationCodeExpirationDate;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private List<AuthorityEntity> authorities;
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public LocalDateTime getVerificationCodeExpirationDate() {
		return verificationCodeExpirationDate;
	}

	public void setVerificationCodeExpirationDate(LocalDateTime verificationCodeExpirationDate) {
		this.verificationCodeExpirationDate = verificationCodeExpirationDate;
	}
	
	public List<AuthorityEntity> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(List<AuthorityEntity> authorities) {
		this.authorities = authorities;
	}
	
}
