package com.sb.integration.service;

import java.sql.Connection;

import com.sb.integration.vo.UserVo;

public interface ValidatorService {
	public void signupValidation(UserVo userDetails, Connection con) throws Exception;
}
