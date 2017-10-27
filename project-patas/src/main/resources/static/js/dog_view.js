
// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	var birthDate = new Date();
	var arrivalDate = new Date();
	birthDate.setTime(json.birthDate);
	arrivalDate.setTime(json.arrivalDate);
	
	$(" #name ").val(json.name);
	$(" #birthDate ").val(dateToString(birthDate));
	$(" #age ").val(computeAge(birthDate));
	$(" #weight ").val(json.weight);
	$(" #sex ").val(json.sex);
	$(" #size ").val(json.size);
	$(" #furColor ").val(json.furColor);
	$(" #arrivalDate ").val(dateToString(arrivalDate));
	$(" #sponsors ").val(json.sponsors);
	
	if (json.castrated == true) {
		$(" #checkboxCastr ").prop('checked', true);

		if (json.castrationDate) {
			var castrationDate = new Date();
			castrationDate.setTime(json.castrationDate);
			$(" #castrationDate ").val(dateToString(castrationDate));
		}
	}
	
	if (json.disease == true) {
		$(" #checkboxDis ").prop('checked', true);
		(json.diseaseDescription ? $(" #diseaseDescription ").val(json.diseaseDescription) : false );
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
		$(" #checkboxCastr ").prop('disabled', false);
		$(" #checkboxDis ").prop('disabled', false);
		$(" #sponsors ").prop('disabled', false);
		
		if( $(" #checkboxCastr ").prop("checked") )
			$(" #castrationDate ").prop('disabled', false);
		
		if( $(" #checkboxDis ").prop("checked") )
			$(" #diseaseDescription ").prop('disabled', false);
		
		$(" #editBtn ").prop('disabled', true);
		$(" #vacinationBtn ").prop('disabled', true);
		$(" #vermifugationBtn ").prop('disabled', true);
		$(" #saveBtn ").prop('disabled', false);
	});
	
	// Set the submit configuration for the form
	$( "#editForm" ).submit(function(event) {
		
		/* TODO: THIS PART IS EQUAL TO THE ONE IN dog_register.js
		 * WITH EXCEPTION OF THE LATTER PART
		 * WE SHOULD CONSIDER REFACTORING IT.
		 */
		event.preventDefault();
		
		// Form validation
		if ( $( "#name" ).val() == "" )
			alert("Preencha o nome do cachorro!");
		else if ( $( "#birthDate" ).hasClass("error_input") )
			alert("Data de nascimento inválida!");
		else if ( $( "#weight" ).hasClass("error_input") )
			alert("Peso inválido!");
		else if ( $( "#arrivalDate" ).hasClass("error_input") )
			alert("Data de chegada inválida!");
		else if ( $( "#arrivalDate" ).val() == "" )
			alert("Preencha a data de chegada!");
		else {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Store the id so we know which dog to edit
			// PS.: THIS IS ALSO UNIQUE
			jsonData["id"] = getUrlParameter('id');
			
			// Fix the checkbox values within the json
			if ( $(" #checkboxDis ").prop("checked") )
				jsonData["disease"] = true;
			else jsonData["disease"] = false;
			
			if ( $(" #checkboxCastr ").prop("checked") )
				jsonData["castrated"] = true;
			else jsonData["castrated"] = false;
			
			// Fix JSON so it's in the right format
			jsonData = JSON.stringify(jsonData);
	
			// Post the data
			$.ajax({
				url: "http://localhost:8080/dog/update",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
			});
			
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
			$(" #checkboxCastr ").prop('disabled', true);
			$(" #checkboxDis ").prop('disabled', true);
			$(" #sponsors ").prop('disabled', true);
			$(" #castrationDate ").prop('disabled', true);
			$(" #diseaseDescription ").prop('disabled', true);
			
			$(" #editBtn ").prop('disabled', false);
			$(" #vacinationBtn ").prop('disabled', false);
			$(" #vermifugationBtn ").prop('disabled', false);
			$(" #saveBtn ").prop('disabled', true);
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
		url: "http://localhost:8080/dog/view",
		type: "POST",
		data: dogId,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			jsonToForm(response);
		},
		error: function(response) {
			alert(response.responseText);
			$(" #editBtn ").prop("disabled", true);
			$(" #vacinationBtn ").prop("disabled", true);
			$(" #vermifugationBtn ").prop("disabled", true);
		}
	});		
});