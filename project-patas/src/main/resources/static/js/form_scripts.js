// getUrlParameter := String -> String
// gets an URL parameter. If it's absent, returns undefined.
function getUrlParameter(param) {
    var URL = decodeURIComponent(window.location.search.substring(1));
    var URL_variables = URL.split("&");
    var param_name;

    for (var i = 0; i < URL_variables.length; i++) {
    	param_name = URL_variables[i].split("=");

        if (param_name[0] === param) {
            return param_name[1];
        }
    }
    
    return undefined;
}

// getIdFromURLasString := Void -> String
// gets the id parameter from the URL. If absent or invalid, set default to 1.
function getIdFromURLasString() {
	var id = getUrlParameter('id');
	if (id === undefined || !(id == parseInt(id, 10)))
		return "1";
	else return id;
}

// arrayContains := Array, Object -> Boolean
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
	if (date_as_string == undefined || date_as_string == "" || date_as_string.indexOf(".") > -1)
		return false;
	
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
	var short_months = new Array(4,6,9,11);

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
// receives a date in a specific format (yyyy-mm-dd) and converts it to a string, in universal format for this project
// Potentially unused. Only used if we use datepicker.
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

// dateToString := Date -> String
// given a date object, return it as string, in dd/mm/yyyy format
function dateToString(date_as_date) {
	var day = date_as_date.getDate();
	var month = date_as_date.getMonth() + 1;
	var year = date_as_date.getFullYear();
	
	return day + "/" + month + "/" + year;
}

// integerToDate := Integer -> Date
// given an integer, return its corresponding date value
function integerToDate(date_as_int) {
	var date = new Date();
	date.setTime(date_as_int);
	return date;
}

// computeAge := Date -> String
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
	
	if (diff_month > 0 || (diff_month == 0 && diff_days >= 0)) {
		if (diff_year > 0)
			return diff_year + " anos";
		else if (diff_month > 0)
			return diff_month + " meses";
		else return diff_days + " dias";
	}
	else {
		if (diff_year - 1 > 0)
			return (diff_year - 1) + " anos";
		else return (12 - Math.abs(diff_month)) + " meses";
	}
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
		//Else, if not empty, it's a string
		else if (input.value != "") {
			JSON_data[input.name] = input.value;
		}
	});

	return JSON_data;
}

// protectNumericField := Object -> Boolean
// function used to prevent invalid characters being inserted inside the numeric fields
function protectNumericField(key) {
	var is_digit = (key.which >= 48 && key.which <= 57);
	var is_slash = (key.which == 191) || (key.which == 193);
	var is_backspace = (key.which == 8);
	var is_tab = (key.which == 9);
	var is_arrow = (key.which >= 37 && key.which <= 40);
	var is_dot = (key.which == 190);
	var is_del = (key.which == 46);
	var is_enter = (key.which == 13);

	if (key.shiftKey || key.altKey)
		return false;
	else if (key.ctrlKey)
		return;
	else if (!is_digit && !is_slash && !is_backspace && !is_tab && !is_arrow && !is_dot && !is_del && !is_enter)
		return false;
}

// protectStringField := Object -> Boolean
// function used to prevent invalid characters being inserted inside the string fields
// these invalid characters are characters that might break a JSON object
// for instance: { } : " '
function protectStringField(key) {
	var is_braces = (key.shiftKey && ((key.which >= 219) && (key.which <= 221)));
	var is_quotes = (key.shiftKey && (key.which == 222 || key.which == 192));
	var is_semicolon = (key.which == 59);
	var is_slash = !key.shiftKey  && ((key.which == 191) || (key.which == 193));	// used to replace commas
	
	if (is_braces || is_quotes || is_semicolon || is_slash)
		return false;
	else return true;
}

// protectSearchStringField := Object -> Boolean
// also prevents commas
function protectSearchStringField(key) {
	if (!protectStringField(key) || key.which == 188)
		return false;
	else return true;
}

