$(document).ready(function() {
	$( "#vermRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateStringField( $( "#dogName" )[0] ) == 0 )
			showAlert($( "#errorDogName" ), "Preencha o nome do cachorro");
		else if ( validateStringField( $( "#vermName" )[0] ) == 0 )
			showAlert($( "#errorVermName" ), "Preencha o nome do vermífugo");
		else if ( validateStringField( $( "#vermName" )[0] ) == -1 )
			showAlert($( "#errorVermName" ), "Nome de vermífugo contém caracteres inválidos");
		else if ( validateNumericField( $( "#amount" )[0] ) == -1 )
			showAlert($( "#errorAmount" ), "Dosagem inválida");
		else if ( validateDateField( $( "#appDate" )[0] ) == -1 )
			showAlert($( "#errorAppDate" ), "Data inválida");
		else if ( validateDateField( $( "#nextAppDate" )[0] ) == -1 )
			showAlert($( "#errorNextAppDate" ), "Data inválida");
		else if ( validateStringField( $( "#obs" )[0] ) == -1 )
			showAlert($( "#errorObs" ), "Observações contém caracteres inválidos!");
		else {
			// Convert form to json and fix its format
			var jsonData = formToJson(this);	
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "http://localhost:8080/verm/register",
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