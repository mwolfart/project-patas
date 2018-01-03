//processResponseJson := Object -> Object
//function used to filter the json returned by the query
//returns another json, containing only the fields we are interested in.
function processResponseJson(response) {
	$("tbody > tr").remove();
	
	$('#users').append(
			$.map(response, function (user_info) {				
				return '<tr><td>' + user_info[1] + '</td><td>'+ user_info[2] +'</td><td>'+ user_info[3] +'</td><td>'
				+'<a href="user_view.html?id='+ user_info[0] +'" class="btn btn-info" role="button">Visualizar</a></td></tr>';
			}).join());
	$( "#users" ).removeClass("disabled-table");

}

// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields	
	$( "#username" ).keydown(protectUserField);
	
	$( "#username" ).focusout( function() { 
		validateUserField(this);
		hideAlert($("#errorUsername"));
	});
	
	// Set the submit configuration for the form
	$( "#userSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateSearchStringField( $("#username")[0] ) == -1 )
			showAlert($( "#errorUsername" ), "Nome de usuário inválido.");
		else {
			jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
			
			$.ajax({
				url: "/user/search",
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
