var username = getCookie("username");

// TODO: STORE PASSWORD HASH AND CHECK?
// TODO: READ MORE ABOUT ONLINE SECURITY

if (username == "")
	window.location.replace("/login.html");