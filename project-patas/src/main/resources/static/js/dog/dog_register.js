
// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#dogRegisterForm" ).submit(function(event) {
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
		else if ( validateStringField( $( "#ration" )[0] ) == -1 ) 
			showAlert($( "#errorRation" ), "Tipo de ração inválido.");
		else if ( validateDateField( $( "#castrationDate" )[0] ) == -1 ) 
			showAlert($( "#errorCastrationDate" ), "Data de castração inválida.");
		else if ( validateStringField( $( "#diseaseDescription" )[0] ) == -1 ) 
			showAlert($( "#errorDiseaseDescription" ), "Descrição inválida.");
		else if ( validateStringField( $( "#sponsors" )[0] ) == -1 ) 
			showAlert($( "#errorSponsors" ), "Nome do(s) padrinho(s) inválido.");
		else {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Fix the checkbox values within the json
			if ( $(" #hasDiseases ").prop("checked") )
				jsonData["hasDiseases"] = true;
			else jsonData["hasDiseases"] = false;
			
			if ( $(" #castrated ").prop("checked") )
				jsonData["castrated"] = true;
			else jsonData["castrated"] = false;
			
			// Save photo
			var photo_as_bytes = [];
			storeImage(document.getElementById("image-input"), function(photo_as_bytes) {
				jsonData["photo"] = photo_as_bytes;
			
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
						alert(response.responseText);
					}
				});
			});
		}
	});
});
