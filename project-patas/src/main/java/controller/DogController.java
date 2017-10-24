package controller;

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

import repository.DogRepository;


@RestController
public class DogController { 
	
	@Autowired 
	private DogRepository dogRepository;
		
	@RequestMapping(value = "/dog/register", method = RequestMethod.POST)
    public ResponseEntity<String> dogRegister(@RequestBody Dog dog) {
		
		if(dog.getName() == null){
			return new ResponseEntity<String>("Nome está em branco",HttpStatus.BAD_REQUEST);
		}
		
		Dog dogExist = dogRepository.findByName(dog.getName());
		if(dogExist != null){
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
	
	//Must rework this
	@RequestMapping(value = "/dog/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<String> dogSearch(@RequestBody String searchQuery) {
		System.out.println(searchQuery);
		
		// TRIED TO USE ENTITY MANAGER. FAILED.
		/*
		EntityManagerFactory fac = Persistence.createEntityManagerFactory("testP");
		EntityManager em = fac.createEntityManager();
		
		Query q = em.createNativeQuery(searchQuery);
		
		List<Dog> dogs = q.getResultList();
		
		System.out.println(dogs);
		*/
		
		// IF THE QUERY IS BUILT IN THE BACKEND, WE NEED THIS (not completed; query being built in frontend)
		/*
		Map<String, String> queryMap = new HashMap<String, String>();
		String[] pairs = searchQuery.split("\\{|,|\\}");

		for (int i=1; i < pairs.length - 1; i++) {
		    String pair = pairs[i];
		    String[] keyValue = pair.split("\"|:|\"");
		    if (keyValue[3] != "")
		    	queryMap.put(keyValue[1], keyValue[3]);
		    else queryMap.put(keyValue[1], keyValue[4]);
		}
		
		System.out.println(queryMap);
		
		String sqlQuery = "SELECT name, sex, arrivalDate FROM Dog WHERE ";

		for (Map.Entry<String, String> entry : queryMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			sqlQuery += key + " = " + value + " AND ";
			
		}
		
		sqlQuery += "1 = 1";
		
		System.out.println(sqlQuery);
		*/
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
