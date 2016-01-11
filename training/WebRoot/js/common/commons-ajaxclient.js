	/**
	 * 同步ajax调用
	 * type为可选参数,可传入'json'，返回json格式的对象
	 */
	 function syncCallService(url,data,type){
		var retData;
		$.ajax({
			type : "POST",
			url  : url,
			data : data,
			async : false,
			cache : false,
			error : function(){
				alert("网络连接失败，请重新加载本页面。。。!");
		    },
			success:function(data){
	    		retData =  data;
			}
		});
		if ( type == "json"||type==null)
			retData = eval("(" + retData + ")");
		return retData;
	}
	/**
	 * 异步ajax调用
	 * type为可选参数,可传入'json'，返回json格式的对象
	 */
	 function asyncCallService(url,data,callback, type){
		if ( $.isFunction( data ) ) {
			callback = data;
			data = {};
		}
		$.ajax({
			type : "POST",
			url  : url,
			data : data,
			async : true,
			cache : false,
			error : function(){
				alert("网络连接失败，请重新加载本页面。。。!");
		    },
			success:function(data){
				if (type == "json"||type==null)
					data = eval("(" + data + ")");
				callback(data);
			}
		});
	}