// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields
	$( "#name" ).keydown(protectStringField);
	$( "#weight" ).keydown(protectNumericField);
	$( "#birthDate" ).keydown(protectNumericField);
	$( "#furColor" ).keydown(protectStringField);
	$( "#castrationDate" ).keydown(protectNumericField);
	$( "#arrivalDate" ).keydown(protectNumericField);
	$( "#diseaseDescription" ).keydown(protectStringField);
	$( "#sponsors" ).keydown(protectStringField);
	
	// Validate numeric and date fields
	$( "#name" ).focusout(function () { validateStringField(this) });
	$( "#weight" ).focusout(function () { validateRealNumberField(this) });
	$( "#furColor" ).focusout(function () { validateStringField(this) });
	$( "#castrationDate" ).focusout(function() { validateDateField(this) });
	$( "#arrivalDate" ).focusout(function() { validateDateField(this) });
	$( "#diseaseDescription" ).focusout(function () { validateStringField(this) });
	$( "#sponsors" ).focusout(function () { validateStringField(this) });
	
	// Validate birth and automatically compute age
	$( "#birthDate" ).focusout( function() {
		var validation = validateDateField(this);
		
		if (validation >= 0) {
			$( "#age" ).val("");
			
			if (validation > 0) {
				var age = computeAge(stringToDate(this.value));
				$( "#age" ).val(age);
			}
		}
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