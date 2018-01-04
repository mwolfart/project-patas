package controller;

import java.util.List;
import java.util.Map;

import model.Vaccination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.DogRepository;
import repository.VaccinationRepository;
import repository.VaccinationSpecifications;

@RestController
public class VaccinationService {
	
	@Autowired
	private VaccinationRepository vaccinationRepository;
	@Autowired
	private DogRepository dogRepository;
	
	// Given the criteria for a vaccination, which contains the name of the dog,
	//  find the id of the given dog.
	private Long getDogIdFromCriteria(Map<String, String> criteria) {
		for(Map.Entry<String, String> criterion : criteria.entrySet()) {
			if (criterion.getKey().equals("dogName")) {
				return dogRepository.findByName(criterion.getValue()).getId();
			}
		}
		
		return null;
	}
	
	// Given a list containing the vaccination data, which has the dog ids,
	//  retrieve the dog names from each id.
	private void getDogNamesFromIds(List<List<Object>> data_list) {
		for(int i = 0; i < data_list.size(); i++) {
			Long dog_id = Helper.objectToLong(data_list.get(i).get(1));
			String dog_name = dogRepository.findById(dog_id).getName();
			data_list.get(i).set(1, dog_name);
		}
	}
	
	// Register (and update)
	@RequestMapping(value = "/vaccination/register", method = RequestMethod.POST)
	public ResponseEntity<?> vaccinationRegister(@RequestBody Vaccination vaccination) {
		if(vaccination.getVaccineName() == null)
			return new ResponseEntity<String>("Nome da vacina est� em branco", HttpStatus.BAD_REQUEST);

		if(vaccination.getDogId() == null)
			return new ResponseEntity<String>("Id do cachorro est� em branco", HttpStatus.BAD_REQUEST);

		if(vaccination.getApplicationDate() == null)
			return new ResponseEntity<String>("Data da aplica��o est� em branco", HttpStatus.BAD_REQUEST);

		vaccinationRepository.saveAndFlush(vaccination);
		return new ResponseEntity<Long>(vaccination.getId(), HttpStatus.OK);
	}

	// View 
	@RequestMapping(value = "/vaccination/view", method = RequestMethod.POST)
	public ResponseEntity<?> vaccinationView(@RequestBody String vaccinationId) {		
		Vaccination vaccination = vaccinationRepository.findOne(Long.parseLong(vaccinationId));
		if (vaccination == null)
			return new ResponseEntity<String>("Registro de vacina��o n�o encontrado.", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<Vaccination>(vaccination, HttpStatus.OK);
	}

	// Search
	@RequestMapping(value = "/vaccination/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> vaccinationSearch(@RequestBody String search_query) {
		String[] pairs = search_query.split("\\{|,|\\}");
		Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
		
		Long dog_id = getDogIdFromCriteria(criteria_list);
		if (dog_id != null)
			criteria_list.put("dogId", Long.toString(dog_id));
		
		List<Specification<Vaccination>> spec_list = VaccinationSpecifications.buildSpecListFromCriteria(criteria_list);
		Specification<Vaccination> final_specification = VaccinationSpecifications.buildSpecFromSpecList(spec_list);

		List<Vaccination> filtered_vaccination_list = vaccinationRepository.findAll(final_specification);
		List<List<Object>> filtered_info_list = VaccinationSpecifications.filterVaccinationInfo(filtered_vaccination_list, new String[] {"id", "dogId", "vaccineName", "applicationDate"});
		getDogNamesFromIds(filtered_info_list);
		
		return new ResponseEntity<List<List<Object>>>(filtered_info_list, HttpStatus.OK);
	}
	
	// Delete
	@RequestMapping(value = "/vaccination/delete", method = RequestMethod.POST)
	public ResponseEntity<String> vaccinationDelete(@RequestBody String vaccinationId) {
		vaccinationRepository.delete(Long.parseLong(vaccinationId));
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
