package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.Antifleas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import repository.AntifleasRepository;
import repository.AntifleasSpecifications; 

public class AntifleasService {
  private static AntifleasRepository antifleasRepository;
	
	@Autowired
	public AntifleasService(AntifleasRepository antifleasRepository) {
		AntifleasService.antifleasRepository = antifleasRepository;
	}
	
	// Given a list of antifleas, return only the desired information
	// Used in search function.
	private List<List<Object>> filterAntifleasInfo(List<Antifleas> antifleas_list) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		for (Antifleas anti : antifleas_list) {
			Long dogId = anti.getDogId();
			String dogName = DogService.getDogNameById(dogId);
			String appDateAsString = df.format(anti.getApplicationDate());
			
			List<Object> entry = new ArrayList<Object>(Arrays.asList(anti.getId(), dogName, anti.getAntifleasName(), appDateAsString));
			filtered_list.add(entry);
		}
		
		return filtered_list;
	}
	
	// Check if a given dog (by id) has any antifleas
	public static Boolean dogHasAntifleas(Long dogId) {
		List<Antifleas> foundAnti = antifleasRepository.findByDogId(dogId);
		return (foundAnti.size() != 0); 
	}
	
	// Register (and update)
	@RequestMapping(value = "/antifleas/register", method = RequestMethod.POST)
	public ResponseEntity<?> antifleasRegister(@RequestBody Antifleas antifleas) {
		if(antifleas.getAntifleasName() == null)
			return new ResponseEntity<String>("Nome do antipulgas está em branco", HttpStatus.BAD_REQUEST);

		if(antifleas.getDogId() == null)
			return new ResponseEntity<String>("Id do cachorro está em branco", HttpStatus.BAD_REQUEST);

		if(antifleas.getApplicationDate() == null)
			return new ResponseEntity<String>("Data da aplicação está em branco", HttpStatus.BAD_REQUEST);

		antifleasRepository.saveAndFlush(antifleas);
		return new ResponseEntity<Long>(antifleas.getId(), HttpStatus.OK);
	}

	// View 
	@RequestMapping(value = "/antifleas/view", method = RequestMethod.POST)
	public ResponseEntity<?> antifleasView(@RequestBody String antifleasId) {	
		try {  
			Antifleas antifleas = antifleasRepository.findOne(Long.parseLong(antifleasId));
			if (antifleas == null)
				return new ResponseEntity<String>("Registro de antipulgas não encontrado.", HttpStatus.BAD_REQUEST);
	
			return new ResponseEntity<Antifleas>(antifleas, HttpStatus.OK);
		} catch(NumberFormatException e) {
			return new ResponseEntity<String>("Id inválido.", HttpStatus.BAD_REQUEST);
		}
	}

	// Search
	@RequestMapping(value = "/antifleas/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> antifleasSearch(@RequestBody String search_query) {
		try {
			String[] pairs = search_query.split("\\{|,|\\}");
			Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
			
			List<Specification<Antifleas>> spec_list = AntifleasSpecifications.buildSpecListFromCriteria(criteria_list);
			Specification<Antifleas> final_specification = Helper.buildSpecFromSpecList(spec_list);
			
			List<Antifleas> filtered_antifleas_list = antifleasRepository.findAll(final_specification);
			List<List<Object>> filtered_data = filterAntifleasInfo(filtered_antifleas_list);
			
			return new ResponseEntity<List<List<Object>>>(filtered_data, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<List<Object>>>(HttpStatus.BAD_REQUEST);
		}	
	}
	
	// Delete
	@RequestMapping(value = "/antifleas/delete", method = RequestMethod.POST)
	public ResponseEntity<String> antifleasDelete(@RequestBody String antifleasId) {
		try {
			antifleasRepository.delete(Long.parseLong(antifleasId));
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch(NumberFormatException e) {
			return new ResponseEntity<String>("Id inválido.", HttpStatus.BAD_REQUEST);
		}
	}
	
	// Delete by dog
	@RequestMapping(value = "/antifleas/delete_by_dog", method = RequestMethod.POST)
	public ResponseEntity<String> antifleasDeleteByDog(@RequestBody String dogId) {
		if (!Helper.isNumeric(dogId)) 
			return new ResponseEntity<String>("Id inválido.", HttpStatus.BAD_REQUEST);
		
		ResponseEntity<List<List<Object>>> search_result = antifleasSearch("{\"dogId\":\""+dogId+"\"}");
		List<List<Object>> found_entries = search_result.getBody();
		
		for(List<Object> entry : found_entries) 
			antifleasDelete(Long.toString(Helper.objectToLong(entry.get(0))));

		return new ResponseEntity<String>(HttpStatus.OK);
	} 
}
