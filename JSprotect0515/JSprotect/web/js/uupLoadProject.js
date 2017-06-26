function confirmOperation()
{
	document.getElementById("info").style.display = "none";
	document.getElementById('wrapper').style.opacity = 1;
	//document.getElementById("adb").disabled = false;
   // document.getElementById("obf").disabled = false;
	//document.getElementById("atp").disabled = false;
	document.getElementById("btnsubmit").disabled = false;
   	window.location.href = "index.jsp";
}

function callback(msg)
{
	if(msg == "success")
	{
		try{						
			document.getElementById('wrapper').style.opacity = 0.7;
			document.getElementById("info").style.display = "";
			document.getElementById("adb").disabled = true;
			document.getElementById("obf").disabled = true;
			document.getElementById("atp").disabled = true;
			document.getElementById("btnsubmit").disabled = true;
		}
		catch(e){
			console.log(e);
		}
		
	}
	else
		alert(msg);
}