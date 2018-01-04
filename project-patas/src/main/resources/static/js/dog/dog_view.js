
// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	var arrival_date = new Date();
	arrival_date.setTime(json.arrivalDate);
	
	$(" #name ").val(json.name);
	$(" #weight ").val(json.weight);
	$(" #sex ").val(json.sex);
	$(" #size ").val(json.size);
	$(" #furColor ").val(json.furColor);
	$(" #status ").val(json.status);
	$(" #arrivalDate ").val(dateToString(arrival_date));
	$(" #ration ").val(json.ration);
	$(" #rationAmount ").val(json.rationAmount);
	$(" #rationMeasurement ").val(json.rationMeasurement);
	$(" #sponsors ").val(json.sponsors);
	
	if (json.birthDate) {
		var birth_date = new Date();
		birth_date.setTime(json.birthDate);
		$(" #birthDate ").val(dateToString(birth_date));
		$(" #age ").val(computeAge(birth_date));
	}
	
	if (json.castrated == true) {
		$(" #castrated ").prop('checked', true);

		if (json.castrationDate) {
			var castration_date = new Date();
			castration_date.setTime(json.castrationDate);
			$(" #castrationDate ").val(dateToString(castration_date));
		}
	}
	
	if (json.hasDiseases == true) {
		$(" #hasDiseases ").prop('checked', true);
		(json.diseaseDescription ? $(" #diseaseDescription ").val(json.diseaseDescription) : false );
	}
	
	if (json.ration == "OUTRO") {
		(json.rationCustomDescription ? $(" #rationCustomDescription ").val(json.rationCustomDescription) : false );
	}
	
	if (json.photo) {
		$.ajax({
			url: "/dog/array_to_photo",
			type: "POST",
			data: json.photo,
			contentType: "application/json; charset=UTF-8",
			success: function(response) {
				$(" #image-input ").val(response);
			}
		});
	}
}

