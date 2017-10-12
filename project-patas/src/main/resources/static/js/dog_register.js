
//arrayContains := Array -> Boolean
// checks if a given array contains a given value
function arrayContains(array, value) {
	var len = array.length;
	var iter = 0;

	while (iter < len && array[iter] != value)
		iter++;

	return (iter < len);
}

// isDateValid := String -> Boolean
// checks if a given date (by string) is valid or not.
function isDateValid(date_as_string) {
	var parts = date_as_string.split("/");

	var year = parseInt(parts[2]);
	var month = parseInt(parts[1]);
	var day = parseInt(parts[0]);

	var year_is_valid = (year > 0);
	var month_is_valid = (month > 0 && month <= 12);

	if (!month_is_valid || !year_is_valid)
		return false;

	var day_is_valid;
	var long_months = new Array(1,3,5,7,8,10,12);
	var short_months = new Array(4,6,9,10);

	if (arrayContains(long_months, month) && day < 32 && day > 0)
		return true;
	else if (arrayContains(short_months, month) && day < 31 && day > 0)
		return true;
	else if (month == 2 && day < 30 && (year % 4 == 0) && day > 0)
		return true;
	else if (month == 2 && day < 29 && day > 0)
		return true;
	else return false;
}

// formToJson := Object -> Object
// recieves an Object (should be of a form) and returns a JSON object
// with the data that the form contains
function formToJson(form){
	JSON_data = {};

	form_data = $(form).serializeArray();

	form_data.forEach( function(input){
		//Test if value is a number (float)
		if (isFinite(input.value))
			JSON_data[input.name] = parseFloat(input.value);
		//Test if value is a date
		else if (isDateValid(input.value))
			JSON_data[input.name] = Date.parse(input.value);
		//Else, it's a string or boolean
		else JSON_data[input.name] = input.value;
	});

	return JSON_data;
}

// Document load script
$(document).ready(function() {
	// Prevent from user typing letters and other symbols to "weight" field
	// PROBLEM: you can use shift to insert symbols (in the digit keys)
	$( "#register_form input[name=weight]" ).keydown(function (key) {
		var isDigit = (key.which >= 48 && key.which <= 57);
		var isDot = (key.which == 190);
		var isBackspace = (key.which == 8);
		var isTab = (key.which == 9);

		if (!isDigit && !isDot && !isBackspace && !isTab)
			return false;
	});

	// Set the submit configuration for the form
	$( "#register_form" ).submit(function(event) {
		event.preventDefault();

		console.log(formToJson(this));

		// Post the data
		$.ajax({
			url: "http://localhost:8080/dogPost",
			type: "POST",
			dataType: "json",
			data: formToJson(this),
			contentType: "application/json; charset=UTF-8",
		});
	});
});
