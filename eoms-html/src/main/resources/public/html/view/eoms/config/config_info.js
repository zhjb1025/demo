/**
 * Created by Auser on 2017/7/15.
 */

var configType_;
$(document).ready(function(){
	configType_=getUrlParam("configType");
    $('#config_info').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        singleSelect:true,
        title:"参数配置",
        pagination:true,
        nowrap:false,
        columns:[[
            {field:'systemCode',title:'系统编码',width:'10%'},
            {field:'systemName',title:'系统名称',width:'10%'},
            {field:'configTypeLable',title:'类型',width:'10%'},
            {field:'configCode',title:'编码',width:'20%'},
            {field:'configValue',title:'值',width:'20%'},
            {field:'remark',title:'说明',width:'20%'},
            {field:'opt',title:'操作',width:'10%',formatter:function (value,row,index) {
                return  "<a href=# onclick=viewConfigInfo("+index+")>修改</a> | <a href=# onclick=viewSystemInfo("+index+")>删除</a>";
            }}
        ]]
    });
    var pager = $('#config_info').datagrid('getPager');
    pager.pagination({
        pageSize:20,
        pageList: [20,30,40,50],
        showRefresh:false,
        onSelectPage:function (pageNumber, pageSize) {
        	querySystemInfo(pageNumber,pageSize);
        }
    });
    
    $("#systemCode").combobox({
	   	 valueField:'id',
	   	 textField:'text'
    });
    $("#system_code").combobox({
	   	 valueField:'id',
	   	 textField:'text'
   });
    
    $("#queryButton").click(queryConfigInfo);
    $("#closeButton").click(function () {
        $('#w').window('close');
    });
    $("#saveButton").click(save);
    $("#addButton").click(addConfigInfo);
    querySystemInfo(1,10000);
    queryConfigInfo(1,pager.pagination("options").pageSize);
});

function getUrlParam(name){  
	//构造一个含有目标参数的正则表达式对象  
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	//匹配目标参数  
	var r = window.location.search.substr(1).match(reg);  
	//返回参数值  
	if (r!=null) return unescape(r[2]);  
	return null;  
}  
function queryConfigInfo(pageNumber,pageSize) {
	if(typeof(pageSize) == 'undefined' ){
        pageNumber=1;
        pageSize=$('#config_info').datagrid('getPager').pagination("options").pageSize;
    }
	var request={};
	request.pageNumber=pageNumber;
	request.pageSize=pageSize;
    request.service ="eoms_page_query_conifg_info";
    request.systemCode=$("#systemCode").combobox("getValue");
    request.configType=configType_;
    request.configCode=$("#configCode").textbox("getValue");
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
    	$('#config_info').datagrid("loadData",rsp);
    }
}
function addConfigInfo(){
	$("#rowIndex").val("");
    $("#system_code").combobox("setValue","");
    $('#system_code').combobox('enable');
    $("#system_code").combobox("resetValidation");
    
    $('#config_code').textbox("setValue","");
    $('#config_code').textbox('enable');
    $("#config_code").textbox("resetValidation");
    
    $('#config_value').textbox("setValue","");
    $("#config_value").textbox("resetValidation");
    $('#remark').textbox("setValue","");
    $('#w').window('open');
}

function querySystemInfo(pageNumber,pageSize) {
	
	var request={};
	request.pageNumber=pageNumber;
	request.pageSize=pageSize;
    request.service ="eoms_page_query_system_info";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
    	var data=new Array();
    	for(var i=0;i<rsp.rows.length;i++){
    		var row={};
    		row.id=rsp.rows[i].systemCode;
    		row.text=rsp.rows[i].systemName;
    		data[data.length]=row;
    	}
    	$('#systemCode').combobox("loadData",data);
    	$('#system_code').combobox("loadData",data);
    }
}
function viewConfigInfo(index){
    var row=$('#config_info').datagrid("getRows")[index];
    $("#rowIndex").val(index);
    
    
    $("#system_code").textbox("setValue",row.systemCode);
    $("#system_name").textbox("setValue",row.systemName);
    $('#system_code').textbox('disable');
    
    $("#system_code").combobox("setValue",row.systemCode);
    $('#system_code').combobox('disable');
 
    
    $('#config_code').textbox("setValue",row.configCode);
    $('#config_type').textbox('disable');
    
    $('#config_value').textbox("setValue",row.configValue);
    
    $('#remark').textbox("setValue",row.remark);
   
    $('#w').window('open');
}

function save(){
	if( ! $('#ff').form('enableValidation').form('validate') ){
	        return ;
	}
	var request={};
	var index= $("#rowIndex").val();
	if(index==null || index==""){
		request.service ="eoms_add_config_info";
		
	}else{
		var row=$('#config_info').datagrid("getRows")[index];
		request.id=row.id;
		request.service ="eoms_update_config_info";
	}
	request.configType=configType_;
	request.systemCode=$("#system_code").combobox("getValue");
	request.configCode=$("#config_code").textbox("getValue");
	request.configValue=$("#config_value").textbox("getValue");
	request.remark=$("#remark").textbox("getValue");
   
	var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $.messager.alert('提示信息',rsp.rspMsg,'info');
    }
    var pageSize=$('#config_info').datagrid('getPager').pagination("options").pageSize;
    var pageNumber=$('#config_info').datagrid('getPager').pagination("options").pageNumber;
    if(index==null || index==""){
        pageNumber=1;
    }
    queryConfigInfo(pageNumber,pageSize);
    $('#w').window('close');
}


