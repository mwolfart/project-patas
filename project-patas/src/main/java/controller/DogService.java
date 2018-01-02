package controller;

import java.util.List;
import java.util.Map;
import model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.DogRepository;
import repository.DogSpecifications;


@RestController
public class DogService { 

	@Autowired 
	private DogRepository dogRepository;
	
	// Register dog
	@RequestMapping(value = "/dog/register", method = RequestMethod.POST)
	public ResponseEntity<?> dogRegister(@RequestBody Dog dog) {
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
		return new ResponseEntity<Long>(dog.getId(), HttpStatus.OK);
	}

	// Update dog
	// This is different from register, because we may accept a dog with the same name if unchanged
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

	// View dog
	@RequestMapping(value = "/dog/view", method = RequestMethod.POST)
	public ResponseEntity<?> dogView(@RequestBody String dogId) {		
		Dog dog = dogRepository.findOne(Long.parseLong(dogId));

		if (dog == null)
			return new ResponseEntity<String>("Cachorro não encontrado.", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<Dog>(dog, HttpStatus.OK);
	}
	
	// Get all dogs
	@RequestMapping(value = "/dog/get", method = RequestMethod.GET)
	public ResponseEntity<List<List<Object>>> dogGetAll() {
		List<Dog> dog_list = dogRepository.findAll();
		List<List<Object>> filtered_info_list = DogSpecifications.filterDogInfo(dog_list, new String[] {"id", "name"});

		return new ResponseEntity<List<List<Object>>>(filtered_info_list, HttpStatus.OK);
	}

	// Search dog
	@RequestMapping(value = "/dog/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> dogSearch(@RequestBody String search_query) {
		// We are going to split the JSON so we get each criteria separately
		//  in the map. First we split the criteria, one from each other
		String[] pairs = search_query.split("\\{|,|\\}");

		// Then, we split each criterion from its key, and build a list from them.
		Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
		List<Specification<Dog>> spec_list = DogSpecifications.buildSpecListFromCriteria(criteria_list);
		Specification<Dog> final_specification = DogSpecifications.buildSpecFromSpecList(spec_list);
		
		List<Dog> filtered_dog_list = dogRepository.findAll(final_specification);
		List<List<Object>> filtered_info_list = DogSpecifications.filterDogInfo(filtered_dog_list, new String[] {"id", "name", "sex", "arrivalDate"});

		return new ResponseEntity<List<List<Object>>>(filtered_info_list, HttpStatus.OK);
	}

}