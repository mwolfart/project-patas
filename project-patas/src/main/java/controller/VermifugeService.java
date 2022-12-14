package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import repository.VermifugeRepository;
import repository.VermifugeSpecifications;

@RestController
public class VermifugeService {
	
	private static VermifugeRepository vermifugeRepository;
	
	@Autowired
	public VermifugeService(VermifugeRepository vermifugeRepository) {
		VermifugeService.vermifugeRepository = vermifugeRepository;
	}
	
	// Given a list of vermifuges, return only the desired information
	// Used in search function.
	private List<List<Object>> filterVermifugesInfo(List<Vermifuge> vermifuge_list) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		for (Vermifuge verm : vermifuge_list) {
			Long dogId = verm.getDogId();
			String dogName = DogService.getDogNameById(dogId);
			String appDateAsString = df.format(verm.getApplicationDate());
			
			List<Object> entry = new ArrayList<Object>(Arrays.asList(verm.getId(), dogName, verm.getVermifugeName(), appDateAsString));
			filtered_list.add(entry);
		}
		
		return filtered_list;
	}
	
	// Check if a given dog (by id) has any vermifug(ations(?))
	public static Boolean dogHasVermifuges(Long dogId) {
		List<Vermifuge> foundVerm = vermifugeRepository.findByDogId(dogId);
		return (foundVerm.size() != 0);
	}
	
	// Register (and update)
	@RequestMapping(value = "/vermifuge/register", method = RequestMethod.POST)
	public ResponseEntity<?> vermifugeRegister(@RequestBody Vermifuge vermifuge) {
		if(vermifuge.getVermifugeName() == null)
			return new ResponseEntity<String>("Nome est?? em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getDogId() == null)
			return new ResponseEntity<String>("Id do cachorro est?? em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getDosage() == null)
			return new ResponseEntity<String>("Dosagem est?? em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getApplicationDate() == null)
			return new ResponseEntity<String>("Data da aplica????o est?? em branco", HttpStatus.BAD_REQUEST);

		vermifugeRepository.saveAndFlush(vermifuge);
		return new ResponseEntity<Long>(vermifuge.getId(), HttpStatus.OK);
	}

	// View 
	@RequestMapping(value = "/vermifuge/view", method = RequestMethod.POST)
	public ResponseEntity<?> vermifugeView(@RequestBody String vermifugeId) {	
		try {
			Vermifuge vermifuge = vermifugeRepository.findOne(Long.parseLong(vermifugeId));
			if (vermifuge == null)
				return new ResponseEntity<String>("Vermifugo n??o encontrado.", HttpStatus.BAD_REQUEST);
	
			return new ResponseEntity<Vermifuge>(vermifuge, HttpStatus.OK);
		} catch(NumberFormatException e) {
			return new ResponseEntity<String>("Id inv??lido.", HttpStatus.BAD_REQUEST);
		}
	}

	// Search
	@RequestMapping(value = "/vermifuge/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> vermifugeSearch(@RequestBody String search_query) {
		try {
			String[] pairs = search_query.split("\\{|,|\\}");
			Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
			
			List<Specification<Vermifuge>> spec_list = VermifugeSpecifications.buildSpecListFromCriteria(criteria_list);
			Specification<Vermifuge> final_specification = Helper.buildSpecFromSpecList(spec_list);
	
			List<Vermifuge> filtered_vermifuge_list = vermifugeRepository.findAll(final_specification);
			List<List<Object>> filtered_data = filterVermifugesInfo(filtered_vermifuge_list);
			
			return new ResponseEntity<List<List<Object>>>(filtered_data, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<List<Object>>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// Delete
	@RequestMapping(value = "/vermifuge/delete", method = RequestMethod.POST)
	public ResponseEntity<String> vermifugeDelete(@RequestBody String vermifugeId) {
		try {
			vermifugeRepository.delete(Long.parseLong(vermifugeId));
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch(NumberFormatException e) {
			return new ResponseEntity<String>("Id inv??lido.", HttpStatus.BAD_REQUEST);
		}
	}
	
	// Delete by dog
	@RequestMapping(value = "/vermifuge/delete_by_dog", method = RequestMethod.POST)
	public ResponseEntity<String> vermifugeDeleteByDog(@RequestBody String dogId) {
		if (!Helper.isNumeric(dogId)) 
			return new ResponseEntity<String>("Id inv??lido.", HttpStatus.BAD_REQUEST);
		
		ResponseEntity<List<List<Object>>> search_result = vermifugeSearch("{\"dogId\":\""+dogId+"\"}");
		List<List<Object>> found_entries = search_result.getBody();
		
		for(List<Object> entry : found_entries)
			vermifugeDelete(Long.toString(Helper.objectToLong(entry.get(0))));

		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
