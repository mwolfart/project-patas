
// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	$(" #dogName ").val(json.dogId);
	$(" #volName ").val(json.responsibleName);
	$(" #location ").val(json.location);
	$(" #vetName ").val(json.vetName);
	$(" #totalCost ").val((json.price).toFixed(2));
	$(" #reason ").val(json.reason);
	$(" #examDescription ").val(json.examDescription);
	$(" #description ").val(json.description);
	
	if (json.appointmentDate) {
		var appointment_date = new Date();
		appointment_date.setTime(json.appointmentDate);
		$(" #appointmentDate ").val(dateToString(appointment_date));
	}
	
	if (json.exam == true)
		$(" #checkboxExam ").prop('checked', true);
}

// Document load script
$(document).ready(function() {
	
	/****************/
	/** EDIT STATE **/
	/****************/
	
	// Edit button onClick handler
	$(" #editBtn ").click(function() {		
		$(" #dogName ").prop('disabled', false);
		$(" #volName ").prop('disabled', false);	
		$(" #location ").prop('disabled', false);
		$(" #appointmentDate ").prop('disabled', false);
		$(" #vetName ").prop('disabled', false);
		$(" #totalCost ").prop('disabled', false);
		$(" #reason ").prop('disabled', false);
		$(" #checkboxExam ").prop('disabled', false);
		$(" #description ").prop('disabled', false);
		
		if ( $(" #checkboxExam ").prop("checked") )
			$(" #examDescription ").prop('disabled', false);
		
		$(" #prescriptionsBtn ").prop('disabled', false);
		$(" #resultsBtn ").prop('disabled', false);
		$(" #receiptsBtn ").prop('disabled', false);
		
		$(" #editBtn ").prop('disabled', true);
		$(" #saveBtn ").prop('disabled', false);
		$(" #deleteBtn ").prop('disabled', true);
	});
	
	// Delete button onClick handler
	$(" #deleteBtn ").click(function(event) {
		event.preventDefault();
		
		if (confirm("Tem certeza que deseja excluir este registro?")) {
			var app_id = getUrlParameter('id');
			
			$.ajax({
				url: "http://localhost:8080/appointment/delete",
				type: "POST",
				data: app_id,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro removido com sucesso!");
					window.location.replace("/appointment/appointment_search.html");
				},
				error: function(response) {
					alert(response.responseText);
				}
			});
		}
	});
	
	// Set the submit configuration for the form
	$( "#appointmentEditForm" ).submit(function(event) {
		event.preventDefault();
		
		// Form validation
		if ( validateStringField( $( "#dogName" )[0] ) == 0 )
			showAlert($( "#errorDogName" ), "Campo obrigatório!");
		else if ( validateStringField( $( "#volName" )[0] ) == -1 )
			showAlert($( "#errorVolName" ), "Nome inválido!");
		else if ( validateDateField( $( "#appointmentDate" )[0] ) == 0 ) 
			showAlert($( "#errorAppointmentDate" ), "Campo obrigatório!");
		else if ( validateDateField( $( "#appointmentDate" )[0] ) == -1 ) 
			showAlert($( "#errorAppointmentDate" ), "Data inválida!");
		else if ( validateStringField( $( "#location" )[0] ) == -1 )
			showAlert($( "#errorLocation" ), "Nome inválido!");
		else if ( validateStringField( $( "#vetName" )[0] ) == -1 )
			showAlert($( "#errorVetName" ), "Nome inválido!");
		else if ( validateCurrencyField( $( "#totalCost" )[0] ) == -1 )
			showAlert($( "#errorTotalCost" ), "Valor inválido!");
		else if ( validateStringField( $( "#reason" )[0] ) == 0 )
			showAlert($( "#errorReason" ), "Campo obrigatório!");
		else if ( validateStringField( $( "#reason" )[0] ) == -1 )
			showAlert($( "#errorReason" ), "Descrição inválida!");
		else if ( validateStringField( $( "#examDescription" )[0] ) == -1 )
			showAlert($( "#errorExamDescription" ), "Descrição inválida!");
		else if ( validateStringField( $( "#description" )[0] ) == -1 )
			showAlert($( "#errorDescription" ), "Descrição inválida!");
		else {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Store id
			jsonData["id"] = parseInt(getUrlParameter('id'));
		
			// Fix the checkbox values within the json
			if ( $(" #checkboxExam ").prop("checked") )
				jsonData["exam"] = true;
			else jsonData["exam"] = false;
			
			// Fix JSON so it's in the right format
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "http://localhost:8080/appointment/register",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					console.log(response);
				},
				success: function() {
					/* UNIQUE PART */
					// Reset the form to previous state
					$(" #dogName ").prop('disabled', true);
					$(" #volName ").prop('disabled', true);	
					$(" #location ").prop('disabled', true);
					$(" #appointmentDate ").prop('disabled', true);
					$(" #vetName ").prop('disabled', true);
					$(" #totalCost ").prop('disabled', true);
					$(" #reason ").prop('disabled', true);
					$(" #checkboxExam ").prop('disabled', true);
					$(" #description ").prop('disabled', true);
					$(" #examDescription ").prop('disabled', true);
					
					$(" #prescriptionsBtn ").prop('disabled', true);
					$(" #resultsBtn ").prop('disabled', true);
					$(" #receiptsBtn ").prop('disabled', true);
					$(" #editBtn ").prop('disabled', false);
					$(" #saveBtn ").prop('disabled', true);
					$(" #deleteBtn ").prop('disabled', false);
				}
			});
		}
	});
	
	/****************/
	/** INIT STATE **/
	/****************/
	
	// Load the data using id (specified in URL)
	var appointment_id = getUrlParameter('id');
	
	// if the id isn't specified, set it to 1 by default
	if (appointment_id == 0)
		appointment_id = "1";
	
	$.ajax({
		url: "http://localhost:8080/appointment/view",
		type: "POST",
		data: appointment_id,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			jsonToForm(response);
		},
		error: function(response) {
			alert(response.responseText);
			$(" #editBtn ").prop("disabled", true);
			$(" #deleteBtn ").prop("disabled", true);
		}
	});
});