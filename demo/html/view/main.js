/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    var user=sessionStorage.getItem("LOGIN_USER");
    var userObject=$.parseJSON(user);
    $("#loginUserInfo").append(userObject.userName);
    $("#logout").click(function () {
        sessionStorage.clear();
        document.location="login.html";
    })
});



