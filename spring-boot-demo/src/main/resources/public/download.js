$(document).ready(function(){
	var config=sessionStorage.getItem("CONFIG");
    if(config!=null){
    	var config=$.parseJSON(config);
    	$("#man").text("联系人："+config.man);
    	$("#phone").text("电话："+config.phone);
    	$("#fax").text("传真："+config.fax);
    	$("#mail").text("Email："+config.mail);
    	queryProductDownload();
    }else{
    	document.location="index.html";
    }
})

function queryProductDownload(){
	var data={};
    data.service ="query_product";
    data.type ="2";
    data.size =1000;
    ajaxPost(data,function(rsp){
    	if(rsp.tradeStatus==1){
            var download='<ul>';
            for(var i=0;i<rsp.list.length;i++){
            	download=download+'<li>['+rsp.list[i].typeName+']'+rsp.list[i].title+'<span><a href="upload/'+rsp.list[i].name+'" target="_bank">点击下载</a><span></li>';
            }
            download=download+'</ul>';
            $("#download").html(download);
        }
    });
}