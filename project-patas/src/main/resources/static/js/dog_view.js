
// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	var birthDate = new Date();
	var arrivalDate = new Date();
	birthDate.setTime(json.birthDate);
	arrivalDate.setTime(json.arrivalDate);
	
	$(" #name ").val(json.name);
	$(" #birthDate ").val(dateToString(birthDate));
	$(" #age ").val(computeAge(birthDate));
	$(" #weight ").val(json.weight);
	$(" #sex ").val(json.sex);
	$(" #size ").val(json.size);
	$(" #furColor ").val(json.furColor);
	$(" #arrivalDate ").val(dateToString(arrivalDate));
	$(" #sponsors ").val(json.sponsors);
	
	if (json.castrated == true) {
		$(" #checkboxCastr ").prop('checked', true);

		if (json.castrationDate) {
			var castrationDate = new Date();
			castrationDate.setTime(json.castrationDate);
			$(" #castrationDate ").val(dateToString(castrationDate));
		}
	}
	
	if (json.disease == true) {
		$(" #checkboxDis ").prop('checked', true);
		(json.diseaseDescription ? $(" #diseaseDescription ").val(json.diseaseDescription) : false );
	}
}

// Document load script
$(document).ready(function() {
	var dogId = getUrlParameter('id');
	
	if (dogId == 0)
		dogId = "1";
	
	$.ajax({
		url: "http://localhost:8080/dog/view",
		type: "POST",
		data: dogId,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			jsonToForm(response);
		},
		error: function(response) {
			alert(response.responseText);
		}
	});		
});