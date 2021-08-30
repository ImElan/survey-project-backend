package com.accolite.survey.DAO.Auth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.accolite.survey.DAO.UserDAO;
import com.accolite.survey.Exception.AuthException.AuthApiRequestException;
import com.accolite.survey.Model.AuthResponse;
import com.accolite.survey.Model.Token;
import com.accolite.survey.Model.TokenType;
import com.accolite.survey.entity.User;
import com.accolite.survey.entity.UserRoles;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Repository
public class AuthDAOImplementation implements AuthDAO {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Value("${JWT_SECRET_KEY}")
	private String SECRET_KEY;
	
	@Value("${JWT_REFRESH_SECRET_KEY}")
	private String REFRESH_SECRET_KEY;
	
	@Value("${GOOGLE_CLIENT_ID}")
	private String CLIENT_ID;
	
	@Autowired
	private UserDAO userDao;
	
	public ResponseEntity<AuthResponse> loginWithGoogle(String googleIdToken) {
		// 1. Get the token from authorization header
		String[] authHeader = googleIdToken.split(" ");
		
		if(authHeader.length < 2) {
			throw new AuthApiRequestException("Invalid Authorization Header.. Should be in the format Bearer {Token}");
		}
		
		String token = authHeader[1];
				
		// 2. Verify the id token from google
		HttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
			    .setAudience(Collections.singletonList(CLIENT_ID))
			    .build();
		
		GoogleIdToken idToken;

		try {
			// verify the id token.
			idToken = verifier.verify(token);
			if(idToken != null) {
				Payload payload = idToken.getPayload();
							
				// 3. Get details from payload.
				String userId = payload.getSubject();
				String email = payload.getEmail();
				String name = (String) payload.get("name");

				// 4. Check if the user already exists in our database.
				Query query = new Query();
				query.addCriteria(Criteria.where("googleId").is(userId));
				List<User> users = mongoTemplate.find(query, User.class);
				
				Token accessToken;
				Token refreshToken;
				// this user doesn't exists (using our application for the first time)
				if(users.size() == 0) {
					// 4.a) create a user in our database.
					User user = new User();
					user.setEmail(email);
					user.setName(name);
					user.setGoogleId(userId);
					user.setRole(UserRoles.EMPLOYEE);
					
					userDao.insert(user);
					
					// 4.b) return the newly user along with access token and refresh token
					accessToken = generateJwtToken(user.getId());
					refreshToken = generateRefreshToken(user.getId());
					
					// setting the access token and access token expiration date.
					user.setAccessToken(accessToken.getTokenId());
					user.setAccessTokenExpirationDate(accessToken.getExpiration());
					
					userDao.save(user);
					
					AuthResponse response = new  AuthResponse(accessToken,refreshToken, user);
					return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);
				}
				
				// 5. user already exists, return the user
				User existingUser = users.get(0);
				
				/*
				 * 	There's two possibilities here.
				 * 		1. User logged out and trying to login again.
				 * 			- In this case accessToken and accessTokenExpiration would have been set to null, so we have to create
				 * 			  a new token.
				 * 		2. User not logged out and trying to login from different machine.
				 * 			- In this case we can send the already existing accessToken to the user.
				 * 
				 * 	In both the case we will need a new refresh token. Because in case 1, refresh token would've been cleared from frontend
				 * 	local storage and in case 2, he/she is using a different machine so we need to send a refresh token.
				 * 
				 */
				
				// case 1: User logged out and logging in again.
				if(existingUser.getAccessToken() == null) {
					// Generate jwt token.
					accessToken = generateJwtToken(existingUser.getId());
					
					// set the token and token expiration in database.
					existingUser.setAccessToken(accessToken.getTokenId());
					existingUser.setAccessTokenExpirationDate(accessToken.getExpiration());
					
					userDao.save(existingUser);
					
				// case 2: User not logged out and trying to login again from different machine.
				} else {
					accessToken = new Token(existingUser.getAccessToken(), existingUser.getAccessTokenExpirationDate());
				}
				
				// Generate refresh token
				refreshToken = generateRefreshToken(existingUser.getId());
				
				// construct and send the response
				AuthResponse response = new  AuthResponse(accessToken,refreshToken, existingUser);
				return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);
			} else {
				throw new AuthApiRequestException("Not a valid token, Try logging in again");
			}
		} catch (GeneralSecurityException e) {
			throw new AuthApiRequestException("Something went wrong: "+e.getMessage());
		} catch (IOException e) {
			throw new AuthApiRequestException("Something went wrong: "+e.getMessage());
		} catch(IllegalArgumentException e) {
			throw new AuthApiRequestException("Invalid token");
		}
	}
	
	/*
	 * 	Method to get new Access Token using refresh token.
	 */
	@Override
	public ResponseEntity<AuthResponse> getAccessTokenUsingRefreshToken(String refreshToken) {
		User user = isAuthenticated(refreshToken, TokenType.REFRESH);
		
		// 1. Get current time
		Instant now = Instant.now();		
		Date curTime = Date.from(now);
		
		// 2. check if a token exists and if it exists, see if it is still valid.
		if(user.getAccessToken() == null || user.getAccessTokenExpirationDate().after(curTime)) {
			/*
			 * 	It is still valid, so our application is not requesting for new access token using refresh token.
			 * 	Refresh token is compromised. (Don't send new Access token in this case).
			 */
			throw new AuthApiRequestException("Unauthorized Access");
		}
		
		// 3. if the token is not valid generate a new access token and update the database.
		Token accessToken = generateJwtToken(user.getId());
		user.setAccessToken(accessToken.getTokenId());
		user.setAccessTokenExpirationDate(accessToken.getExpiration());
		userDao.save(user);
		
		// return the new access token
		AuthResponse response = new  AuthResponse(accessToken,null, user);
		return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);
	}
	
	
	/*
	 * 	Method to logout a user.
	 */
	@Override
	public ResponseEntity<Object> logout(String bearerToken) {
		User user = isAuthenticated(bearerToken, TokenType.ACCESS);
		user.setAccessToken(null);
		user.setAccessTokenExpirationDate(null);
		
		userDao.save(user);
		
		// construct the response
		HashMap<String, String> response = new HashMap<>();
		response.put("message", "User logged out");
		
		// send the response
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@Override
	public String authRouteCheck(String bearerToken) {
		isAuthenticated(bearerToken, TokenType.ACCESS);
		return "Authenticated";
	}
	
	@Override
	public String authRouteWithRolesCheck(String bearerToken) {
		User user = isAuthenticated(bearerToken, TokenType.ACCESS);
		
		UserRoles[] roles = {UserRoles.HR, UserRoles.PM, UserRoles.EMPLOYEE};
//		UserRoles[] roles = {UserRoles.HR};
//		UserRoles[] roles = {UserRoles.HR, UserRoles.PN};
		restrictTo(roles, user);
		return "Username: "+user.getName()+", Role: "+user.getRole()+" has access to this route.";
	}
	
	
	/*
	 * 	Method to restrict particular routes to users with specific role.
	 */
	public void restrictTo(UserRoles[] roles, User user) {
		// 1. Get the current user role.
		UserRoles currentUserRole = user.getRole();
				
		// 2. Check if he/she has access to that particular route.
		boolean canAccess = doesAllowedRolesContainsCurrentUserRole(roles, currentUserRole);
		
		if(!canAccess) {
			// you're not allowed to access this route (should be thrown as error)
			throw new AuthApiRequestException("You're not allowed to access this route.");
		}
	}
	
	/*
	 *  Helper method to check if current user's role is in the allowed roles array.
	 */
	public boolean doesAllowedRolesContainsCurrentUserRole(UserRoles[] roles, UserRoles currentUserRole) {	
		// loop through the allowed roles who can access that particular route
		for(UserRoles role:roles) {
			
			// if current user role is in allowed role return true meaning he/she can access that route
			if(role.equals(currentUserRole)) {
				return true;
			}
		}
		
		// otherwise return false
		return false;
	}
	
	
	/*
	 * 	Method to generate JWT Token. (user id is attached with the jwt token)
	 */
	public Token generateJwtToken(String userId) {
		// 1. secret key to encode the token.
		byte[] JWT_SECRET_KEY = Base64.getDecoder().decode(SECRET_KEY);
		
		// 2. Generate expiration time (1 hour for now)
		Instant now = Instant.now();
		
		/* TESTING (EXPIRATION = 1 MINUTE) */
//		Date expirationTime = Date.from(now.plus(2, ChronoUnit.MINUTES));
		
		/* PRODUCTION (EXPIRATION = 1 HOUR) */
		Date expirationTime = Date.from(now.plus(1, ChronoUnit.HOURS));
		
		// 3. Build and return the token
		String token = Jwts.builder()
				.setSubject("jwtToken")
				.setAudience("surveyUsers")
				.claim("userId", userId)
				.setIssuedAt(Date.from(now))
				.setExpiration(expirationTime)
				.signWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY))
				.compact();
		
		return new Token(token,expirationTime);
	}
	
	/*
	 * 	Method to generate Refresh Token.
	 * 
	 */
	public Token generateRefreshToken(String userId) {
		// 1. secret key to encode the token.
		byte[] JWT_REFRESH_SECRET_KEY = Base64.getDecoder().decode(REFRESH_SECRET_KEY);
		
		// 2. Generate expiration time (1 hour for now)
		Instant now = Instant.now();
		
//		Date expirationTime = Date.from(now.plus(5, ChronoUnit.SECONDS));
		
		Date expirationTime = Date.from(now.plus(7, ChronoUnit.DAYS));
		
		// 3. Build and return the token
		String token = Jwts.builder()
				.setSubject("refreshToken")
				.setAudience("surveyUsersRefreshToken")
				.claim("userId", userId)
				.setIssuedAt(Date.from(now))
				.setExpiration(expirationTime)
				.signWith(Keys.hmacShaKeyFor(JWT_REFRESH_SECRET_KEY))
				.compact();
		
		return new Token(token,expirationTime);
	}
	
	
	/*
	 * 	Method to check if the currect user who is trying to access the route is logged in - ROUTE PROTECTION
	 */
	public User isAuthenticated(String bearerToken, TokenType tokenType) {
		// 1. Get the token from authorization header
		String[] authHeader = bearerToken.split(" ");
		
		if(authHeader.length < 2) {
			// not a valid auth header
			throw new AuthApiRequestException("Invalid Authorization Header.. Should be in the format Bearer {Token}");
		}
		
		String token = authHeader[1];
		
		// 2. decode the token to see if its a valid token
		Jws<Claims> decodedPayload;
		
		// based on the token type select appropriate method to get payload.
		if(tokenType == TokenType.ACCESS) {
			decodedPayload = getPayloadFromToken(token);
		} else {
			decodedPayload = getPayloadFromRefreshToken(token);
		}
		
		if(decodedPayload == null) {
			// not a valid token
			throw new AuthApiRequestException("Invalid token.");
		}
		
		// 3. get the userId from payload
		String userId = decodedPayload.getBody().get("userId", String.class);		
		
		// 4. find the user using id.
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userId));
		List<User> users = mongoTemplate.find(query, User.class);
		
		// if no users exists for that user id, user might have been removed after token creation.
		if(users.size() == 0) {
			throw new AuthApiRequestException("Your account no longer exists");
		}
				
		// if every condition is passed he is a valid authenticated user.
		return users.get(0);
	}
	
	
	/*
	 * 	Helper method to get payload from a token.
	 */
	public Jws<Claims> getPayloadFromToken(String token) {
		byte[] JWT_SECRET_KEY = Base64.getDecoder().decode(SECRET_KEY);
		
		try {
			Jws<Claims> result = Jwts.parserBuilder()
					.requireAudience("surveyUsers")
					.setAllowedClockSkewSeconds(60)
					.setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET_KEY))
					.build()
					.parseClaimsJws(token);
			return result;
			
		} catch(IncorrectClaimException e) {
			throw new AuthApiRequestException("Unauthorized access. You're not allowed to access this route.");
		} catch(ExpiredJwtException e) {
			throw new AuthApiRequestException("ACCESS_TOKEN_EXPIRED");
		} catch(SignatureException e) {
			throw new AuthApiRequestException("Invalid token");
		} catch(JwtException e) {
			// general jwt error
			throw new AuthApiRequestException("Something went wrong: "+e.getMessage());
		}
	}
	
	/*
	 * 	Helper method to get payload from a refresh token.
	 */
	public Jws<Claims> getPayloadFromRefreshToken(String token) {
		byte[] JWT_REFRESH_SECRET_KEY = Base64.getDecoder().decode(REFRESH_SECRET_KEY);
		
		try {
			Jws<Claims> result = Jwts.parserBuilder()
					.requireAudience("surveyUsersRefreshToken")
					.setAllowedClockSkewSeconds(60)
					.setSigningKey(Keys.hmacShaKeyFor(JWT_REFRESH_SECRET_KEY))
					.build()
					.parseClaimsJws(token);
			return result;
			
		} catch(IncorrectClaimException e) {
			throw new AuthApiRequestException("Unauthorized access. You're not allowed to access this route.");
		} catch(ExpiredJwtException e) {
			throw new AuthApiRequestException("Refresh Token expired please login again");
		} catch(SignatureException e) {
			throw new AuthApiRequestException("Invalid token");
		} catch(JwtException e) {
			// general jwt error
			throw new AuthApiRequestException("Something went wrong: "+e.getMessage());
		}
	}

	/*
	 * 	Method to change the role of a single user (USED IN ADMIN PANEL IN FRONTEND)
	 */
	@Override
	public ResponseEntity<Object> grantAccess(User user, String bearerToken) {
		if(user == null) {
			throw new AuthApiRequestException("Please select a user to change the role.");
		}
		
		if(user.getRole() == null) {
			throw new AuthApiRequestException("Please select a role to change the user's role to.");
		}
				
		// see if the user performing this action is logged in.
		User userPerformingThisOperation = isAuthenticated(bearerToken, TokenType.ACCESS);
		
		// see if the user performing this action is an Admin or HR.
		UserRoles[] allowedRolesToAccessThisRoute = {UserRoles.ADMIN, UserRoles.HR};
		restrictTo(allowedRolesToAccessThisRoute, userPerformingThisOperation);
		
		// get the user whose role have to be changed.
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(user.getEmail()));
		List<User> users = mongoTemplate.find(query, User.class);
		
		// check if user exists for the given user id.
		if(users == null || users.size() == 0) {
			throw new AuthApiRequestException("No user exists with the given email id");
		}
		
		User curUser = users.get(0);
		
		// update the role and save the user.
		curUser.setRole(user.getRole());
		userDao.save(curUser);
		
		
		// construct and send the response.
		HashMap<String,String> response = new HashMap<>();
		response.put("message", curUser.getName()+" is given "+curUser.getRole()+" access.");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	/*
	 *  Method to get user by email.
	 */
	@Override
	public User getUserByEmail(String email) {
		if(email == null) {
			throw new AuthApiRequestException("Please provide an email id.");
		}
		
		// get the user using given email id.
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));
		List<User> users = mongoTemplate.find(query, User.class);
		
		// check if user exists for the given user id.
		if(users == null || users.size() == 0) {
			throw new AuthApiRequestException("No user exists with the given email id");
		}
		
		User user = users.get(0);
		return user;
	}
	
	/*
	 * 	Method to change the role of list of users
	 */
	@Override
	public ResponseEntity<Object> grantAccess(String[] emails, UserRoles role, String bearerToken) {
		// if emails array is not provided or if an empty array is provided throw an error
		if(emails == null || emails.length == 0) {
			throw new AuthApiRequestException("Please provie email ids to give access to");
		}
		
		// if the role to which the users should be changed to is not given throw an error.
		if(role == null) {
			throw new AuthApiRequestException("Please provide a role to change the access to");
		}
		
		/*
		 * 	maintain two different list, one to store all the emails whose role has been changed and other one is to store failed 
		 * 	ones, meaning those users doesn't exists in our database.
		 */
		List<String> successEmails = new ArrayList<>();
		List<String> failureEmails = new ArrayList<>();
		
		// check if the user performing this operation is authenticated
		User user = isAuthenticated(bearerToken, TokenType.ACCESS);
		
		// check if he/she is an admin (only admin can perform this operation)
		UserRoles[] roles = {UserRoles.ADMIN};
		restrictTo(roles, user);
		
		// loop over the emails list
		// 		- if exists change the role and add to success list
		// 		- if it doesn't exist add to failure list
		for(String email: emails) {
			// query to find the email
			Query query = new Query();
			query.addCriteria(Criteria.where("email").is(email));
			
			// execute the query and see if the user actually exists
			List<User> users = mongoTemplate.find(query, User.class);
			boolean userExists = users.size() == 1;
			
			// user exists
			if(userExists) {
				User curUser = users.get(0);
				curUser.setRole(role);
				mongoTemplate.save(curUser);
				
				successEmails.add(email);
			
			// user doesn't exists.
			} else {
				failureEmails.add(email);
			}
		}
		
		// construct the response
		HashMap<String, List<String>> response = new HashMap<>();
		response.put("grantAccessSuccess", successEmails);
		response.put("grantAccessFailure (Emails Doesn't Exists)", failureEmails);
		
		// send the response
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	/*
	 * =======================================================================================================================
	 * 							LEGACY CODE (SWITCHED TO GOOGLE AUTHENTICATION)
	 * =======================================================================================================================
	 */
	
	/*
	 * 	Login method.
	 */
//	@Override
//	public ResponseEntity<AuthResponse> login(User user) {
//		// 1. check if email and password is actually being passed
//		if(user.getEmail() == null || user.getPassword() == null) {
//			// throw an exception.
//			System.out.println("Please provide email and password for login");
//		}
//		
//		// 2. Find if user exists 
//		Query query = new Query();
//		query.addCriteria(Criteria.where("email").is(user.getEmail()));
//		
//		List<User> users = mongoTemplate.find(query, User.class);
//		boolean userExists = users.size() == 1;
//				
//		if(!userExists) {
//			System.out.println("Incorrect email or password");
//			return null;
//		}
//		
//		User curUser = users.get(0);
//		
//		// 3. check if the password is correct
//		if(!curUser.getPassword().equals(user.getPassword())) {
//			System.out.println("Incorrect email or password.");
//			return null;
//		}
//		
//		// 4. Generate jwt and send back the response
//		String token = generateJwtToken(curUser.getId());
//			
//		// 5. construct response with jwt token and return.
//		AuthResponse response = new  AuthResponse(token, curUser);
//		return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);		
//	}
}
