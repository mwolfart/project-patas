//processResponseJson := Object -> Object
//function used to filter the json returned by the query
//returns another json, containing only the fields we are interested in.
function processResponseJson(response) {
	$("tbody > tr").remove();
	if (response.length>0){
		$('#vacinations').append(
				$.map(response, function (vac_info) {				
					return '<tr><td>' + vac_info[1] + '</td><td>'+ vac_info[2] +'</td><td>'+ dateToString(integerToDate(vac_info[3])) +'</td><td>'
					+'<a href="vaccination_view.html?id='+ vac_info[0] +'" class="btn" role="button">Visualizar</a></td></tr>';
				}).join());
	}else{
		$('#vacinations').append('<tr><td colspan="4"> Nenhum registro encontrado. </td></tr>');
	}
	$( "#vacinations" ).removeClass("disabled-table");

}

// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields	
	$( "#vaccineName" ).keydown(protectSearchStringField);
	$( "#applicationDate" ).keydown(protectNumericField);
	$( "#nextApplicationDate" ).keydown(protectNumericField);
	
	$( "#vaccineName" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorVaccineName"));
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
	$( "#vaccinationSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateSearchStringField( $("#vaccineName")[0] ) == -1 )
			showAlert($( "#errorVaccineName" ), "Nome inválido.");
		else if ( validateDateField( $("#applicationDate")[0] ) == -1 )
			showAlert($( "#errorApplicationDate" ), "Data inválida.");
		else if ( validateDateField( $("#nextApplicationDate")[0] ) == -1 )
			showAlert($( "#errorNextApplicationDate" ), "Data inválida.");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			$.ajax({
				url: "/vaccination/search",
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
	 
	// Configure dog combobox and load if specified
	$.ajax({
		url: "/dog/get",
		type: "GET",
		success: function(data) {
			putDogsInComboBox(data);
			
			var dog_id = getUrlParameter("dogId");
			var jsonData = {};
			
			if (dog_id != 0) {
				$( "#dogId" ).val(dog_id);
				jsonData = {"dogId": dog_id};
				jsonData = JSON.stringify(jsonData);
			}
			
			$.ajax({
				url: "/vaccination/search",
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
