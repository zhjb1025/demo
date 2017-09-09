var config;
$(document).ready(function(){
	var config=sessionStorage.getItem("CONFIG");
    if(config!=null){
    	var config=$.parseJSON(config);
    	$("#man").text("联系人："+config.man);
    	$("#phone").text("电话："+config.phone);
    	$("#fax").text("传真："+config.fax);
    	$("#mail").text("Email："+config.mail);
    	$("#name").text(config.name);
    	$("#address").text(config.address);
    	$("#postCode").text(config.postCode);
    	$("#config_man").text(config.man);
    	$("#config_phone").text(config.phone);
    	$("#config_fax").text(config.fax);
    	$("#config_mail").text(config.mail);
    	
    	$("#factoryMan").text(config.factoryMan);
    	$("#factoryPhone").text(config.factoryPhone);
    	$("#factoryFax").text(config.factoryFax);
    	$("#factoryAddress").text(config.factoryAddress);
    	
    	
    }else{
    	document.location="index.html";
    }
    
    initMapFactory();//创建和初始化地图
    initMapCompany();//创建和初始化地图
})


//创建和初始化地图函数：
 var companyMap;
function initMapCompany(){
    createMapCompany();//创建地图
    setMapEventCompany();//设置地图事件
    addMapControlCompany();//向地图添加控件
    addMarkerCompany();//向地图中添加marker
}

//创建地图函数：
function createMapCompany(){
    var map = new BMap.Map("company");//在百度地图容器中创建一个地图
    var point = new BMap.Point(114.221121,22.725952);//定义一个中心点坐标
    map.centerAndZoom(point,17);//设定地图的中心点和坐标并将地图显示在地图容器中
    companyMap = map;//将map变量存储在全局
}

//地图事件设置函数：
function setMapEventCompany(){
    companyMap.enableDragging();//启用地图拖拽事件，默认启用(可不写)
    companyMap.disableScrollWheelZoom();//禁用地图滚轮放大缩小，默认禁用(可不写)
    companyMap.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
    companyMap.enableKeyboard();//启用键盘上下左右键移动地图
}

//地图控件添加函数：
function addMapControlCompany(){
    //向地图中添加缩放控件
		var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_LEFT,type:BMAP_NAVIGATION_CONTROL_LARGE});
		companyMap.addControl(ctrl_nav);
            //向地图中添加比例尺控件
		var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT});
		companyMap.addControl(ctrl_sca);
}

//标注点数组
var markerArrCompany = [{title:"公司地址",content:"电话：0755-89312316<br/>地址：中国广东深圳市龙岗区坪地富心路27路",point:"114.303934|22.758956",isOpen:1,icon:{w:23,h:25,l:46,t:21,x:9,lb:12}}];
//创建marker
function addMarkerCompany(){
    for(var i=0;i<markerArrCompany.length;i++){
        var json = markerArrCompany[i];
        var p0 = json.point.split("|")[0];
        var p1 = json.point.split("|")[1];
        var point = new BMap.Point(p0,p1);
					var iconImg = createIconCompany(json.icon);
        var marker = new BMap.Marker(point,{icon:iconImg});
					var iw = createInfoWindowCompany(i);
					var label = new BMap.Label(json.title,{"offset":new BMap.Size(json.icon.lb-json.icon.x+10,-20)});
					marker.setLabel(label);
        companyMap.addOverlay(marker);
        label.setStyle({
                    borderColor:"#808080",
                    color:"#333",
                    cursor:"pointer"
        });
		
		(function(){
			var index = i;
			var _iw = createInfoWindowCompany(i);
			var _marker = marker;
			_marker.addEventListener("click",function(){
			    this.openInfoWindow(_iw);
		    });
		    _iw.addEventListener("open",function(){
			    _marker.getLabel().hide();
		    })
		    _iw.addEventListener("close",function(){
			    _marker.getLabel().show();
		    })
			label.addEventListener("click",function(){
			    _marker.openInfoWindow(_iw);
		    })
			if(!!json.isOpen){
				label.hide();
				_marker.openInfoWindow(_iw);
			}
		})()
    }
}
//创建InfoWindow
function createInfoWindowCompany(i){
    var json = markerArrCompany[i];
    var iw = new BMap.InfoWindow("<b class='iw_poi_title' title='" + json.title + "'>" + json.title + "</b><div class='iw_poi_content'>"+json.content+"</div>");
    return iw;
}
//创建一个Icon
function createIconCompany(json){
    var icon = new BMap.Icon("http://openapi.baidu.com/map/images/us_mk_icon.png", new BMap.Size(json.w,json.h),{imageOffset: new BMap.Size(-json.l,-json.t),infoWindowOffset:new BMap.Size(json.lb+5,1),offset:new BMap.Size(json.x,json.h)})
    return icon;
}


    
  //创建和初始化地图函数：
