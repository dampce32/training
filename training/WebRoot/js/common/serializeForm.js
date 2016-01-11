(function($){
	$.fn.serializeObject = function() { 
	    var o = {}; 
	    var fields  = this.serializeArray();
	    $.each(fields, function() { 
            o[this.name] = this.value || ''; 
	    }); 
	    return o; 
	}
})(jQuery) 
