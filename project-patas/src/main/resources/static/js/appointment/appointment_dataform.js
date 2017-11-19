// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields
	$( "#volName" ).keydown(protectStringField);
	$( "#appointmentDate" ).keydown(protectNumericField);
	$( "#location" ).keydown(protectStringField);
	$( "#vetName" ).keydown(protectStringField);
	$( "#totalCost" ).keydown(protectStringField);
	$( "#reason" ).keydown(protectStringField);
	$( "#examDescription" ).keydown(protectStringField);
	$( "#description" ).keydown(protectStringField);
	
	// Validate numeric and date fields
	$( "#dogName" ).focusout(function () { 
		if (validateStringField(this) > 0)
			hideAlert($( "#errorDogName" ));
	});
	
	$( "#volName" ).focusout(function () { 
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorVolName" ));
	});
	
	$( "#appointmentDate" ).focusout(function () { 
		if (validateDateField(this) > 0)
			hideAlert($( "#errorAppointmentDate" ));
	});
	
	$( "#location" ).focusout(function () { 
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorLocation" ));
	});
	
	$( "#vetName" ).focusout(function () { 
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorVetName" ));
	});
	
	$( "#totalCost" ).focusout(function () { 
		if (validateCurrencyField(this) >= 0)
			hideAlert($( "#errorTotalCost" ));
	});
	
	$( "#reason" ).focusout(function () { 
		if (validateStringField(this) > 0)
			hideAlert($( "#errorReason" ));
	});
	
	$( "#examDescription" ).focusout(function () { 
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorExamDescription" ));
	});
	
	$( "#description" ).focusout(function () { 
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorDescription" ));
	});
	
	//Enable/disable the exam field
	$( "#checkboxExam" ).click(function() {
		if (this.checked)
			$( "#examDescription" ).prop("disabled", false);
		else {
			$( "#examDescription" ).prop("disabled", true);
			$( "#examDescription" ).val("");
			$( "#examDescription" ).removeClass("error_input");
			hideAlert($( "#errorExamDescription" ));
		}
	});	
	
	// Configure dog combobox
	$.ajax({
		url: "http://localhost:8080/dog/get",
		type: "GET",
		success: function(data) {
			putDogsInComboBox(data);
		}
	});
})