package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.Appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.DogRepository;
import repository.AppointmentRepository;
import repository.AppointmentSpecifications;

@RestController
public class AppointmentService {
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	@Autowired
	private DogRepository dogRepository;
	
	// Given a list of appointments, return only the desired information
	// Used in search function.
	private List<List<Object>> filterAppointmentsInfo(List<Appointment> appointment_list) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		for (Appointment app : appointment_list) {
			Long dogId = app.getDogId();			
			String dogName = dogRepository.findById(dogId).getName();
			String appDateAsString = df.format(app.getAppointmentDate());
			String appLocation = app.getLocation() != null ? app.getLocation() : "";
			String appVetName = app.getVetName() != null ? app.getVetName() : "";
			
			List<Object> entry = new ArrayList<Object>(Arrays.asList(app.getId(), dogName, appDateAsString, appLocation, appVetName));
			filtered_list.add(entry);
		}
		
		return filtered_list;
	}
	
	// Register (and update)
	@RequestMapping(value = "/appointment/register", method = RequestMethod.POST)
	public ResponseEntity<?> appointmentRegister(@RequestBody Appointment appointment) {
		if(appointment.getDogId() == null)
			return new ResponseEntity<String>("Id do cachorro está em branco", HttpStatus.BAD_REQUEST);

		if(appointment.getAppointmentDate() == null)
			return new ResponseEntity<String>("Data da consulta está em branco", HttpStatus.BAD_REQUEST);
		
		if(appointment.getReason() == null)
			return new ResponseEntity<String>("Motivo da consulta está em branco", HttpStatus.BAD_REQUEST);

		appointmentRepository.saveAndFlush(appointment);
		return new ResponseEntity<Long>(appointment.getId(), HttpStatus.OK);
	}

	// View 
	@RequestMapping(value = "/appointment/view", method = RequestMethod.POST)
	public ResponseEntity<?> appointmentView(@RequestBody String appointmentId) {		
		try { 
			Appointment appointment = appointmentRepository.findOne(Long.parseLong(appointmentId)); 
			
			if (appointment == null)
				return new ResponseEntity<String>("Registro de consulta não encontrado.", HttpStatus.BAD_REQUEST);
			else return new ResponseEntity<Appointment>(appointment, HttpStatus.OK);
		} catch(NumberFormatException e) {
			return new ResponseEntity<String>("Id inválido.", HttpStatus.BAD_REQUEST);
		}
	}

	// Search
	@RequestMapping(value = "/appointment/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> appointmentSearch(@RequestBody String search_query) {
		try {
			String[] pairs = search_query.split("\\{|,|\\}");
			Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);
			
			List<Specification<Appointment>> spec_list = AppointmentSpecifications.buildSpecListFromCriteria(criteria_list);
			Specification<Appointment> final_specification = Helper.buildSpecFromSpecList(spec_list);
	
			List<Appointment> filtered_appointment_list = appointmentRepository.findAll(final_specification);
			List<List<Object>> filtered_data = filterAppointmentsInfo(filtered_appointment_list);
	
			return new ResponseEntity<List<List<Object>>>(filtered_data, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<List<Object>>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// Delete
	@RequestMapping(value = "/appointment/delete", method = RequestMethod.POST)
	public ResponseEntity<String> appointmentDelete(@RequestBody String appointmentId) {
		try {
			appointmentRepository.delete(Long.parseLong(appointmentId));
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch(NumberFormatException e) {
			return new ResponseEntity<String>("Id inválido.", HttpStatus.BAD_REQUEST);
		}
	}
	
	// Delete by dog
	@RequestMapping(value = "/appointment/delete_by_dog", method = RequestMethod.POST)
	public ResponseEntity<String> appointmentDeleteByDog(@RequestBody String dogId) {
		if (!Helper.isNumeric(dogId)) 
			return new ResponseEntity<String>("Id inválido.", HttpStatus.BAD_REQUEST);
	
		ResponseEntity<List<List<Object>>> search_result = appointmentSearch("{\"dogId\":\""+dogId+"\"}");
		List<List<Object>> found_entries = search_result.getBody();
		
		for(List<Object> entry : found_entries)
			appointmentDelete(Long.toString(Helper.objectToLong(entry.get(0))));

		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
