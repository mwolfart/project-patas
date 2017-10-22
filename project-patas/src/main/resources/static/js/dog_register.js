// computeAge := Date -> Integer
// given an arbitrary date, computes the age (difference between this date and the current one).
// If the given date is greater (more recent) than the current date, it uses the given date as "current date"
function computeAge(datee) {
	var cur_date = new Date();
	
	if (datee > cur_date) {
		cur_date = datee;
		datee = new Date();
	}
	
	var diff_year = cur_date.getFullYear() - datee.getFullYear();
	var diff_month = cur_date.getMonth() - datee.getMonth();
	var diff_days = cur_date.getDate() - datee.getDate();
	
	if (diff_month > 0 || (diff_month == 0 && diff_days >= 0))
		return diff_year;
	else return diff_year - 1;
}

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
