package com.jardvcode.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jardvcode.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	
	public Optional<UserEntity> findByUsername(String username);
	public Optional<UserEntity> findByUsernameAndVerificationCode(String username, String verificationCode);

}
