package com.sb.integration.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sb.integration.dao.UserRepository;
import com.sb.integration.util.TimeStamp;
import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CouponVo;
import com.sb.integration.vo.RoleVo;
import com.sb.integration.vo.UserVo;

public class UserRepositoryImpl implements UserRepository {

	private static final String USER_NAME_EXIST = "SELECT COUNT(*) FROM  USERS WHERE USER_NAME = ?";

	private static final String REG_NEW_USER = "INSERT INTO  USERS (EMAIL,MOBILE_NUMBER,"
			+ "CREATED_DATE,MODIFIED_DATE,ACTIVE,PASSWORD,NAME,ACCOUNT_SOURCE, TOTAL_WALLET_AMOUNT)" + " VALUES(?,?,?,?,?,?,?,?,?)";

	private static final String CHECK_LOGIN = "SELECT * FROM  USERS WHERE (EMAIL = ? OR "
			+ "MOBILE_NUMBER = ?) AND PASSWORD=?";

	private static final String CHECK_EMAIL_OR_CONTACT = "SELECT EMAIL,MOBILE_NUMBER FROM  USERS "
			+ " WHERE EMAIL = ? OR MOBILE_NUMBER = ?";

	private static final String GET_ADDRESS_FOR_USER = "SELECT A.* FROM  ADDRESS A,  USER_ADDRESS UA "
			+ " WHERE A.ADDRESS_ID = UA.ADDRESS_ID AND A.ACTIVE=1 AND UA.ACTIVE=1 AND UA.USER_ID=?";

