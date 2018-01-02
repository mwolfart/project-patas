$(document).ready(function() {
	// Protect fields
	$( "#username" ).keydown(protectUserField);
	$( "#password" ).keydown(protectUserField);
	$( "#passwordConf" ).keydown(protectUserField);
	$( "#fullName" ).keydown(protectStringField);

	//Validate fields
	$( "#username" ).focusout(function() {
		if (validateUserField(this) > 0)
			hideAlert($( "#errorUsername" ));
	})
	
	$( "#password" ).focusout(function() {
		if (validateUserField(this) > 0)
			hideAlert($( "#errorPassword" ));
	})
	
	$( "#passwordConf" ).focusout(function() {
		if (validateUserField(this) > 0)
			hideAlert($( "#errorPasswordConf" ));
	})
	
	$( "#fullName" ).focusout(function() {
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorFullName" ));
	})
});