// Document load script
$(document).ready(function() {
	
	/**************/
	/* EDIT STATE */
	/**************/
	
	// Edit button onClick handler
	$(" #editBtn ").click(function() {		
		$(" #name ").prop('disabled', false);
		$(" #birthDate ").prop('disabled', false);
		$(" #weight ").prop('disabled', false);
		$(" #sex ").prop('disabled', false);
		$(" #size ").prop('disabled', false);
		$(" #furColor ").prop('disabled', false);
		$(" #status ").prop('disabled', false);
		$(" #arrivalDate ").prop('disabled', false);
		$(" #ration ").prop('disabled', false);
		$(" #rationAmount ").prop('disabled', false);
		$(" #rationMeasurement ").prop('disabled', false);
		$(" #castrated ").prop('disabled', false);
		$(" #hasDiseases ").prop('disabled', false);
		$(" #sponsors ").prop('disabled', false);
		
		if ( $(" #castrated ").prop("checked") )
			$(" #castrationDate ").prop('disabled', false);
		
		if ( $(" #hasDiseases ").prop("checked") )
			$(" #diseaseDescription ").prop('disabled', false);
		
		if ( $(" #ration ").value == "OUTRO" )
			$(" #rationCustomDescription ").prop('disabled', false);
		
		$(" #editBtn ").prop('disabled', true);
		$(" #appointmentBtn ").prop('disabled', false);
		$(" #vaccinationBtn ").prop('disabled', true);
		$(" #vermifugeBtn ").prop('disabled', true);
		$(" #saveBtn ").prop('disabled', false);
		$(" #deleteBtn ").prop('disabled', true);
	});
	
	// Vermifuge button onClick handler
	$(" #vermifugeBtn ").click(function() {
		window.location.replace("/vermifuge/vermifuge_search.html?dogName=" + $("#name").val());
	});
	
	// Vaccination button onClick handler
	$(" #vaccinationBtn ").click(function() {
		window.location.replace("/vaccination/vaccination_search.html?dogName=" + $("#name").val());
	});
	
	// Appointment button onClick handler
	$(" #appointmentBtn ").click(function() {
		window.location.replace("/appointment/appointment_search.html?dogName=" + $("#name").val());
	});
	
	// Set the submit configuration for the form
	$( "#dogEditForm" ).submit(function(event) {
		
		/* TODO: THIS PART IS EQUAL TO THE ONE IN dog_register.js
		 * WITH EXCEPTION OF THE LATTER PART
		 * WE SHOULD CONSIDER REFACTORING IT.
		 */
		event.preventDefault();
		
		// Form validation
		if ( validateStringField( $( "#name" )[0] ) == 0 )
			showAlert($( "#errorName" ), "Nome do cachorro deve ser informado.");
		else if ( validateStringField( $( "#name" )[0] ) == -1 ) 
			showAlert($( "#errorName" ), "Nome inválido.");
		else if ( validateDateField( $( "#birthDate" )[0] ) == -1 ) 
			showAlert($( "#errorBirthDate" ), "Data inválida.");
		else if ( validateRealNumberField( $( "#weight" )[0] ) == -1 ) 
			showAlert($( "#errorWeight" ), "Peso inválido.");
		else if ( validateStringField( $( "#furColor" )[0] ) == -1 ) 
			showAlert($( "#errorFurColor" ), "Cor de pelo inválida.");
		else if ( validateDateField( $( "#arrivalDate" )[0] ) == -1 ) 
			showAlert($( "#errorArrivalDate" ), "Data de chegada inválida.");
		else if ( validateDateField( $( "#arrivalDate" )[0] ) == 0 ) 
			showAlert($( "#errorArrivalDate" ), "Preencha a data de chegada.");
		else if ( validateStringField( $( "#rationCustomDescription" )[0] ) == -1 ) 
			showAlert($( "#errorRationCustomDescription" ), "Tipo de ração inválido.");
		else if ( validateDateField( $( "#castrationDate" )[0] ) == -1 ) 
			showAlert($( "#errorCastrationDate" ), "Data de chegada inválida.");
		else if ( validateStringField( $( "#diseaseDescription" )[0] ) == -1 ) 
			showAlert($( "#errorDiseaseDescription" ), "Descrição inválida.");
		else if ( validateStringField( $( "#sponsors" )[0] ) == -1 ) 
			showAlert($( "#errorSponsors" ), "Nome do(s) padrinho(s) inválido.");
		else {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Store the id so we know which dog to edit
			// PS.: THIS IS ALSO UNIQUE
			jsonData["id"] = parseInt(getUrlParameter('id'));
			
			// Fix the checkbox values within the json
			if ( $(" #hasDiseases ").prop("checked") )
				jsonData["hasDiseases"] = true;
			else jsonData["hasDiseases"] = false;
			
			if ( $(" #castrated ").prop("checked") )
				jsonData["castrated"] = true;
			else jsonData["castrated"] = false;
			
			// Save photo
			var image_path = document.getElementById("image-input").files[0].name;
			if (image_path) {
				$.ajax({
					url: "/dog/photo_to_array",
					type: "POST",
					data: image_path,
					contentType: "application/json; charset=UTF-8",
					success: function(response) {
						jsonData["photo"] = response;
					}
				});
			}
			
			// Fix JSON so it's in the right format
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "/dog/update",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					console.log(response);
				},
				success: function() {
					/* UNIQUE PART */
					// Reset the form to previous state
					$(" #name ").prop('disabled', true);
					$(" #birthDate ").prop('disabled', true);
					$(" #weight ").prop('disabled', true);
					$(" #sex ").prop('disabled', true);
					$(" #size ").prop('disabled', true);
					$(" #furColor ").prop('disabled', true);
					$(" #status ").prop('disabled', true);
					$(" #arrivalDate ").prop('disabled', true);
					$(" #ration ").prop('disabled', true);
					$(" #rationAmount ").prop('disabled', true);
					$(" #rationCustomDescription ").prop('disabled', true);
					$(" #castrated ").prop('disabled', true);
					$(" #hasDiseases ").prop('disabled', true);
					$(" #sponsors ").prop('disabled', true);
					$(" #castrationDate ").prop('disabled', true);
					$(" #diseaseDescription ").prop('disabled', true);
					
					$(" #editBtn ").prop('disabled', false);
					$(" #appointmentBtn ").prop('disabled', false);
					$(" #vaccinationBtn ").prop('disabled', false);
					$(" #vermifugeBtn ").prop('disabled', false);
					$(" #saveBtn ").prop('disabled', true);
					$(" #deleteBtn ").prop('disabled', false);
				}
			});
		}
	});
	
	/**************/
	/* INIT STATE */
	/**************/
	
	// Load the data using dog id (specified in URL)
	var dogId = getUrlParameter('id');
	
	// if the id isn't specified, set it to 1 by default
	if (dogId == 0)
		dogId = "1";
	
	$.ajax({
		url: "/dog/view",
		type: "POST",
		data: dogId,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			jsonToForm(response);
		},
		error: function(response) {
			alert(response.responseText);
			$(" #editBtn ").prop("disabled", true);
			$(" #vaccinationBtn ").prop("disabled", true);
			$(" #vermifugeBtn ").prop("disabled", true);
			$(" #appointmentBtn ").prop("disabled", true);
			$(" #deleteBtn ").prop("disabled", true);
		}
	});
});