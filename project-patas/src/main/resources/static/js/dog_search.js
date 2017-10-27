//filterFields := Object -> Object
//given a Dog json, return only the fields we are interested in.
function filterFields(object) {
	return {"id": object.id, "name": object.name, "sex": object.sex, "arrivalDate": object.arrivalDate};
}

//processResponseJson := Object -> Object
//function used to filter the json returned by the query
//returns another json, containing only the fields we are interested in.
function processResponseJson(response) {
	$("tbody > tr").remove();
	
	$('#dogs').append(
			$.map(response, function (dog) {
				return '<tr><td>' +dog.name+ '</td><td>'+dog.sex+'</td><td>'+dog.arrivalDate+'</td><td>'
				+'<a href="dog_view.html?id='+dog.id+'" class="btn btn-info" role="button">Visualizar</a></td></tr>';
			}).join());
	$( "#dogs" ).removeClass("disabled-table");

}

// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields	
	$( "#name" ).keydown(protectStringField);
	
	$( "#birthYear" ).keydown(protectNumericField);
	$( "#arrivalYear" ).keydown(protectNumericField);
	
	$( "#birthYear" ).focusout( function() { validateNatNumberField(this) });
	$( "#arrivalYear" ).focusout( function() { validateNatNumberField(this) });

	// Set the submit configuration for the form
	$( "#dogSearchForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( $("#birthYear").hasClass("error_input") || 
				$("#arrivalYear").hasClass("error_input") )
			alert("Há erros nos dados preenchidos!");
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
					console.log(processResponseJson(response));
				}
			});			
		}
	});
	
});
