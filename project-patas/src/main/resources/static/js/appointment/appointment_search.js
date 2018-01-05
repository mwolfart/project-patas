//processResponseJson := Object -> Object
//function used to filter the json returned by the query
//returns another json, containing only the fields we are interested in.
function processResponseJson(response) {
	$("tbody > tr").remove();
	
	$('#appointments').append(
			$.map(response, function (appoint_info) {
				var location = "";
				var vetName = "";
				
				if (appoint_info[3] != null) location = appoint_info[3];
				if (appoint_info[4] != null) vetName = appoint_info[4];
				
				return '<tr><td>' + appoint_info[1] + '</td><td>'+ dateToString(integerToDate(appoint_info[2])) +'</td><td>'+
					location +'</td><td>'+ vetName +'</td><td>'+
					'<a href="appointment_view.html?id='+ appoint_info[0] +
					'" class="btn" role="button">Visualizar</a></td></tr>';
			}).join());
	$( "#appointments" ).removeClass("disabled-table");

}

// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields
	$( "#location" ).keydown(protectSearchStringField);
	$( "#vetName" ).keydown(protectSearchStringField);
	$( "#appointmentDate" ).keydown(protectNumericField);
	
	$( "#location" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorLocation"));
	});
	
	$( "#vetName" ).focusout( function() { 
		validateSearchStringField(this);
		hideAlert($("#errorVetName"));
	});
	
	$( "#appointmentDate" ).focusout( function() { 
		validateDateField(this);
		hideAlert($("#errorAppointmentDate"));
	});

	// Set the submit configuration for the form
	$( "#appointmentSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateSearchStringField( $("#vetName")[0] ) == -1 )
			showAlert($( "#errorVetName" ), "Nome inválido.");
		else if ( validateSearchStringField( $("#location")[0] ) == -1 )
			showAlert($( "#errorLocation" ), "Nome inválido.");
		else if ( validateDateField( $("#appointmentDate")[0] ) == -1 )
			showAlert($( "#errorAppointmentDate" ), "Data inválida.");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			$.ajax({
				url: "/appointment/search",
				type: "POST",
				data: jsonData,
				dataType: "json",
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
				url: "/appointment/search",
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
