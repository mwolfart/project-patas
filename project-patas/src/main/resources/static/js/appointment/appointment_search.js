// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields
	$( "#location" ).keydown(protectSearchStringField);
	$( "#vetName" ).keydown(protectSearchStringField);
	$( "#appointmentDate" ).keydown(protectNumericField);
	
	$( "#location" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorLocation"));
	});
	
	$( "#vetName" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorVetName"));
	});
	
	$( "#appointmentDate" ).focusout( function() { 
		validateDateField(this);
		hideAlert($("#errorAppointmentDate"));
	});

	// Set the submit configuration for the form
	$( "#appointmentSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateSearchStringField( $("#vetName")[0] ) == -1 )
			showAlert($( "#errorVetName" ), "Nome inválido.");
		else if ( validateSearchStringField( $("#location")[0] ) == -1 )
			showAlert($( "#errorLocation" ), "Nome inválido.");
		else if ( validateDateField( $("#appointmentDate")[0] ) == -1 )
			showAlert($( "#errorAppointmentDate" ), "Data inválida.");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			$.ajax({
				url: "/appointment/search",
				type: "POST",
				data: jsonData,
				dataType: "json",
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					arrayToTable($('#appointments'), "appointment", response);
				}
			});
		}
	});	
	
	/****************/
	/** INIT STATE **/
	/****************/
	 
	// Configure dog combobox and load if specified			
	var dog_id = getUrlParameter("dogId");
	var jsonData = {};
	
	if ((dog_id != undefined) && (dog_id == parseInt(dog_id, 10))) {
		putDogsInComboBoxAndSet(dog_id);
		jsonData = {"dogId": dog_id};
		jsonData = JSON.stringify(jsonData);
	} else putDogsInComboBox();
	
	$.ajax({
		url: "/appointment/search",
		type: "POST",
		dataType: "json",
		data: jsonData,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			arrayToTable($('#appointments'), "appointment", response);
		}
	});
});
