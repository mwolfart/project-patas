
// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields
	$( "#weight" ).keydown(protectNumericField);
	$( "#birthDate" ).keydown(protectNumericField);
	$( "#castrationDate" ).keydown(protectNumericField);
	$( "#arrivalDate" ).keydown(protectNumericField);
	
	// Validate numeric and date fields
	$( "#weight" ).focusout(function () { validateRealNumberField(this) });
	$( "#castrationDate" ).focusout(function() { validateDateField(this) });
	$( "#arrivalDate" ).focusout(function() { validateDateField(this) });
	
	// Validate birth and automatically compute age
	$( "#birthDate" ).focusout( function() {
		if (isDateValid(this.value)) {
			var age = computeAge(stringToDate(this.value));
			
			$(this).removeClass("error_input");
			$( "#age" ).val(age);
		}
		else if (this.value == "") 
			$(this).removeClass("error_input");
		else 
			$(this).addClass("error_input");
	});
	
	//Enable/disable the castration date field
	$( "#checkboxCastr" ).click(function() {
		if (this.checked)
			$( "#castrationDate" ).prop("disabled", false);
		else {
			$( "#castrationDate" ).prop("disabled", true);
			$( "#castrationDate" ).val("");
			$( "#castrationDate" ).removeClass("error_input");
		}
	});
	
	//Enable/disable the disease description field
	$( "#checkboxDis" ).click(function() {
		if (this.checked)
			$( "#diseaseDescription" ).prop("disabled", false);
		else {
			$( "#diseaseDescription" ).prop("disabled", true);
			$( "#diseaseDescription" ).val("");
		}
	});
	
	// Set the submit configuration for the form
	$( "#registerForm" ).submit(function(event) {
		event.preventDefault();
		
		// Form validation
		if ( $( "#name" ).val() == "" )
			alert("Preencha o nome do cachorro!");
		else if ( $( "#birthDate" ).hasClass("error_input") )
			alert("Data de nascimento inválida!");
		else if ( $( "#weight" ).hasClass("error_input") )
			alert("Peso inválido!");
		else if ( $( "#arrivalDate" ).hasClass("error_input") )
			alert("Data de chegada inválida!");
		else if ( $( "#arrivalDate" ).val() == "" )
			alert("Preencha a data de chegada!");
		else {
			// Convert form to json
			var jsonData = formToJson(this);
			
			// Fix the checkbox values within the json
			if ( document.getElementById("checkboxDis").checked )
				jsonData["disease"] = true;
			else jsonData["disease"] = false;
			
			if ( document.getElementById("checkboxCastr").checked )
				jsonData["castrated"] = true;
			else jsonData["castrated"] = false;
			
			// Fix JSON so it's in the right format
			jsonData = JSON.stringify(jsonData);
	
			// Post the data
			$.ajax({
				url: "http://localhost:8080/dog/register",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
			});
		}
	});
});
