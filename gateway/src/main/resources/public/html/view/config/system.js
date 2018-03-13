/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    $('#system_info').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        singleSelect:true,
        title:"接入系统",
        pagination:false,
        nowrap:false,
        columns:[[
            {field:'systemCode',title:'系统编码',width:'40%'},
            {field:'systemName',title:'系统名称',width:'50%'},
            {field:'opt',title:'操作',width:'10%',formatter:function (value,row,index) {
                return  "<a href=# onclick=viewsystemInfo("+index+")>修改</a>";
            }}
        ]]
    });
    
    $("#queryButton").click(querySystemInfo);
    $("#closeButton").click(function () {
        $('#w').window('close');
    });
    $("#saveButton").click(saveConfig);
    $("#addButton").click(addSystemInfo);
    querySystemInfo();
});
function addSystemInfo(){
    $("#system_code").textbox("setValue","");
    $("#system_name").textbox("setValue","");
    $('#system_code').textbox('enable');
    $('#w').window('open');
}

function querySystemInfo() {
	var request={};
    request.service ="query_group_config";
    request.group=group;
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
    	var data={};
    	data.rows=rsp.configList;
    	$('#config').datagrid("loadData",data);
    }
}
function viewsystemInfo(index){
    var row=$('#system_info').datagrid("getRows")[index];
    $("#rowIndex").val(index);
    $("#system_code").textbox("setValue",row.systemCode);
    $("#system_name").textbox("setValue",row.systemName);
    $('#system_code').textbox('disable');
   
    $('#w').window('open');
}

function saveConfig(){
	if( ! $('#ff').form('enableValidation').form('validate') ){
	        return ;
	}
	var request={};
	var index= $("#rowIndex").val();
	if(index==null || index==""){
		request.service ="add_config";
		request.group=$("#group").textbox("getValue");
		request.key=$("#key").textbox("getValue");
	}else{
		request.service ="update_config";
		var row=$('#config').datagrid("getRows")[index];
		request.group=row.group;
		request.key=row.key;
	}
	request.value= $("#value").textbox("getValue");
	request.remark= $("#remark").textbox("getValue");
   
	var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $.messager.alert('提示信息',rsp.rspMsg,'info');
    }
    queryConfig( request.group);
    if(index==null || index==""){
    	queryGroup();
    }
    $('#w').window('close');
}


