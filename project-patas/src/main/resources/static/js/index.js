function displayTodaysVaccinations(vacArray) {
	if (vacArray.length > 0) {
		$("#vacinacoes").html(
			$.map(vacArray, function (vac_info) {
				return '<a href="vaccination/vaccination_view.html?id=' + vac_info[0] + '">' + vac_info[1] + '</a><br/>';
			}).join(""));
	}
}

function displayTodaysVermifugations(vermArray) {
	if (vermArray.length > 0) {
		$("#vermifugacoes").html(
			$.map(vermArray, function (verm_info) {
				return '<a href="vermifuge/vermifuge_view.html?id=' + verm_info[0] + '">' + verm_info[1] + '</a><br/>';
			}).join(""));
	}
}

$(document).ready(function() {	
	var current_date = new Date();
		
	var jsonData = {};
	jsonData["nextApplicationDate"] = current_date.getTime();
	jsonData = JSON.stringify(jsonData);
	
	$.ajax({
		url: "/vaccination/search",
		type: "POST",
		dataType: "json",
		data: jsonData,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			displayTodaysVaccinations(response);
		}
	});
	
	$.ajax({
		url: "/vermifuge/search",
		type: "POST",
		dataType: "json",
		data: jsonData,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			displayTodaysVermifugations(response);
		}
	});
});