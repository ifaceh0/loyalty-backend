package com.sts.dto;

import lombok.Data;

@Data
public class UserDto {
//	private String phoneNumber;
//	private Long User_Id;
//	private String Name;
//	private Integer Totalpoints;
//	private String qrToken;
//    private String qrCodeBase64;
//    private String Email;

	private String phone;
	private Long userId;
	private String firstName;
	private Integer availablePoints;
	private String qrToken;
	private String qrCodeBase64;
	private String email;
}
