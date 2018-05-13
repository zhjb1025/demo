/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    $('#table_template').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        singleSelect:true,
        title:"配置参数",
        pagination:true,
        nowrap:false,
        columns:[[
        	{field:'id',title:'ID',width:'5%'},
            {field:'service',title:'接口编码',width:'25%'},
            {field:'remark',title:'接口名称',width:'35%'},
            {field:'version',title:'接口版本号',width:'20%'},
            {field:'opt',title:'操作',width:'15%',formatter:function (value,row,index) {
                return  "<a href=# onclick=view("+index+")>修改</a> ";
            }}
        ]]
    });
    var pager = $('#table_template').datagrid('getPager');
    pager.pagination({
        pageSize:15,
        pageList: [15,30,40,50],
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
        pageSize=$('#table_template').datagrid('getPager').pagination("options").pageSize;
    }
	var request={};
	request.pageNumber=pageNumber;
	request.pageSize=pageSize;
    request.service ="eoms_page_query_api_info";
    request.apiCode=$("#apiCode").textbox("getValue");
    request.apiName=$("#apiName").textbox("getValue");
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
    	$('#table_template').datagrid("loadData",rsp);
    }
}
function add(){
	$("#rowIndex").val("");
    $("#api_code").textbox("setValue","");
    $('#api_code').textbox('enable');
    $("#api_code").textbox("resetValidation");
    
    $("#api_name").textbox("setValue","");
    $('#api_name').textbox('enable');
    $("#api_name").textbox("resetValidation");
    
    $("#api_version").textbox("setValue","");
    $('#api_version').textbox('enable');
    $("#api_version").textbox("resetValidation");
    
    $('#w').window('open');
}
function view(index){
    var row=$('#table_template').datagrid("getRows")[index];
    $("#rowIndex").val(index);
    
    
    $("#api_code").textbox("setValue",row.service);
    $("#api_name").textbox("setValue",row.remark);
    $("#api_version").textbox("setValue",row.version);
    $('#w').window('open');
}

function save(){
	if( ! $('#ff').form('enableValidation').form('validate') ){
	        return ;
	}
	var request={};
	var index= $("#rowIndex").val();
	if(index==null || index==""){
		request.service ="eoms_add_api_info";
		
	}else{
		request.service ="eoms_update_api_info";
		var row=$('#table_template').datagrid("getRows")[index];
		request.id=row.id;
	}
	request.apiCode=$("#api_code").textbox("getValue");
	request.apiName=$("#api_name").textbox("getValue");
	request.apiVersion=$("#api_version").textbox("getValue");
   
	var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $.messager.alert('提示信息',rsp.rspMsg,'info');
    }
    var pageSize=$('#table_template').datagrid('getPager').pagination("options").pageSize;
    var pageNumber=$('#table_template').datagrid('getPager').pagination("options").pageNumber;
    if(index==null || index==""){
        pageNumber=1;
    }
    query(pageNumber,pageSize);

    $('#w').window('close');
}


