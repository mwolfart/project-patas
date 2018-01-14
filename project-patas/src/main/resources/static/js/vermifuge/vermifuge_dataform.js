function validateForm() {
	var formOK = false;
	
	if ( validateStringField( $( "#dogId" )[0] ) == 0 )
		showAlert($( "#errorDogId" ), "Nome do cachorro deve ser informado.");
	else if ( validateStringField( $( "#vermifugeName" )[0] ) == 0 )
		showAlert($( "#errorVermifugeName" ), "Nome do vermífugo deve ser informado.");
	else if ( validateDateField( $( "#applicationDate" )[0] ) == 0 )
		showAlert($( "#errorApplicationDate" ), "Data deve ser informada.");
	else if ( validateNatNumberField( $( "#dosage" )[0] ) == 0 )
		showAlert($( "#errorDosage" ), "Dosagem deve ser informada.");
	else if ( validateStringField( $( "#vermifugeName" )[0] ) == -1 )
		showAlert($( "#errorVermifugeName" ), "Nome inválido.");
	else if ( validateDateField( $( "#applicationDate" )[0] ) == -1 )
		showAlert($( "#errorApplicationDate" ), "Data inválida.");
	else if ( validateNatNumberField( $( "#dosage" )[0] ) == -1 )
		showAlert($( "#errorDosage" ), "Dosagem inválida.");
	else if ( validateDateField( $( "#nextApplicationDate" )[0] ) == -1 )
		showAlert($( "#errorNextApplicationDate" ), "Data inválida.");
	else if ( validateStringField( $( "#obs" )[0] ) == -1 )
		showAlert($( "#errorObs" ), "Observações contém caracteres inválidos.");
	else formOK = true;
	
	return formOK;
}

$(document).ready(function() {
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
});