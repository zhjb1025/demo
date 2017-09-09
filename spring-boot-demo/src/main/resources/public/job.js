$(document).ready(function(){
	var config=sessionStorage.getItem("CONFIG");
    if(config!=null){
    	var config=$.parseJSON(config);
    	$("#man").text("联系人："+config.man);
    	$("#phone").text("电话："+config.phone);
    	$("#fax").text("传真："+config.fax);
    	$("#mail").text("Email："+config.mail);
    	queryJob();
    }else{
    	document.location="index.html";
    }
})

function queryJob(){
	var data={};
    data.service ="query_job";
    ajaxPost(data,function(rsp){
    	if(rsp.tradeStatus==1){
            for(var i=0;i<rsp.list.length;i++){
            	var newRow = '<tr><td><a href="job_show.html?id='+rsp.list[i].id+'">'+rsp.list[i].title+'</a></td><td>'+rsp.list[i].endDate+'</td><td>'+rsp.list[i].post+'</td><td>'+rsp.list[i].workYear+'</td><td>'+rsp.list[i].education+'</td><td>'+rsp.list[i].number+'</td><tr>';
            	$("#job tr:last").after(newRow);
            }
        }
    });
}