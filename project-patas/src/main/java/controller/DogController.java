package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import repository.DogRepository;


@RestController
public class DogController { 
	
	@Autowired 
	private DogRepository dogRepository;
	
	// DogMatchesCriteria: Dog, Map<String, String> returns Boolean
	// Given a Dog class and a map of criteria the class has to obey
	// (name must be "Lilica", sex must be "F", etc), check if
	// the class obeys these criteria.
	/* THIS FUNCTION MIGHT/SHOULD BE REFACTORED */
	private Boolean dogMatchesCriteria(Dog dog, Map<String, String> criteria) {
		// Process each available criterion
		for (Map.Entry<String, String> crit : criteria.entrySet()) {
			
			// Stores key (field) and value (what it has to obey)
			String key = crit.getKey();
			String value = crit.getValue();
			
			// Calendar variable so we can work with dates
			Calendar cal = Calendar.getInstance();
			
			// Process each given criterion
			switch(key) {
			case "arrivalDay":
				// If the date is null, return false
				if (dog.getArrivalDate() == null)
					return false;
				
				cal.setTime(dog.getArrivalDate());
				
				if (cal.get(Calendar.DAY_OF_MONTH) != Integer.parseInt(value))
					return false;
				break;
			case "arrivalMonth":
				if (dog.getArrivalDate() == null)
					return false;
				
				cal.setTime(dog.getArrivalDate());
				
				if (cal.get(Calendar.MONTH) + 1 != Integer.parseInt(value))
					return false;
				break;
			case "arrivalYear":
				if (dog.getArrivalDate() == null)
					return false;
				
				cal.setTime(dog.getArrivalDate());
				
				if (cal.get(Calendar.YEAR) != Integer.parseInt(value))
					return false;
				break;
			case "birthDay":
				if (dog.getBirthDate() == null)
					return false;
				
				cal.setTime(dog.getBirthDate());
				
				if (cal.get(Calendar.DAY_OF_MONTH) != Integer.parseInt(value))
					return false;
				break;
			case "birthMonth":
				if (dog.getBirthDate() == null)
					return false;
				
				cal.setTime(dog.getBirthDate());
				
				if (cal.get(Calendar.MONTH) + 1 != Integer.parseInt(value))
					return false;
				break;
			case "birthYear":
				if (dog.getBirthDate() == null)
					return false;
				
				cal.setTime(dog.getBirthDate());
				
				if (cal.get(Calendar.YEAR) != Integer.parseInt(value))
					return false;
				break;
			case "sex":
				if (!dog.getSex().equals(value))
					return false;
				break;
			case "name":
				if (!dog.getName().equals(value))
					return false;
				break;
			case "castrated":
				if (dog.getCastrated() != Boolean.valueOf(value))
					return false;
				break;
			case "vermifuged":
				// TODO (needs vermifugation table)
			case "vacinated":
				// TODO (needs vacination table)
				break;
			}
		}	
		
		return true;
	}
	
	// filterDogs: List<Dog>, Map<String, String> returns List<Dog>
	// given a List object of Dog classes, checks which of these classes
	// obey the given criteria, and return them.
	private List<Dog> filterDogs(List<Dog> dogList, Map<String, String> criteria) {
		List<Dog> filteredList = new ArrayList<Dog>();
		
		for (Dog dog : dogList) {
			if (dogMatchesCriteria(dog, criteria))
				filteredList.add(dog);
		}
		
		return filteredList;
	}
	
	// getDogRequiredInfo: List<Dog> returns List<List<String>>
	// used so we can filter only the fields we want to, and then return them
	private List<List<String>> getDogRequiredInfo(List<Dog> dogList) {
		List<List<String>> filteredDogList = new ArrayList<List<String>>();
		/*
		for (Dog dog : dogList) {
			List<String> filteredInfo = new ArrayList<String>();
			
			filteredInfo.add(Long.toString(dog.getId()));
			filteredInfo.add(dog.getName());
			filteredInfo.add(dog.getSex());
		}
		*/
		return filteredDogList;
	}
	
