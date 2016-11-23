package com.cigital.integration.service;

import java.sql.Connection;

import com.cigital.integration.vo.UserVo;

public interface ValidatorService {
	public void signupValidation(UserVo userDetails, Connection con) throws Exception;
}
