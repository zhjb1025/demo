/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    $('#user').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        title:"用户信息",
        pagination:true,
        singleSelect:true,
        nowrap:false,
        columns:[[
            {field:'loginName',title:'登录名',width:'8%'},
            {field:'userName',title:'用户名',width:'8%'},
            {field:'branchName',title:'所属机构',width:'8%'},
            {field:'statusLabel',title:'状态',width:'5%'},
            {field:'role',title:'角色',width:'12%',formatter:function (value,row,index) {
                var val="";
                for(var i=0;i<row.roleInfoList.length;i++){
                    val=val+row.roleInfoList[i].roleName+"|"
                }
                return val.substring(0,val.length-1);
            }},
            {field:'createTime',title:'创建时间',width:'13%'},
            {field:'createUser',title:'创建人',width:'8%'},
            {field:'updateTime',title:'修改时间',width:'13%'},
            {field:'updateUser',title:'修改人',width:'8%'},
            {field:'opt',title:'操作',width:'17%',formatter:function (value,row,index) {
                if(row.status=="02"){
                    return "";
                }
                var update=" <a href=# onclick=viewUser("+index+")>修改</a> ";

                var reset=" | <a href=# onclick=resetPassword("+index+")>重置密码</a> ";
                var status=" | <a href=# onclick=updateStatus("+index+",'02')>注销</a> ";
                if(row.status=="00"){
                    status=status+" | <a href=# onclick=updateStatus("+index+",'01')>冻结</a> ";
                }else if(row.status=="01"){
                    status=status+" | <a href=# onclick=updateStatus("+index+",'00')>解冻</a> ";
                }
                return update+reset+status;
            }}
        ]]
    });
    var pager = $('#user').datagrid('getPager');
    pager.pagination({
        pageSize:20,
        pageList: [20,30,40,50],
        showRefresh:false,
        onSelectPage:function (pageNumber, pageSize) {
            queryUser(pageNumber,pageSize);
        }
    });

    $("#queryButton").click(queryUser);
    $("#closeButton").click(function () {
        $('#w').window('close');
    });
    $("#saveButton").click(saveUser);
    $("#addButton").click(function () {
        $("#loginName").textbox("setValue","");
        $("#userName").textbox("setValue","");
        $("#branchName").textbox("setValue","");

        $("#id").val("");
        $('#loginName').textbox('enable');
        $("#roleName").tagbox("clear");
        $("#branch_id").val("");
        $("#loginName").textbox("resetValidation");
        $("#userName").textbox("resetValidation");

        $('#w').window('open');
    });

    $('#branchTree').tree({
        onClick: function (node) {
            $("#branchName").textbox("setValue",node.text);
            $("#branch_id").val(node.id);
        }
    });
    queryUser(1,pager.pagination("options").pageSize);
    queryRole();
    queryBranchInfo();

});

function saveUser() {
    if( ! $('#ff').form('enableValidation').form('validate') ){
        return ;
    }
    if($("#branch_id").val()==""){
        $.messager.alert('提示信息',"请选择机构",'info');
        return;
    }
    if($('#roleName').tagbox("getValues").length==0){
        $.messager.alert('提示信息',"请选择角色",'info');
        return;
    }
    var request={};
    var id=$("#id").val();
    if(id==null || id==""){
        request.service ="add_user";
        request.loginName =$("#loginName").textbox("getValue");
    }else{
        request.service ="update_user";
        request.id=id;
    }
    request.userName =$("#userName").textbox("getValue");
    request.roleList=$('#roleName').tagbox("getValues");
    request.branchId=$("#branch_id").val();
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $.messager.alert('提示信息',rsp.rspMsg,'info');
    }
    var pageSize=$('#user').datagrid('getPager').pagination("options").pageSize;
    var pageNumber=$('#user').datagrid('getPager').pagination("options").pageNumber;
    if(id==null || id==""){
        pageNumber=1;
    }
    queryUser(pageNumber,pageSize);

    $('#w').window('close');
}
function queryUser(pageNumber,pageSize) {
    if(typeof(pageSize) == 'undefined' ){
        pageNumber=1;
        pageSize=pageSize=$('#user').datagrid('getPager').pagination("options").pageSize;
    }

    var request={};
    request.loginName=$("#queryLoginName").textbox("getValue");
    request.userName=$("#queryUserName").textbox("getValue");
    request.pageNumber=pageNumber;
    request.pageSize=pageSize;
    request.service ="page_query_user";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $('#user').datagrid("loadData",rsp);
    }
}
function viewUser(index){
    var row=$('#user').datagrid("getRows")[index];

    $("#loginName").textbox("setValue",row.loginName);
    $("#userName").textbox("setValue",row.userName);
    $("#branchName").textbox("setValue",row.branchName);
    $("#id").val(row.id);
    $("#branch_id").val(row.branchId);

    $('#roleName').tagbox("clear");
    for(var i=0;i<row.roleInfoList.length;i++){
        $('#roleName').tagbox("select",row.roleInfoList[i].id);
    }
    var node=$('#branchTree').tree("find",row.branchId);
    $('#branchTree').tree('select', node.target);
    $('#loginName').textbox('disable');
    $('#w').window('open');
}

function updateStatus(index,status) {
    var row=$('#user').datagrid("getRows")[index];
    var request={};
    request.id=row.id;
    request.status=status;
    request.service ="update_user_status";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }
    var pageSize=$('#user').datagrid('getPager').pagination("options").pageSize;
    var pageNumber=$('#user').datagrid('getPager').pagination("options").pageNumber;
    queryUser(pageNumber,pageSize);
}

function resetPassword(index) {
    var row=$('#user').datagrid("getRows")[index];
    var request={};
    request.id=row.id;
    request.status=status;
    request.service ="reset_password";
    var rsp=ajaxPostSynch(request);
    $.messager.alert('提示信息',rsp.rspMsg,'info');
}

function queryRole() {
    var request={};
    request.service ="query_all_role";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $('#roleName').tagbox("loadData",rsp.rows);

    }
}


function queryBranchInfo() {
    var request={};
    request.service ="query_all_branch";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }

    var branchList = new Array();
    var index=0;
    var nodeMap={};
    //var menuTree = $('#menu');
    for(var i=0;i<rsp.branchList.length;i++){
        if(typeof(rsp.branchList[i].parentId ) == undefined || rsp.branchList[i].parentId == null){
            var node ={};
            node.id=rsp.branchList[i].id;
            node.text=rsp.branchList[i].branchName;
            node.attributes=rsp.branchList[i].remark;
            node.children=new Array();
            branchList[index++]=node;
            nodeMap[node.id]=node;
        }
    }

    for(var i=0;i<rsp.branchList.length;i++){
        if(typeof(rsp.branchList[i].parentId ) == undefined || rsp.branchList[i].parentId == null){
            continue;
        }
        var node ={};
        node.id=rsp.branchList[i].id;
        node.text=rsp.branchList[i].branchName;
        node.attributes=rsp.branchList[i].remark;
        node.children=new Array();
        var pNode=nodeMap[rsp.branchList[i].parentId ];
        pNode.children[pNode.children.length]=node;
        nodeMap[node.id]=node;
    }
    var branchTree = $('#branchTree');
    branchTree.tree('loadData',branchList);
}


