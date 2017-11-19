// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	var app_date = new Date();
	var next_app_date = new Date();
	app_date.setTime(json.appDate);
	next_app_date.setTime(json.nextAppDate);
	
	$(" #dogName ").val(json.dogName);
	$(" #vermName ").val(json.vermName);
	$(" #amount ").val(json.amount);
	$(" #appDate ").val(dateToString(app_date));
	$(" #nextAppDate ").val(dateToString(next_app_date));
	$(" #obs ").val(json.obs);
}

$(document).ready(function() {
	// Edit button onClick handler
	$(" #editBtn ").click(function() {		
		$(" #dogName ").prop('disabled', false);
		$(" #vermName ").prop('disabled', false);
		$(" #amount ").prop('disabled', false);
		$(" #appDate ").prop('disabled', false);
		$(" #nextAppDate ").prop('disabled', false);
		$(" #obs ").prop('disabled', false);
		
		$(" #editBtn ").prop('disabled', true);
		$(" #saveBtn ").prop('disabled', false);
		$(" #deleteBtn ").prop('disabled', true);
	});
	
	$( "#vermEditForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateStringField( $( "#dogName" )[0] ) == 0 )
			showAlert($( "#errorDogName" ), "Campo obrigatório!");
		else if ( validateStringField( $( "#vermName" )[0] ) == 0 )
			showAlert($( "#errorVermName" ), "Campo obrigatório!");
		else if ( validateDateField( $( "#appDate" )[0] ) == 0 )
			showAlert($( "#errorAppDate" ), "Campo obrigatório!");
		else if ( validateNumericField( $( "#amount" )[0] ) == 0 )
			showAlert($( "#errorAmount" ), "Campo obrigatório!");
		else if ( validateStringField( $( "#vermName" )[0] ) == -1 )
			showAlert($( "#errorVermName" ), "Nome inválido!");
		else if ( validateDateField( $( "#appDate" )[0] ) == -1 )
			showAlert($( "#errorAppDate" ), "Data inválida!");
		else if ( validateNumericField( $( "#amount" )[0] ) == -1 )
			showAlert($( "#errorAmount" ), "Dosagem inválida");
		else if ( validateDateField( $( "#nextAppDate" )[0] ) == -1 )
			showAlert($( "#errorNextAppDate" ), "Data inválida");
		else if ( validateStringField( $( "#obs" )[0] ) == -1 )
			showAlert($( "#errorObs" ), "Observações contém caracteres inválidos!");
		else {
			// Convert form to json and fix its format
			var jsonData = formToJson(this);	
			
			// Store the id so we know which dog to edit
			jsonData["id"] = parseInt(getUrlParameter('id'));
			
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "http://localhost:8080/vermifuge/register",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					alert(response);
				},
				success: function() {
					/* UNIQUE PART */
					// Reset the form to previous state
					$(" #dogName ").prop('disabled', true);
					$(" #vermName ").prop('disabled', true);
					$(" #amount ").prop('disabled', true);
					$(" #appDate ").prop('disabled', true);
					$(" #nextAppDate ").prop('disabled', true);
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
		url: "http://localhost:8080/vermifuge/view",
		type: "POST",
		data: verm_id,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			jsonToForm(response);
		},
		error: function(response) {
			alert(response.responseText);
			$(" #editBtn ").prop("disabled", true);
		}
	});
});