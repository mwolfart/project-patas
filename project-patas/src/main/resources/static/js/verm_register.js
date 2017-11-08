$(document).ready(function() {
	$( "#vermName" ).keydown(protectStringField);
	$( "#amount" ).keydown(protectNumericField);
	$( "#appDate" ).keydown(protectDateField);
	$( "#nextAppDate" ).keydown(protectDateField);
});