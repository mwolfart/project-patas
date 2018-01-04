// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#vacRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateStringField( $( "#dogId" )[0] ) == 0 )
			showAlert($( "#errorDogId" ), "Nome do cachorro deve ser informado.");
		else if ( validateStringField( $( "#vaccineName" )[0] ) == 0 )
			showAlert($( "#errorVaccineName" ), "Nome da vacina deve ser informado.");
		else if ( validateDateField( $( "#applicationDate" )[0] ) == 0 )
			showAlert($( "#errorApplicationDate" ), "Data deve ser informada.");
		else if ( validateStringField( $( "#vaccineName" )[0] ) == -1 )
			showAlert($( "#errorVaccineName" ), "Nome inválido.");
		else if ( validateDateField( $( "#applicationDate" )[0] ) == -1 )
			showAlert($( "#errorApplicationDate" ), "Data inválida.");
		else if ( validateDateField( $( "#nextApplicationDate" )[0] ) == -1 )
			showAlert($( "#errorNextApplicationDate" ), "Data inválida.");
		else if ( validateStringField( $( "#obs" )[0] ) == -1 )
			showAlert($( "#errorObs" ), "Observações contém caracteres inválidos.");
		else {
			// Convert form to json and fix
			var jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
	
			// Post the data
			$.ajax({
				url: "/vaccination/register",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro cadastrado com sucesso!");
					window.location.replace("/vaccination/vaccination_view.html?id=" + response);
				},
				error: function(response) {
					alert(response.responseText);
				}
			});
		}
	});
});
