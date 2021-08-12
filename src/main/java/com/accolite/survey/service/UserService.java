package com.accolite.survey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accolite.survey.dao.UserDAO;
import com.accolite.survey.entity.User;

@Service
public class UserService{
	
	private final UserDAO userDAO;
	
	@Autowired
	public UserService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void addUser(User user) {
		userDAO.insert(user);
	}
	
	public void updateUser(User user) {
		User savedUser = userDAO.findById(user.getEmployeeId())
				.orElseThrow(() -> new RuntimeException(
						String.format("Cannot find User by Id %s", user.getEmployeeId())));
		
		savedUser.setName(user.getName());
		savedUser.setPassword(user.getPassword());
		savedUser.setRole(user.getRole());
		savedUser.setDateOfJoining(user.getDateOfJoining());
		savedUser.setEmail(user.getEmail());
		
		userDAO.save(user);
		
	}
	
	public List<User> getAllUsers() {
		return userDAO.findAll();
	}
	
	public User getUserByEmployeeId(String employeeId) {
		return userDAO.findById(employeeId).orElseThrow(()-> new RuntimeException(
				String.format("Cannot find User with EmplyeeId %s", employeeId)));
	}
	
	public void deleteUser(String id) {
		userDAO.deleteById(id);
	}
	
	public void findByEmployeeIdAndPassword(String employeeId, String password) {
		userDAO.findByEmployeeIdAndPassword(employeeId, password);
	}
	
}
