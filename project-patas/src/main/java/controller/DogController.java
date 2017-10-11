package controller;

import java.util.Date;
import model.Dog;
import model.Dog.Availability;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DogController {
	// REQUEST
	@RequestMapping(value = "/dogGet")
    public Dog dogRegisterGet(@RequestParam(value="name",required = false) String name) {
		name = "veia";
		Double weight = 20.5;
	    String gender = "F";
	    String size = "M";
	    String pelageColor = "Preta";
	    Date dateBirth = new Date();
	    Integer age = 5;
	    Date arrivalDate = new Date();
		Boolean castrated = true;
		Date castrationDate = new Date();
		Availability availability = Availability.DISPONIVEL;
		Boolean disease = false;
		String diseaseDescription = "";
		String godfathers = "José";
        return new Dog(name, weight, gender, size, pelageColor, dateBirth, age, arrivalDate, 
        		castrated, castrationDate, availability, disease, diseaseDescription, godfathers) ;
    }  
	
	// POST
	@RequestMapping(value = "/dogPost", method = RequestMethod.POST)
    public ResponseEntity<?> dogRegister(@RequestBody Dog dog) {
		
		//Dog newDog = new Dog(dog.getName(), dog.getWeight(), dog.getGender(), dog.getSize(), dog.getPelageColor(), dog.getDateBirth(), dog.getAge(), dog.getArrivalDate(), dog.getCastrated(), dog.getCastrationDate(), dog.getAvailability(), dog.getDisease(), dog.getDiseaseDescription(), dog.getGodfathers());
		System.out.print(dog);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
