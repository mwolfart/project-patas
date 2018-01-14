package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.UserRepository;
import repository.UserSpecifications;

@RestController
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	// Given a list of users, return only the desired information
	// Used in search function.
	private List<List<Object>> filterUsersInfo(List<User> user_list) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();
		
		for (User user : user_list) {			
			String userType = user.getUserType() == 1 ? "Administrador" : "Membro";
			
			List<Object> entry = new ArrayList<Object>(Arrays.asList(user.getId(), user.getUsername(), userType, user.getFullName()));
			filtered_list.add(entry);
		}
		
		return filtered_list;
	}
	
	// Register
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public ResponseEntity<?> userRegister(@RequestBody String userData) {
		String[] pairs = userData.split("\\{|,|\\}");
		Map<String, String> userDataHashMap = Helper.splitCriteriaFromKeys(pairs);
		User new_user = new User();
		
		try {
			// TODO: I think we don't need this for loop - we can access the data by hash
			for(Map.Entry<String, String> datum : userDataHashMap.entrySet()) {
				switch(datum.getKey()) {
				// TODO: username has to contain at least 6 characters, and a mix of numbers and letters
				case "username":
					if (userRepository.findByUsername(datum.getValue()) != null)
						return new ResponseEntity<String>("Nome de usuário já existe.", HttpStatus.BAD_REQUEST);
					new_user.setUsername(datum.getValue());
					break;
				case "userType":
					new_user.setUserType(Integer.parseInt(datum.getValue()));
					break;
				case "password":
					byte[] newSalt = Password.getNextSalt();
					new_user.setSalt(newSalt);
					byte[] hashedPassword = Password.hash((datum.getValue()).toCharArray(), newSalt);
					new_user.setPasswordHash(hashedPassword);
					break;
				case "fullName":
					new_user.setFullName(datum.getValue());
					break;
				}
			}
			
			if(new_user.getUsername() == null)
				return new ResponseEntity<String>("Nome de usuário está em branco.", HttpStatus.BAD_REQUEST);
			else if(new_user.getPasswordHash() == null)
				return new ResponseEntity<String>("Senha está em branco.", HttpStatus.BAD_REQUEST);
			else if(new_user.getUserType() == null)
				return new ResponseEntity<String>("Tipo de usuário está em branco.", HttpStatus.BAD_REQUEST);
			else {
				userRepository.saveAndFlush(new_user);
				return new ResponseEntity<Long>(new_user.getId(), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Dados mal formatados.", HttpStatus.BAD_REQUEST);
		}
	}
	
	// Update
	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public ResponseEntity<?> userUpdate(@RequestBody String userData) {
		String[] pairs = userData.split("\\{|,|\\}");
		Map<String, String> userDataHashMap = Helper.splitCriteriaFromKeys(pairs);
		
		try {
			String username = userDataHashMap.get("username");
			User user = userRepository.findByUsername(username);
			
			// TODO: I think we don't need this for loop - we can access the data by hash
			for(Map.Entry<String, String> datum : userDataHashMap.entrySet()) {
				switch(datum.getKey()) {
				case "userType":
					user.setUserType(Integer.parseInt(datum.getValue()));
					break;
				case "password":
					byte[] newSalt = Password.getNextSalt();
					user.setSalt(newSalt);
					byte[] hashedPassword = Password.hash((datum.getValue()).toCharArray(), newSalt);
					user.setPasswordHash(hashedPassword);
					break;
				case "fullName":
					user.setFullName(datum.getValue());
					break;
				}
			}
			
			if(user.getPasswordHash() == null)
				return new ResponseEntity<String>("Senha está em branco.", HttpStatus.BAD_REQUEST);
			else if(user.getUserType() == null)
				return new ResponseEntity<String>("Tipo de usuário está em branco.", HttpStatus.BAD_REQUEST);
			else {
				userRepository.saveAndFlush(user);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Dados mal formatados.", HttpStatus.BAD_REQUEST);
		}
	}

	// Password change
	@RequestMapping(value = "/user/set_password", method = RequestMethod.POST)
	public ResponseEntity<String> userPasswordChange(@RequestBody String userData) {
		String[] pairs = userData.split("\\{|,|\\}");
		Map<String, String> userDataHashMap = Helper.splitCriteriaFromKeys(pairs);
		
		try {
			String username = userDataHashMap.get("username");
			User user = userRepository.findByUsername(username);
			char[] password = userDataHashMap.get("password").toCharArray();
			
			if (Password.isExpectedPassword(password, user.getSalt(), user.getPasswordHash())) {
				byte[] new_salt = Password.getNextSalt();
				char[] new_password = userDataHashMap.get("new_password").toCharArray();
				byte[] new_password_hash = Password.hash(new_password, new_salt);
				user.setPasswordHash(new_password_hash);
				user.setSalt(new_salt);
				userRepository.saveAndFlush(user);
				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Senha incorreta.", HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<String>("Dados mal formatados.", HttpStatus.BAD_REQUEST);
		}
	}
	
	// Password check
	@RequestMapping(value = "/user/check_password", method = RequestMethod.POST)
	public ResponseEntity<?> userPasswordCheck(@RequestBody String userData) {
		String[] pairs = userData.split("\\{|,|\\}");
		Map<String, String> userDataHashMap = Helper.splitCriteriaFromKeys(pairs);
		
		try {
			String username = userDataHashMap.get("username");
			String password = userDataHashMap.get("password");
			
			if (username == null)
				return new ResponseEntity<String>("Usuário está em branco", HttpStatus.BAD_REQUEST);
			
			if (password == null)
				return new ResponseEntity<String>("Senha está em branco", HttpStatus.BAD_REQUEST);
				
			User user = userRepository.findByUsername(username);
			
			if (user == null)
				return new ResponseEntity<String>("Usuário inexistente", HttpStatus.BAD_REQUEST);
				
			char[] password_as_char = (password).toCharArray();
			
			if (Password.isExpectedPassword(password_as_char, user.getSalt(), user.getPasswordHash())) {
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			} else {
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// View 
	@RequestMapping(value = "/user/view", method = RequestMethod.POST)
	public ResponseEntity<?> userView(@RequestBody String usernameOrId) {
		User user = null;
		if (Helper.isNumeric(usernameOrId))
			user = userRepository.findOne(Long.parseLong(usernameOrId));
		else user = userRepository.findByUsername(usernameOrId);
		
		if (user == null)
			return new ResponseEntity<String>("Usuário não encontrado.", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	// Search
	@RequestMapping(value = "/user/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> userSearch(@RequestBody String search_query) {
		String[] pairs = search_query.split("\\{|,|\\}");
		Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
		
		List<Specification<User>> spec_list = UserSpecifications.buildSpecListFromCriteria(criteria_list);
		Specification<User> final_specification = Helper.buildSpecFromSpecList(spec_list);

		List<User> filtered_user_list = userRepository.findAll(final_specification);
		List<List<Object>> filtered_data = filterUsersInfo(filtered_user_list); 
				
		return new ResponseEntity<List<List<Object>>>(filtered_data, HttpStatus.OK);
	}
	
	// Delete
	@RequestMapping(value = "/user/delete", method = RequestMethod.POST)
	public ResponseEntity<String> userDelete(@RequestBody String userId) {
		userRepository.delete(Long.parseLong(userId));
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
