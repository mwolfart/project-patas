$(document).ready(function() {
	$( "#vermRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateStringField( $( "#dogId" )[0] ) == 0 )
			showAlert($( "#errorDogId" ), "Nome do cachorro deve ser informado.");
		else if ( validateStringField( $( "#vermifugeName" )[0] ) == 0 )
			showAlert($( "#errorVermifugeName" ), "Nome do vermifugo deve ser informado.");
		else if ( validateDateField( $( "#applicationDate" )[0] ) == 0 )
			showAlert($( "#errorApplicationDate" ), "Data deve ser informada.");
		else if ( validateNatNumberField( $( "#dosage" )[0] ) == 0 )
			showAlert($( "#errorDosage" ), "Dosagem deve ser informada.");
		else if ( validateStringField( $( "#vermifugeName" )[0] ) == -1 )
			showAlert($( "#errorVermifugeName" ), "Nome inválido.");
		else if ( validateDateField( $( "#applicationDate" )[0] ) == -1 )
			showAlert($( "#errorApplicationDate" ), "Data inválida.");
		else if ( validateNatNumberField( $( "#dosage" )[0] ) == -1 )
			showAlert($( "#errorDosage" ), "Dosagem inválida.");
		else if ( validateDateField( $( "#nextApplicationDate" )[0] ) == -1 )
			showAlert($( "#errorNextApplicationDate" ), "Data inválida.");
		else if ( validateStringField( $( "#obs" )[0] ) == -1 )
			showAlert($( "#errorObs" ), "Observações contém caracteres inválidos.");
		else {
			// Convert form to json and fix its format
			var jsonData = formToJson(this);	
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "/vermifuge/register",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro cadastrado com sucesso!");
					window.location.replace("/vermifuge/vermifuge_view.html?id=" + response);
				},
				error: function(response) {
					alert(response.responseText);
				}
			});
		}
		
	});
});