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

import repository.VermifugeRepository;
import repository.VermifugeSpecifications;

@RestController
public class VermifugeService {

	@Autowired
	private VermifugeRepository vermifugeRepository;

	// Register 
	@RequestMapping(value = "/vermifuge/register", method = RequestMethod.POST)
	public ResponseEntity<String> vermifugeRegister(@RequestBody Vermifuge vermifuge) {

		if(vermifuge.getVermifugeName() == null)
			return new ResponseEntity<String>("Nome está em branco", HttpStatus.BAD_REQUEST);

		//VER ISSO, PEGAR COM ID.. tenho q me lembrar..
		//pq ele passa o nome mas com o nome pegamos o id.. mas nao tem campo pro nome só
		//if(vermifuge.getDog().getName() == null)
		//return new ResponseEntity<String>("Nome do cachorro está em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getDosage() == null)
			return new ResponseEntity<String>("Dosagem está em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getApplicationDate() == null)
			return new ResponseEntity<String>("Data da aplicação está em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getNextapplicationDate() == null)
			return new ResponseEntity<String>("Data da proxima aplicação está em branco", HttpStatus.BAD_REQUEST);

		vermifugeRepository.saveAndFlush(vermifuge);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	// Update 
	@RequestMapping(value = "/vermifuge/update", method = RequestMethod.POST)
	public ResponseEntity<String> vermifugeUpdate(@RequestBody Vermifuge vermifuge) {
		if(vermifuge.getVermifugeName() == null)
			return new ResponseEntity<String>("Nome está em branco", HttpStatus.BAD_REQUEST);

		//VER ISSO, PEGAR COM ID.. tenho q me lembrar..
		//pq ele passa o nome mas com o nome pegamos o id.. mas nao tem campo pro nome só
		//if(vermifuge.getDog().getName() == null)
		//return new ResponseEntity<String>("Nome do cachorro está em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getDosage() == null)
			return new ResponseEntity<String>("Dosagem está em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getApplicationDate() == null)
			return new ResponseEntity<String>("Data da aplicação está em branco", HttpStatus.BAD_REQUEST);

		if(vermifuge.getNextapplicationDate() == null)
			return new ResponseEntity<String>("Data da proxima aplicação está em branco", HttpStatus.BAD_REQUEST);

		vermifugeRepository.saveAndFlush(vermifuge);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	// View 
	@RequestMapping(value = "/vermifuge/view", method = RequestMethod.POST)
	public ResponseEntity<?> vermifugeView(@RequestBody String vermifugeId) {		
		Vermifuge vermifuge = vermifugeRepository.findOne(Long.parseLong(vermifugeId));
		if (vermifuge == null)
			return new ResponseEntity<String>("Vermifugo não encontrado.", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<Vermifuge>(vermifuge, HttpStatus.OK);
	}

	// Search vermifuge
	@RequestMapping(value = "/vermifuge/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> vermifugeSearch(@RequestBody String search_query) {		
		// We are going to split the JSON so we get each criteria separately
		//  in the map. First we split the criteria, one from each other
		String[] pairs = search_query.split("\\{|,|\\}");

		// Then, we split each criterion from its key, and build a list from them.
		Map<String, String> criteria_list = VermifugeSpecifications.splitCriteriaFromKeys(pairs);
		List<Specification<Vermifuge>> spec_list = VermifugeSpecifications.buildSpecListFromCriteria(criteria_list);
		Specification<Vermifuge> final_specification = VermifugeSpecifications.buildSpecFromSpecList(spec_list);

		List<Vermifuge> filtered_vermifuge_list = vermifugeRepository.findAll(final_specification);
		List<List<Object>> filtered_info_list = VermifugeSpecifications.filterVermifugeInfo(filtered_vermifuge_list, new String[] {"id", "name", "sex", "arrivalDate"});

		System.out.println(filtered_info_list);

		return new ResponseEntity<List<List<Object>>>(filtered_info_list, HttpStatus.OK);
	}
}
