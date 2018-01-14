$(document).ready(function() {
	$( "#vermRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if (validateForm()) {
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
					alert(response.responseText);
				}
			});
		}
		
	});
	
	putDogsInComboBox();
});