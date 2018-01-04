$(document).ready(function() {
	// Protect fields
	$( "#vaccineName" ).keydown(protectStringField);
	$( "#applicationDate" ).keydown(protectNumericField);
	$( "#nextApplicationDate" ).keydown(protectNumericField);
	$( "#obs" ).keydown(protectStringField);

	//Validate fields
	$( "#dogId" ).focusout(function() {
		if (validateStringField(this) > 0)
			hideAlert($( "#errorDogId" ));
	})
	
	$( "#vaccineName" ).focusout(function() {
		if (validateStringField(this) > 0)
			hideAlert($( "#errorVaccineName" ));
	})
	
	$( "#applicationDate" ).focusout(function() {
		if (validateDateField(this) > 0)
			hideAlert($( "#errorApplicationDate" ));
	})
	
	$( "#nextApplicationDate" ).focusout(function() {
		if (validateDateField(this) >= 0)
			hideAlert($( "#errorNextApplicationDate" ));
	})
	
	$( "#obs" ).focusout(function() {
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorObs" ));
	})
	
	// Configure dog combobox
	$.ajax({
		url: "/dog/get",
		type: "GET",
		success: function(data) {
			putDogsInComboBox(data);
		}
	});
});