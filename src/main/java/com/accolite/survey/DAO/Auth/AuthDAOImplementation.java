package com.accolite.survey.DAO.Auth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
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
	
	@Value("${GOOGLE_CLIENT_ID}")
	private String CLIENT_ID;
	
	@Autowired
	private UserDAO userDao;
	
	public ResponseEntity<AuthResponse> loginWithGoogle(String bearerToken) {
		// 1. Get the token from authorization header
		String[] authHeader = bearerToken.split(" ");
		
		if(authHeader.length < 2) {
			throw new AuthApiRequestException("Invalid Authorization Header.. Should be in the format Bearer {Token}");
		}
		
		String token = authHeader[1];
				
		// 2. Verify the access token
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
				
				Token jwtToken;
				// this user doesn't exists (using our application for the first time)
				if(users.size() == 0) {
					// 4.a) create a user in our database.
					User user = new User();
					user.setEmail(email);
					user.setName(name);
					user.setGoogleId(userId);
					user.setRole(UserRoles.EMPLOYEE);
					
					userDao.insert(user);
					
					// 4.b) return the newly user along with jwt token
					jwtToken = generateJwtToken(user.getId());
					AuthResponse response = new  AuthResponse(jwtToken, user);
					return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);
				}
				
				// 5. user already exists, return the user
				User existingUser = users.get(0);
				
				// Generate jwt token.
				jwtToken = generateJwtToken(existingUser.getId());
				
				// construct and send the response
				AuthResponse response = new  AuthResponse(jwtToken, existingUser);
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
		} catch (Exception e) {
			throw new AuthApiRequestException("Something went wrong with the server. Please try again later.");
		}
	}
	
	@Override
	public String authRouteCheck(String bearerToken) {
		isAuthenticated(bearerToken);
		return "Authenticated";
	}
	
	@Override
	public String authRouteWithRolesCheck(String bearerToken) {
		User user = isAuthenticated(bearerToken);
		
		UserRoles[] roles = {UserRoles.HR, UserRoles.PN, UserRoles.EMPLOYEE};
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
	 * 	Method to check if the currect user who is trying to access the route is logged in - ROUTE PROTECTION
	 */
	public User isAuthenticated(String bearerToken) {
		// 1. Get the token from authorization header
		String[] authHeader = bearerToken.split(" ");
		
		if(authHeader.length < 2) {
			// not a valid auth header
			throw new AuthApiRequestException("Invalid Authorization Header.. Should be in the format Bearer {Token}");
		}
		
		String token = authHeader[1];
		
		// 2. decode the token to see if its a valid token
		Jws<Claims> decodedPayload = getPayloadFromToken(token);
		if(decodedPayload == null) {
			// not a valid token
			throw new AuthApiRequestException("Invalid token.");
		}
		
		// 3. get the empId from payload
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
					.setAllowedClockSkewSeconds(300)
					.setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET_KEY))
					.build()
					.parseClaimsJws(token);
			return result;
			
		} catch(IncorrectClaimException e) {
			throw new AuthApiRequestException("Unauthorized access. You're not allowed to access this route.");
		} catch(ExpiredJwtException e) {
			throw new AuthApiRequestException("Token expired please login again");
		} catch(SignatureException e) {
			throw new AuthApiRequestException("Invalid token");
		} catch(JwtException e) {
			// general jwt error
			throw new AuthApiRequestException("Something went wrong: "+e.getMessage());
		} catch(Exception e) {
			throw new AuthApiRequestException("Something went wrong...Please try again later");
		}
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
