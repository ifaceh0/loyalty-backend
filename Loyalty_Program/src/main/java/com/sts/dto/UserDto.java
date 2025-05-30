package com.sts.dto;

public class UserDto 
{
	private String phoneNumber;
	private Long User_Id;
	private String Name;
	private Integer Totalpoints;
	private String qrToken;
    private String qrCodeBase64;
    private String Email;
	
	public Integer getTotalpoints() {
		return Totalpoints;
	}
	public void setTotalpoints(Integer totalpoints) {
		Totalpoints = totalpoints;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Long getUser_Id() {
		return User_Id;
	}
	public void setUser_Id(Long user_Id) {
		User_Id = user_Id;
	}				
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}		
	public String getQrToken() {
		return qrToken;
	}
	public void setQrToken(String qrToken) {
		this.qrToken = qrToken;
	}
	public String getQrCodeBase64() {
		return qrCodeBase64;
	}
	public void setQrCodeBase64(String qrCodeBase64) {
		this.qrCodeBase64 = qrCodeBase64;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
}
