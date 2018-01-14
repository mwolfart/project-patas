
// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	$(" #name ").val(json.name);
	$(" #weight ").val(json.weight);
	$(" #sex ").val(json.sex);
	$(" #size ").val(json.size);
	$(" #furColor ").val(json.furColor);
	$(" #status ").val(json.status);
	putDateInField($(" #arrivalDate "), json.arrivalDate);
	$(" #ration ").val(json.ration);
	$(" #rationAmount ").val(json.rationAmount);
	$(" #rationMeasurement ").val(json.rationMeasurement);
	$(" #sponsors ").val(json.sponsors);
	
	if (json.birthDate) {
		var bday = new Date();
		bday.setTime(json.birthDate);
		$(" #birthDate ").val(dateToString(bday));
		$(" #age ").val(computeAge(bday));
	}
	
	if(json.photo) {
		var image = "data:image/png;base64," + json.photo;
		$(" #dogPhoto ").attr("src", image);
		document.getElementById("dogPhoto").width = 200;
		document.getElementById("dogPhoto").height = 200;
	}
	
	if (json.castrated == true) {
		$(" #castrated ").prop('checked', true);
		putDateInField($(" #castrationDate "), json.castrationDate);
	}
	
	if (json.hasDiseases == true) {
		$(" #hasDiseases ").prop('checked', true);
		(json.diseaseDescription ? $(" #diseaseDescription ").val(json.diseaseDescription) : false );
	}
	
	if (json.ration == "OUTRO") {
		(json.rationCustomDescription ? $(" #rationCustomDescription ").val(json.rationCustomDescription) : false );
	}
	
	if (json.photo) {
		$.ajax({
			url: "/dog/array_to_photo",
			type: "POST",
			data: json.photo,
			contentType: "application/json; charset=UTF-8",
			success: function(response) {
				$(" #image-input ").val(response);
			}
		});
	}
}

// Document load script
$(document).ready(function() {
	
	/**************/
	/* EDIT STATE */
	/**************/
	
	// Edit button onClick handler
	$(" #editBtn ").click(function() {	
		$(" #checker ").prop('disabled', false);
		$(" #name ").prop('disabled', false);
		$(" #birthDate ").prop('disabled', false);
		$(" #weight ").prop('disabled', false);
		$(" #sex ").prop('disabled', false);
		$(" #size ").prop('disabled', false);
		$(" #furColor ").prop('disabled', false);
		$(" #status ").prop('disabled', false);
		$(" #arrivalDate ").prop('disabled', false);
		$(" #ration ").prop('disabled', false);
		$(" #rationAmount ").prop('disabled', false);
		$(" #rationMeasurement ").prop('disabled', false);
		$(" #castrated ").prop('disabled', false);
		$(" #hasDiseases ").prop('disabled', false);
		$(" #sponsors ").prop('disabled', false);
		
		if ( $(" #castrated ").prop("checked") )
			$(" #castrationDate ").prop('disabled', false);
		
		if ( $(" #hasDiseases ").prop("checked") )
			$(" #diseaseDescription ").prop('disabled', false);
		
		if ( $(" #ration ").value == "OUTRO" )
			$(" #rationCustomDescription ").prop('disabled', false);
		
		$(" #editBtn ").prop('disabled', true);
		$(" #appointmentBtn ").prop('disabled', false);
		$(" #vaccinationBtn ").prop('disabled', true);
		$(" #vermifugeBtn ").prop('disabled', true);
		$(" #saveBtn ").prop('disabled', false);
		$(" #deleteBtn ").prop('disabled', true);
	});
	
	// Vermifuge button onClick handler
	$(" #vermifugeBtn ").click(function() {
		window.location.replace("/vermifuge/vermifuge_search.html?dogId=" + getUrlParameter('id'));
	});
	
	// Vaccination button onClick handler
	$(" #vaccinationBtn ").click(function() {
		window.location.replace("/vaccination/vaccination_search.html?dogId=" + getUrlParameter('id'));
	});
	
	// Appointment button onClick handler
	$(" #appointmentBtn ").click(function() {
		window.location.replace("/appointment/appointment_search.html?dogId=" + getUrlParameter('id'));
	});
	
	// Delete button onClick handler
	$(" #deleteBtn ").click(function(event) {
		event.preventDefault();
		
		if (confirm("Tem certeza que deseja excluir este cachorro? Todos os registros relacionados à ele (vacinações, vermifugações, consultas) serão também excluídos.")) {
			var dog_id = getIdFromURLasString();
			
			$.ajax({
				url: "/vaccination/delete_by_dog",
				type: "POST",
				data: dog_id,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					alert(response.responseText);
				}
			});
			
			$.ajax({
				url: "/vermifuge/delete_by_dog",
				type: "POST",
				data: dog_id,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					alert(response.responseText);
				}
			});
			
			$.ajax({
				url: "/appointment/delete_by_dog",
				type: "POST",
				data: dog_id,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					alert(response.responseText);
				}
			});

			$.ajax({
				url: "/dog/delete",
				type: "POST",
				data: dog_id,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro removido com sucesso!");
					window.location.replace("/dog/dog_search.html");
				},
				error: function(response) {
					alert(response.responseText);
				}
			});
		}
	});
	
	// Set the submit configuration for the form
	$( "#dogEditForm" ).submit(function(event) {
		event.preventDefault();
		
		// Form validation
		if (validateForm()) {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Store the id so we know which dog to edit
			jsonData["id"] = parseInt(getIdFromURLasString());
			
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
					url: "/dog/update",
					type: "POST",
					data: jsonData,
					contentType: "application/json; charset=UTF-8",
					error: function(response) {
						console.log(response);
					},
					success: function() {
						// Reset the form to previous state
						$(" #name ").prop('disabled', true);
						$(" #checker ").prop('disabled', true);
						$(" #birthDate ").prop('disabled', true);
						$(" #weight ").prop('disabled', true);
						$(" #sex ").prop('disabled', true);
						$(" #size ").prop('disabled', true);
						$(" #furColor ").prop('disabled', true);
						$(" #status ").prop('disabled', true);
						$(" #arrivalDate ").prop('disabled', true);
						$(" #ration ").prop('disabled', true);
						$(" #rationAmount ").prop('disabled', true);
						$(" #rationCustomDescription ").prop('disabled', true);
						$(" #castrated ").prop('disabled', true);
						$(" #hasDiseases ").prop('disabled', true);
						$(" #sponsors ").prop('disabled', true);
						$(" #castrationDate ").prop('disabled', true);
						$(" #diseaseDescription ").prop('disabled', true);
						
						$(" #editBtn ").prop('disabled', false);
						$(" #appointmentBtn ").prop('disabled', false);
						$(" #vaccinationBtn ").prop('disabled', false);
						$(" #vermifugeBtn ").prop('disabled', false);
						$(" #saveBtn ").prop('disabled', true);
						$(" #deleteBtn ").prop('disabled', false);
					}
				});
			});
		}
	});
	
	/**************/
	/* INIT STATE */
	/**************/
	
	// Load the data using dog id (specified in URL)
	var dogId = getIdFromURLasString();
	
	$.ajax({
		url: "/dog/view",
		type: "POST",
		data: dogId,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			jsonToForm(response);
		},
		error: function(response) {
			alert(response.responseText);
			$(" #editBtn ").prop("disabled", true);
			$(" #vaccinationBtn ").prop("disabled", true);
			$(" #vermifugeBtn ").prop("disabled", true);
			$(" #appointmentBtn ").prop("disabled", true);
			$(" #deleteBtn ").prop("disabled", true);
		}
	});
});