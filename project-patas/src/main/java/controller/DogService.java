package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	// Given a list of dogs, return only the desired information
	// Used in search function and also in the function to get all dogs.
	//   (that's why there's a boolean parameter)
	private List<List<Object>> filterDogsInfo(List<Dog> dog_list, Boolean search_filter) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		for (Dog dog : dog_list) {
			if (search_filter) {
				String arDateAsString = df.format(dog.getArrivalDate());
				String dogSex = (dog.getSex() == "M") ? "Macho" : 
								((dog.getSex() == "F") ? "Fêmea" : "");
				List<Object> entry = new ArrayList<Object>(Arrays.asList(dog.getId(), dog.getName(), dogSex, arDateAsString));
				filtered_list.add(entry);
			} else {
				List<Object> entry = new ArrayList<Object>(Arrays.asList(dog.getId(), dog.getName()));
				filtered_list.add(entry);
			}			
		}
		
		return filtered_list;
	}
	
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
	// TODO: CHECK IF REFACTORABLE
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
		List<List<Object>> filtered_data = filterDogsInfo(dog_list, false);

		return new ResponseEntity<List<List<Object>>>(filtered_data, HttpStatus.OK);
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
		List<List<Object>> filtered_data = filterDogsInfo(filtered_dog_list, true);
		
		return new ResponseEntity<List<List<Object>>>(filtered_data, HttpStatus.OK);
	}
	
	// Delete
	@RequestMapping(value = "/dog/delete", method = RequestMethod.POST)
	public ResponseEntity<String> dogDelete(@RequestBody String dogId) {
		dogRepository.delete(Long.parseLong(dogId));
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
