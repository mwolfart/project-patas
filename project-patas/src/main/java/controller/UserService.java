package controller;

import java.util.List;
import java.util.Map;

import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.UserRepository;

@RestController
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

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
				case "username":
					if (userRepository.findByUsername(datum.getValue()) != null)
						return new ResponseEntity<String>("Nome de usuário já existe.", HttpStatus.BAD_REQUEST);
					new_user.setUsername(datum.getValue());
					break;
				case "userType":
					new_user.setUserType(Integer.parseInt(datum.getValue()));
					break;
				case "password":
					byte[] hashedPassword = Password.hash((datum.getValue()).toCharArray(), Password.getNextSalt());
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
			Long userId = Long.parseLong(userDataHashMap.get("id"));
			User user = userRepository.findById(userId);
			
			// TODO: I think we don't need this for loop - we can access the data by hash
			for(Map.Entry<String, String> datum : userDataHashMap.entrySet()) {
				switch(datum.getKey()) {
				case "userType":
					user.setUserType(Integer.parseInt(datum.getValue()));
					break;
				case "password":
					byte[] hashedPassword = Password.hash((datum.getValue()).toCharArray(), Password.getNextSalt());
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

	// View 
	@RequestMapping(value = "/user/view", method = RequestMethod.POST)
	public ResponseEntity<?> userView(@RequestBody String userId) {		
		User user = userRepository.findOne(Long.parseLong(userId));
		if (user == null)
			return new ResponseEntity<String>("Usuário não encontrado.", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/*
	// Search
	@RequestMapping(value = "/user/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> userSearch(@RequestBody String search_query) {
		String[] pairs = search_query.split("\\{|,|\\}");
		Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
		
		List<Specification<User>> spec_list = UserSpecifications.buildSpecListFromCriteria(criteria_list);
		Specification<User> final_specification = UserSpecifications.buildSpecFromSpecList(spec_list);

		List<User> filtered_user_list = userRepository.findAll(final_specification);
		List<List<Object>> filtered_info_list = UserSpecifications.filterVaccinationInfo(filtered_user_list, new String[] {"id", "username", "fullName", "userType"});
		
		return new ResponseEntity<List<List<Object>>>(filtered_info_list, HttpStatus.OK);
	}
	*/
	
	// Delete
	@RequestMapping(value = "/user/delete", method = RequestMethod.POST)
	public ResponseEntity<String> userDelete(@RequestBody String userId) {
		userRepository.delete(Long.parseLong(userId));
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
