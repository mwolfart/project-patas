package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import repository.VermifugeRepository;

@RestController
public class VermifugeController {

	@Autowired
	private VermifugeRepository vermifugeRepository;
	
}
