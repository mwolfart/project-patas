// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#appointmentRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		// Form validation
		if ( validateStringField( $( "#dogName" )[0] ) == 0 )
			showAlert($( "#errorDogName" ), "Nome do cachorro deve ser informado.");
		else if ( validateStringField( $( "#volName" )[0] ) == -1 )
			showAlert($( "#errorVolName" ), "Nome inv�lido.");
		else if ( validateDateField( $( "#appointmentDate" )[0] ) == 0 ) 
			showAlert($( "#errorAppointmentDate" ), "Data deve ser informada.");
		else if ( validateDateField( $( "#appointmentDate" )[0] ) == -1 ) 
			showAlert($( "#errorAppointmentDate" ), "Data inv�lida.");
		else if ( validateStringField( $( "#location" )[0] ) == -1 )
			showAlert($( "#errorLocation" ), "Nome inv�lido.");
		else if ( validateStringField( $( "#vetName" )[0] ) == -1 )
			showAlert($( "#errorVetName" ), "Nome inv�lido.");
		else if ( validateCurrencyField( $( "#totalCost" )[0] ) == -1 )
			showAlert($( "#errorTotalCost" ), "Pre�o deve ser informado.");
		else if ( validateStringField( $( "#reason" )[0] ) == 0 )
			showAlert($( "#errorReason" ), "O motivo deve ser informado.");
		else if ( validateStringField( $( "#reason" )[0] ) == -1 )
			showAlert($( "#errorReason" ), "Descri��o inv�lida.");
		else if ( validateStringField( $( "#examDescription" )[0] ) == -1 )
			showAlert($( "#errorExamDescription" ), "Descri��o inv�lida.");
		else if ( validateStringField( $( "#description" )[0] ) == -1 )
			showAlert($( "#errorDescription" ), "Descri��o inv�lida.");
		else {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Fix the checkbox values within the json
			if ( $(" #checkboxExam ").prop("checked") )
				jsonData["exam"] = true;
			else jsonData["exam"] = false;
		
			// Fix JSON so it's in the right format
			jsonData = JSON.stringify(jsonData);
	
			// Post the data
			$.ajax({
				url: "/appointment/register",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro cadastrado com sucesso!");
					window.location.replace("/appointment/appointment_view.html?id=" + response);
				},
				error: function(response) {
					console.log(response);
				}
			});
		}
	});
});
