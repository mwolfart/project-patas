$(document).ready(function() {
	$( "#vermRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateStringField( $( "#dogName" )[0] ) == 0 )
			showAlert($( "#errorDogName" ), "Campo obrigat�rio!");
		else if ( validateStringField( $( "#vermName" )[0] ) == 0 )
			showAlert($( "#errorVermName" ), "Campo obrigat�rio!");
		else if ( validateDateField( $( "#appDate" )[0] ) == 0 )
			showAlert($( "#errorAppDate" ), "Campo obrigat�rio!");
		else if ( validateNumericField( $( "#amount" )[0] ) == 0 )
			showAlert($( "#errorAmount" ), "Campo obrigat�rio!");
		else if ( validateStringField( $( "#vermName" )[0] ) == -1 )
			showAlert($( "#errorVermName" ), "Nome inv�lido!");
		else if ( validateDateField( $( "#appDate" )[0] ) == -1 )
			showAlert($( "#errorAppDate" ), "Data inv�lida!");
		else if ( validateNumericField( $( "#amount" )[0] ) == -1 )
			showAlert($( "#errorAmount" ), "Dosagem inv�lida");
		else if ( validateDateField( $( "#nextAppDate" )[0] ) == -1 )
			showAlert($( "#errorNextAppDate" ), "Data inv�lida");
		else if ( validateStringField( $( "#obs" )[0] ) == -1 )
			showAlert($( "#errorObs" ), "Observa��es cont�m caracteres inv�lidos!");
		else {
			// Convert form to json and fix its format
			var jsonData = formToJson(this);	
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "http://localhost:8080/vermifuge/register",
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