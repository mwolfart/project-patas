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
	$( "#name" ).focusout(function () { 
		if (validateStringField(this) > 0)
			hideAlert($( "#errorName" ));
	});
	
	$( "#weight" ).focusout(function () { 
		if (validateRealNumberField(this) >= 0)
			hideAlert($( "#errorWeight" )); 
	});
	
	$( "#furColor" ).focusout(function () { 
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorFurColor" )); 
	});
	
	$( "#castrationDate" ).focusout(function() { 
		if (validateDateField(this) >= 0)
			hideAlert($( "#errorCastrationDate" )); 
	});
	
	$( "#arrivalDate" ).focusout(function() { 
		if (validateDateField(this) > 0)
			hideAlert($( "#errorArrivalDate" )); 
	});
	
	$( "#diseaseDescription" ).focusout(function () { 
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorDisease" )); 
	});
	
	$( "#sponsors" ).focusout(function () { 
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorSponsors" )); 
	});
	
	// Validate birth and automatically compute age
	$( "#birthDate" ).focusout( function() {
		var validation = validateDateField(this);
		
		if (validation >= 0) {
			$( "#age" ).val("");
			hideAlert($( "#errorBirthDate" ));
			
			if (validation > 0) {
				var age = computeAge(stringToDate(this.value));
				$( "#age" ).val(age);
			}
		}
	});
	
	//Enable/disable the castration date field
	$( "#castrated" ).click(function() {
		if (this.checked)
			$( "#castrationDate" ).prop("disabled", false);
		else {
			$( "#castrationDate" ).prop("disabled", true);
			$( "#castrationDate" ).val("");
			$( "#castrationDate" ).removeClass("error_input");
			hideAlert($( "#errorCastrationDate" ));
		}
	});
	
	//Enable/disable the disease description field
	$( "#hasDiseases" ).click(function() {
		if (this.checked)
			$( "#diseaseDescription" ).prop("disabled", false);
		else {
			$( "#diseaseDescription" ).prop("disabled", true);
			$( "#diseaseDescription" ).val("");
			hideAlert($( "#errorDiseaseDescription" ));
		}
	});
	
	$( "#ration" ).change(function() {
		if (this.value == "OUTRO") 
			$( "#rationCustomDescription" ).prop("disabled", false);
		else {
			$( "#rationCustomDescription" ).prop("disabled", true);
			$( "#rationCustomDescription" ).val("");
			hideAlert($( "#errorRationCustomDescription" ));
		}
	});
	
	//Enable/disable profile photo to edition
	$('#checker').click(function() {
		$("#image-input").prop("disabled", !this.checked);
	    $("#picup-load").toggle(this.checked);
	});	
	
	// Read image file and show it
	$("#image-input").change(function() {
		readURL(this);
		$("#picup-load").toggle(this.checked);
	});	
	
	$("#birthDate").bind('keyup',function(event){
	    formatDate(event, $("#birthDate"));
	});
	
	$("#arrivalDate").bind('keyup',function(event){
	    formatDate(event, $("#arrivalDate"));
	});
	
	$("#castrationDate").bind('keyup',function(event){
	    formatDate(event, $("#castrationDate"));
	});
})
