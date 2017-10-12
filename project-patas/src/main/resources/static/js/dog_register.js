
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

// fixDateFormat := String -> String
// receives a date in a specific format and converts it to a string, in universal format for this project
// potentially unused. Only used if we use datepicker.
function fixDateFormat(date) {
	var parts = date.split("-");
	
	if (parts.length == 3) {
		var correctDate = parts[2] + "/" + parts[1] + "/" + parts[0];
		
		return correctDate;	
	}
}

// stringToDate := String -> Date
// given a date in string format, returns the date in Date format.
// if invalid, returns null.
function stringToDate(date_as_string) {
	if (!isDateValid(date_as_string))
		return null;
	
	var new_date = new Date();
	var date_str_parts = date_as_string.split("/");
	
	new_date.setDate(date_str_parts[0]);
	new_date.setMonth(date_str_parts[1] - 1);
	new_date.setFullYear(date_str_parts[2]);
	
	return new_date;	
}

// formToJson := Object -> Object
// recieves an Object (should be of a form) and returns a JSON object
// with the data that the form contains
function formToJson(form){
	JSON_data = {};

	form_data = $(form).serializeArray();
	
	form_data.forEach( function(input){
		//Test if value is a number (float)
		if (isFinite(input.value) && input.value != "")
			JSON_data[input.name] = parseFloat(input.value);
		//Test if value is a date
		else if (isDateValid(input.value) && input.value != "")
			JSON_data[input.name] = stringToDate(input.value).getTime();
		//Else, if not empty, it's a string or boolean
		else if (input.value != "")
			JSON_data[input.name] = input.value;
	});

	return JSON_data;
}

// computeAge := Date -> Integer
// given an arbitrary date, computes the age (difference between this date and the current one).
// If the given date is greater (more recent) than the current date, it uses the given date as "current date"
function computeAge(datee) {
	var cur_date = new Date();
	
	if (datee > cur_date) {
		cur_date = datee;
		datee = new Date();
	}
	
	var diff_year = cur_date.getFullYear() - datee.getFullYear();
	var diff_month = cur_date.getMonth() - datee.getMonth();
	var diff_days = cur_date.getDate() - datee.getDate();
	
	if (diff_month > 0 || (diff_month == 0 && diff_days >= 0))
		return diff_year;
	else return diff_year - 1;
}

// Document load script
$(document).ready(function() {
	// Prevent from user typing letters and other symbols to "weight" field
	// PROBLEM: you can use shift to insert symbols (in the digit keys)
	$( "#registerForm input[name=weight]" ).keydown(function (key) {
		var isDigit = (key.which >= 48 && key.which <= 57);
		var isDot = (key.which == 190);
		var isBackspace = (key.which == 8);
		var isTab = (key.which == 9);

		if (!isDigit && !isDot && !isBackspace && !isTab)
			return false;
	});

	//Automatically compute age
	$( "#registerForm input[name=birthDate]" ).focusout( function() {
		if (isDateValid(this.value)) {
			var age = computeAge(stringToDate(this.value));
			
			$( "#registerForm input[name=age]" ).val(age);
		}
	});
	
	//Enable/disable the castration date field
	$( "#checkboxCastr" ).click(function() {
		if (this.checked)
			$( "#registerForm input[name=castrationDate]" ).prop("disabled", false);
		else {
			$( "#registerForm input[name=castrationDate]" ).prop("disabled", true);
			$( "#registerForm input[name=castrationDate]" ).val("");
		}
	});
	
	//Enable/disable the disease description field
	$( "#checkboxDis" ).click(function() {
		if (this.checked)
			$( "#registerForm input[name=diseaseDescription]" ).prop("disabled", false);
		else {
			$( "#registerForm input[name=diseaseDescription]" ).prop("disabled", true);
			$( "#registerForm input[name=diseaseDescription]" ).val("");
		}
	});
	
	// Set the submit configuration for the form
	$( "#registerForm" ).submit(function(event) {
		event.preventDefault();
		
		// Convert form to json
		var jsonData = formToJson(this);
		
		// Fix the checkbox values within the json
		if ( document.getElementById("checkboxDis").checked )
			jsonData["disease"] = true;
		else jsonData["disease"] = false;
		
		if ( document.getElementById("checkboxCastr").checked )
			jsonData["castrated"] = true;
		else jsonData["castrated"] = false;
		
		// Fix JSON so it's in the right format
		jsonData = JSON.stringify(jsonData);
		
		console.log(jsonData);

		// Post the data
		$.ajax({
			url: "http://localhost:8080/dog/register",
			type: "POST",
			dataType: "json",
			data: jsonData,
			contentType: "application/json; charset=UTF-8",
		});
	});
});
