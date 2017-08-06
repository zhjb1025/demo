/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){

    $('#branchTree').tree({
        animate:true,
        onClick: function (node) {
            showBranchInfo(node);
        },
        onContextMenu: function(e,node){
            e.preventDefault();
            showBranchInfo(node);
            $(this).tree('select',node.target);
            $('#mm').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        }
    });

    $("#saveButton").click(saveBranch);
    $("#addButton").click(addBranch);
    //查询用户菜单信息
    queryBranchInfo();
});
function addBranch(){
    $("#branchName").textbox("setValue","");
    $("#branchName").textbox("resetValidation");
    $("#remark").textbox("setValue","");
    $("#id").val("");
    var node=$('#branchTree').tree("getSelected");
    if(node==null){
        $.messager.alert('提示信息',"请选择机构",'info');
        return;
    }
    $("#parentName").textbox("setValue",node.text);
}
function saveBranch(){
    var node=$('#branchTree').tree("getSelected");
    if(node==null){
        $.messager.alert('提示信息',"请选择机构",'info');
        return;
    }
    if( ! $('#ff').form('enableValidation').form('validate') ){
        return ;
    }
    var request={};
    var id=$("#id").val();
    if(id==null || id==""){
        request.service ="add_branch";
        request.parentId =node.id;
    }else{
        request.service ="update_branch";
        request.id=id;
    }
    request.branchName=$("#branchName").textbox("getValue");
    request.remark=$("#remark").textbox("getValue");
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $.messager.alert('提示信息',rsp.rspMsg,'info');
    }
    $('#ff').form('clear');
    queryBranchInfo();
}


function showBranchInfo(node){
    $("#branchName").textbox("setValue",node.text);
    $("#remark").textbox("setValue",node.attributes);
    $("#id").val(node.id);

    var parentNode=$('#branchTree').tree("getParent",node.target);
    if(parentNode !=null && typeof(parentNode) != undefined ){
        $("#parentName").textbox("setValue",parentNode.text);
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



