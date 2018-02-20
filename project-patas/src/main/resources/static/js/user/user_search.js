// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields	
	$( "#username" ).keydown(protectUserField);
	
	$( "#username" ).focusout( function() { 
		validateUserField(this);
		hideAlert($("#errorUsername"));
	});
	
	// Set the submit configuration for the form
	$( "#userSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateSearchStringField( $("#username")[0] ) == -1 )
			showAlert($( "#errorUsername" ), "Nome de usuário inválido.");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			$.ajax({
				url: "/user/search",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					arrayToTable($('#users'), "user", response, "username");
				}
			});
		}
	});
});
