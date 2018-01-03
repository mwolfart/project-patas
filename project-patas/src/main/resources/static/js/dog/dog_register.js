
// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#dogRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		// Form validation
		if ( validateStringField( $( "#name" )[0] ) == 0 )
			showAlert($( "#errorName" ), "Nome do cachorro deve ser informado.");
		else if ( validateStringField( $( "#name" )[0] ) == -1 ) 
			showAlert($( "#errorName" ), "Nome inv�lido.");
		else if ( validateDateField( $( "#birthDate" )[0] ) == -1 ) 
			showAlert($( "#errorBirthDate" ), "Data inv�lida.");
		else if ( validateRealNumberField( $( "#weight" )[0] ) == -1 ) 
			showAlert($( "#errorWeight" ), "Peso inv�lido.");
		else if ( validateStringField( $( "#furColor" )[0] ) == -1 ) 
			showAlert($( "#errorFurColor" ), "Cor de pelo inv�lida.");
		else if ( validateDateField( $( "#arrivalDate" )[0] ) == -1 ) 
			showAlert($( "#errorArrivalDate" ), "Data de chegada inv�lida.");
		else if ( validateDateField( $( "#arrivalDate" )[0] ) == 0 ) 
			showAlert($( "#errorArrivalDate" ), "Preencha a data de chegada.");
		else if ( validateStringField( $( "#rationOther" )[0] ) == -1 ) 
			showAlert($( "#errorRation" ), "Tipo de ra��o inv�lido.");
		else if ( validateDateField( $( "#castrationDate" )[0] ) == -1 ) 
			showAlert($( "#errorCastrationDate" ), "Data de chegada inv�lida.");
		else if ( validateStringField( $( "#diseaseDescription" )[0] ) == -1 ) 
			showAlert($( "#errorDisease" ), "Descri��o inv�lida.");
		else if ( validateStringField( $( "#sponsors" )[0] ) == -1 ) 
			showAlert($( "#errorSponsors" ), "Nome do(s) padrinho(s) inv�lido.");
		else {
			// Convert form to json
			var jsonData = formToJson(this);
			
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
				url: "/dog/register",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Cachorro cadastrado com sucesso!");
					window.location.replace("/dog/dog_view.html?id=" + response);
				},
				error: function(response) {
					alert(response);
					console.log(response);
				}
			});
		}
	});
});