var factoryMap;
function initMapFactory(){
    createMapFactory();//创建地图
    setMapEventFactory();//设置地图事件
    addMapControlFactory();//向地图添加控件
    addMarkerFactory();//向地图中添加marker
}

//创建地图函数：
function createMapFactory(){
    var map = new BMap.Map("factory");//在百度地图容器中创建一个地图
    var point = new BMap.Point(114.299997,22.673862);//定义一个中心点坐标
    map.centerAndZoom(point,18);//设定地图的中心点和坐标并将地图显示在地图容器中
    factoryMap = map;//将map变量存储在全局
}

//地图事件设置函数：
function setMapEventFactory(){
    factoryMap.enableDragging();//启用地图拖拽事件，默认启用(可不写)
    factoryMap.disableScrollWheelZoom();//禁用地图滚轮放大缩小，默认禁用(可不写)
    factoryMap.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
    factoryMap.enableKeyboard();//启用键盘上下左右键移动地图
}

//地图控件添加函数：
function addMapControlFactory(){
    //向地图中添加缩放控件
		var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_LEFT,type:BMAP_NAVIGATION_CONTROL_LARGE});
		factoryMap.addControl(ctrl_nav);
            //向地图中添加比例尺控件
		var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT});
		factoryMap.addControl(ctrl_sca);
}

//标注点数组
var markerArrFactory = [{title:"工厂地址",content:"电话：0755-89784580<br/>地址：中国湖南省益阳市资阳区长春东路电子产业园",point:"112.372995|28.613144 ",isOpen:1,icon:{w:23,h:25,l:46,t:21,x:9,lb:12}}];
//创建marker
function addMarkerFactory(){
    for(var i=0;i<markerArrFactory.length;i++){
        var json = markerArrFactory[i];
        var p0 = json.point.split("|")[0];
        var p1 = json.point.split("|")[1];
        var point = new BMap.Point(p0,p1);
					var iconImg = createIconFactory(json.icon);
        var marker = new BMap.Marker(point,{icon:iconImg});
					var iw = createInfoWindowFactory(i);
					var label = new BMap.Label(json.title);
					marker.setLabel(label);
        factoryMap.addOverlay(marker);
        label.setStyle({
                    borderColor:"#808080",
                    color:"#333",
                    cursor:"pointer"
        });
		
		(function(){
			var index = i;
			var _iw = createInfoWindowFactory(i);
			var _marker = marker;
			_marker.addEventListener("click",function(){
			    this.openInfoWindow(_iw);
		    });
		    _iw.addEventListener("open",function(){
			    _marker.getLabel().hide();
		    })
		    _iw.addEventListener("close",function(){
			    _marker.getLabel().show();
		    })
			label.addEventListener("click",function(){
			    _marker.openInfoWindow(_iw);
		    })
			if(!!json.isOpen){
				label.hide();
				_marker.openInfoWindow(_iw);
			}
		})()
    }
}
//创建InfoWindow
function createInfoWindowFactory(i){
    var json = markerArrFactory[i];
    var iw = new BMap.InfoWindow("<b class='iw_poi_title' title='" + json.title + "'>" + json.title + "</b><div class='iw_poi_content'>"+json.content+"</div>");
    return iw;
}
//创建一个Icon
function createIconFactory(json){
    var icon = new BMap.Icon("http://openapi.baidu.com/map/images/us_mk_icon.png", new BMap.Size(json.w,json.h),{imageOffset: new BMap.Size(-json.l,-json.t),infoWindowOffset:new BMap.Size(json.lb+5,1),offset:new BMap.Size(json.x,json.h)})
    return icon;
}
    
   