	/* SERVICE METHODS */
	
	// Register dog
	@RequestMapping(value = "/dog/register", method = RequestMethod.POST)
    public ResponseEntity<String> dogRegister(@RequestBody Dog dog) {
		
		if(dog.getName() == null){
			return new ResponseEntity<String>("Nome está em branco",HttpStatus.BAD_REQUEST);
		}
		
		Dog dogWithSameName = dogRepository.findByName(dog.getName());
		if(dogWithSameName != null){
			return new ResponseEntity<String>("Já existe um cachorro com este nome",HttpStatus.BAD_REQUEST);
		}
		
		if(dog.getArrivalDate() == null){
			return new ResponseEntity<String>("Data de chegada está em branco",HttpStatus.BAD_REQUEST);
		}
		
		if(dog.getCastrated() == null){
			return new ResponseEntity<String>("Flag de castrado está em branco",HttpStatus.BAD_REQUEST);
		}
		
		dogRepository.saveAndFlush(dog);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	// Search dog
	@RequestMapping(value = "/dog/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<String> dogSearch(@RequestBody String searchQuery) {
		
		// Save the given criteria in a Map object
		Map<String, String> criteria = new HashMap<String, String>();
		
		// We are going to split the JSON so we get each criteria separately
		//  in the map. First we split the criteria, one from each other
		String[] pairs = searchQuery.split("\\{|,|\\}");

		// And then we save each criterion in the map,
		//  splitting value from key
		for (int i=1; i < pairs.length; i++) {
		    String pair = pairs[i];
		    String[] keyValue = pair.split("\"|:|\"");
		    
		    if (keyValue.length == 4)
		    	criteria.put(keyValue[1], keyValue[3]);
		    else criteria.put(keyValue[1], keyValue[4]);
		}
		
		// Find all the dogs in the table and filter them.
		/* TODO: MAYBE WE CAN INJECT A SQL STATEMENT HERE SO */
		/* WE DON'T HAVE TO FILTER THE CLASSES INSIDE THE    */
		/* BACKEND                                           */
		List<Dog> dogList = dogRepository.findAll();
		List<Dog> filteredList = filterDogs(dogList, criteria);	
		
		// Maybe we don't need this at all
		//List<List<String>> filteredDogInfo = getDogRequiredInfo(filteredList);

		Gson gson = new Gson();
		String filteredDogJson = gson.toJson(filteredList);
				
		return new ResponseEntity<String>(filteredDogJson, HttpStatus.OK);
	}
	
	// View dog
	@RequestMapping(value = "/dog/view", method = RequestMethod.POST)
	public ResponseEntity dogView(@RequestBody String dogId) {		
		Dog dog = dogRepository.findById(Long.parseLong(dogId));
		
		if (dog == null)
			return new ResponseEntity("Cachorro não encontrado.", HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity(dog, HttpStatus.OK);
	}
	
	// Update dog
	@RequestMapping(value = "/dog/update", method = RequestMethod.POST)
    public ResponseEntity<String> dogUpdate(@RequestBody Dog dog) {
		
		if(dog.getName() == null){
			return new ResponseEntity<String>("Nome está em branco",HttpStatus.BAD_REQUEST);
		}
		
		Dog dogWithSameName = dogRepository.findByName(dog.getName());
		
		if(dogWithSameName != null && dogWithSameName.getId() != dog.getId()){
			return new ResponseEntity<String>("Já existe um cachorro com este nome",HttpStatus.BAD_REQUEST);
		}
		
		if(dog.getArrivalDate() == null){
			return new ResponseEntity<String>("Data de chegada está em branco",HttpStatus.BAD_REQUEST);
		}
		
		if(dog.getCastrated() == null){
			return new ResponseEntity<String>("Flag de castrado está em branco",HttpStatus.BAD_REQUEST);
		}
		
		dogRepository.saveAndFlush(dog);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
