// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields	
	$( "#vaccineName" ).keydown(protectSearchStringField);
	$( "#applicationDate" ).keydown(protectNumericField);
	$( "#nextApplicationDate" ).keydown(protectNumericField);
	
	$( "#vaccineName" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorVaccineName"));
	});
	
	$( "#applicationDate" ).focusout( function() { 
		validateDateField(this);
		hideAlert($("#errorApplicationDate"));
	});
	
	$( "#nextApplicationDate" ).focusout( function() { 
		validateDateField(this);
		hideAlert($("#errorNextApplicationDate"));
	});

	// Set the submit configuration for the form
	$( "#vaccinationSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateSearchStringField( $("#vaccineName")[0] ) == -1 )
			showAlert($( "#errorVaccineName" ), "Nome inválido.");
		else if ( validateDateField( $("#applicationDate")[0] ) == -1 )
			showAlert($( "#errorApplicationDate" ), "Data inválida.");
		else if ( validateDateField( $("#nextApplicationDate")[0] ) == -1 )
			showAlert($( "#errorNextApplicationDate" ), "Data inválida.");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			$.ajax({
				url: "/vaccination/search",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					arrayToTable($('#vaccinations'), "vaccination", response);
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
		url: "/vaccination/search",
		type: "POST",
		dataType: "json",
		data: jsonData,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			arrayToTable($('#vaccinations'), "vaccination", response);
		}
	});
});
