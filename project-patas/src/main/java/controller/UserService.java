package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.Session;
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

	private List<Session> sessions = new ArrayList<Session>();
	
	// Given a list of users, return only the desired information
	// Used in search function.
	private List<List<Object>> filterUsersInfo(List<User> user_list) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();
		
		for (User user : user_list) {			
			String userType = user.getUserType() == 1 ? "Administrador" : "Membro";
			String fullName = user.getFullName() == null? "" : user.getFullName();
			
			List<Object> entry = new ArrayList<Object>(Arrays.asList(user.getId(), user.getUsername(), fullName, userType));
			filtered_list.add(entry);
		}
		
		return filtered_list;
	}
	
	// Check if an username is valid
	private Boolean isUsernameValid(String username) {
		if (username.length() < 6 || username.length() > 20)
			return false;
		else return true;
	}
	
	// Check if a password is valid
	private Boolean isPasswordValid(String password) {
		if (password.length() < 6 || password.length() > 20
				|| Helper.countCharInString("digits", password) == 0 
				|| Helper.countCharInString("letters", password) == 0)
			return false;
		else return true;
	}
	
	// Validate user and save. Acts slightly different if updating or creating.
	private ResponseEntity<String> validateAndSaveUser(String userDataAsString, Boolean isUserNew) {
		String[] pairs = userDataAsString.split("\\{|,|\\}");
		Map<String, String> userData = Helper.splitCriteriaFromKeys(pairs);
		
		try {
			String username = userData.get("username");
			String password = userData.get("password");
			String userType = userData.get("userType");
			
			if (username == null)
				return new ResponseEntity<String>("Nome de usu??rio est?? em branco.", HttpStatus.BAD_REQUEST);
			else if (isUserNew && userRepository.findByUsername(username) != null)
				return new ResponseEntity<String>("Nome de usu??rio j?? existe.", HttpStatus.BAD_REQUEST);
			else if (isUserNew && !isUsernameValid(username))
				return new ResponseEntity<String>(
						"Nome de usu??rio deve conter de 6 a 20 car??cteres.", HttpStatus.BAD_REQUEST);
			else if (isUserNew && password == null)
				return new ResponseEntity<String>("Senha est?? em branco.", HttpStatus.BAD_REQUEST);
			else if (password != null && !isPasswordValid(password))
				return new ResponseEntity<String>(
						"Senha deve conter de 6 a 20 car??cteres, bem como uma mistura de n??meros e letras.", HttpStatus.BAD_REQUEST);
			else if (isUserNew && userType == null)
				return new ResponseEntity<String>("Tipo de usu??rio est?? em branco.", HttpStatus.BAD_REQUEST);
			
			User user = isUserNew ? new User() : userRepository.findByUsername(username);
			
			if (!isUserNew && user == null)
				return new ResponseEntity<String>("Usu??rio n??o encontrado.", HttpStatus.BAD_REQUEST);
			
			// Set basic info
			if (isUserNew) 
				user.setUsername(username);
			if (userType != null)
				user.setUserType(Integer.parseInt(userType));
			user.setFullName(userData.get("fullName"));
			
			// Set password hash
			if (password != null) {
				byte[] newSalt = Password.getNextSalt();
				user.setSalt(newSalt);
				byte[] hashedPassword = Password.hash(password.toCharArray(), newSalt);
				user.setPasswordHash(hashedPassword);
			}

			userRepository.saveAndFlush(user);
			return new ResponseEntity<String>(user.getUsername(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Dados mal formatados.", HttpStatus.BAD_REQUEST);
		}
	}
	
	// Register
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public ResponseEntity<String> userRegister(@RequestBody String userDataAsString) {
		return validateAndSaveUser(userDataAsString, true);
	}
	
	// Update
	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public ResponseEntity<String> userUpdate(@RequestBody String userDataAsString) {
		return validateAndSaveUser(userDataAsString, false);
	}

	// Password change
	@RequestMapping(value = "/user/set_password", method = RequestMethod.POST)
	public ResponseEntity<String> userPasswordChange(@RequestBody String userDataAsString) {
		String[] pairs = userDataAsString.split("\\{|,|\\}");
		Map<String, String> userData = Helper.splitCriteriaFromKeys(pairs);
		
		try {
			String username = userData.get("username");
			
			if (username == null)
				return new ResponseEntity<String>("Usu??rio n??o especificado.", HttpStatus.BAD_REQUEST);
			else if (userData.get("password") == null)
				return new ResponseEntity<String>("Senha n??o especificada.", HttpStatus.BAD_REQUEST);
			else if (userData.get("new_password") == null)
				return new ResponseEntity<String>("Nova senha n??o especificada.", HttpStatus.BAD_REQUEST);
			else if (!isPasswordValid(userData.get("new_password")))
				return new ResponseEntity<String>(
						"Senha deve conter de 6 a 20 car??cteres, bem como uma mistura de n??meros e letras.", HttpStatus.BAD_REQUEST);
			
			User user = userRepository.findByUsername(username);
			if (user == null)
				return new ResponseEntity<String>("Usu??rio n??o encontrado.", HttpStatus.BAD_REQUEST);
			
			char[] password = userData.get("password").toCharArray();
			
			if (Password.isExpectedPassword(password, user.getSalt(), user.getPasswordHash())) {
				byte[] new_salt = Password.getNextSalt();
				char[] new_password = userData.get("new_password").toCharArray();
				byte[] new_password_hash = Password.hash(new_password, new_salt);
				user.setPasswordHash(new_password_hash);
				user.setSalt(new_salt);
				userRepository.saveAndFlush(user);
				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Senha incorreta.", HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Dados mal formatados.", HttpStatus.BAD_REQUEST);
		}
	}
	
	// Password check
	@RequestMapping(value = "/user/check_password", method = RequestMethod.POST)
	public ResponseEntity<?> userPasswordCheck(@RequestBody String userDataAsString) {
		String[] pairs = userDataAsString.split("\\{|,|\\}");
		Map<String, String> userData = Helper.splitCriteriaFromKeys(pairs);
		
		try {
			String username = userData.get("username");
			String password = userData.get("password");
			
			if (username == null)
				return new ResponseEntity<String>("Usu??rio n??o especificado.", HttpStatus.BAD_REQUEST);
			else if (password == null)
				return new ResponseEntity<String>("Senha n??o especificada.", HttpStatus.BAD_REQUEST);
				
			User user = userRepository.findByUsername(username);
			if (user == null)
				return new ResponseEntity<String>("Usu??rio n??o encontrado.", HttpStatus.BAD_REQUEST);
				
			char[] password_as_char = password.toCharArray();
			
			if (Password.isExpectedPassword(password_as_char, user.getSalt(), user.getPasswordHash())) {
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			} else {
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// Administrator check
	@RequestMapping(value = "/user/check_admin", method = RequestMethod.POST)
	public ResponseEntity<Boolean> userIsAdmin(@RequestBody String username) {
		try {
			User user = userRepository.findByUsername(username);
			
			if (user == null)
				return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
			else if (user.getUserType() == 1)
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			else return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
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
			return new ResponseEntity<String>("Usu??rio n??o encontrado.", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	// Search
	@RequestMapping(value = "/user/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> userSearch(@RequestBody String search_query) {
		try {
			String[] pairs = search_query.split("\\{|,|\\}");
			Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
			
			List<Specification<User>> spec_list = UserSpecifications.buildSpecListFromCriteria(criteria_list);
			Specification<User> final_specification = Helper.buildSpecFromSpecList(spec_list);
	
			List<User> filtered_user_list = userRepository.findAll(final_specification);
			List<List<Object>> filtered_data = filterUsersInfo(filtered_user_list); 
					
			return new ResponseEntity<List<List<Object>>>(filtered_data, HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<List<Object>>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// Delete
	@RequestMapping(value = "/user/delete", method = RequestMethod.POST)
	public ResponseEntity<String> userDelete(@RequestBody String username) {
		try {
			// TODO: MAKE USERNAME PRIMARY KEY???? CONSULT WITH CLIENTS
			userRepository.delete(userRepository.findByUsername(username));
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch(NumberFormatException e) {
			return new ResponseEntity<String>("Nome de usu??rio inv??lido.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "user/set_session", method = RequestMethod.POST)
	public ResponseEntity<String> setSession(@RequestBody String sessionDataAsString) {
		try {
			String[] pairs = sessionDataAsString.split("\\{|,|\\}");
			Map<String, String> sessionData = Helper.splitCriteriaFromKeys(pairs);
			
			String key = sessionData.get("key");
			String value = sessionData.get("value");
			Long duration = Long.parseLong(sessionData.get("duration"));
			Long createdTime = System.currentTimeMillis();
			
			Session session = new Session(key, value, duration, createdTime);
			sessions.add(session);
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "user/get_session", method = RequestMethod.POST)
	public ResponseEntity<String> getSession(@RequestBody String key) {
		Integer num_of_sessions = sessions.size();
		
		// TODO: SEMAPHORES?
		
		for (int i = 0; i < num_of_sessions; i++) {
			Session s = sessions.get(i);
			
			if (s.getKey().equals(key)) {
				Long currentTime = System.currentTimeMillis();
				if (s.getCreatedTime() + s.getDuration() > currentTime)
					return new ResponseEntity<String>(s.getValue(), HttpStatus.OK);
				else {
					sessions.remove(i);
					num_of_sessions--;
					i--;
				}
			}
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "user/clear_session", method = RequestMethod.POST)
	public ResponseEntity<String> clearSession(@RequestBody String key) {
		for (Session s : sessions) {
			if (s.getKey().equals(key)) {
				sessions.remove(s);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
		
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}
}
