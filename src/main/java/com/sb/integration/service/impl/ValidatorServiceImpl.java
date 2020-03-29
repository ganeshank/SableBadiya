package com.sb.integration.service.impl;

import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sb.integration.service.UserService;
import com.sb.integration.service.ValidatorService;
import com.sb.integration.vo.UserVo;

public class ValidatorServiceImpl implements ValidatorService {

	public void signupValidation(UserVo userDetails, Connection con) throws Exception {

		// Email validation...
		Boolean emailValidation = getValidate(userDetails.getEmail(),
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		if (!emailValidation) {
			System.out.println("Signup emailValidation is failed.....");
			throw new Exception("Email format is not correct!!!!");
		}

		// password validation...
		String password = userDetails.getPassword();
		System.out.println(password.length());
		Boolean passwordValidation = (password.length() > 6 && password.length() < 20) ? true : false;

		if (!passwordValidation) {
			System.out.println("Signup passwordValidation is failed.....");
			throw new Exception("Password should have atleast 6 to 20 characters!!!");
		}

		// Contact Number validation
		Boolean contactValidation = getValidate(userDetails.getMobileNumber(), "[0-9]+");
		if (!contactValidation) {
			System.out.println("Signup contact number validation is failed.....");
			throw new Exception("Contact number should have Numbers Only");
		}

		// Checking email or contact is already exist.
		UserService userService = new UserServiceImpl();

		userService.isEmailOrContactAlreadyExist(userDetails.getEmail(), userDetails.getMobileNumber(), con);
	}

	private Boolean getValidate(String validatorStr, String pattern) {

		Pattern patter = Pattern.compile(pattern);
		Matcher matcher = patter.matcher(validatorStr);

		return matcher.matches();
	}
}
