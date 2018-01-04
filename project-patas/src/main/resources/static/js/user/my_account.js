// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	$(" #username ").val(json.username);
	$(" #fullName ").val(json.fullName);
}

$(document).ready(function() {
	// Protect fields
	$( "#fullName" ).keydown(protectStringField);

	// Validate fields
	$( "#fullName" ).focusout(function() {
		if (validateStringField(this) >= 0)
			hideAlert($( "#errorFullName" ));
	})	
	
	// Edit button onClick handler
	$(" #editBtn ").click(function() {
		$(" #fullName ").prop('disabled', false);
		
		$(" #editBtn ").prop('disabled', true);
		$(" #saveBtn ").prop('disabled', false);
		$(" #changePasswordBtn ").prop('disabled', true);
		$(" #deleteBtn ").prop('disabled', true);
	});
	
	$(" #changePasswordBtn ").click(function() {
		window.location.replace("/user/change_password.html");
	});
	
	// Delete button onClick handler
	$(" #deleteBtn ").click(function(event) {
		event.preventDefault();
		
		if (confirm("Tem certeza que deseja excluir sua conta?")) {
			var POST = getUrlParameter('id');
			
			var username = $.session.get("username");
			if (username != "") {
				$.ajax({
					url: "/user/delete",
					type: "POST",
					data: username,
					contentType: "application/json; charset=UTF-8",
					success: function(response) {
						alert("Conta excluída com sucesso!");
						window.location.replace("/login.html");
					},
					error: function(response) {
						alert(response.responseText);
					}
				});
			} else {
				window.location.replace("/login.html");
			}
		}
	});
	
	$( "#userEditForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateStringField( $( "#fullName" )[0] ) == -1 )
			showAlert($( "#errorFullName" ), "Nome contém caracteres inválidos.");
		else {
			// Convert form to json and fix its format
			var jsonData = formToJson(this);	
			
			// Store the id so we know which user to edit
			jsonData["username"] = $.session.get("username");
			if (jsonData["username"] == "")
				window.location.replace("/login.html");
			
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "/user/update",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					alert(response.responseText);
				},
				success: function() {
					// Reset the form to previous state
					$(" #fullName ").prop('disabled', true);
					
					$(" #editBtn ").prop('disabled', false);
					$(" #saveBtn ").prop('disabled', true);
					$(" #changePasswordBtn ").prop('disabled', false);
					$(" #deleteBtn ").prop('disabled', false);
				}
			});
		}
	});
	
	var username = $.session.get("username");
	
	if (username != "") {
		$.ajax({
			url: "/user/view",
			type: "POST",
			data: username,
			contentType: "application/json; charset=UTF-8",
			success: function(response) {
				jsonToForm(response);
			},
			error: function(response) {
				alert(response.responseText);
				$(" #editBtn ").prop("disabled", true);
				$(" #deleteBtn ").prop("disabled", true);
				$(" #changePasswordBtn ").prop("disabled", true);
			}
		});
	} else {
		window.location.replace("/login.html");
	}
});