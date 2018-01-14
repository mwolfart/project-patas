
// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#dogRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		// Form validation
		if (validateForm()) {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Fix the checkbox values within the json
			$(" #hasDiseases ").prop("checked") ? jsonData["hasDiseases"] = true : jsonData["hasDiseases"] = false;
			$(" #castrated ").prop("checked") ? jsonData["castrated"] = true : jsonData["castrated"] = false;
			
			// Save photo
			var photo_as_bytes = [];
			storeImage(document.getElementById("image-input"), function(photo_as_bytes) {
				jsonData["photo"] = photo_as_bytes;
			
				// Fix JSON so it's in the right format
				jsonData = JSON.stringify(jsonData);
	
				// Post the data
				$.ajax({
					url: "/dog/register",
					type: "POST",
					dataType: "json",
					data: jsonData,
					contentType: "application/json; charset=UTF-8",
					success: function(response) {
						alert("Cachorro cadastrado com sucesso!");
						window.location.replace("/dog/dog_view.html?id=" + response);
					},
					error: function(response) {
						alert(response.responseText);
					}
				});
			});
		}
	});
});
