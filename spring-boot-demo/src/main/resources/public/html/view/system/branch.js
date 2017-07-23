/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    //查询用户菜单信息
    queryBranchInfo();
});

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
        if(rsp.branchList[i].parentId == null){
            var node ={};
            node.id=rsp.branchList[i].id;
            node.text=rsp.branchList[i].branchName;
            node.children=new Array();
            branchList[index++]=node;
            nodeMap[node.id]=node;
        }
    }

    for(var i=0;i<rsp.branchList.length;i++){
        if(rsp.branchList[i].parentId == null){
            continue;
        }
        var node ={};
        node.id=rsp.branchList[i].id;
        node.text=rsp.branchList[i].branchName;
        var pNode=nodeMap[rsp.branchList[i].parentId ];
        pNode.children[pNode.children.length]=node;
    }
    var branchTree = $('#branchTree');
    branchTree.tree('loadData',branchList);
}



