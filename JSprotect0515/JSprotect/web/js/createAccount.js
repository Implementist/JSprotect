function check(){
	if(document.getElementById('user_login').value == ""){
		alert("Please input username");
		return ;
	}
	if(document.getElementById('user_email').value == "")
	{
		alert("Please input the Email Address");
		return ;
	}
	else
	{
		/* Check the email address*/
	}
	if(document.getElementById('user_password').value == "")
	{
		alert("Please input the password");
		return ;
	}
	if(document.getElementById('user_password').value == "")
	{
		alert("Please input the password");
		return ;
	}		
	if(document.getElementById('user_password_confirmation').value == "")
	{
		alert("Please confirm your password");
		return;
	}
	if(document.getElementById('user_password').value != document.getElementById('user_password_confirmation').value)
	{
		alert("The confirm password is inconsistent with the passowrd you input");
		document.getElementById('user_password').value = "";
		document.getElementById('user_password_confirmation').value = "";
		return;
	}
	
	create(document.getElementById('user_login').value, document.getElementById('user_email').value, document.getElementById('user_password').value);
}
function create(username, emailAddress, password){
	var xmlhttp;
	if(window.XMLHttpRequest)
	{
		xmlhttp = new XMLHttpRequest();
	}
	else
		xml = new ActiveXObject("Microsoft.XMLHTTP");
	xmlhttp.onreadystatechange = function()
	{
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200)
		{
			if(xmlhttp.responseText == "EXIST"){
				alert("The username exists, please change another one");
				document.getElementById('user_login').value = "";
			}
			else if(xmlhttp.responseText = "SUCC_CREATE")
				{	
					alert("Your account are successfully created, please sign in");
					window.location.href = "login.jsp";
				}
			else {
				alert("Create failure, please create once again.");
				clear();		
			}

		}

	}
	xmlhttp.open("POST","createAccount.jsp",true);
	xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlhttp.send("username=" + username + "&password=" + password + "&emailAddress=" + emailAddress);
}
function clear(){
	document.getElementById('user_login').value = "";
	document.getElementById('user_email').value = "";
	document.getElementById('user_password').value = "";
	document.getElementById('user_password_confirmation').value = "";				
}