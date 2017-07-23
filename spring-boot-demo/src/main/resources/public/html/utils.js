/**
 * Created by Auser on 2017/7/15.
 */

var serviceVersion="1.0.0";

function getSeqNo() {
    var  now = new Date().format("yyyyMMddhhmmss");
    var user=sessionStorage.getItem("LOGIN_USER");

    var  userId="000000";
    if(user!=null){
        var userObject=$.parseJSON(user);
        userId=userObject.userId;
    }
    var operateCount=sessionStorage.getItem("OPERATE_COUNT");
    if(operateCount==null){
        operateCount=0;
    }
    operateCount++;
    sessionStorage.setItem("OPERATE_COUNT",operateCount);


    var seqNo=now+prefixInteger(userId,6)+prefixInteger((operateCount%100),2);
    return seqNo;
}
function prefixInteger(num, length) {
    return (Array(length).join('0') + num).slice(-length);
}
/**
 * 时间对象的格式化;
 */
Date.prototype.format = function(format) {
    /*
     * eg:format="YYYY-MM-dd hh:mm:ss";
     */
    var o = {
        "M+" :this.getMonth() + 1, // month
        "d+" :this.getDate(), // day
        "h+" :this.getHours(), // hour
        "m+" :this.getMinutes(), // minute
        "s+" :this.getSeconds(), // second
        "q+" :Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" :this.getMilliseconds()
        // millisecond
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    }

    for ( var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}


function ajaxPost(data,callBack){
    data=fillData(data);
    var jsonString=JSON.stringify(data);
    $.ajax({
        type: "POST",
        dataType:"json" ,
        contentType: "application/json",
        url: "/gateway",
        data: jsonString,
        error:function(msg){
            var rsp={};
            rsp.rspCode="9999";
            rsp.rspMsg="系统异常 请联系相关人员";
            rsp.tradeStatus=3;
            callBack(rsp);
        },
        success: function(msg){
            callBack(msg);
        }
    });
}

function ajaxPostSynch(data){
    var rsp={};
    rsp.rspCode="9999";
    rsp.rspMsg="系统异常 请联系相关人员";
    rsp.tradeStatus=3;
    data=fillData(data);
    var jsonString=JSON.stringify(data);
    $.ajax({
        type: "POST",
        dataType:"json" ,
        async:false,
        contentType: "application/json",
        url: "/gateway",
        data: jsonString,
        success: function(msg){
            rsp=msg;
        }
    });
    return rsp;
}

function fillData(data){
    var user=sessionStorage.getItem("LOGIN_USER");
    if(user!=null){
        var userObject=$.parseJSON(user);
        data.token=userObject.token;
        data.userId=userObject.userId;
    }
    if(typeof(data.version) != undefined ){
        data.version=serviceVersion;
    }
    if(typeof(data.seqNo) != undefined){
        data.seqNo=getSeqNo();
    }
    return data;
}
