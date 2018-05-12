/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    $('#log').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        singleSelect:true,
        title:"日志信息",
        pagination:true,
        columns:[[
            {field:'startTimestampLabel',title:'时间',width:'15%'},
            {field:'seqNo',title:'流水号',width:'18%'},
            {field:'service',title:'服务名',width:'10%'},
            {field:'version',title:'版本号',width:'8%'},
            {field:'userId',title:'用户ID',width:'8%'},
            {field:'tradeStatusLabel',title:'处理状态',width:'8%'},
            {field:'dealTime',title:'耗时',width:'8%'},
            {field:'rspCode',title:'响应码',width:'5%'},
            {field:'rspMsg',title:'响应信息',width:'13%'},
            {field:'opt',title:'操作',width:'7%',formatter:function (value,row,index) {
                return  "<a href=# onclick=viewLog("+index+")>详细信息</a> ";
            }}
        ]]
    });
    var pager = $('#log').datagrid('getPager');
    pager.pagination({
        pageSize:15,
        pageList: [15,30,40,50],
        showRefresh:false,
        onSelectPage:function (pageNumber, pageSize) {
            queryLog(pageNumber,pageSize);
        }
    });

    $("#queryButton").click(queryLog);
    $("#closeButton").click(function () {
        $('#w').window('close');
    });

    $.extend($.fn.validatebox.defaults.rules, {
        startEnd: {
            validator: function(value, param){

                var start = $(param).datetimebox('getValue');
                var end = value;
                start=start.replace(/[- :]/g, "");
                end=end.replace(/[- :]/g, "");
                return start<=end;
            },
            message: '结束时间不能早于开始时间'
        },
        sameYear: {
            validator: function(value, param){
                var start = $(param).datetimebox('getValue');
                return start.substring(0,4)==value.substring(0,4);
            },
            message: '不能进行跨年查询'
        },
        sameMonth: {
            validator: function(value, param){
                var start = $(param).datetimebox('getValue');
                return start.substring(0,7)==value.substring(0,7);
            },
            message: '不能进行跨月查询'
        },
        intervalDay: {
            validator: function(value, param){
                var start = $(param[0]).datetimebox('getValue');
                start=$.fn.datebox.defaults.parser(start);
                var end=$.fn.datebox.defaults.parser(value);
                var interval=end-start;
                return (interval/1000/60/60/24)<param[1];
            },
            message: '查询跨度不能超过10天'
        }
    });
});

function queryLog(pageNumber,pageSize) {
    if( ! $('#ff').form('enableValidation').form('validate') ){
        return ;
    }
    if(typeof(pageSize) == 'undefined' ){
        pageNumber=1;
        pageSize=pageSize=$('#log').datagrid('getPager').pagination("options").pageSize;
    }
    var request={};
    request.startTime=$("#startTime").datetimebox("getValue");
    request.endTime=$("#endTime").datetimebox("getValue");
    request.queryService=$("#service").textbox("getValue");
    request.queryVersion=$("#version").textbox("getValue");
    request.queryUserId=$("#userId").textbox("getValue");
    request.dealTime=$("#dealTime").textbox("getValue");
    request.pageNumber=pageNumber;
    request.pageSize=pageSize;
    request.service ="eoms_page_query_access_log";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $('#log').datagrid("loadData",rsp);
    }
}
function viewLog(index){
    var row=$('#log').datagrid("getRows")[index];
    var data={};
    data.total=0;
    data.rows= new Array();
    var request=JSON.parse(row.request);
    for (x in request){
        var logRow={};
        logRow.name=x;
        logRow.value=request[x];
        logRow.group="请求参数";
        data.rows[data.rows.length]=logRow;

        //$('#pg').propertygrid('appendRow',logRow);
    }
    var response=JSON.parse(row.response);
    for (x in response){
        var logRow={};
        logRow.name=x;
        logRow.value=response[x];
        logRow.group="响应参数";
        data.rows[data.rows.length]=logRow;
        // $('#pg').propertygrid('appendRow',logRow);
    }
    data.total=data.rows.length;
    $('#pg').propertygrid("loadData",data);
    $('#w').window('open');
}




