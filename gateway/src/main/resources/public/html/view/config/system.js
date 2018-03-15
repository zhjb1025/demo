/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    $('#system_info').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        singleSelect:true,
        title:"接入系统",
        pagination:true,
        nowrap:false,
        columns:[[
            {field:'systemCode',title:'系统编码',width:'40%'},
            {field:'systemName',title:'系统名称',width:'50%'},
            {field:'opt',title:'操作',width:'10%',formatter:function (value,row,index) {
                return  "<a href=# onclick=viewSystemInfo("+index+")>修改</a>";
            }}
        ]]
    });
    var pager = $('#system_info').datagrid('getPager');
    pager.pagination({
        pageSize:20,
        pageList: [20,30,40,50],
        showRefresh:false,
        onSelectPage:function (pageNumber, pageSize) {
        	querySystemInfo(pageNumber,pageSize);
        }
    });
    $("#queryButton").click(querySystemInfo);
    $("#closeButton").click(function () {
        $('#w').window('close');
    });
    $("#saveButton").click(saveSystemInfo);
    $("#addButton").click(addSystemInfo);
    querySystemInfo(1,pager.pagination("options").pageSize);
});
function addSystemInfo(){
    $("#system_code").textbox("setValue","");
    $("#system_name").textbox("setValue","");
    $('#system_code').textbox('enable');
    $("#rowIndex").val("");
    $("#system_code").textbox("resetValidation");
    $("#system_name").textbox("resetValidation");
    $('#w').window('open');
}

function querySystemInfo(pageNumber,pageSize) {
	if(typeof(pageSize) == 'undefined' ){
        pageNumber=1;
        pageSize=$('#system_info').datagrid('getPager').pagination("options").pageSize;
    }
	var request={};
	request.pageNumber=pageNumber;
	request.pageSize=pageSize;
    request.service ="page_query_system_info";
    request.systemCode=$("#systemCode").textbox("getValue");;
    request.systemName=$("#systemCode").textbox("getValue");;
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
    	$('#system_info').datagrid("loadData",rsp);
    }
}
function viewSystemInfo(index){
    var row=$('#system_info').datagrid("getRows")[index];
    $("#rowIndex").val(index);
    $("#system_code").textbox("setValue",row.systemCode);
    $("#system_name").textbox("setValue",row.systemName);
    $('#system_code').textbox('disable');
   
    $('#w').window('open');
}

function saveSystemInfo(){
	if( ! $('#ff').form('enableValidation').form('validate') ){
	        return ;
	}
	var request={};
	var index= $("#rowIndex").val();
	if(index==null || index==""){
		request.service ="add_system_info";
		
	}else{
		request.service ="update_system_info";
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
    querySystemInfo(pageNumber,pageSize);

    $('#w').window('close');
}


