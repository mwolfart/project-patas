// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#appointmentRegisterForm" ).submit(function(event) {
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
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro cadastrado com sucesso!");
					window.location.replace("/appointment/appointment_view.html?id=" + response);
				},
				error: function(response) {
					console.log(response.responseText);
				}
			});
		}
	});
});
