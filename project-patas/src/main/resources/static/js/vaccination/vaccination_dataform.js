$(document).ready(function() {
	// Protect fields
	$( "#vacName" ).keydown(protectStringField);
	$( "#appDate" ).keydown(protectNumericField);
	$( "#nextAppDate" ).keydown(protectNumericField);
	$( "#obs" ).keydown(protectStringField);

	//Validate fields
	$( "#dogName" ).focusout(function() {
		if (validateStringField(this) > 0)
			hideAlert($( "#errorDogName" ));
	})
	
	$( "#vacName" ).focusout(function() {
		if (validateStringField(this) > 0)
			hideAlert($( "#errorVacName" ));
	})
	
	$( "#appDate" ).focusout(function() {
		if (validateDateField(this) > 0)
			hideAlert($( "#errorAppDate" ));
	})
	
	$( "#nextAppDate" ).focusout(function() {
		if (validateDateField(this) >= 0)
			hideAlert($( "#errorNextAppDate" ));
	})
	
	$( "#obs" ).focusout(function() {
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorObs" ));
	})
	
	// Configure dog combobox
	$.ajax({
		url: "http://localhost:8080/dog/get",
		type: "GET",
		success: function(data) {
			putDogsInComboBox(data);
		}
	});
});