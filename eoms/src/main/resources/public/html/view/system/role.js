/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    $('#role').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        singleSelect:true,
        title:"角色信息",
        pagination:true,
        columns:[[
            {field:'roleName',title:'角色名称',width:'20%'},
            {field:'remark',title:'备注',width:'30%'},
            {field:'createTime',title:'创建时间',width:'13%'},
            {field:'createUser',title:'创建人',width:'8%'},
            {field:'updateTime',title:'修改时间',width:'13%'},
            {field:'updateUser',title:'修改人',width:'8%'},
            {field:'opt',title:'操作',width:'8%',formatter:function (value,row,index) {
                return  "<a href=# onclick=viewRole("+index+")>修改</a> ";
            }}
        ]]
    });
    var pager = $('#role').datagrid('getPager');
    pager.pagination({
        pageSize:20,
        pageList: [20,30,40,50],
        showRefresh:false,
        onSelectPage:function (pageNumber, pageSize) {
            queryRole(pageNumber,pageSize);
        }
    });

    $("#queryButton").click(queryRole);
    $("#closeButton").click(function () {
        $('#w').window('close');
    });
    $("#saveButton").click(saveRole);
    $("#addButton").click(function () {
        var nodes = $('#tree').tree('getChecked',['checked','indeterminate']);
        for(var i=0; i<nodes.length; i++){
            $('#tree').tree("uncheck",nodes[i].target);
        }
        $("#roleName").textbox("setValue","");
        $("#roleName").textbox("resetValidation");
        $("#remark").textbox("setValue","");
        $("#id").val("");
        $('#w').window('open');
    });

    queryRole(1,pager.pagination("options").pageSize);
    queryAllMenuApi();
});
function saveRole() {
    if( ! $('#ff').form('enableValidation').form('validate') ){
        return ;
    }
    var request={};
    var id=$("#id").val();
    if(id==null || id==""){
        request.service ="eoms_add_role";
    }else{
        request.service ="eoms_update_role";
        request.id=id;
    }
    request.roleName=$("#roleName").textbox("getValue");
    request.remark=$("#remark").textbox("getValue");
    request.apiIDs=new Array();
    request.menuIDs=new Array();
    var nodes = $('#tree').tree('getChecked',['checked','indeterminate']);
    for(var i=0; i<nodes.length; i++){
        if(nodes[i].attributes=='menu'){
            request.menuIDs[request.menuIDs.length]=nodes[i].id;
        }else{
            request.apiIDs[request.apiIDs.length]=nodes[i].id.replace(/api/, "");
        }
    }

    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $.messager.alert('提示信息',rsp.rspMsg,'info');
    }
    var pageSize=$('#role').datagrid('getPager').pagination("options").pageSize;
    var pageNumber=$('#role').datagrid('getPager').pagination("options").pageNumber;
    if(id==null || id==""){
        pageNumber=1;
    }
    queryRole(pageNumber,pageSize);
    $('#w').window('close');
}
function queryRole(pageNumber,pageSize) {
    if(typeof(pageSize) == 'undefined' ){
        pageNumber=1;
        pageSize=pageSize=$('#role').datagrid('getPager').pagination("options").pageSize;
    }
    var request={};
    request.roleName=$("#queryRoleName").textbox("getValue");
    request.pageNumber=pageNumber;
    request.pageSize=pageSize;
    request.service ="eoms_page_query_role";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $('#role').datagrid("loadData",rsp);
    }
}
function viewRole(index){
    var row=$('#role').datagrid("getRows")[index];
    var nodes = $('#tree').tree('getChecked',['checked','indeterminate']);
    for(var i=0; i<nodes.length; i++){
        $('#tree').tree("uncheck",nodes[i].target);
    }

    $("#roleName").textbox("setValue",row.roleName);
    $("#remark").textbox("setValue",row.remark);
    $("#id").val(row.id);
    var request={};
    request.roleID=row.id;
    request.service ="eoms_query_role_menu_api";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }

    for(var i=0;i<rsp.menuIDs.length;i++){
        var node = $('#tree').tree('find', rsp.menuIDs[i]);
        if($('#tree').tree('isLeaf',node.target)){
            $('#tree').tree('check',node.target);
        }
    }
    var apiIDMap={};
    for(var j=0;j<rsp.apiIDs.length;j++){
        //var node = $('#tree').tree('find', "api"+rsp.apiIDs[j]);
        //$('#tree').tree('check',node.target);
        apiIDMap["api"+rsp.apiIDs[j]]=rsp.apiIDs[j];
    }
    var root= $('#tree').tree('getRoots');

    for(var i=0;i<root.length;i++){
        var children=$('#tree').tree('getChildren',root[i].target);
        for(var j=0;j<children.length;j++){
            if($('#tree').tree('isLeaf',children[j].target) && apiIDMap[children[j].id]!=null){
                $('#tree').tree('check',children[j].target);
            }
        }
    }
    $('#w').window('open');
}

function queryAllMenuApi() {
    var request={};
    request.service ="eoms_query_all_menu_api";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        var menuList = new Array();
        var index=0;
        var nodeMap={};
        for(var i=0;i<rsp.menuInfoList.length;i++){
            if(rsp.menuInfoList[i].parentId == null){
                var node ={};
                node.id=rsp.menuInfoList[i].id;
                node.text=rsp.menuInfoList[i].menuName;
                node.attributes='menu';
                node.children=new Array();
                menuList[index++]=node;
                nodeMap[node.id]=node;
            }
        }

        for(var i=0;i<rsp.menuInfoList.length;i++){
            if(rsp.menuInfoList[i].parentId == null){
                continue;
            }
            var node ={};
            node.id=rsp.menuInfoList[i].id;
            node.text=rsp.menuInfoList[i].menuName;
            node.attributes='menu';
            node.children=new Array();
            if(rsp.menuInfoList[i].apiServiceInfos.length>0){
                for(var j=0;j<rsp.menuInfoList[i].apiServiceInfos.length;j++){
                    var n={};
                    n.id="api"+rsp.menuInfoList[i].apiServiceInfos[j].id;
                    n.text=rsp.menuInfoList[i].apiServiceInfos[j].remark;
                    n.attributes='api';
                    node.children[node.children.length]=n;
                }
            }
            var pNode=nodeMap[rsp.menuInfoList[i].parentId ];
            pNode.children[pNode.children.length]=node;
            nodeMap[node.id]=node;
        }
        var menuTree = $('#tree');
        menuTree.tree('loadData',menuList);
    }
}



