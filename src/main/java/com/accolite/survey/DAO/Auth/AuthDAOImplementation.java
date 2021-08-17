package com.accolite.survey.DAO.Auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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

import com.accolite.survey.Model.AuthResponse;
import com.accolite.survey.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Repository
public class AuthDAOImplementation implements AuthDAO {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Value("${JWT_SECRET_KEY}")
	private String SECRET_KEY;
	
	/*
	 * 	Login method.
	 */
	@Override
	public ResponseEntity<AuthResponse> login(User user) {
		// 1. check if email and password is actually being passed
		if(user.getEmail() == null || user.getPassword() == null) {
			// throw an exception.
			System.out.println("Please provide email and password for login");
		}
		
		// 2. Find if user exists 
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(user.getEmail()));
		
		List<User> users = mongoTemplate.find(query, User.class);
		boolean userExists = users.size() == 1;
				
		if(!userExists) {
			System.out.println("Incorrect email or password");
			return null;
		}
		
		User curUser = users.get(0);
		
		// 3. check if the password is correct
		if(!curUser.getPassword().equals(user.getPassword())) {
			System.out.println("Incorrect email or password.");
			return null;
		}
		
		// 4. Generate jwt and send back the response
		byte[] JWT_SECRET_KEY = Base64.getDecoder().decode(SECRET_KEY);
		
		Instant now = Instant.now();
		
		String token = Jwts.builder()
				.setSubject("jwtToken")
				.setAudience("surveyUsers")
				.claim("empId", curUser.getEmployeeId())
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
				.signWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY))
				.compact();
			
		// 5. construct response with jwt token and return.
		AuthResponse response = new  AuthResponse(token, curUser);
		return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);		
	}
	
	@Override
	public String authRouteCheck(String bearerToken) {
		User user = isAuthenticated(bearerToken);
		if(user == null) {
			return "Not authenticated";
		}
		return "Authenticated";
	}
	
	
	/*
	 * 	Method to check if the currect user who is trying to access the route is logged in - ROUTE PROTECTION
	 */
	public User isAuthenticated(String bearerToken) {
		// 1. Get the token from authorization header
		String[] authHeader = bearerToken.split(" ");
		
		if(authHeader.length < 2) {
			// not a valid auth header
			System.out.println("\n\n\n not a valid auth header \n\n\n");
			return null;
		}
		
		String token = authHeader[1];
		
		// 2. decode the token to see if its a valid token
		Jws<Claims> decodedPayload = getPayloadFromToken(token);
		if(decodedPayload == null) {
			// not a valid token
			System.out.println("\n\n\n not a valid token \n\n\n");
			return null;
		}
		
		// 3. get the empId from payload
		String empId = decodedPayload.getBody().get("empId", String.class);
		
		// 4. find the user using employee id.
		Query query = new Query();
		query.addCriteria(Criteria.where("employeeId").is(empId));
		List<User> users = mongoTemplate.find(query, User.class);
		
		// if no users exists for that employee id, user might have been remove after token creation.
		if(users.size() == 0) {
			// this user no longer exists
			System.out.println("\n\n\n this user no longer exists \n\n\n");
			return null;
		}
				
		// if every condition is passed he is a valid authenticated user.
		return users.get(0);
	}
	
	public void restrictTo(String[] roles, User user) {
		// 1. Get the current user role.
		String currentUserRole = user.getRole();
		
		// 2. Check if he/she has access to that particular route.
		boolean canAccess = doesAllowedRolesContainsCurrentUserRole(roles, currentUserRole);
		
		if(!canAccess) {
			// you're not allowed to access this route (should be thrown as error)
			System.out.println("\n\n\nYou are not allowed to access this route.\n\n\n");
		}
		
	}
	
	/*
	 *  Helper method to check if current user's role is in the allowed roles array.
	 */
	public boolean doesAllowedRolesContainsCurrentUserRole(String[] roles, String currentUserRole) {	
		// loop through the allowed roles who can access that particular route
		for(String role:roles) {
			
			// if current user role is in allowed role return true meaning he/she can access that route
			if(role.equals(currentUserRole)) {
				return true;
			}
		}
		
		// otherwise return false
		return false;
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
			// invalid token
			System.out.println("\n\n\ninvalid token.\n\n\n");
		} catch(ExpiredJwtException e) {
			// token expired.
			System.out.println("\n\n\ntoken expired.\n\n\n");
		} catch(JwtException e) {
			// general jwt error
			e.printStackTrace();
			System.out.println("\n\n\ngeneral jwt error\n\n\n");
		}	
		return null;
	}
}
