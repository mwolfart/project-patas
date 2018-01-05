package controller;

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
	
	// Given a list containing the appointment data, which has the dog ids,
	//  retrieve the dog names from each id.
	// TODO: REFACTOR!!
	private void getDogNamesFromIds(List<List<Object>> data_list) {
		for(int i = 0; i < data_list.size(); i++) {
			Long dog_id = Helper.objectToLong(data_list.get(i).get(1));
			String dog_name = dogRepository.findById(dog_id).getName();
			data_list.get(i).set(1, dog_name);
		}
	}
	
	// Register (and update)
	@RequestMapping(value = "/appointment/register", method = RequestMethod.POST)
	public ResponseEntity<?> appointmentRegister(@RequestBody Appointment appointment) {
		System.out.println(appointment);
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
		Appointment appointment = appointmentRepository.findOne(Long.parseLong(appointmentId));
		if (appointment == null)
			return new ResponseEntity<String>("Registro de consulta não encontrado.", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<Appointment>(appointment, HttpStatus.OK);
	}

	// Search
	@RequestMapping(value = "/appointment/search", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<List<List<Object>>> appointmentSearch(@RequestBody String search_query) {
		String[] pairs = search_query.split("\\{|,|\\}");
		Map<String, String> criteria_list = Helper.splitCriteriaFromKeys(pairs);

		List<Specification<Appointment>> spec_list = AppointmentSpecifications.buildSpecListFromCriteria(criteria_list);
		Specification<Appointment> final_specification = AppointmentSpecifications.buildSpecFromSpecList(spec_list);

		List<Appointment> filtered_appointment_list = appointmentRepository.findAll(final_specification);
		List<List<Object>> filtered_info_list = AppointmentSpecifications.filterAppointmentInfo(filtered_appointment_list, new String[] {"id", "dogId", "appointmentDate", "location", "vetName"});
		getDogNamesFromIds(filtered_info_list);
		
		return new ResponseEntity<List<List<Object>>>(filtered_info_list, HttpStatus.OK);
	}
	
	// Delete
	@RequestMapping(value = "/appointment/delete", method = RequestMethod.POST)
	public ResponseEntity<String> appointmentDelete(@RequestBody String appointmentId) {
		appointmentRepository.delete(Long.parseLong(appointmentId));
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	// Delete by dog
	@RequestMapping(value = "/appointment/delete_by_dog", method = RequestMethod.POST)
	public ResponseEntity<String> appointmentDeleteByDog(@RequestBody String dogId) {
		// TODO: ERROR TREATMENT
		ResponseEntity<List<List<Object>>> search_result = appointmentSearch("{\"dogId\":\""+dogId+"\"}");
		List<List<Object>> found_entries = search_result.getBody();
		// TODO: MAP FUNCTION?
		List<Long> ids_to_delete = Helper.getIds(found_entries);
		for(Long id : ids_to_delete) {
			appointmentDelete(Long.toString(id));
		}		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
