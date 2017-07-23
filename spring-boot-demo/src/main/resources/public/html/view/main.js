/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    var user=sessionStorage.getItem("LOGIN_USER");
    userObject=$.parseJSON(user);
    $("#loginUserInfo").append(userObject.userName);
    $("#logout").click(function () {
        sessionStorage.clear();
        document.location="login.html";
    })

    //查询用户菜单信息
    queryMenuInfo();
});

function queryMenuInfo() {
    var request={};
    request.service ="query_user_menu";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus==1){
        $.messager.alert('提示信息',JSON.stringify(rsp),'info');
    }else {
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
    }
}



