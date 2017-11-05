package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import repository.VermRepository;

@RestController
public class VermController {

	@Autowired
	private VermRepository vermRepository;
	
}