	private static final String ADD_NEW_ADDRESS = "INSERT INTO  ADDRESS (CONTACT_NAME, CONTACT_NUMBER, "
			+ "ADDRESS_LINE1, ADDRESS_LINE2, CITY, STATE, COUNTRY, PINCODE, LANDMARK, "
			+ "CREATED_BY, CREATED_DATE, ACTIVE, EMAIL) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private final static String ADD_ADDRESS_FOR_USER = "INSERT INTO  USER_ADDRESS (USER_ID, ADDRESS_ID,"
			+ "CREATED_BY, CREATED_DATE, ACTIVE) VALUES(?,?,?,?,?)";
	
	private final static String ADD_USER_ROLE="INSERT INTO  USER_ROLE (USER_ID, ROLE_ID, ACTIVE) VALUES(?,?,?)";
	
	private final static String GET_ROLE_FOR_USER = "SELECT R.* FROM  ROLE R,  USER_ROLE UR,  USERS U WHERE R.ROLE_ID = UR.ROLE_ID AND "
			+ " UR.USER_ID = U.USER_ID AND R.ACTIVE=1 AND UR.ACTIVE=1 AND U.ACTIVE=1 AND UR.USER_ID=?";
	
	private final static String GET_COADMIN_USER = "SELECT U.USER_ID FROM USERS U, USER_ROLE UR WHERE "
			+ "U.USER_ID = UR.USER_ID AND UR.ROLE_ID=4";
	
	private final static String GET_NOTIFICATION_FOR_USER = "SELECT NOTIFICATION_ID FROM NOTIFICATIONS WHERE USER_ID=? AND ACTIVE=1";
	
	private final static String INSERT_NOTIFICATION_MESSAGE_FOR_USER = "INSERT INTO NOTIFICATION_MESSAGE "
			+ "(NOTIFICATION_ID, NOTIFICATION_MESSAGE, CREATED_DATE, ACTIVE) VALUES(?,?,?,?)";
	
	private final static String UPDATE_ADDRESS = "UPDATE ADDRESS SET CONTACT_NAME=?, CONTACT_NUMBER=?, ADDRESS_LINE1=?, ADDRESS_LINE2=?,"
			+ "CITY=?, PINCODE=?, LANDMARK=?, MODIFIED_DATE=? WHERE ADDRESS_ID=?";
	
	private final static String UPDATE_USER_PERSONAL_INFO = "UPDATE USERS SET NAME=?, EMAIL=?, MOBILE_NUMBER=? WHERE USER_ID=? AND ACTIVE=1";

	private final static String SAVE_CUSTOMER_QUERY = "INSERT INTO CUSTOMER_QUERY(CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_CONTACT,"
			+ "SUBJECT, MESSAGE, ACTIVE, CREATED_DATE) VALUES(?,?,?,?,?,?,?)";
	
	private final static String GET_USER_BY_ID = "SELECT * FROM USERS WHERE USER_ID=?";
	
	private final static String VALIDATE_COUPON = "SELECT * FROM COUPONS WHERE COUPONS_NUMBER=? AND ACTIVE=?";
	
	private final static String SEND_MESSAGE = "SELECT CUSTOMER_NUMBER FROM dotd_customer_number WHERE ACTIVE=1";

	private final static String IS_EMAIL_EXIST = "SELECT * FROM USERS WHERE EMAIL=? AND ACTIVE=1";
	
	private static final String CHECK_LOGIN_WITH_EMAIL = "SELECT * FROM  USERS WHERE EMAIL = ? AND PASSWORD=?";
	
	private static final String CREATE_USER_WALLET = "INSERT INTO USERS_WALLET(USER_ID, WALLET_AMOUNT, COMMENT, ACTIVE, CREATED_DATE)"
			+ " VALUES(?,?,?,?,?)";
	
	private static final String UPDATE_USER_WALLET = "UPDATE USERS SET TOTAL_WALLET_AMOUNT=? WHERE USER_ID=?";
	
	public Boolean isUserNameExist(String userName, Connection con) throws Exception {

		try {
			PreparedStatement pst = con.prepareStatement(USER_NAME_EXIST);
			pst.setString(1, userName);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	public Boolean registerNewUser(UserVo userVo, Connection con, Integer accountSourceType) throws Exception {

		try {
			PreparedStatement pst = con.prepareStatement(REG_NEW_USER, Statement.RETURN_GENERATED_KEYS);

			pst.setString(1, userVo.getEmail());
			
			// For Gmail SignUp, mobile number is not able to fetch.
			if(accountSourceType.equals(new Integer(2)) && userVo.getMobileNumber()==null){
				pst.setNull(2, java.sql.Types.VARCHAR);
			}else{
				pst.setString(2, userVo.getMobileNumber());
			}
			
			//pst.setDate(3, new Date(new java.util.Date().getTime()));
			pst.setString(3, TimeStamp.getAsiaTimeStamp());
			pst.setDate(4, null);
			pst.setBoolean(5, true);
			pst.setString(6, userVo.getPassword());
			pst.setString(7, userVo.getFullName());
			pst.setInt(8, accountSourceType);
			pst.setBigDecimal(9, userVo.getWalletAmount()==null ? BigDecimal.ZERO : userVo.getWalletAmount());

			int result = pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			Long userId = null;
			if (rs != null && rs.next()) {
				userId = rs.getLong(1);
			}

			userVo.setUserId(userId);
			if (result != 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	public UserVo checkLoginCredential(String emailOrConatct, String password, Connection con) throws Exception {

		try {
			PreparedStatement pst = con.prepareStatement(CHECK_LOGIN);
			pst.setString(1, emailOrConatct);
			pst.setString(2, emailOrConatct);
			pst.setString(3, password);

			ResultSet rs = pst.executeQuery();

			UserVo userDetails = null;
			if (rs.next()) {
				userDetails = new UserVo();
				userDetails.setUserId(rs.getLong("USER_ID"));
				userDetails.setUserName(rs.getString("NAME"));
				userDetails.setFullName(rs.getString("NAME"));
				userDetails.setEmail(rs.getString("EMAIL"));
				userDetails.setMobileNumber(rs.getString("MOBILE_NUMBER"));
				userDetails.setAccountSourceType(rs.getInt("ACCOUNT_SOURCE"));
				userDetails.setWalletAmount(rs.getBigDecimal("TOTAL_WALLET_AMOUNT"));
				return userDetails;
			} else {
				return userDetails;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public Boolean isEmailOrContactAlreadyExist(String email, String contact, Connection con) throws Exception {

		try {
			PreparedStatement pst = con.prepareStatement(CHECK_EMAIL_OR_CONTACT);
			pst.setString(1, email);
			pst.setString(2, contact);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				String existEmail = rs.getString(1);
				String existContact = rs.getString(2);

				if (existEmail.equalsIgnoreCase(email)) {
					// Email is already used earlier.
					throw new Exception("Email is already used earlier, Please try with other email");
				} else if (existContact.equalsIgnoreCase(contact)) {
					// Contact is already user earlier.
					throw new Exception("Contact is already used earlier, Please try with other contact");
				} else {
					// Nothing TODO.
				}
			} else {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}

		return null;
	}

	@Override
	public List<AddressVo> getAddressesForUser(Connection con, Long userId) throws Exception {

		try {
			PreparedStatement pst = con.prepareStatement(GET_ADDRESS_FOR_USER);
			pst.setLong(1, userId);

			ResultSet rs = pst.executeQuery();

			List<AddressVo> addressVos = new ArrayList<>();
			AddressVo addressVo = null;
			while (rs.next()) {
				addressVo = new AddressVo();
				addressVo.setAddressId(rs.getLong("ADDRESS_ID"));
				addressVo.setAddressLine1(rs.getString("ADDRESS_LINE1"));
				addressVo.setAddressLine2(rs.getString("ADDRESS_LINE2"));
				addressVo.setCity(rs.getString("CITY"));
				addressVo.setContactName(rs.getString("CONTACT_NAME"));
				addressVo.setContactNumber(rs.getString("CONTACT_NUMBER"));
				addressVo.setCountry(rs.getString("COUNTRY"));
				addressVo.setLandMark(rs.getString("LANDMARK"));
				addressVo.setPinCode(rs.getString("PINCODE"));
				addressVo.setState(rs.getString("STATE"));
				addressVo.setEmail(rs.getString("EMAIL"));

				addressVos.add(addressVo);
			}

			return addressVos;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void addAddressForUser(Connection con, AddressVo addressVo, Long userId, Boolean isGuestUser) throws Exception {

		try {
			PreparedStatement pst = con.prepareStatement(ADD_NEW_ADDRESS, Statement.RETURN_GENERATED_KEYS);

			pst.setString(1, addressVo.getContactName());
			pst.setString(2, addressVo.getContactNumber());
			pst.setString(3, addressVo.getAddressLine1());
			pst.setString(4, addressVo.getAddressLine2());
			pst.setString(5, addressVo.getCity());
			pst.setString(6, addressVo.getState());
			pst.setString(7, "India");
			pst.setString(8, addressVo.getPinCode());
			pst.setString(9, addressVo.getLandMark());
			if(isGuestUser)
				pst.setNull(10, java.sql.Types.BIGINT);
			else
				pst.setLong(10, userId);
			//pst.setDate(11, new Date(new java.util.Date().getTime()));
			pst.setString(11, TimeStamp.getAsiaTimeStamp());
			pst.setBoolean(12, true);
			pst.setString(13, addressVo.getEmail());

			int result = pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			Long addressId = null;
			if (rs != null && rs.next()) {
				addressId = rs.getLong(1);
			}

			addressVo.setAddressId(addressId);

			if (result != 0) {
				// Insert into USER_ADDRESS table.\
				if(!isGuestUser)
					addAddressForUserAddressEntity(con, userId, addressId);
			} else {
				throw new Exception();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void addAddressForUserAddressEntity(Connection con, Long userId, Long addressId) throws Exception {

		try {
			PreparedStatement pst = con.prepareStatement(ADD_ADDRESS_FOR_USER);

			pst.setLong(1, userId);
			pst.setLong(2, addressId);
			pst.setLong(3, userId);
			//pst.setDate(4, new Date(new java.util.Date().getTime()));
			pst.setString(4, TimeStamp.getAsiaTimeStamp());
			pst.setBoolean(5, true);

			int result = pst.executeUpdate();

			if (result == 0) {
				throw new Exception();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void createUserRole(Connection con, Long userId, Integer roleId) throws Exception {

		try {
			PreparedStatement pst = con.prepareStatement(ADD_USER_ROLE);

			pst.setLong(1, userId);
			pst.setLong(2, roleId);
			pst.setBoolean(3, true);

			int result = pst.executeUpdate();

			if (result == 0) {
				throw new Exception();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
		
	}
	
	@Override
	public RoleVo getRoleForUser(Connection con, Long userId) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_ROLE_FOR_USER);
			pst.setLong(1, userId);

			ResultSet rs = pst.executeQuery();

			RoleVo role = null;
			while (rs.next()) {
				role = new RoleVo();
				role.setRoleId(rs.getLong("ROLE_ID"));
				role.setRoleName(rs.getString("ROLE_NAME"));
				role.setRoleDesc(rs.getString("ROLE_DESCRIPTION"));
			}

			return role;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public Long getCoAdminUser(Connection con) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_COADMIN_USER);

			ResultSet rs = pst.executeQuery();

			Long userId = null;
			// It may possible CoAdmin can be multiple, but for now we are taking first one.
			if (rs.next()) {
				userId = rs.getLong(1);
			}

			return userId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public Long getNotificationForUser(Connection con, Long userId) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_NOTIFICATION_FOR_USER);
			pst.setLong(1, userId);

			ResultSet rs = pst.executeQuery();

			Long notificationId = null;
			// It may possible CoAdmin can be multiple, but for now we are taking first one.
			if (rs.next()) {
				notificationId = rs.getLong(1);
			}

			return notificationId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void addNotification(Connection con, Long notificationId, String notificationMessage) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(INSERT_NOTIFICATION_MESSAGE_FOR_USER);

			pst.setLong(1, notificationId);
			pst.setString(2, notificationMessage);
			//pst.setDate(3, new Date(new java.util.Date().getTime()));
			pst.setString(3, TimeStamp.getAsiaTimeStamp());
			pst.setBoolean(4, true);

			int result = pst.executeUpdate();

			if (result == 0) {
				throw new Exception();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void updateAddress(Connection con, AddressVo addressVo) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(UPDATE_ADDRESS);
			pst.setString(1, addressVo.getContactName());
			pst.setString(2, addressVo.getContactNumber());
			pst.setString(3, addressVo.getAddressLine1());
			pst.setString(4, addressVo.getAddressLine2());
			pst.setString(5, addressVo.getCity());
			pst.setString(6, addressVo.getPinCode());
			pst.setString(7, addressVo.getLandMark());
			pst.setString(8, TimeStamp.getAsiaTimeStamp());
			pst.setLong(9, addressVo.getAddressId());

			int result = pst.executeUpdate();

			if (result == 0) {
				throw new Exception();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void updateUserPersonalInfo(Connection con, UserVo userVo) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(UPDATE_USER_PERSONAL_INFO);
			pst.setString(1, userVo.getFullName());
			pst.setString(2, userVo.getEmail());
			pst.setString(3, userVo.getMobileNumber());
			pst.setLong(4, userVo.getUserId());

			int result = pst.executeUpdate();

			if (result == 0) {
				throw new Exception();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void saveCustomerQuery(Connection con, String name, String email, String contact, String subject,
			String message) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(SAVE_CUSTOMER_QUERY);

			pst.setString(1, name);
			pst.setString(2, email);
			pst.setString(3, contact);
			pst.setString(4, subject);
			pst.setString(5, message);
			pst.setBoolean(6, true);
			pst.setString(7, TimeStamp.getAsiaTimeStamp());

			int result = pst.executeUpdate();

			if (result != 0) {
				System.out.println("Customer query is saved into DB.");
			} else {
				throw new Exception();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public UserVo getUserDetailById(Connection con, Long userId) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_USER_BY_ID);
			pst.setLong(1, userId);

			ResultSet rs = pst.executeQuery();

			UserVo userDetails = null;
			if (rs.next()) {
				userDetails = new UserVo();
				userDetails.setUserId(rs.getLong("USER_ID"));
				userDetails.setUserName(rs.getString("NAME"));
				userDetails.setFullName(rs.getString("NAME"));
				userDetails.setEmail(rs.getString("EMAIL"));
				userDetails.setMobileNumber(rs.getString("MOBILE_NUMBER"));
				userDetails.setWalletAmount(rs.getBigDecimal("TOTAL_WALLET_AMOUNT"));
				return userDetails;
			} else {
				return userDetails;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public CouponVo isCouponValid(Connection con, String couponNumber) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(VALIDATE_COUPON);
			pst.setString(1, couponNumber);
			pst.setBoolean(2, true);

			ResultSet rs = pst.executeQuery();
			CouponVo couponVo = null;
			if(rs.next()){
				couponVo = new CouponVo();
				couponVo.setCouponId(rs.getLong("COUPONS_ID"));
				couponVo.setCouponNumber(couponNumber);
				couponVo.setOfferPercentage(rs.getInt("OFFER_PERCENTAGE"));
			}
			return couponVo;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public ArrayList<String> getDOTDCustomerNumber(Connection con) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(SEND_MESSAGE);

			ResultSet rs = pst.executeQuery();
			ArrayList<String> customerNumber = new ArrayList<>();
			while(rs.next()){
				customerNumber.add(rs.getString(1));
			}
			return customerNumber;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public UserVo isEmailAlreadyExist(Connection con, String emailId)throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(IS_EMAIL_EXIST);
			pst.setString(1, emailId);
			
			ResultSet rs = pst.executeQuery();
			UserVo userDetails = null;
			if (rs.next()) {
				userDetails = new UserVo();
				userDetails.setUserId(rs.getLong("USER_ID"));
				userDetails.setUserName(rs.getString("NAME"));
				userDetails.setFullName(rs.getString("NAME"));
				userDetails.setEmail(rs.getString("EMAIL"));
				userDetails.setMobileNumber(rs.getString("MOBILE_NUMBER"));
				userDetails.setAccountSourceType(rs.getInt("ACCOUNT_SOURCE"));
				userDetails.setWalletAmount(rs.getBigDecimal("TOTAL_WALLET_AMOUNT"));
				return userDetails;
			} else {
				return userDetails;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public UserVo checkLoginCredentialWithEmail(String email, String password, Connection con) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(CHECK_LOGIN_WITH_EMAIL);
			pst.setString(1, email);
			pst.setString(2, password);

			ResultSet rs = pst.executeQuery();

			UserVo userDetails = null;
			if (rs.next()) {
				userDetails = new UserVo();
				userDetails.setUserId(rs.getLong("USER_ID"));
				userDetails.setUserName(rs.getString("NAME"));
				userDetails.setFullName(rs.getString("NAME"));
				userDetails.setEmail(rs.getString("EMAIL"));
				userDetails.setMobileNumber(rs.getString("MOBILE_NUMBER"));
				return userDetails;
			} else {
				return userDetails;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void createUserWallet(Connection con, Long userId, String walletAmount, String comment) throws Exception {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pst = con.prepareStatement(CREATE_USER_WALLET);
			pst.setLong(1, userId);
			pst.setString(2, walletAmount);
			pst.setString(3, comment);
			pst.setBoolean(4, true);
			pst.setString(5, TimeStamp.getAsiaTimeStamp());

			pst.executeUpdate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public void updateUserWallet(Connection con, Long userId, BigDecimal walletAmount) throws Exception {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pst = con.prepareStatement(UPDATE_USER_WALLET);
			pst.setBigDecimal(1, walletAmount);
			pst.setLong(2, userId);

			pst.executeUpdate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}
}
