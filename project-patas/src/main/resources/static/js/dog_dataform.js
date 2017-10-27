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
	
})