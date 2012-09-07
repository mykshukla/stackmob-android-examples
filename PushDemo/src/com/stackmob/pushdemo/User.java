package com.stackmob.pushdemo;

import com.stackmob.sdk.model.StackMobUser;

public class User extends StackMobUser {

	public User(String username, String password) {
		super(User.class, username, password);
	}
}
