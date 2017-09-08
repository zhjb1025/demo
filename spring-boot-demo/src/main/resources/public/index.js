/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
	queryConfig();
	queryProduct();
	queryProductDownload();
})


function queryConfig(){
	var data={};
    data.service ="query_config";
    var rsp=ajaxPost(data,function(rsp){
    	if(rsp.tradeStatus==1){
            sessionStorage.setItem("CONFIG",JSON.stringify(rsp.config));
            $("#titleName").text("欢迎访问"+rsp.config.name);
            $("#footName").text("版权所有:"+rsp.config.name);
            $("#summary").text(rsp.config.summary);
            $("#contact").html("联系人："+rsp.config.man+"<br>电话："+rsp.config.phone+"<br>传真："+rsp.config.fax+"<br>地址："+rsp.config.address);
        }
    });
}

function queryProduct(){
	var data={};
    data.service ="query_product";
    data.type ="1";
    data.size =3;
    var rsp=ajaxPost(data,function(rsp){
    	if(rsp.tradeStatus==1){
            var contentPic='<ul>';
            var contentList='<ul>';
            for(var i=0;i<rsp.list.length;i++){
            	contentPic=contentPic+'<li><div class="pic"><a href="#" target="_bank"><img src="upload/'+rsp.list[i].name+'" width="139" height="139" border="0" /></a></div></li>';
            	contentList=contentList+'<li><a href="#" target="_bank">'+rsp.list[i].title+'</a></li>';
            }
            contentPic=contentPic+'</ul>';
            contentList=contentList+'</ul>';
            $("#product_show").html(contentPic);
            $("#product_menu").html(contentList);
        }
    });
}

function queryProductDownload(){
	var data={};
    data.service ="query_product";
    data.type ="2";
    data.size =6;
    var rsp=ajaxPost(data,function(rsp){
    	if(rsp.tradeStatus==1){
            var download='<ul>';
            for(var i=0;i<rsp.list.length;i++){
            	download=download+'<li><a href="upload/'+rsp.list[i].name+'" target="_bank" class="download_ico">'+rsp.list[i].title+'</a></li>';
            }
            download=download+'</ul>';
            $("#download").html(download);
        }
    });
}

