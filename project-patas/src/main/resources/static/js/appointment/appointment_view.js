
// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	$(" #dogId ").val(json.dogId);
	$(" #responsibleName ").val(json.responsibleName);
	$(" #location ").val(json.location);
	$(" #vetName ").val(json.vetName);
	$(" #reason ").val(json.reason);
	$(" #examDescription ").val(json.examDescription);
	$(" #appointmentDescription ").val(json.appointmentDescription);

	if (json.totalCost)
		$(" #totalCost ").val((json.totalCost).toFixed(2));
	
	if (json.appointmentDate) {
		var appointment_date = new Date();
		appointment_date.setTime(json.appointmentDate);
		$(" #appointmentDate ").val(dateToString(appointment_date));
	}
	
	if (json.examinated == true)
		$(" #examinated ").prop('checked', true);
}

// Document load script
$(document).ready(function() {
	
	/****************/
	/** EDIT STATE **/
	/****************/
	
	// Edit button onClick handler
	$(" #editBtn ").click(function() {		
		$(" #dogId ").prop('disabled', false);
		$(" #responsibleName ").prop('disabled', false);	
		$(" #location ").prop('disabled', false);
		$(" #appointmentDate ").prop('disabled', false);
		$(" #vetName ").prop('disabled', false);
		$(" #totalCost ").prop('disabled', false);
		$(" #reason ").prop('disabled', false);
		$(" #examinated ").prop('disabled', false);
		$(" #appointmentDescription ").prop('disabled', false);
		
		if ( $(" #examinated ").prop("checked") )
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
				url: "/appointment/delete",
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
		if ( validateStringField( $( "#dogId" )[0] ) == 0 )
			showAlert($( "#errorDogId" ), "Nome do cachorro deve ser informado.");
		else if ( validateStringField( $( "#responsibleName" )[0] ) == -1 )
			showAlert($( "#errorResponsibleName" ), "Nome inválido.");
		else if ( validateDateField( $( "#appointmentDate" )[0] ) == 0 ) 
			showAlert($( "#errorAppointmentDate" ), "Data deve ser informada.");
		else if ( validateDateField( $( "#appointmentDate" )[0] ) == -1 ) 
			showAlert($( "#errorAppointmentDate" ), "Data inválida.");
		else if ( validateStringField( $( "#location" )[0] ) == -1 )
			showAlert($( "#errorLocation" ), "Nome inválido.");
		else if ( validateStringField( $( "#vetName" )[0] ) == -1 )
			showAlert($( "#errorVetName" ), "Nome inválido.");
		else if ( validateCurrencyField( $( "#totalCost" )[0] ) == -1 )
			showAlert($( "#errorTotalCost" ), "Preço deve ser informado.");
		else if ( validateStringField( $( "#reason" )[0] ) == 0 )
			showAlert($( "#errorReason" ), "O motivo deve ser informado.");
		else if ( validateStringField( $( "#reason" )[0] ) == -1 )
			showAlert($( "#errorReason" ), "Descrição inválida.");
		else if ( validateStringField( $( "#examDescription" )[0] ) == -1 )
			showAlert($( "#errorExamDescription" ), "Descrição inválida.");
		else if ( validateStringField( $( "#appointmentDescription" )[0] ) == -1 )
			showAlert($( "#errorAppointmentDescription" ), "Descrição inválida.");
		else {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Store id
			jsonData["id"] = parseInt(getUrlParameter('id'));
		
			// Fix the checkbox values within the json
			if ( $(" #examinated ").prop("checked") )
				jsonData["examinated"] = true;
			else jsonData["examinated"] = false;
			
			// Fix JSON so it's in the right format
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "/appointment/register",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					console.log(response);
				},
				success: function() {
					/* UNIQUE PART */
					// Reset the form to previous state
					$(" #dogId ").prop('disabled', true);
					$(" #responsibleName ").prop('disabled', true);	
					$(" #location ").prop('disabled', true);
					$(" #appointmentDate ").prop('disabled', true);
					$(" #vetName ").prop('disabled', true);
					$(" #totalCost ").prop('disabled', true);
					$(" #reason ").prop('disabled', true);
					$(" #examinated ").prop('disabled', true);
					$(" #appointmentDescription ").prop('disabled', true);
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
		url: "/appointment/view",
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