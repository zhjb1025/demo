/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    setMaxDigits(130);
    $("#submitButton").click(submitForm);
    $("#clearButton").click(clearForm);
})


function submitForm(){
    if($('#ff').form('enableValidation').form('validate') ){
        var data={};
        data.service ="user_login";
        data.loginName =$('#loginName').val();
        var key = new RSAKeyPair("10001","","cfcbc0ca7b1656934c219695216692989660918a8934f37f5cb9538a64ca4596953ebb71a93168b3c557d2daaf82f48101727d7fcdee1f47403b6674590727f53c1fc665bbdb3cc7b0aab969cdecf0d7ce8e56227d41f2b14e626a3762597a0b0bcda28bd85ea191914ebbadbfce40b0048a16b957f50f64da3d43f59cc66fe3",1024);
        var ciphertext = encryptedString(key, $('#pwd').val());
        data.pwd =ciphertext;

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