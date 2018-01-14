// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields	
	$( "#name" ).keydown(protectSearchStringField);
	$( "#arrivalYear" ).keydown(protectNumericField);
	
	$( "#name" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorName"));
	});
	
	$( "#arrivalYear" ).focusout( function() { 
		validateNatNumberField(this);
		hideAlert($("#errorArrivalYear"));
	});

	// Set the submit configuration for the form
	$( "#dogSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateSearchStringField( $("#name")[0] ) == -1 )
			showAlert($( "#errorName" ), "Nome inválido.");
		else if ( validateNatNumberField( $("#arrivalYear")[0] ) == -1 )
			showAlert($( "#errorArrivalYear" ), "Ano de chegada inválido.");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			$.ajax({
				url: "/dog/search",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					arrayToTable($('#dogs'), "dog", response);
				}
			});
		}
	});	
});
