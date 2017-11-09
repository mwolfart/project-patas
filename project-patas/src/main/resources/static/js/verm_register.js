$(document).ready(function() {
	$( "#vermName" ).keydown(protectStringField);
	$( "#amount" ).keydown(protectNumericField);
	$( "#appDate" ).keydown(protectNumericField);
	$( "#nextAppDate" ).keydown(protectNumericField);

	$.ajax({
		url: "http://localhost:8080/dog/get",
		type: "GET",
		success: function(data) {
			console.log(data);
			//processReturnedData(data);
		}
	});
});