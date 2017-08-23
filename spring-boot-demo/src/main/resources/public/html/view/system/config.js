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
                return  "<a href=# onclick=viewLog("+index+")>修改</a> ";
            }}
        ]]
    });
    
    $("#group").combobox({
    	 valueField:'id',
    	 textField:'text',
    	 onSelect: function(rec){
    		 queryConfig(rec.id);
    	 }
    })
    $("#queryButton").click(queryConfig);
    $("#closeButton").click(function () {
        $('#w').window('close');
    });
    
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
function viewLog(index){
    var row=$('#log').datagrid("getRows")[index];
    var data={};
    data.total=0;
    data.rows= new Array()
    for (x in row.request){
        var logRow={};
        logRow.name=x;
        logRow.value=row.request[x];
        logRow.group="请求参数";
        data.rows[data.rows.length]=logRow;

        //$('#pg').propertygrid('appendRow',logRow);
    }
    for (x in row.response){
        var logRow={};
        logRow.name=x;
        logRow.value=row.response[x];
        logRow.group="响应参数";
        data.rows[data.rows.length]=logRow;
        // $('#pg').propertygrid('appendRow',logRow);
    }
    data.total=data.rows.length;
    $('#pg').propertygrid("loadData",data);
    $('#w').window('open');
}




