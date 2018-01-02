package controller;

import java.util.List;
import java.util.Map;

import model.Vermifuge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.DogRepository;
import repository.VermifugeRepository;
import repository.VermifugeSpecifications;

@RestController
public class VermifugeService {
	
	@Autowired
	private VermifugeRepository vermifugeRepository;
	@Autowired
	private DogRepository dogRepository;
	
	// Given the criteria for a vermifuge, which contains the name of the dog,
	//  find the id of the given dog.
	private Long getDogIdFromCriteria(Map<String, String> criteria) {
		for(Map.Entry<String, String> criterion : criteria.entrySet()) {
			if (criterion.getKey().equals("dogName")) {
				return dogRepository.findByName(criterion.getValue()).getId();
			}
		}
		
		return null;
	}
	
	// Given a list containing the vermifuge data, which has the dog ids,
	//  retrieve the dog names from each id.
	private void getDogNamesFromIds(List<List<Object>> data_list) {
		for(int i = 0; i < data_list.size(); i++) {
			Long dog_id = Helper.objectToLong(data_list.get(i).get(1));
			String dog_name = dogRepository.findById(dog_id).getName();
			data_list.get(i).set(1, dog_name);
		}
	}
	
	// Register (and update)
	@RequestMapping(value = "/vermifuge/register", method = RequestMethod.POST)
	public ResponseEntity<?> vermifugeRegister(@RequestBody Vermifuge vermifuge) {
		if(vermifuge.getVermifugeName() == null)
			return new ResponseEntity<String>("Nome está em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getDogId() == null)
			return new ResponseEntity<String>("Id do cachorro está em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getDosage() == null)
			return new ResponseEntity<String>("Dosagem está em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getApplicationDate() == null)
			return new ResponseEntity<String>("Data da aplicação está em branco", HttpStatus.BAD_REQUEST);

		vermifugeRepository.saveAndFlush(vermifuge);
		return new ResponseEntity<Long>(vermifuge.getId(), HttpStatus.OK);
	}

	// View 
	@RequestMapping(value = "/vermifuge/view", method = RequestMethod.POST)
	public ResponseEntity<?> vermifugeView(@RequestBody String vermifugeId) {		
		Vermifuge vermifuge = vermifugeRepository.findOne(Long.parseLong(vermifugeId));
		if (vermifuge == null)
			return new ResponseEntity<String>("Vermifugo não encontrado.", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<Vermifuge>(vermifuge, HttpStatus.OK);
	}

	// Search
	@RequestMapping(value = "/vermifuge/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> vermifugeSearch(@RequestBody String search_query) {
		String[] pairs = search_query.split("\\{|,|\\}");
		Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
		
		Long dog_id = getDogIdFromCriteria(criteria_list);
		if (dog_id != null)
			criteria_list.put("dogId", Long.toString(dog_id));
		
		System.out.println(criteria_list);
		
		List<Specification<Vermifuge>> spec_list = VermifugeSpecifications.buildSpecListFromCriteria(criteria_list);
		Specification<Vermifuge> final_specification = VermifugeSpecifications.buildSpecFromSpecList(spec_list);

		List<Vermifuge> filtered_vermifuge_list = vermifugeRepository.findAll(final_specification);
		List<List<Object>> filtered_info_list = VermifugeSpecifications.filterVermifugeInfo(filtered_vermifuge_list, new String[] {"id", "dogId", "name", "applicationDate"});
		getDogNamesFromIds(filtered_info_list);
		
		return new ResponseEntity<List<List<Object>>>(filtered_info_list, HttpStatus.OK);
	}
	
	// Delete
	@RequestMapping(value = "/vermifuge/delete", method = RequestMethod.POST)
	public ResponseEntity<String> vermifugeDelete(@RequestBody String vermifugeId) {
		vermifugeRepository.delete(Long.parseLong(vermifugeId));
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
