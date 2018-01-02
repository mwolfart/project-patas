package controller;

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

@RestController
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	// Register (and update)
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public ResponseEntity<?> userRegister(@RequestBody String userData) {
		System.out.println(userData);
		String[] pairs = userData.split("\\{|,|\\}");
		Map<String, String> userDataHashMap = Helper.splitCriteriaFromKeys(pairs);
		System.out.println(userDataHashMap);
		/*
		if(user.getUsername() == null)
			return new ResponseEntity<String>("Nome de usuário está em branco", HttpStatus.BAD_REQUEST);

		if(user.getPasswordHash() == null)
			return new ResponseEntity<String>("Senha está em branco", HttpStatus.BAD_REQUEST);

		if(user.getFullName() == null)
			return new ResponseEntity<String>("Nome completo está em branco", HttpStatus.BAD_REQUEST);
		
		userRepository.saveAndFlush(user);
		*/
		return new ResponseEntity<Long>(/*user.getId(),*/ HttpStatus.OK);
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
	@RequestMapping(value = "/appointment/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> appointmentSearch(@RequestBody String search_query) {
		System.out.println(search_query);
		String[] pairs = search_query.split("\\{|,|\\}");
		Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
		
		Long dog_id = getDogIdFromCriteria(criteria_list);
		if (dog_id != null)
			criteria_list.put("dogId", Long.toString(dog_id));
		
		List<Specification<Appointment>> spec_list = AppointmentSpecifications.buildSpecListFromCriteria(criteria_list);
		Specification<Appointment> final_specification = AppointmentSpecifications.buildSpecFromSpecList(spec_list);

		List<Appointment> filtered_appointment_list = appointmentRepository.findAll(final_specification);
		List<List<Object>> filtered_info_list = AppointmentSpecifications.filterVaccinationInfo(filtered_appointment_list, new String[] {"id", "dogId", "appointmentDate", "location", "vetName"});
		getDogNamesFromIds(filtered_info_list);
		
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
