package com.stackmob.gigyademo;

import com.stackmob.sdk.model.StackMobUser;

public class User extends StackMobUser {

	public User() {
		super(User.class);
	}
	
	private String name;
	
	public String getName() { return name; }
	
	public void setName(String name) { this.name = name; }

}
