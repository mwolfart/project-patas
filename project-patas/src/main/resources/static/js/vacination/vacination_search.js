//processResponseJson := Object -> Object
//function used to filter the json returned by the query
//returns another json, containing only the fields we are interested in.
function processResponseJson(response) {
	$("tbody > tr").remove();
	
	$('#vacinations').append(
			$.map(response, function (vac_info) {				
				return '<tr><td>' + vac_info[1] + '</td><td>'+ vac_info[2] +'</td><td>'+ dateToString(integerToDate(vac_info[3])) +'</td><td>'
				+'<a href="vacination_view.html?id='+ vac_info[0] +'" class="btn btn-info" role="button">Visualizar</a></td></tr>';
			}).join());
	$( "#vacinations" ).removeClass("disabled-table");

}

// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields	
	$( "#vermName" ).keydown(protectSearchStringField);
	$( "#dogName" ).keydown(protectSearchStringField);
	$( "#appDate" ).keydown(protectNumericField);
	$( "#nextAppDate" ).keydown(protectNumericField);
	
	$( "#dogName" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorDogName"));
	});
	
	$( "#vermName" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorVermName"));
	});
	
	$( "#appDate" ).focusout( function() { 
		validateDateField(this);
		hideAlert($("#errorAppDate"));
	});
	
	$( "#nextAppDate" ).focusout( function() { 
		validateDateField(this);
		hideAlert($("#errorNextAppDate"));
	});

	// Set the submit configuration for the form
	$( "#vermifugeSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateSearchStringField( $("#dogName")[0] ) == -1 )
			showAlert($( "#errorDogName" ), "Nome inválido!");
		else if ( validateSearchStringField( $("#vermName")[0] ) == -1 )
			showAlert($( "#errorVermName" ), "Nome inválido!");
		else if ( validateDateField( $("#appDate")[0] ) == -1 )
			showAlert($( "#errorAppDate" ), "Data inválida!");
		else if ( validateNatNumberField( $("#nextAppDate")[0] ) == -1 )
			showAlert($( "#errorNextAppDate" ), "Data inválida!");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			$.ajax({
				url: "http://localhost:8080/vermifuge/search",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					processResponseJson(response);
				}
			});
		}
	});	
});
