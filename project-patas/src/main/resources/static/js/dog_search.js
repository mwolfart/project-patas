// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields
	$( "#birthYear" ).keydown(protectNumericField);
	$( "#ageSup" ).keydown(protectNumericField);
	$( "#ageInf" ).keydown(protectNumericField);
	$( "#weightSup" ).keydown(protectNumericField);
	$( "#weightInf" ).keydown(protectNumericField);
	$( "#arrivalYear" ).keydown(protectNumericField);
	$( "#castrationYear" ).keydown(protectNumericField);
	
	$( "#birthYear" ).focusout( function() { validateNatNumberField(this) });
	$( "#ageSup" ).focusout( function() { validateNatNumberField(this) });
	$( "#ageInf" ).focusout( function() { validateNatNumberField(this) });
	$( "#weightSup" ).focusout( function() { validateRealNumberField(this) });
	$( "#weightInf" ).focusout( function() { validateRealNumberField(this) });
	$( "#arrivalYear" ).focusout( function() { validateNatNumberField(this) });
	$( "#castrationYear" ).focusout( function() { validateNatNumberField(this) });
	
	//Enable/disable the castration date field
	$( "#checkboxCastr" ).click(function() {
		if (this.checked) {
			$( "#castrationDay" ).prop("disabled", false);
			$( "#castrationMonth" ).prop("disabled", false);
			$( "#castrationYear" ).prop("disabled", false);
		}
		else {
			$( "#castrationDay" ).prop("disabled", true);
			$( "#castrationMonth" ).prop("disabled", true);
			$( "#castrationYear" ).prop("disabled", true);
			$( "#castrationDay" ).val("");
			$( "#castrationMonth" ).val("");
			$( "#castrationYear" ).val("");
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
});
