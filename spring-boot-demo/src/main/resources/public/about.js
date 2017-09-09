$(document).ready(function(){
	var config=sessionStorage.getItem("CONFIG");
    if(config!=null){
    	var config=$.parseJSON(config);
    	$("#man").text("联系人："+config.man);
    	$("#phone").text("电话："+config.phone);
    	$("#fax").text("传真："+config.fax);
    	$("#mail").text("Email："+config.mail);
    	$("#content").html(config.content);
    }else{
    	document.location="index.html";
    }
    
})

