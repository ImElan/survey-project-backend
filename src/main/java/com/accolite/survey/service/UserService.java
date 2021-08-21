package com.accolite.survey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.UserDAO;
import com.accolite.survey.entity.User;

@Service
public class UserService {

	private final UserDAO userDAO;

	@Autowired
	public UserService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void addUser(User user) {
		userDAO.insert(user);
	}

	public void updateUser(User user) {
		User savedUser = userDAO.findById(user.getId()).orElseThrow(
				() -> new RuntimeException(String.format("Cannot find User by Id %s", user.getId())));

		savedUser.setName(user.getName());
//		savedUser.setPassword(user.getPassword());
		savedUser.setRole(user.getRole());
		savedUser.setEmail(user.getEmail());

		userDAO.save(user);

	}

	public List<User> getAllUsers() {
		return userDAO.findAll();
	}

	public User getUserByEmployeeId(String employeeId) {
		return userDAO.findById(employeeId).orElseThrow(
				() -> new RuntimeException(String.format("Cannot find User with EmplyeeId %s", employeeId)));
	}

	public void deleteUser(String id) {
		userDAO.deleteById(id);
	}

//	public User findByEmployeeIdAndPassword(String employeeId, String password) {
//		
//		User user = userDAO.findById(employeeId).orElseThrow(
//				() -> new RuntimeException(String.format("Cannot find User with EmplyeeId %s", employeeId)));
//		System.out.println(user.getPassword()+"  : "+password);
//		if((user.getPassword()).equals(password))
//			return user;
//		else
//			throw new RuntimeException(String.format("Incorrect password"));
//	}
}
