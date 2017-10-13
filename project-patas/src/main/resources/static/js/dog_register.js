
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

// protectNumericField := Object -> Boolean
// function used to prevent invalid characters being inserted inside the numeric fields
function protectNumericField(key) {
	var isDigit = (key.which >= 48 && key.which <= 57);
	var isSlash = (key.which == 191);
	var isBackspace = (key.which == 8);
	var isTab = (key.which == 9);
	var isArrow = (key.which >= 37 && key.which <= 40);
	var isDot = (key.which == 190);

	if (key.shiftKey || key.altKey)
		return false;
	else if (key.ctrlKey)
		return;
	else if (!isDigit && !isSlash && !isBackspace && !isTab && !isArrow && !isDot)
		return false;
}

// Document load script
$(document).ready(function() {
	// Prevent user from typing letters and other symbols into numeric fields
	$( "#weight" ).keydown(protectNumericField);
	$( "#birthDate" ).keydown(protectNumericField);
	$( "#castrationDate" ).keydown(protectNumericField);
	$( "#arrivalDate" ).keydown(protectNumericField);
	
	// Validate weight field
	$( "#weight" ).focusout(function () {
		if (!isFinite(this.value))
			$(this).addClass("error_input");
		else $(this).removeClass("error_input");
	});
	
	// Validate castration date field
	$( "#castrationDate" ).focusout(function() {
		if (!isDateValid(this.value) && this.value != "")
			$(this).addClass("error_input");
		else $(this).removeClass("error_input");
	});
	
	// Validate arrival date field
	$( "#arrivalDate" ).focusout(function() {
		if (!isDateValid(this.value))
			$(this).addClass("error_input");
		else $(this).removeClass("error_input");
	});
	
	// Validate birth and automatically compute age
	$( "#birthDate" ).focusout( function() {
		if (isDateValid(this.value)) {
			var age = computeAge(stringToDate(this.value));
			
			$(this).removeClass("error_input");
			$( "#age" ).val(age);
		}
		else if (this.value == "") 
			$(this).removeClass("error_input");
		else 
			$(this).addClass("error_input");
	});
	
	//Enable/disable the castration date field
	$( "#checkboxCastr" ).click(function() {
		if (this.checked)
			$( "#castrationDate" ).prop("disabled", false);
		else {
			$( "#castrationDate" ).prop("disabled", true);
			$( "#castrationDate" ).val("");
		}
	});
	
	//Enable/disable the disease description field
	$( "#checkboxDis" ).click(function() {
		if (this.checked)
			$( "diseaseDescription" ).prop("disabled", false);
		else {
			$( "diseaseDescription" ).prop("disabled", true);
			$( "diseaseDescription" ).val("");
		}
	});
	
	// Set the submit configuration for the form
	$( "#registerForm" ).submit(function(event) {
		event.preventDefault();
		
		// Form validation
		if ( $( "#name" ).val() == "" )
			alert("Preencha o nome do cachorro!");
		else if ( $( "#birthDate" ).hasClass("error_input") )
			alert("Data de nascimento inválida!");
		else if ( $( "#weight" ).hasClass("error_input") )
			alert("Peso inválido!");
		else if ( $( "#arrivalDate" ).hasClass("error_input") )
			alert("Data de chegada inválida!");
		else if ( $( "#arrivalDate" ).val() == "" )
			alert("Preencha a data de chegada!");
		else {
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
	
			// Post the data
			$.ajax({
				url: "http://localhost:8080/dog/register",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
			});
		}
	});
});
