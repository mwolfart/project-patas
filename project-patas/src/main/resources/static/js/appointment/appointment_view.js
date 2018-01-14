
// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	putDogsInComboBoxAndSet(json.dogId);
	$(" #responsibleName ").val(json.responsibleName);
	$(" #location ").val(json.location);
	$(" #vetName ").val(json.vetName);
	$(" #reason ").val(json.reason);
	$(" #examDescription ").val(json.examDescription);
	$(" #appointmentDescription ").val(json.appointmentDescription);
	putDateInField($(" #appointmentDate "), json.appointmentDate);
	json.totalCost && $(" #totalCost ").val((json.totalCost).toFixed(2));
	json.examinated && $(" #examinated ").prop('checked', true);
}

// Document load script
$(document).ready(function() {
	
	/****************/
	/** EDIT STATE **/
	/****************/
	
	// Edit button onClick handler
	$(" #editBtn ").click(function() {		
		$(" #dogId ").prop('disabled', false);
		$(" #responsibleName ").prop('disabled', false);	
		$(" #location ").prop('disabled', false);
		$(" #appointmentDate ").prop('disabled', false);
		$(" #vetName ").prop('disabled', false);
		$(" #totalCost ").prop('disabled', false);
		$(" #reason ").prop('disabled', false);
		$(" #examinated ").prop('disabled', false);
		$(" #appointmentDescription ").prop('disabled', false);
		
		if ( $(" #examinated ").prop("checked") )
			$(" #examDescription ").prop('disabled', false);
		
		$(" #prescriptionsBtn ").prop('disabled', false);
		$(" #resultsBtn ").prop('disabled', false);
		$(" #receiptsBtn ").prop('disabled', false);
		
		$(" #editBtn ").prop('disabled', true);
		$(" #saveBtn ").prop('disabled', false);
		$(" #deleteBtn ").prop('disabled', true);
	});
	
	// Delete button onClick handler
	$(" #deleteBtn ").click(function(event) {
		event.preventDefault();
		
		if (confirm("Tem certeza que deseja excluir este registro?")) {
			var app_id = getIdFromURLasString();
			
			$.ajax({
				url: "/appointment/delete",
				type: "POST",
				data: app_id,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro removido com sucesso!");
					window.location.replace("/appointment/appointment_search.html");
				},
				error: function(response) {
					alert(response.responseText);
				}
			});
		}
	});
	
	// Set the submit configuration for the form
	$( "#appointmentEditForm" ).submit(function(event) {
		event.preventDefault();
		
		// Form validation
		if (validateForm()) {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Store id
			jsonData["id"] = parseInt(getIdFromURLasString());
		
			// Fix the checkbox values within the json
			$(" #examinated ").prop("checked") ? jsonData["examinated"] = true : jsonData["examinated"] = false;
			
			// Fix JSON so it's in the right format
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "/appointment/register",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					console.log(response);
				},
				success: function() {
					// Reset the form to previous state
					$(" #dogId ").prop('disabled', true);
					$(" #responsibleName ").prop('disabled', true);	
					$(" #location ").prop('disabled', true);
					$(" #appointmentDate ").prop('disabled', true);
					$(" #vetName ").prop('disabled', true);
					$(" #totalCost ").prop('disabled', true);
					$(" #reason ").prop('disabled', true);
					$(" #examinated ").prop('disabled', true);
					$(" #appointmentDescription ").prop('disabled', true);
					$(" #examDescription ").prop('disabled', true);
					
					$(" #prescriptionsBtn ").prop('disabled', true);
					$(" #resultsBtn ").prop('disabled', true);
					$(" #receiptsBtn ").prop('disabled', true);
					$(" #editBtn ").prop('disabled', false);
					$(" #saveBtn ").prop('disabled', true);
					$(" #deleteBtn ").prop('disabled', false);
				}
			});
		}
	});
	
	/****************/
	/** INIT STATE **/
	/****************/
	
	// Load the data using dog id (specified in URL)
	var appointment_id = getIdFromURLasString();
	
	$.ajax({
		url: "/appointment/view",
		type: "POST",
		data: appointment_id,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			jsonToForm(response);
		},
		error: function(response) {
			alert(response.responseText);
			$(" #editBtn ").prop("disabled", true);
			$(" #deleteBtn ").prop("disabled", true);
		}
	});
});