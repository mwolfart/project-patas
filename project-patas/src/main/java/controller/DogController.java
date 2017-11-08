package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.DogRepository;
import repository.DogSpecifications;


@RestController
public class DogController { 
	
	@Autowired 
	private DogRepository dogRepository;
	
	/* HELPER METHODS */
	
	// splitCriteriaFromKeys
	// given a list of criteria (strings) in the format "crit":"val",
	//   split them in a hashmap.
	private HashMap<String, String> splitCriteriaFromKeys(String[] criteria_pairs) {
		HashMap<String, String> criteria_list = new HashMap<String, String>();
		
		for (int i=1; i < criteria_pairs.length; i++) {
		    String pair = criteria_pairs[i];
		    String[] splitted_pair = pair.split("\"|:|\"");
		    Boolean value_is_not_string = (splitted_pair.length == 4);
		    
		    if (value_is_not_string)
		    	criteria_list.put(splitted_pair[1], splitted_pair[3]);
		    else criteria_list.put(splitted_pair[1], splitted_pair[4]);
		}
		
		return criteria_list;
	}
	
	// buildSpecList
	// given a list of criteria in the format of a hashmap, build a list
	//   of specifications used for the querying.
	private List<Specification<Dog>> buildSpecListFromCriteria(Map<String, String> criteria_list) {
		List<Specification<Dog>> spec_list = new ArrayList<Specification<Dog>>();
		
		for(Map.Entry<String, String> criterion : criteria_list.entrySet())
			spec_list.add(buildSpecFromCriterion(criterion));
		
		return spec_list;
	}

	// buildSpecFromCriterion
	// given a griterion entry from a hashmap, convert it to a specification
	// OBS.: the conditional values used in this function are directly related to the 
	//   fields "name" attribute in the html. Whenever one is changed, the other also has to.
	private Specification<Dog> buildSpecFromCriterion(Entry<String, String> criterion) {
		switch(criterion.getKey()) {
		case "name":
			return DogSpecifications.dogNameEquals(criterion.getValue());
		case "birthMonth":
			return DogSpecifications.dogBirthMonthEquals(Integer.parseInt(criterion.getValue()));
		case "birthYear":
			return DogSpecifications.dogBirthYearEquals(Integer.parseInt(criterion.getValue()));
		case "sex":
			return DogSpecifications.dogSexEquals(criterion.getValue());
		case "arrivalDay":
			return DogSpecifications.dogArrivalDayEquals(Integer.parseInt(criterion.getValue()));
		case "arrivalMonth":
			return DogSpecifications.dogArrivalMonthEquals(Integer.parseInt(criterion.getValue()));
		case "arrivalYear":
			return DogSpecifications.dogArrivalYearEquals(Integer.parseInt(criterion.getValue()));
		case "castrated":
			return DogSpecifications.dogCastratedEquals(Boolean.parseBoolean(criterion.getValue()));
		case "vacinated":
			/* TODO */
		case "vermifuged":
			/* TODO */
		default:
			return null;
		}
	}

	// buildSpecFromSpecList
	// given a list of specification, return one specification containing all the given specs
	private Specification<Dog> buildSpecFromSpecList(List<Specification<Dog>> spec_list) {
		if (spec_list.size() < 1)
			return null;
		
		Specification<Dog> result_spec = spec_list.get(0);
		for (int i=1; i < spec_list.size(); i++)
			result_spec = Specifications.where(result_spec).and(spec_list.get(i));
		
		return result_spec;
	}
	
	// filterDogInfo
	// given a list of dog classes, filter the information we want
	//   and return it in a list.
	private List<List<Object>> filterDogInfo(List<Dog> dog_list) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();
		
		for(Dog dog : dog_list) {
			List<Object> desired_info = new ArrayList<Object>();
			desired_info.add(dog.getId());
			desired_info.add(dog.getName());
			desired_info.add(dog.getArrivalDate());
			desired_info.add(dog.getSex());
			filtered_list.add(desired_info);
		}
		
		return filtered_list;
	}
	
	/* SERVICE METHODS */
	
	// Register dog
	@RequestMapping(value = "/dog/register", method = RequestMethod.POST)
    public ResponseEntity<String> dogRegister(@RequestBody Dog dog) {
		
		if(dog.getName() == null)
			return new ResponseEntity<String>("Nome está em branco", HttpStatus.BAD_REQUEST);
		
		Dog dogWithSameName = dogRepository.findByName(dog.getName());
		if(dogWithSameName != null)
			return new ResponseEntity<String>("Já existe um cachorro com este nome", HttpStatus.BAD_REQUEST);
		
		if(dog.getArrivalDate() == null)
			return new ResponseEntity<String>("Data de chegada está em branco", HttpStatus.BAD_REQUEST);
		
		if(dog.getCastrated() == null)
			return new ResponseEntity<String>("Flag de castrado está em branco", HttpStatus.BAD_REQUEST);
		
		dogRepository.saveAndFlush(dog);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	// Search dog
	@RequestMapping(value = "/dog/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<Dog>> dogSearch(@RequestBody String search_query) {		
		// We are going to split the JSON so we get each criteria separately
		//  in the map. First we split the criteria, one from each other
		String[] pairs = search_query.split("\\{|,|\\}");

		// Then, we split each criterion from its key, and build a list from them.
		Map<String, String> criteria_list = splitCriteriaFromKeys(pairs);
		List<Specification<Dog>> spec_list = buildSpecListFromCriteria(criteria_list);
		Specification<Dog> final_specification = buildSpecFromSpecList(spec_list);
		
		List<Dog> filteredList = dogRepository.findAll(final_specification);
				
		return new ResponseEntity<List<Dog>>(filteredList, HttpStatus.OK);
	}

	// View dog
	@RequestMapping(value = "/dog/view", method = RequestMethod.POST)
	public ResponseEntity<?> dogView(@RequestBody String dogId) {		
		Dog dog = dogRepository.findById(Long.parseLong(dogId));
		
		if (dog == null)
			return new ResponseEntity<String>("Cachorro não encontrado.", HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<Dog>(dog, HttpStatus.OK);
	}
	
	// Update dog
	@RequestMapping(value = "/dog/update", method = RequestMethod.POST)
    public ResponseEntity<String> dogUpdate(@RequestBody Dog dog) {
		if(dog.getName() == null)
			return new ResponseEntity<String>("Nome está em branco", HttpStatus.BAD_REQUEST);
		
		Dog dogWithSameName = dogRepository.findByName(dog.getName());
		if(dogWithSameName != null && dogWithSameName.getId() != dog.getId())
			return new ResponseEntity<String>("Já existe um cachorro com este nome", HttpStatus.BAD_REQUEST);
		
		if(dog.getArrivalDate() == null)
			return new ResponseEntity<String>("Data de chegada está em branco", HttpStatus.BAD_REQUEST);
		
		if(dog.getCastrated() == null)
			return new ResponseEntity<String>("Flag de castrado está em branco", HttpStatus.BAD_REQUEST);
		
		dogRepository.saveAndFlush(dog);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	// Get all dogs
	@RequestMapping(value = "/dog/get", method = RequestMethod.GET)
	public ResponseEntity<List<List<Object>>> dogGetAll() {
		List<Dog> dog_list = dogRepository.findAll();
		List<List<Object>> filtered_info_list = filterDogInfo(dog_list);
		
		return new ResponseEntity<List<List<Object>>>(filtered_info_list, HttpStatus.OK);
	}
}
