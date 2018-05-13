/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    $('#api_info').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        singleSelect:true,
        title:"配置参数",
        pagination:true,
        nowrap:false,
        columns:[[
            {field:'apiCode',title:'接口编码',width:'30%'},
            {field:'apiName',title:'接口名称',width:'40%'},
            {field:'apiVersion',title:'接口版本号',width:'30%'},
            {field:'opt',title:'操作',width:'10%',formatter:function (value,row,index) {
                return  "<a href=# onclick=view("+index+")>修改</a> | <a href=# onclick=viewSystemInfo("+index+")>删除</a>";
            }}
        ]]
    });
    var pager = $('#api_info').datagrid('getPager');
    pager.pagination({
        pageSize:20,
        pageList: [20,30,40,50],
        showRefresh:false,
        onSelectPage:function (pageNumber, pageSize) {
        	query(pageNumber,pageSize);
        }
    });
    
   
    
    $("#queryButton").click(query);
    $("#closeButton").click(function () {
        $('#w').window('close');
    });
    $("#saveButton").click(save);
    $("#addButton").click(add);
    query(1,pager.pagination("options").pageSize);
});

function query(pageNumber,pageSize) {
	if(typeof(pageSize) == 'undefined' ){
        pageNumber=1;
        pageSize=$('#api_info').datagrid('getPager').pagination("options").pageSize;
    }
	var request={};
	request.pageNumber=pageNumber;
	request.pageSize=pageSize;
    request.service ="eoms_page_query_api_info";
    request.systemCode=$("#systemCode").combobox("getValue");
    request.configType=$("#configType").combobox("getValue");
    request.configCode=$("#configCode").textbox("getValue");
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
    	$('#config_info').datagrid("loadData",rsp);
    }
}
function add(){
	$("#rowIndex").val("");
    $("#system_code").combobox("setValue","");
    $('#system_code').combobox('enable');
    $("#system_code").combobox("resetValidation");
    
    $("#config_type").combobox("setValue","");
    $('#config_type').combobox('enable');
    $("#config_type").combobox("resetValidation");
    
    $('#config_code').textbox("setValue","");
    $('#config_code').textbox('enable');
    $("#config_code").textbox("resetValidation");
    
    $('#config_value').textbox("setValue","");
    $("#config_value").textbox("resetValidation");
    
    $('#remark').textbox("setValue","");
    
    $('#w').window('open');
}
function view(index){
    var row=$('#config_info').datagrid("getRows")[index];
    $("#rowIndex").val(index);
    
    
    $("#system_code").textbox("setValue",row.systemCode);
    $("#system_name").textbox("setValue",row.systemName);
    $('#system_code').textbox('disable');
    
    $("#system_code").combobox("setValue",row.systemCode);
    $('#system_code').combobox('disable');
    
    $("#config_type").combobox("setValue",row.configType);
    $('#config_type').combobox('disable');
    
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
		request.service ="eoms_add_system_info";
		
	}else{
		request.service ="eoms_update_system_info";
	}
	request.systemCode=$("#system_code").textbox("getValue");
	request.systemName=$("#system_name").textbox("getValue");
   
	var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $.messager.alert('提示信息',rsp.rspMsg,'info');
    }
    var pageSize=$('#system_info').datagrid('getPager').pagination("options").pageSize;
    var pageNumber=$('#system_info').datagrid('getPager').pagination("options").pageNumber;
    if(index==null || index==""){
        pageNumber=1;
    }
    query(pageNumber,pageSize);

    $('#w').window('close');
}


