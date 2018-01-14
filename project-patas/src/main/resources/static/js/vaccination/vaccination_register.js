// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#vacRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if (validateForm()) {
			// Convert form to json and fix
			var jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
	
			// Post the data
			$.ajax({
				url: "/vaccination/register",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro cadastrado com sucesso!");
					window.location.replace("/vaccination/vaccination_view.html?id=" + response);
				},
				error: function(response) {
					alert(response.responseText);
				}
			});
		}
	});
	
	putDogsInComboBox();
});
