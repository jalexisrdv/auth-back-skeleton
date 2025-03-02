package com.jardvcode.authentication.mapper;

import com.jardvcode.authentication.dto.UserDTO;
import com.jardvcode.shared.mapper.AbstractMapper;
import com.jardvcode.user.entity.UserEntity;

public class UserMapper extends AbstractMapper<UserDTO, UserEntity> {
	
	public UserEntity fromDTO(UserDTO dto) {
		UserEntity userEntity = new UserEntity();
		userEntity.setUsername(dto.email());
		userEntity.setPassword(dto.password());
		userEntity.setVerificationCode(dto.verificationCode());
		return userEntity;
	}
	
	public UserDTO fromEntity(UserEntity entity) {
		UserDTO dto = new UserDTO(entity.getUsername(), null, null);
		return dto;
	}

}
