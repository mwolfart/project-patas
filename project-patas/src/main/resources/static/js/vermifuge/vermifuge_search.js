//processResponseJson := Object -> Object
//function used to filter the json returned by the query
//returns another json, containing only the fields we are interested in.
function processResponseJson(response) {
	$("tbody > tr").remove();
	
	$('#vermifugations').append(
			$.map(response, function (verm_info) {				
				return '<tr><td>' + verm_info[1] + '</td><td>'+ verm_info[2] +'</td><td>'+ dateToString(integerToDate(verm_info[3])) +'</td><td>'
				+'<a href="vermifuge_view.html?id='+ verm_info[0] +'" class="btn" role="button">Visualizar</a></td></tr>';
			}).join());
	$( "#vermifugations" ).removeClass("disabled-table");

}

// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields	
	$( "#vermifugeName" ).keydown(protectSearchStringField);
	$( "#applicationDate" ).keydown(protectNumericField);
	$( "#nextApplicationDate" ).keydown(protectNumericField);
	
	$( "#vermifugeName" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorVermifugeName"));
	});
	
	$( "#applicationDate" ).focusout( function() { 
		validateDateField(this);
		hideAlert($("#errorApplicationDate"));
	});
	
	$( "#nextApplicationDate" ).focusout( function() { 
		validateDateField(this);
		hideAlert($("#errorNextApplicationDate"));
	});

	// Set the submit configuration for the form
	$( "#vermifugeSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateSearchStringField( $("#vermifugeName")[0] ) == -1 )
			showAlert($( "#errorVermifugeName" ), "Nome inválido.");
		else if ( validateDateField( $("#applicationDate")[0] ) == -1 )
			showAlert($( "#errorApplicationDate" ), "Data inválida.");
		else if ( validateNatNumberField( $("#nextApplicationDate")[0] ) == -1 )
			showAlert($( "#errorNextApplicationDate" ), "Data inválida.");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			$.ajax({
				url: "/vermifuge/search",
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
	
	/****************/
	/** INIT STATE **/
	/****************/
	 
	var dog_id = getUrlParameter("dogId");
	var jsonData = {};
	
	if (dog_id != 0) {
		$( "#dogId" ).val(dogId);
		jsonData = {"dogId": dogId};
		jsonData = JSON.stringify(jsonData);
	}
	
	$.ajax({
		url: "/vermifuge/search",
		type: "POST",
		dataType: "json",
		data: jsonData,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			processResponseJson(response);
		}
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
