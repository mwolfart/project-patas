// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	var app_date = new Date();
	app_date.setTime(json.applicationDate);
	
	$(" #dogId ").val(json.dogId);
	$(" #vermifugeName ").val(json.vermifugeName);
	$(" #dosage ").val(json.dosage);
	$(" #dosageMeasurement ").val(json.dosageMeasurement);
	$(" #applicationDate ").val(dateToString(app_date));
	$(" #obs ").val(json.obs);
	
	if (json.nextApplicationDate) {
		var next_app_date = new Date();
		next_app_date.setTime(json.nextApplicationDate);
		$(" #nextApplicationDate ").val(dateToString(next_app_date));
	}
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
			var verm_id = getUrlParameter('id');
			
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
		
		if ( validateStringField( $( "#dogId" )[0] ) == 0 )
			showAlert($( "#errorDogId" ), "Nome do cachorro deve ser informado.");
		else if ( validateStringField( $( "#vermifugeName" )[0] ) == 0 )
			showAlert($( "#errorVermifugeName" ), "Nome do vermífugo deve ser informado.");
		else if ( validateDateField( $( "#applicationDate" )[0] ) == 0 )
			showAlert($( "#errorApplicationDate" ), "Data deve ser informada.");
		else if ( validateNatNumberField( $( "#dosage" )[0] ) == 0 )
			showAlert($( "#errorDosage" ), "Dosagem deve ser informada.");
		else if ( validateStringField( $( "#vermifugeName" )[0] ) == -1 )
			showAlert($( "#errorVermifugeName" ), "Nome inválido.");
		else if ( validateDateField( $( "#applicationDate" )[0] ) == -1 )
			showAlert($( "#errorApplicationDate" ), "Data inválida.");
		else if ( validateNatNumberField( $( "#dosage" )[0] ) == -1 )
			showAlert($( "#errorDosage" ), "Dosagem inválida.");
		else if ( validateDateField( $( "#nextApplicationDate" )[0] ) == -1 )
			showAlert($( "#errorNextApplicationDate" ), "Data inválida.");
		else if ( validateStringField( $( "#obs" )[0] ) == -1 )
			showAlert($( "#errorObs" ), "Observações contém caracteres inválidos.");
		else {
			// Convert form to json and fix its format
			var jsonData = formToJson(this);	
			
			// Store the id so we know which dog to edit
			jsonData["id"] = parseInt(getUrlParameter('id'));
			
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
					/* UNIQUE PART */
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
	var verm_id = getUrlParameter('id');
	
	// if the id isn't specified, set it to 1 by default
	if (verm_id == 0)
		verm_id = "1";
	
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