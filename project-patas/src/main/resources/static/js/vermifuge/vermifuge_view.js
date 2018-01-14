// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	putDogsInComboBoxAndSet(json.dogId);
	$(" #vermifugeName ").val(json.vermifugeName);
	$(" #dosage ").val(json.dosage);
	$(" #dosageMeasurement ").val(json.dosageMeasurement);
	putDateInField($(" #applicationDate "), json.applicationDate);
	$(" #obs ").val(json.obs);
	putDateInField($(" #nextApplicationDate "), json.nextApplicationDate);
}

$(document).ready(function() {
	// Edit button onClick handler
	$(" #editBtn ").click(function() {		
		$(" #dogId ").prop('disabled', false);
		$(" #vermifugeName ").prop('disabled', false);
		$(" #dosage ").prop('disabled', false);
		$(" #dosageMeasurement ").prop('disabled', false);
		$(" #applicationDate ").prop('disabled', false);
		$(" #nextApplicationDate ").prop('disabled', false);
		$(" #obs ").prop('disabled', false);
		
		$(" #editBtn ").prop('disabled', true);
		$(" #saveBtn ").prop('disabled', false);
		$(" #deleteBtn ").prop('disabled', true);
	});
	
	// Delete button onClick handler
	$(" #deleteBtn ").click(function(event) {
		event.preventDefault();
		
		if (confirm("Tem certeza que deseja excluir este registro?")) {
			var verm_id = getIdFromURLasString();
			
			$.ajax({
				url: "/vermifuge/delete",
				type: "POST",
				data: verm_id,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro removido com sucesso!");
					window.location.replace("/vermifuge/vermifuge_search.html");
				},
				error: function(response) {
					alert(response.responseText);
				}
			});
		}
	});
	
	$( "#vermEditForm" ).submit(function(event) {
		event.preventDefault();
		
		if (validateForm()) {
			// Convert form to json and fix its format
			var jsonData = formToJson(this);	
			
			// Store the id so we know which register to edit
			jsonData["id"] = parseInt(getIdFromURLasString());
			
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "/vermifuge/register",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					alert(response);
				},
				success: function() {
					// Reset the form to previous state
					$(" #dogId ").prop('disabled', true);
					$(" #vermifugeName ").prop('disabled', true);
					$(" #dosage ").prop('disabled', true);
					$(" #dosageMeasurement ").prop('disabled', true);
					$(" #applicationDate ").prop('disabled', true);
					$(" #nextApplicationDate ").prop('disabled', true);
					$(" #obs ").prop('disabled', true);
					
					$(" #editBtn ").prop('disabled', false);
					$(" #saveBtn ").prop('disabled', true);
					$(" #deleteBtn ").prop('disabled', false);
				}
			});			
		}
	});
		
	// Load the data using dog id (specified in URL)
	var verm_id = getIdFromURLasString();
	
	$.ajax({
		url: "/vermifuge/view",
		type: "POST",
		data: verm_id,
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