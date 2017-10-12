$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/"
    }).then(function(data) { 
    	$("#register").click( function(){
    	
    		alert("Fui clicado");
    	}
    	
    	)
    });
});