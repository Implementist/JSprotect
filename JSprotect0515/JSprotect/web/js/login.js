function query() {
    var username = document.getElementById("login_field").value;
    var password = document.getElementById("password").value;
    var xmlhttp;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    else
        xml = new ActiveXObject("Microsoft.XMLHTTP");
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var responseText = xmlhttp.responseText;
            //对从服务器传来的响应文本进行剪切
            responseText = responseText.trim();
            // console.log("ResponseText: " + responseText);

            switch (responseText) {
                case "-1":
                    alert("username doesn't matches with password. Please check and input again.");
                    break;
                case "0":
                    window.location.href = "index.jsp";
                    break;
                case "1":
                    window.location.href = "UserManagement.jsp";
                    break;
            }
            username.Value = "";
            password.Value = "";
        }

    };
    xmlhttp.open("POST", "verify.jsp", true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send("username=" + username + "&password=" + password);
}