//protectUserField := Object -> Boolean
//function used to prevent invalid characters being inserted inside the user fields
function protectUserField(key) {
	var is_alphanumeric = (key.which >= 48 && key.which <= 90);
	var is_backspace = (key.which == 8);
	var is_tab = (key.which == 9);
	var is_arrow = (key.which >= 37 && key.which <= 40);
	var is_del = (key.which == 46);
	var is_enter = (key.which == 13);

	if (key.shiftKey || key.altKey)
		return false;
	else if (key.ctrlKey)
		return;
	else if (!is_alphanumeric && !is_backspace && !is_tab && !is_arrow && !is_del && !is_enter)
		return false;
}

// isNatural := String -> Boolean
// checks if a given number (as string) is natural
function isNatural(num_str) {
	number = parseInt(num_str, 10);
	
	return (number >= 0 && number.toString() === num_str);
}

// validateNatNumberField := Object -> Integer
// if a given form object (edit box) is not a valid integer, an error class is added to it
function validateNatNumberField(field) {
	if (field.value == "") {
		$(field).removeClass("error_input");
		return 0;
	}
	else if (!isNatural(field.value)) {
		$(field).addClass("error_input");
		return -1;
	}
	else { 
		$(field).removeClass("error_input");
		return 1;
	}
}

// validateRealNumberField := Object -> Integer
// if a given form object (edit box) is not a valid real number, an error class is added to it
function validateRealNumberField(field) {
	if (field.value == "") {
		$(field).removeClass("error_input");
		return 0;
	}
	else if (!isFinite(field.value)) {
		$(field).addClass("error_input");
		return -1;
	}
	else {
		$(field).removeClass("error_input");
		return 1;
	}
}

//validateUserField := Object -> Integer
//if a given form object (edit box) is not a valid username/password, an error class is added to it
function validateUserField(field) {
	var available_chars = "abcdefghijklmnopqrstuvwxyz0123456789"
	if (field.value == "") {
		$(field).removeClass("error_input");
		return 0;
	}
	else {
		for (char in field.value) {
			if (available_chars.indexOf(field.value[char]) == -1) {
				$(field).addClass("error_input");
				return -1;
			}
		}
		
		$(field).removeClass("error_input");
		return 1;
	}
}

//validateCurrencyField := Object -> Integer
//if a given form object (edit box) is not a valid currency value, an error class is added to it
function validateCurrencyField(field) {
	if (field.value == "") {
		$(field).removeClass("error_input");
		return 0;
	}
	else if (!isFinite(field.value)) {
		$(field).addClass("error_input");
		return -1;
	}
	else if (isFinite(field.value) && field.value.split(".")[1].length > 2) {
		$(field).addClass("error_input");
		return -1;
	}
	else {
		$(field).removeClass("error_input");
		return 1;
	}
}

// validateDateField := Object -> Integer
// if a given form object (edit box) is not a valid date, an error class is added to it
function validateDateField(field) {
	if (field.value == "") {
		$(field).removeClass("error_input");
		return 0;
	}
	else if (!isDateValid(field.value)) {
		$(field).addClass("error_input");
		return -1;
	}
	else {
		$(field).removeClass("error_input");
		return 1;
	}
}

// validateStringField := Object -> Integer
// if a given form object (edit box) is not a valid string, an error class is added to it
// a valid string has only some set of characters in it.
function validateStringField(field) {	
	var value = field.value;
	
	if (value == "") {
		$(field).removeClass("error_input");
		return 0;
	}
	
	var restricted_chars = ":/\\;\'\"{}";
	
	for (char in value) {
		if (restricted_chars.indexOf(value[char]) > -1) {
			$(field).addClass("error_input");
			return -1;
		}
	}
	
	$(field).removeClass("error_input");
	return 1;
}

// validateSearchStringField := Object -> Integer
// also prevents commas
function validateSearchStringField(field) {
	var validation = validateStringField(field);
	var value = field.value
	
	if (validation == 1) {
		for (char in value) {
			if (value[char] == ',') {
				$(field).addClass("error_input");
				return -1;
			}
		}
	}
	return validation;
}

