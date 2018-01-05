$(document).ready(function() {

	// TODO: FIX FIELDS IDS IN HTML
	
	// Protect fields
	$( "#vermifugeName" ).keydown(protectStringField);
	$( "#dosage" ).keydown(protectNumericField);
	$( "#applicationDate" ).keydown(protectNumericField);
	$( "#nextApplicationDate" ).keydown(protectNumericField);
	$( "#obs" ).keydown(protectStringField);

	//Validate fields
	$( "#dogId" ).focusout(function() {
		if (validateStringField(this) > 0)
			hideAlert($( "#errorDogId" ));
	})
	
	$( "#vermifugeName" ).focusout(function() {
		if (validateStringField(this) > 0)
			hideAlert($( "#errorVermifugeName" ));
	})
	
	$( "#dosage" ).focusout(function() {
		if (validateRealNumberField(this) > 0)
			hideAlert($( "#errorDosage" ));
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
		if (validateStringField(this) > 0)
			hideAlert($( "#errorObs" ));
	})
	
	$("#applicationDate").bind('keyup',function(event){
	    formatDate(event, $("#applicationDate"));
	});
	
	$("#nextApplicationDate").bind('keyup',function(event){
	    formatDate(event, $("#nextApplicationDate"));
	});
	
	// Configure dog combobox
	$.ajax({
		url: "/dog/get",
		type: "GET",
		success: function(data) {
			putDogsInComboBox(data);
		}
	});
});