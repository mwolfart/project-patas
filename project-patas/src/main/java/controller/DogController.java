package controller;

import java.util.Date;

import model.Dog;
import model.Dog.Availability;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DogController {
	
	@RequestMapping("/dog")
    public Dog dogRegister(@RequestParam(value="name",required = false) String name) {
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
}