// showAlert : Object, String -> Void
// Show an error alert 
function showAlert(element, msg) {	
	element.text(msg);
	element.removeClass("disabled-alert");	
}

// hideAlert : Object -> Void
// Remove the error alert
function hideAlert(element) {	
	element.text("");
	element.addClass("disabled-alert");	
}

// putDogsInComboBox := Void -> Void
// given a list of dog ids and names, put them into the
// dogName combobox (vacination, vermifugation, appointments)
function putDogsInComboBox() {
	$.ajax({
		url: "/dog/get",
		type: "GET",
		success: function(dogs) {
			$.each(dogs, function(i, dog) {
				$( "#dogId" ).append($("<option>", {
					value: dog[0],
					text: dog[1]
				}));
			});
		}
	});
}

// putDogsInComboBoxAndSet := Long -> Void
// similar to putDogsInComboBox, but also sets the value of the combobox.
function putDogsInComboBoxAndSet(value) {
	$.ajax({
		url: "/dog/get",
		type: "GET",
		success: function(dogs) {
			$.each(dogs, function(i, dog) {
				$( "#dogId" ).append($("<option>", {
					value: dog[0],
					text: dog[1]
				}));
			});
			
			$( "#dogId" ).val(value);
		}
	});	
}

//readURL : Object -> Void
//Read an image file
function readURL(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$('.img-circle').attr('src', e.target.result).width(200)
					.height(200);
		};
		reader.readAsDataURL(input.files[0]);		
	}
}

//Format a date input
function formatDate(event, input){
    var key = event.keyCode || event.charCode;
    if (key == 8 || key == 46) return false;
    var strokes = input.val().length;
    if(strokes === 2 || strokes === 5){
        var thisVal = input.val();
        thisVal += '/';
        input.val(thisVal);
    }
}

//Store an image as byte arrays
function storeImage(input, callback) {
	if (input.files && input.files[0]) {
		var imageByteArray = [];
		var reader = new FileReader();
		reader.onloadend = function(evt) {
			if (evt.target.readyState == FileReader.DONE) {
		        var arrayBuffer = evt.target.result,
		        array = new Uint8Array(arrayBuffer);
		        for (var i = 0; i < array.length; i++) {
		        	imageByteArray.push(array[i]);
		        }
		        callback(imageByteArray);
			}
		}
		reader.readAsArrayBuffer(input.files[0]);
	} else callback([]);
}

// putDateInField : Object, Date -> Void
// Set a field to the specified date.
function putDateInField(field, date) {
	if (date) {
		var date2 = new Date();
		date2.setTime(date);
		field.val(dateToString(date2));
	}
}

// arrayToTable : String, Object[] -> Void
// Send the data received from the database to a table
// Used in search forms
function arrayToTable(table, table_type, entry_array, primary_field = "id") {
	$("tbody > tr").remove();
	if (entry_array.length > 0) {
		table.append(
				$.map(entry_array, function (entry) {
					if (entry.size < 2)
						return '<tr><td>Erro no carregamento do registro</td></tr>';
					
					var return_string = '<tr><td>';
					for (var iter = 1; iter < entry.length; iter++)
						return_string += entry[iter] + '</td><td>';
					
					return_string += '<a href="' + table_type + '_view.html?' + primary_field + '=' + entry[0] 
										+ '" class="btn" role="button">Visualizar</a></td></tr>';
					return return_string;
				}).join());
	} else {
		table.append('<tr><td colspan="5"> Nenhum registro encontrado. </td></tr>');
	}
	table.removeClass("disabled-table");
}

// Post a json object to a given url
// Used to simplify the calls to the server
// TODO: CHECK IF THIS HAS ANY USE
function ajaxPostJson(url, data, success_callback, error_callback) {
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		data: data,
		contentType: "application/json; charset=UTF-8",
		success: success_callback(),
		error: error_callback()
	});
}
