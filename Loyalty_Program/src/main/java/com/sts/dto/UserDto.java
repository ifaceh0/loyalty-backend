package com.sts.dto;

public class UserDto 
{
		private String phoneNumber;
		private Long User_Id;
		private String Name;
		private Integer Totalpoints;
		
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
		
		
		

		
		
		
}
