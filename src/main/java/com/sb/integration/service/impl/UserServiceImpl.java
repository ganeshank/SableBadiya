package com.sb.integration.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.sb.integration.dao.UserRepository;
import com.sb.integration.dao.impl.UserRepositoryImpl;
import com.sb.integration.service.CartService;
import com.sb.integration.service.SecurityService;
import com.sb.integration.service.UserService;
import com.sb.integration.util.Constants;
import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CouponVo;
import com.sb.integration.vo.RoleVo;
import com.sb.integration.vo.SellerVo;
import com.sb.integration.vo.UserVo;

public class UserServiceImpl implements UserService {
	
	public UserRepository userRepository = new UserRepositoryImpl();

	public void registerNewUser(UserVo userVo, Connection con)throws Exception {
	
		try {
			userRepository.registerNewUser(userVo, con, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public Boolean isUserNameExist(String userName, Connection con) throws Exception {

		try {
			Boolean isUserExist = userRepository.isUserNameExist(userName, con);
			return !isUserExist;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public UserVo checkLoginCredential(String emailOrConatct, String password, Connection con)throws Exception {
		try {
			return userRepository.checkLoginCredential(emailOrConatct, password, con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public Boolean isEmailOrContactAlreadyExist(String email, String contact, Connection con) throws Exception {

		try {
			return userRepository.isEmailOrContactAlreadyExist(email, contact, con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public List<AddressVo> getAddressesForUser(Connection con, Long userId) throws Exception {
	
		try {
			return userRepository.getAddressesForUser(con, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public void addAddressForUser(Connection con, AddressVo addressVo, Long userId, Boolean isGuestUser) throws Exception {
		try {
			userRepository.addAddressForUser(con, addressVo, userId, isGuestUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public void createUserRole(Connection con, Long userId, Integer roleId)throws Exception {
		try {
			userRepository.createUserRole(con, userId, roleId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public RoleVo getRoleForUser(Connection con, Long userId) throws Exception {
		try {
			return userRepository.getRoleForUser(con, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		
	}

	@Override
	public void addSeller(Connection con, SellerVo sellerVo) throws Exception {

		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAddress(Connection con, AddressVo addressVo) throws Exception {
		try {
			userRepository.updateAddress(con, addressVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateUserPersonalInfo(Connection con, UserVo userVo) throws Exception {
		try {
			userRepository.updateUserPersonalInfo(con, userVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveCustomerQuery(Connection con, String name, String email, String contact, String subject,
			String message) throws Exception {
		try {
			userRepository.saveCustomerQuery(con, name, email, contact, subject, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public CouponVo isCouponValid(Connection con, String couponNumber) throws Exception {
		// TODO Auto-generated method stub
		try {
			return userRepository.isCouponValid(con, couponNumber);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<String> getDOTDCustomerNumber(Connection con) throws Exception {
		try {
			return userRepository.getDOTDCustomerNumber(con);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public UserVo otherSignUpAndSignIn(UserVo userVo, Connection con, Integer accountSource) throws Exception {
		try{
			con.setAutoCommit(false);
			// varify email if already exist!!!
			UserVo userDetails = userRepository.isEmailAlreadyExist(con, userVo.getEmail());
			userVo.setAccountSourceType(accountSource);
			
			if(userDetails!=null){
				// then check that email id is other type of signin or not(1- Website login, 2-Gmail login....)
				// Type is other than 1 then proceed otherwise stop it.
				userVo.setUserId(userDetails.getUserId());
				/*if(userDetails.getAccountSourceType().equals(new Integer(1))){
					System.out.println("Account Type:::::"+ userDetails.getAccountSourceType());
					throw new Exception("Account type is not valid");
				}*/
			}
			else{
				// Signup the user and then login 
				
				SecurityService securityService = new SecurityServiceImpl();
				String encryptedPassword = securityService.getEncryptedPassword(userVo.getPassword());

				userVo.setPassword(encryptedPassword);
				userVo.setWalletAmount(BigDecimal.ZERO);

				// If validation is getting successful then create new record in the
				// table called USERS.
				UserService userService = new UserServiceImpl();
				userRepository.registerNewUser(userVo, con, accountSource);

				// Then create an empty cart for the new registered user.
				CartService cartService = new CartServiceImpl();
				cartService.createEmptyCart(con, userVo.getUserId());
				
				// Assign the role to new user.
				userService.createUserRole(con, userVo.getUserId(), Constants.REG_USER_ROLE_ID);
			}
			
			RoleVo roleVo = getRoleForUser(con, userVo.getUserId());
			userVo.setUserRole(roleVo);
			
			con.commit();
			return userVo;
		}catch(Exception e){
			con.rollback();
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public void createUserWallet(Connection con, Long userId, String walletAmount, String comment) throws Exception {
		// TODO Auto-generated method stub
		try {
			userRepository.createUserWallet(con, userId, walletAmount, comment);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateUserWallet(Connection con, Long userId, BigDecimal walletAmount) throws Exception {
		// TODO Auto-generated method stub
		try {
			userRepository.updateUserWallet(con, userId, walletAmount);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public UserVo getUserDetailById(Connection con, Long userId) throws Exception {
		try {
			return userRepository.getUserDetailById(con, userId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
