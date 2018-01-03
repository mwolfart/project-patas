$(document).ready(function() {
	$( "#vermRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateStringField( $( "#dogName" )[0] ) == 0 )
			showAlert($( "#errorDogName" ), "Nome do cachorro deve ser informado.");
		else if ( validateStringField( $( "#vermName" )[0] ) == 0 )
			showAlert($( "#errorVermName" ), "Nome do vermifugo deve ser informado.");
		else if ( validateDateField( $( "#appDate" )[0] ) == 0 )
			showAlert($( "#errorAppDate" ), "Data deve ser informada.");
		else if ( validateNatNumberField( $( "#amount" )[0] ) == 0 )
			showAlert($( "#errorAmount" ), "Dosagem deve ser informada.");
		else if ( validateStringField( $( "#vermName" )[0] ) == -1 )
			showAlert($( "#errorVermName" ), "Nome inválido.");
		else if ( validateDateField( $( "#appDate" )[0] ) == -1 )
			showAlert($( "#errorAppDate" ), "Data inválida.");
		else if ( validateNatNumberField( $( "#amount" )[0] ) == -1 )
			showAlert($( "#errorAmount" ), "Dosagem inválida.");
		else if ( validateDateField( $( "#nextAppDate" )[0] ) == -1 )
			showAlert($( "#errorNextAppDate" ), "Data inválida.");
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
					console.log(response);
				}
			});
		}
		
	});
});