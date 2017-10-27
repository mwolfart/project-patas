
// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#dogRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		// Form validation
		if ( $( "#name" ).val() == "" ){
			showAlert($( "#errorName" ), "Preencha o nome do cachorro!");
			//alert("Preencha o nome do cachorro!");
		}else if ( $( "#birthDate" ).hasClass("error_input") ){
			showAlert($( "#errorBirthDate" ), "Data de nascimento inválida!");
			//alert("Data de nascimento inv�lida!");
		}else if ( $( "#weight" ).hasClass("error_input") ){
			showAlert($( "#errorWeight" ), "Peso inválido!");
			//alert("Peso inv�lido!");
		}else if ( $( "#arrivalDate" ).hasClass("error_input") ){
			showAlert($( "#errorArrivalDate" ), "Data de chegada inválida!");
			//alert("Data de chegada inv�lida!");
		}else if ( $( "#arrivalDate" ).val() == "" ){
			showAlert($( "#errorArrivalDate" ), "Preencha a data de chegada!");
			//alert("Preencha a data de chegada!");
		}else {
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
				url: "http://localhost:8080/dog/register",
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
