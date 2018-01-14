package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	// Given a list of vaccinations, return only the desired information
	// Used in search function.
	private List<List<Object>> filterVaccinationsInfo(List<Vaccination> vaccination_list) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		for (Vaccination vac : vaccination_list) {
			Long dogId = vac.getDogId();
			String dogName = dogRepository.findById(dogId).getName();
			String appDateAsString = df.format(vac.getApplicationDate());
			
			List<Object> entry = new ArrayList<Object>(Arrays.asList(vac.getId(), dogName, vac.getVaccineName(), appDateAsString));
			filtered_list.add(entry);
		}
		
		return filtered_list;
	}
	
	// Register (and update)
	@RequestMapping(value = "/vaccination/register", method = RequestMethod.POST)
	public ResponseEntity<?> vaccinationRegister(@RequestBody Vaccination vaccination) {
		if(vaccination.getVaccineName() == null)
			return new ResponseEntity<String>("Nome da vacina está em branco", HttpStatus.BAD_REQUEST);

		if(vaccination.getDogId() == null)
			return new ResponseEntity<String>("Id do cachorro está em branco", HttpStatus.BAD_REQUEST);

		if(vaccination.getApplicationDate() == null)
			return new ResponseEntity<String>("Data da aplicação está em branco", HttpStatus.BAD_REQUEST);

		vaccinationRepository.saveAndFlush(vaccination);
		return new ResponseEntity<Long>(vaccination.getId(), HttpStatus.OK);
	}

	// View 
	@RequestMapping(value = "/vaccination/view", method = RequestMethod.POST)
	public ResponseEntity<?> vaccinationView(@RequestBody String vaccinationId) {		
		Vaccination vaccination = vaccinationRepository.findOne(Long.parseLong(vaccinationId));
		if (vaccination == null)
			return new ResponseEntity<String>("Registro de vacinação não encontrado.", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<Vaccination>(vaccination, HttpStatus.OK);
	}

	// Search
	@RequestMapping(value = "/vaccination/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> vaccinationSearch(@RequestBody String search_query) {
		String[] pairs = search_query.split("\\{|,|\\}");
		Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
		
		List<Specification<Vaccination>> spec_list = VaccinationSpecifications.buildSpecListFromCriteria(criteria_list);
		Specification<Vaccination> final_specification = VaccinationSpecifications.buildSpecFromSpecList(spec_list);

		List<Vaccination> filtered_vaccination_list = vaccinationRepository.findAll(final_specification);
		List<List<Object>> filtered_data = filterVaccinationsInfo(filtered_vaccination_list);
		
		return new ResponseEntity<List<List<Object>>>(filtered_data, HttpStatus.OK);
	}
	
	// Delete
	@RequestMapping(value = "/vaccination/delete", method = RequestMethod.POST)
	public ResponseEntity<String> vaccinationDelete(@RequestBody String vaccinationId) {
		vaccinationRepository.delete(Long.parseLong(vaccinationId));
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	// Delete by dog
	@RequestMapping(value = "/vaccination/delete_by_dog", method = RequestMethod.POST)
	public ResponseEntity<String> vaccinationDeleteByDog(@RequestBody String dogId) {
		// TODO: ERROR TREATMENT
		ResponseEntity<List<List<Object>>> search_result = vaccinationSearch("{\"dogId\":\""+dogId+"\"}");
		List<List<Object>> found_entries = search_result.getBody();
		// TODO: MAP FUNCTION?
		List<Long> ids_to_delete = Helper.getIds(found_entries);
		for(Long id : ids_to_delete) {
			vaccinationDelete(Long.toString(id));
		}		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
