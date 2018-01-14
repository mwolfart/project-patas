function validateForm() {
	var formOK = false;
	
	if ( validateStringField( $( "#dogId" )[0] ) == 0 )
		showAlert($( "#errorDogId" ), "Nome do cachorro deve ser informado.");
	else if ( validateStringField( $( "#responsibleName" )[0] ) == -1 )
		showAlert($( "#errorResponsibleName" ), "Nome inválido.");
	else if ( validateDateField( $( "#appointmentDate" )[0] ) == 0 ) 
		showAlert($( "#errorAppointmentDate" ), "Data deve ser informada.");
	else if ( validateDateField( $( "#appointmentDate" )[0] ) == -1 ) 
		showAlert($( "#errorAppointmentDate" ), "Data inválida.");
	else if ( validateStringField( $( "#location" )[0] ) == -1 )
		showAlert($( "#errorLocation" ), "Nome inválido.");
	else if ( validateStringField( $( "#vetName" )[0] ) == -1 )
		showAlert($( "#errorVetName" ), "Nome inválido.");
	else if ( validateCurrencyField( $( "#totalCost" )[0] ) == -1 )
		showAlert($( "#errorTotalCost" ), "Preço deve ser informado.");
	else if ( validateStringField( $( "#reason" )[0] ) == 0 )
		showAlert($( "#errorReason" ), "O motivo deve ser informado.");
	else if ( validateStringField( $( "#reason" )[0] ) == -1 )
		showAlert($( "#errorReason" ), "Descrição inválida.");
	else if ( validateStringField( $( "#examDescription" )[0] ) == -1 )
		showAlert($( "#errorExamDescription" ), "Descrição inválida.");
	else if ( validateStringField( $( "#appointmentDescription" )[0] ) == -1 )
		showAlert($( "#errorAppointmentDescription" ), "Descrição inválida.");
	else formOK = true;
	
	return formOK;
}

// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields
	$( "#responsibleName" ).keydown(protectStringField);
	$( "#appointmentDate" ).keydown(protectNumericField);
	$( "#location" ).keydown(protectStringField);
	$( "#vetName" ).keydown(protectStringField);
	$( "#totalCost" ).keydown(protectStringField);
	$( "#reason" ).keydown(protectStringField);
	$( "#examDescription" ).keydown(protectStringField);
	$( "#description" ).keydown(protectStringField);
	
	// Validate numeric and date fields
	$( "#dogId" ).focusout(function () { 
		if (validateStringField(this) > 0)
			hideAlert($( "#errorDogId" ));
	});
	
	$( "#responsibleName" ).focusout(function () { 
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorResponsibleName" ));
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
	
	$( "#appointmentDescription" ).focusout(function () { 
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorAppointmentDescription" ));
	});
	
	//Enable/disable the exam field
	$( "#examinated" ).click(function() {
		if (this.checked)
			$( "#examDescription" ).prop("disabled", false);
		else {
			$( "#examDescription" ).prop("disabled", true);
			$( "#examDescription" ).val("");
			$( "#examDescription" ).removeClass("error_input");
			hideAlert($( "#errorExamDescription" ));
		}
	});
	
	$("#appointmentDate").bind('keyup',function(event){
	    formatDate(event, $("#appointmentDate"));
	});
})