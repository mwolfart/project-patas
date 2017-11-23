package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


//COLOCAR ELA NO APPLICATION TALVEZ.. TESTAR!
@Controller
public class HomeController {
	 
	@RequestMapping("/")
	public String index() {
		return "index.html";
	}
}
