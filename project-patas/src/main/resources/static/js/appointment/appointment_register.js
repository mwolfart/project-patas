// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#appointmentRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		// Form validation
		if (validateForm()) {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Fix the checkbox values within the json
			$(" #examinated ").prop("checked") ? jsonData["examinated"] = true : jsonData["examinated"] = false;
		
			// Fix JSON so it's in the right format
			jsonData = JSON.stringify(jsonData);
	
			// Post the data
			$.ajax({
				url: "/appointment/register",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro cadastrado com sucesso!");
					window.location.replace("/appointment/appointment_view.html?id=" + response);
				},
				error: function(response) {
					console.log(response.responseText);
				}
			});
		}
	});
	
	putDogsInComboBox();
});
