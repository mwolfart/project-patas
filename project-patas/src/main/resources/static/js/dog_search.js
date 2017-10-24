// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields
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
			// Build the query
			var query = "SELECT name, sex, arrivalDate FROM Dog WHERE ";
			
			if ($( "#name" ).val() != "")
				query += "name = \"" + $( "#name" ).val() + "\" AND ";
			if ($( "#birthMonth" ).val() != "")
				query += "EXTRACT(MONTH FROM birthDate) = " + $( "#birthMonth" ).val() + " AND ";
			if ($( "#birthYear" ).val() != "")
				query += "EXTRACT(YEAR FROM birthDate) = " + $( "#birthYear" ).val() + " AND ";
			if ($( "#sex" ).val() != "")
				query += "sex = " + $( "#sex" ).val() + " AND ";
			if ($( "#arrivalDay" ).val() != "")
				query += "EXTRACT(DAY FROM arrivalDate) = " + $( "#arrivalDay" ).val() + " AND ";
			if ($( "#arrivalMonth" ).val() != "")
				query += "EXTRACT(MONTH FROM arrivalDate) = " + $( "#arrivalMonth" ).val() + " AND ";
			if ($( "#arrivalYear" ).val() != "")
				query += "EXTRACT(YEAR FROM arrivalDate) = " + $( "#arrivalYear" ).val() + " AND ";
			if ($( "#castrated" ).val() != "")
				query += "castrated = " + $( "#castrated" ).val() + " AND ";
			
			//Gambiarra
			query += "1 = 1";
			
			console.log(query);
			
			// Send the query to the backend and retrieve the data
			$.ajax({
				url: "http://localhost:8080/dog/search",
				type: "POST",
				dataType: "json",
				data: query,
				contentType: "application/json; charset=UTF-8"
			});
			
			// USED ONLY IF WE WANT TO BUILD THE QUERY IN THE BACKEND (which isn't the case for now)
			
			/*
			jsonData = formToJson(this);
			
			jsonData = JSON.stringify(jsonData);
			
			console.log(jsonData);
			
			
			// Get the data
			$.ajax({
				url: "http://localhost:8080/dog/search",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8"
			});
			*/
			
			
		}
	});
	
});
