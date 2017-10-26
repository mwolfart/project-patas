// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields	
	$( "#birthYear" ).keydown(protectNumericField);
	$( "#arrivalYear" ).keydown(protectNumericField);
	
	$( "#birthYear" ).focusout( function() { validateNatNumberField(this) });
	$( "#arrivalYear" ).focusout( function() { validateNatNumberField(this) });

	// Set the submit configuration for the form
	$( "#dogSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( $("#birthYear").hasClass("error_input") || 
				$("#arrivalYear").hasClass("error_input") )
			alert("Há erros nos dados preenchidos!");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			console.log(jsonData);
			
			$.ajax({
				url: "http://localhost:8080/dog/search",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8"
			});			
		}
	});
	
});
