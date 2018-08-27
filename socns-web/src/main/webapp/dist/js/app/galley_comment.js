
(function($){
	var mtons = window.mtons,
	Widget = mtons.Widget,
	J = jQuery;
	
    var page_count = 0;
    var topHeight = 0;
	var Comment = Widget.extend({
        name : 'Comment',
        current_id : '',
        init : function (element, options, cid) {
            Widget.fn.init.call(this, element, options);
			this.current_id = cid;
            var that = this;

            that.pageCallback(1);

            $('#btn-chat'+ "_" + cid).click(function () {
                var text = $('#chat_text'+ "_" + cid).val();
                var toPid = $('#chat_pid'+ "_" + cid).val();
                that.post(that.options.toId,toPid,text);
            });
        },
        defaults: {
        	load_url : null,
        	post_url : null,
        	toId : 0,
        	maxResults :6,
            // callback
            onLoad : function (i, data) {}
        },
        bindEvents : function () {
        	var that = this;
        	// that.pageCallback(1);
         //    var eid = that.element[0].id;
         //    var cur_id = eid.substring(eid.lastIndexOf("_") + 1);
        	// $('#btn-chat'+ "_" + cur_id).click(function () {
        	// 	var text = $('#chat_text'+ "_" + cur_id).val();
        	// 	var toPid = $('#chat_pid'+ "_" + cur_id).val();
        	// 	that.post(that.options.toId,toPid,text);
        	// });
        },
        
        onLoad : function () {
        	this.pageCallback(1);
        },
        
        pageCallback: function (pn) {
        	var opts = this.options;
        	var that = this;
        	
        	var $list = this.element;
        	var html = '';

        	J.getJSON(opts.load_url, {maxResults : opts.maxResults, pn: pn}, function (ret) {
        		
        		$('#chat_count'+ "_" + that.current_id).html(ret.totalCount);
        		
          		jQuery.each(ret.results, function(i, n) {
    				var item = opts.onLoad.call(this, i, n);
    				html += item;
          		});
        	
	        	$list.empty().append(html);
	        	
	    		if (ret.pageCount > 1) {
	    			$("#pager"+ "_" + that.current_id).page(ret, J.proxy(that, 'pageCallback'));
	    		}

                // var comment_length = ret.results.length;
                // if(comment_length >= 3){
                //     $list.parent().parent().parent().next().css("top", );
                // }
                page_count++;

                topHeight = 0;
                if(page_count == 8){
                    var all_item = document.getElementById("blog-wrap");
                    jQuery.each($("#blog-wrap").children(), function(i, childitem) {
                        var child_top = childitem.style.top;
                        if(child_top != "0px"){
                            childitem.style.top = (parseInt(child_top.substring(0, child_top.length - 2)) + topHeight) + "px";

                        }

                        var comment_length = $("#" + childitem.id).find(".chat").children().length;
                        // var comment_length = childitem.children[3].children[0].children[0].children.length;
                        if(comment_length >= 3){
                           topHeight += (comment_length - 2) * 45;     
                        }

                        // if(parseInt(child_top.substring(0, child_top.length)) < lastOffestHeight){
                        //     childitem.style.top = lastOffsetHeight + "px";    
                        // }
                        
                    });
                }
        	});
        },
        
        post: function (toId,toPid,text) {
        	var opts = this.options;
        	var that = this;
        	
        	if (text.length == 0) {
        		alert('请输入内容再提交!');
        		return false;
        	}
        	if (text.length > 255) {
        		alert('内容过长，请输入140以内个字符');
        		return false;
        	}
        	
        	jQuery.ajax({
        		url: opts.post_url, 
        		data: {'toId': toId,'toPid':toPid, 'text': text},
        		dataType: "json",
        		type :  "POST",
        		cache : false,
        		async: false,
        		error : function(i, g, h) {
        			alert("发生错误");
        		},
        		success: function(ret){
        			if(ret){
        				if (ret.code >= 0) {
        					$('#chat_text'+ "_" + that.current_id).val('');
                            var add_item = "<li class='left clearfix' id='aw_char_" + toId + "_" + toPid + "'>" + 
                                "<font color='blue'>我</font>：" + text + "</li>";
        					$('#chat_container'+ "_" + that.current_id).append(add_item);
        					//that.pageCallback(1);
        				} else {
        					alert(ret.message);
        				}
        				
        			}
              	}
        	});
        },
        
        destroy : function () {
        }
    });
	
	this.Comment = Comment;
	
})(window.jQuery);