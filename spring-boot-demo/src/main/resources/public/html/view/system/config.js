/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    $('#config').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        singleSelect:true,
        title:"配置信息",
        pagination:false,
        nowrap:false,
        columns:[[
            {field:'group',title:'组',width:'10%'},
            {field:'key',title:'KEY',width:'40%'},
            {field:'value',title:'值',width:'20%'},
            {field:'remark',title:'说明',width:'20%'},
            {field:'opt',title:'操作',width:'10%',formatter:function (value,row,index) {
                return  "<a href=# onclick=viewConfig("+index+")>修改</a> ";
            }}
        ]]
    });
    
    $("#group_").combobox({
    	 valueField:'id',
    	 textField:'text',
    	 onSelect: function(rec){
    		 queryConfig(rec.id);
    	 }
    });
    
    $("#group").combobox({
    	 valueField:'id',
    	 textField:'text',
    });
    
    $("#queryButton").click(queryConfig);
    $("#closeButton").click(function () {
        $('#w').window('close');
    });
    $("#saveButton").click(saveConfig);
    queryGroup();
});

function queryGroup() {
	var request={};
    request.service ="query_all_group";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
    	var data=new Array();
    	for(var i=0;i<rsp.groupList.length;i++){
    		var row={};
    		row.id=rsp.groupList[i];
    		row.text=rsp.groupList[i];
    		data[data.length]=row;
    	}
        $('#group_').combobox("loadData",data);
        $('#group').combobox("loadData",data);
    }
}
function queryConfig( group) {
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
function viewConfig(index){
    var row=$('#config').datagrid("getRows")[index];
    $("#rowIndex").val(index);
    $("#key").textbox("setValue",row.key);
    $("#remark").textbox("setValue",row.remark);
    $("#value").textbox("setValue",row.value);
    $('#key').textbox('disable');
    $('#group').combobox("select",row.group);
    $('#group').combobox('disable');
   
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
    $('#w').window('close');
}




