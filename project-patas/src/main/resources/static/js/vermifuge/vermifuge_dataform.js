$(document).ready(function() {

	// TODO: FIX FIELDS IDS IN HTML
	
	// Protect fields
	$( "#vermName" ).keydown(protectStringField);
	$( "#amount" ).keydown(protectNumericField);
	$( "#appDate" ).keydown(protectNumericField);
	$( "#nextAppDate" ).keydown(protectNumericField);
	$( "#obs" ).keydown(protectStringField);

	//Validate fields
	$( "#dogName" ).focusout(function() {
		if (validateStringField(this) > 0)
			hideAlert($( "#errorDogName" ));
	})
	
	$( "#vermName" ).focusout(function() {
		if (validateStringField(this) > 0)
			hideAlert($( "#errorVermName" ));
	})
	
	$( "#amount" ).focusout(function() {
		if (validateRealNumberField(this) > 0)
			hideAlert($( "#errorAmount" ));
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
		if (validateStringField(this) > 0)
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