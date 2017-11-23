//processResponseJson := Object -> Object
//function used to filter the json returned by the query
//returns another json, containing only the fields we are interested in.
function processResponseJson(response) {
	$("tbody > tr").remove();
	
	$('#dogs').append(
			$.map(response, function (dog_info) {
				if (dog_info[2] == "F")
					dog_sex = "F�mea";
				else if (dog_info[2] == "M")
					dog_sex = "Macho";
				else dog_sex = "";
				
				return '<tr><td>' + dog_info[1] + '</td><td>'+ dog_sex +'</td><td>'+ dateToString(integerToDate(dog_info[3])) +'</td><td>'
				+'<a href="dog_view.html?id='+ dog_info[0] +'" class="btn btn-info" role="button">Visualizar</a></td></tr>';
			}).join());
	$( "#dogs" ).removeClass("disabled-table");

}

// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields	
	$( "#name" ).keydown(protectSearchStringField);
	$( "#birthYear" ).keydown(protectNumericField);
	$( "#arrivalYear" ).keydown(protectNumericField);
	
	$( "#name" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorName"));
	});
	
	$( "#birthYear" ).focusout( function() { 
		validateNatNumberField(this);
		hideAlert($("#errorBirthYear"));
	});
	
	$( "#arrivalYear" ).focusout( function() { 
		validateNatNumberField(this);
		hideAlert($("#errorArrivalYear"));
	});

	// Set the submit configuration for the form
	$( "#dogSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateSearchStringField( $("#name")[0] ) == -1 )
			showAlert($( "#errorName" ), "Nome inv�lido!");
		else if ( validateNatNumberField( $("#birthYear")[0] ) == -1 )
			showAlert($( "#errorBirthYear" ), "Ano de nascimento inv�lido!");
		else if ( validateNatNumberField( $("#arrivalYear")[0] ) == -1 )
			showAlert($( "#errorArrivalYear" ), "Ano de chegada inv�lido!");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			$.ajax({
				url: "http://localhost:8080/dog/search",
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