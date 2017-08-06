/**
 * Created by Auser on 2017/7/15.
 */


$(document).ready(function(){
    var user=sessionStorage.getItem("LOGIN_USER");
    userObject=$.parseJSON(user);
    $("#loginUserInfo").append(userObject.userName);
    $("#logout").click(function () {
        sessionStorage.clear();
        document.location="login.html";
    })
    $('#treeMenu').tree({
        animate:true,
        onClick: function (node) {
            if(node.attributes!=null){
                addTab(node.text,node.attributes);
            }
        }
    });

    $("#modifyPassword").click(function () {
        $("#old_password").passwordbox("setValue","");
        $("#new_password").passwordbox("setValue","");
        $("#pass").passwordbox("setValue","");

        $("#old_password").passwordbox("resetValidation");
        $("#new_password").passwordbox("resetValidation");
        $("#pass").passwordbox("resetValidation");
        $('#w').window('open');
    })

    $("#closeButton").click(function () {
        $('#w').window('close');
    });

    $("#saveButton").click(function () {
        modifyPassword();
    });

    $.extend($.fn.validatebox.defaults.rules, {
        confirmPass: {
            validator: function(value, param){
                var pass = $(param).passwordbox('getValue');
                return value == pass;
            },
            message: '两次输入的密码不一致'
        }
    });

    $.extend($.fn.validatebox.defaults.rules, {
        password: {
            validator: function(value,para){
                var patt= /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*_]+$)[a-zA-Z\d!@#$%^&*_]+$/;
                return patt.test(value);
            },
            message: "密码不合规，必须是字母+数字，字母+特殊字符，数字+特殊字符"
        }
    });
    //查询用户菜单信息
    queryMenuInfo();
});

function queryMenuInfo() {
    var request={};
    request.service ="query_user_menu";
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }

    var menuList = new Array();
    var index=0;
    var nodeMap={};
    //var menuTree = $('#menu');
    for(var i=0;i<rsp.menuInfoList.length;i++){
        if(rsp.menuInfoList[i].parentId == null){
            var node ={};
            node.id=rsp.menuInfoList[i].id;
            node.text=rsp.menuInfoList[i].menuName;
            node.attributes=rsp.menuInfoList[i].url;
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
        node.attributes=rsp.menuInfoList[i].url;
        node.children=new Array();
        var pNode=nodeMap[rsp.menuInfoList[i].parentId ];
        pNode.children[pNode.children.length]=node;
        nodeMap[node.id]=node;
    }
    var menuTree = $('#treeMenu');
    menuTree.tree('loadData',menuList);
}


function addTab(subtitle,url){
    if(!$('#tabs').tabs('exists',subtitle)){
        $('#tabs').tabs('add',{
            title:subtitle,
            content:createFrame(url),
            closable:true,
            width:$('#mainPanle').width()-10,
            height:$('#mainPanle').height()-100
        });
    }else{
        $('#tabs').tabs('select',subtitle);
    }

}

function createFrame(url) {
    var s = '<iframe name="mainFrame" scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
    return s;
}

function modifyPassword() {
    if( ! $('#ff').form('enableValidation').form('validate') ){
        return ;
    }
    var request={};
    request.service ="modify_password";
    request.password=$("#old_password").passwordbox('getValue');
    request.newPassword=$("#new_password").passwordbox('getValue');
    var rsp=ajaxPostSynch(request);
    if(rsp.tradeStatus!=1){
        $.messager.alert('错误提示信息',rsp.rspMsg,'error');
        return ;
    }
    $.messager.alert('提示信息',rsp.rspMsg,'info');
    $('#w').window('close');
}



