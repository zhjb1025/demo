/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    $("#submitButton").click(submitForm);
    $("#clearButton").click(clearForm);
})


function submitForm(){
    if($('#ff').form('enableValidation').form('validate') ){
        var data={};
        data.service ="user_login";
        data.loginName =$('#loginName').val();
        data.pwd =$('#pwd').val();

        var rsp=ajaxPostSynch(data);
        if(rsp.tradeStatus==1){
            sessionStorage.setItem("LOGIN_USER",JSON.stringify(rsp));
            document.location="main.html";
        }else {
            $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        }
    }
}
function clearForm(){
    $('#ff').form('clear');
}