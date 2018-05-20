/**
 * Created by Auser on 2017/7/15.
 */
$(document).ready(function(){
	$('#cancelParent').hide();
    $('#menuTree').tree({
        animate:true,
        onClick: function (node) {
        	 $('#cancelParent').hide();
        	 $("#parentId").val("");
            showMenufo(node);
        },
        onContextMenu: function(e,node){
            e.preventDefault();
            showMenufo(node);
            $(this).tree('select',node.target);
            $('#mm').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        }
    });
    
    $('#table_api').datagrid({
        rownumbers:true,
        toolbar:'#tb',
        title:"接口信息",
        pagination:true,
        nowrap:false,
        columns:[[
        	{field:'ck',checkbox:true,width:'10%'},
        	{field:'service',title:'接口编码',width:'45%'},
            {field:'remark',title:'接口名称',width:'35%'},
            {field:'version',title:'接口版本号',width:'10%'}
        ]],
        onSelect:function (index,row){
        	menuApiMap[row.id]=row;
        	showApi();
        	//matchApi();
        },
        onUnselect:function (index,row){
        	delete menuApiMap[row.id];
        	showApi();
        	//matchApi();
        },
        onLoadSuccess: function () {   //隐藏表头的checkbox
            $("#table_api").parent().find("div .datagrid-header-check").children("input[type=\"checkbox\"]").eq(0).attr("style", "display:none;");
        }
    });
    
    var pager = $('#table_api').datagrid('getPager');
    pager.pagination({
        pageSize:5,
        pageList: [5,10,20],
        showRefresh:false,
        onSelectPage:function (pageNumber, pageSize) {
        	queryApi(pageNumber,pageSize);
        }
    });
    
    $("#saveButton").click(save);
    $("#addButton").click(add);
    $("#deleteButton").click(deleteMenu);
    $("#queryButton").click(queryApi);
    
    $("#cancelParent").click(function () {
    	 
    	 $("#id").val("");
    	 $("#parentId").val("");
    	 $("#parentName").textbox("setValue","");
    });
    
    $("#api").tagbox({
    	valueField: 'id',
		textField: 'remark',
		onRemoveTag:function(id){
			delete menuApiMap[id]; 
			matchApi();
		}
    });
    //查询用户菜单信息
    query();
    queryApi(1,pager.pagination("options").pageSize);
   
});

/**
 * 选中菜单对应API信息
 */
var menuApiMap={};

function deleteMenu(){
	var id=$("#id").val();
	var request={};
	request.id=id;
	request.service ="eoms_delete_menu";
	var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $.messager.alert('提示信息',rsp.rspMsg,'info');
    }
    query();
    
}
function add(){
	menuApiMap={};
	$("#menuName").textbox("setValue","");
	$("#url").textbox("setValue","");
    $("#menuCode").textbox("setValue","");
    $("#id").val("");
   
    var node=$('#menuTree').tree("getSelected");
    if(node==null){
        $.messager.alert('提示信息',"父菜单",'info');
        return;
    }
    $("#parentId").val(node.id);
    $("#parentName").textbox("setValue",node.text);
    $('#cancelParent').show();
    $('#table_api').datagrid("unselectAll");
    $('#api').tagbox("clear");
    $("#menuName").textbox("resetValidation");
    $("#menuCode").textbox("resetValidation");
    $("#api").tagbox("resetValidation");
}

function save(){
    if( ! $('#ff').form('enableValidation').form('validate') ){
        return ;
    }
    var request={};
    var id=$("#id").val();
    if(id==null || id==""){
        request.service ="eoms_add_menu";
        request.parentId =$("#parentId").val();
    }else{
        request.service ="eoms_update_menu";
        request.id=id;
        request.parentId =$("#parentId").val();
    }
    request.menuName=$("#menuName").textbox("getValue");
    request.menuCode=$("#menuCode").textbox("getValue");
    request.url=$("#url").textbox("getValue");
    request.api= $('#api').tagbox("getValues");
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
        $.messager.alert('提示信息',rsp.rspMsg,'info');
    }
    $('#ff').form('clear');
    $('#table_api').datagrid("unselectAll");
    $('#api').tagbox("clear");
    $("#menuName").textbox("resetValidation");
    $("#menuCode").textbox("resetValidation");
    $("#api").tagbox("resetValidation");
    query();
   
}

function queryMenuApi(id) {
	var request={};
	request.service ="eoms_query_menu_api";
    request.id=id;
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
    	 menuApiMap={};
    	 for(var i=0;i<rsp.rows.length;i++){
    		 menuApiMap[rsp.rows[i].id]=rsp.rows[i];
    	 }
    	 showApi();
    	 matchApi();
    }
}
function showApi(){
	//清除原来的数据
	$('#api').tagbox("clear");
	var apiList = new Array();
	var rows=new Array();
	for(var arr in menuApiMap){
		rows[rows.length]=menuApiMap[arr];
		apiList[apiList.length]=menuApiMap[arr].id;
	}
	
	//显示新的API
	$('#api').tagbox("loadData",rows);
	$('#api').tagbox("setValues",apiList);
}
function matchApi(){
	$('#table_api').datagrid("unselectAll");
	var rows=$('#table_api').datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		 if(menuApiMap[rows[i].id]!=null){
			 $('#table_api').datagrid("selectRow",i);
		 }
	}
}
function queryApi(pageNumber,pageSize) {
	var request={};
	if(typeof(pageSize) == 'undefined' ){
        pageNumber=1;
        pageSize=$('#table_api').datagrid('getPager').pagination("options").pageSize;
    }
	request.pageNumber=pageNumber;
	request.pageSize=pageSize;
    request.service ="eoms_page_query_api_info";
    request.apiCode=$("#apiCode").textbox("getValue");
    request.apiName=$("#apiName").textbox("getValue");
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }else{
    	$('#table_api').datagrid("loadData",rsp);
    	matchApi();
    }
}

function showMenufo(node){
    $("#menuName").textbox("setValue",node.text);
    var temp=node.attributes.split(":");
    $("#menuCode").textbox("setValue",temp[0]);
    if(temp[1] != 'undefined'){
    	$("#url").textbox("setValue",temp[1]);
    }else{
    	$("#url").textbox("setValue",null);
    }
    $("#id").val(node.id);
    var parentNode=$('#menuTree').tree("getParent",node.target);
    if(parentNode !=null && typeof(parentNode) != undefined ){
        $("#parentName").textbox("setValue",parentNode.text);
    }else{
    	$("#parentName").textbox("setValue","");
    }
    queryMenuApi(node.id);
}
function query() {
    var request={};
    request.service ="eoms_query_all_menu";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }

    var menuList = new Array();
    var index=0;
    var nodeMap={};
    for(var i=0;i<rsp.menuInfoList.length;i++){
        if(rsp.menuInfoList[i].parentId == null){
            var node ={};
            node.id=rsp.menuInfoList[i].id;
            node.text=rsp.menuInfoList[i].menuName;
            node.attributes=rsp.menuInfoList[i].menuCode+":"+rsp.menuInfoList[i].url;
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
        node.attributes=rsp.menuInfoList[i].menuCode+":"+rsp.menuInfoList[i].url;
        node.children=new Array();
        var pNode=nodeMap[rsp.menuInfoList[i].parentId ];
        pNode.children[pNode.children.length]=node;
        nodeMap[node.id]=node;
    }
    var menuTree = $('#menuTree');
    menuTree.tree('loadData',menuList);
}



