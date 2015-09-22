define('tui/widget/customeraccount/ScrollerCustom', [
  'dojo',
  'dojo/dom-style',
  'dojo/dom-attr',
  'dojo/dom-geometry',
  "tui/widget/_TuiBaseWidget"], function(dojo, domStyle, domAttr, domGeom) {
dojo.declare("tui.widget.customeraccount.ScrollerCustom", [tui.widget._TuiBaseWidget], {
  EXTRA_SPACE : 10,

  calulateWidth: function(xs) {
    var width = 0;
    _.each(xs, function(el) {
      width += domGeom.position(el).w;
    })
    return width;
  },
  postCreate:function(){
        var self = this;
	  _.each(dojo.query('.scroll'), function(element) {
		var ul = dojo.query('ul', element)[0];
		var dynamicWidth = dojo.fromJson(domAttr.get(element, 'data-scroll-dw') || "false");
		if(ul && dynamicWidth) {
		  domStyle.set(ul, 'width', self.calulateWidth(dojo.query('li', ul)) + 'px');
		}
		new IScroll(element, dojo.fromJson(domAttr.get(element, 'data-scroll-options') || {}));
	  });
  },
  applyScroller:function(){
        var self = this;
	  _.each(dojo.query('.cvscroll'), function(element,i) {
		  var pinDiv=dojo.query('.holiday-info');
			if(pinDiv){
				pinDiv[0].style.width=dojo.position(pinDiv[1]).w+'px';
			}
			
		//new IScroll(element, dojo.fromJson(domAttr.get(element, 'data-scroll-options') || {}));
		if(dojo.hasClass(element, "pin")){
			var ul = dojo.query('ul', element)[0];
			var dynamicWidth = dojo.fromJson(domAttr.get(element, 'data-scroll-dw') || "false");
			if(ul && dynamicWidth) {
			  domStyle.set(ul, 'width', self.calulateWidth(dojo.query('li', ul)) + 'px');
			}
			
			pinedScroll=new IScroll(element, dojo.fromJson(domAttr.get(element, 'data-scroll-options') || {}));
			dojo.query(".pinning").style({'display':'none'});
		}else{
			var ul = dojo.query('ul', element)[0];
			var dynamicWidth = dojo.fromJson(domAttr.get(element, 'data-scroll-dw') || "false");
			if(ul && dynamicWidth) {
			  domStyle.set(ul, 'width', self.calulateWidth(dojo.query('li', ul)) + 'px');
			}
			
			pinScroll=new IScroll(element, dojo.fromJson(domAttr.get(element, 'data-scroll-options') || {}));
		}
	  });
	  self.syncScrollers();
  },
  syncScrollers:function(){
	  setTimeout(function(){
		  if(typeof pinedScroll != "undefined" && typeof pinScroll != "undefined"){
			  
			//stick the titles on horizontal scroll
			var titles=dojo.query('.hol1 .category-title span'),
				pin=dojo.query(".pinit"),
				pinChildren=pin.children('li:not(.empty)'),
				len=pinChildren.length,
				pinLiW=dojo.position(pinChildren[0]).w,
				totalW=pinLiW*len;
				titleWidth=[],
				maxW=0;
				
			titles.forEach(function(node){
				titleWidth.push(dojo.position(node).w);
			});
			
			maxW=Math.max.apply(null, titleWidth);
			titles.style({"width":"100%"});
			
			pinedScroll.on('scroll',function(){
				pinScroll.scrollTo(this.x, 0);
				var xpos=Math.abs(this.x)+"px";
				if(len>1){
					if(this.x < 0){
						if(parseInt(xpos) < totalW-maxW){
							titles.style({"margin-left":xpos});
						}
					}
				}
			});
			
			pinScroll.on('scroll',function(){
				pinedScroll.scrollTo(this.x, 0);
				var xpos=Math.abs(this.x)+"px";
				if(len>1){
					if(this.x < 0){
						if(parseInt(xpos) < totalW-maxW){
							titles.style({"margin-left":xpos});
						}
					}
				}
			});
		  }
	  },250);
  }
  
});
 return tui.widget.customeraccount.ScrollerCustom;
});
