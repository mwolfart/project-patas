// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#vacRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateStringField( $( "#dogName" )[0] ) == 0 )
			showAlert($( "#errorDogName" ), "Campo obrigatório!");
		else if ( validateStringField( $( "#vacName" )[0] ) == 0 )
			showAlert($( "#errorVacName" ), "Campo obrigatório!");
		else if ( validateDateField( $( "#appDate" )[0] ) == 0 )
			showAlert($( "#errorAppDate" ), "Campo obrigatório!");
		else if ( validateStringField( $( "#vacName" )[0] ) == -1 )
			showAlert($( "#errorVacName" ), "Nome inválido!");
		else if ( validateDateField( $( "#appDate" )[0] ) == -1 )
			showAlert($( "#errorAppDate" ), "Data inválida!");
		else if ( validateDateField( $( "#nextAppDate" )[0] ) == -1 )
			showAlert($( "#errorNextAppDate" ), "Data inválida");
		else if ( validateStringField( $( "#obs" )[0] ) == -1 )
			showAlert($( "#errorObs" ), "Observações contém caracteres inválidos!");
		else {
			// Convert form to json and fix
			var jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
	
			// Post the data
			$.ajax({
				url: "http://localhost:8080/vacination/register",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					alert(response);
				}
			});
		}
	});
});
