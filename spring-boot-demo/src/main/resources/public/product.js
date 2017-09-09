$(document).ready(function(){
	var config=sessionStorage.getItem("CONFIG");
    if(config!=null){
    	var config=$.parseJSON(config);
    	$("#man").text("联系人："+config.man);
    	$("#phone").text("电话："+config.phone);
    	$("#fax").text("传真："+config.fax);
    	$("#mail").text("Email："+config.mail);
    	queryProduct();
    }else{
    	document.location="index.html";
    }
})

function queryProduct(){
	var data={};
    data.service ="query_product";
    data.type ="1";
    data.size =1000;
    ajaxPost(data,function(rsp){
    	if(rsp.tradeStatus==1){
            var product='<ul>';
            for(var i=0;i<rsp.list.length;i++){
            	product=product+'<li><dl><dd><h1><span>产品分类：'+rsp.list[i].typeName+'</span>产品名称:'+rsp.list[i].title+'</h1>';
            	product=product+'<p>'+rsp.list[i].info+'</p></dd><dt><div class="pic_box"><img src="upload/'+rsp.list[i].name+'"  width="100" height="100"/></div></dt></dl></li>';
            }
            product=product+'</ul>';
            $("#product").html(product);
        }
    });
}