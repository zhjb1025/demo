/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    $('#role').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        title:"角色信息",
        pagination:true,
        columns:[[
            {field:'roleName',title:'角色名称',width:'20%'},
            {field:'remark',title:'备注',width:'30%'},
            {field:'createTime',title:'创建时间',width:'13%'},
            {field:'createUser',title:'创建人',width:'8%'},
            {field:'updateTime',title:'修改时间',width:'13%'},
            {field:'updateUser',title:'修改人',width:'8%'},
            {field:'opt',title:'操作',width:'8%'}
        ]]
    });
    var pager = $('#role').datagrid('getPager');
    pager.pagination({
        pageSize:10,
        pageList: [10,20,30,50],
        showRefresh:false,
        onSelectPage:function (pageNumber, pageSize) {
            alert("onSelectPage:"+pageSize);
        },
        onRefresh:function (pageNumber, pageSize) {
            alert("onRefresh:"+pageNumber+","+pageSize);
        }
    });

    $("#queryButton").click(queryRole);
    queryRole();

});

function queryRole() {
    var request={};
    request.roleName=$("#roleName").textbox("getValue");
    request.pageNumber=0;
    request.pageSize=10;
    request.service ="page_query_role";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $('#role').datagrid("loadData",rsp);
    }
}

