$(document).ready(function(){
	var config=sessionStorage.getItem("CONFIG");
    if(config!=null){
    	var config=$.parseJSON(config);
    	$("#man").text("联系人："+config.man);
    	$("#phone").text("电话："+config.phone);
    	$("#fax").text("传真："+config.fax);
    	$("#mail").text("Email："+config.mail);
    	$("#job_mail").text("有意者将简历发送到邮箱："+config.mail);
    	getJob();
    }else{
    	document.location="index.html";
    }
})

function getJob(){
	var data={};
    data.service ="get_job";
    data.id=getUrlParam('id');
    ajaxPost(data,function(rsp){
    	if(rsp.tradeStatus==1){
    		$("#endDate").text(rsp.list[0].endDate);
    		$("#post").text(rsp.list[0].post);
    		$("#workYear").text(rsp.list[0].workYear);
    		$("#education").text(rsp.list[0].education);
    		$("#number").text(rsp.list[0].number);
    		$("#jobRequirements").html(rsp.list[0].jobRequirements);
    		$("#jobResponsibilities").html(rsp.list[0].jobResponsibilities);
        }
    });
}
//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}