define("tui/widget/maps/cruise/CruiseAreasMapScroller", [
	"dojo",
	"dojo/_base/declare",
	"dojo/query",
	"dojo/on",
	"dojo/mouse",
	"dojo/dom-style",
	"dojo/dom-attr",
	"dojo/dom-class",
	"tui/widget/ScrollPanel"],function(dojo, declare, query, on, mouse, domStyle, domAttr, domClass){

	return declare("tui.widget.maps.cruise.CruiseAreasMapScroller", [tui.widget.ScrollPanel], {

		scrollUpNode : null,

		scrollDownNode: null,

		trackNode: null,

		updateFlag: true,

		postCreate: function() {
			var crMapScroll = this;
			crMapScroll.getParent().crMapScroll = crMapScroll;
			crMapScroll.inherited(arguments);
			crMapScroll.scrollUpNode = query("div.up", crMapScroll.getParent().domNode)[0];
			crMapScroll.scrollDownNode = query("div.down", crMapScroll.getParent().domNode)[0];
			crMapScroll.trackNode = query("div.track", crMapScroll.domNode)[0];
			domStyle.set(crMapScroll.viewport, "height", "451px");
			crMapScroll.subscribe("tui/widget/maps/cruise/CruiseAreasMapScroller/afterToggle", function(){
				if(crMapScroll.updateFlag){
					crMapScroll.update();
					crMapScroll.updateFlag = false;
				}
				domStyle.set(crMapScroll.trackNode, "display", "none");
				setTimeout(function(){ crMapScroll.showScrollIndicator(); }, 500);

			});
			on(crMapScroll.scrollDownNode, "click", function(event){
				crMapScroll.showScrollIndicator();
				crMapScroll.scrollEventHandler(false);
			})
			on(crMapScroll.scrollUpNode, "click", function(event){
				crMapScroll.showScrollIndicator();
				crMapScroll.scrollEventHandler(true);
			})
		},
		showScrollIndicator: function () {
			var crMapScroll = this, displaytrack, scrollTop, scrollMax, viewPortHt, scrollHeight;
			viewPortHt = crMapScroll.viewport.offsetHeight;
			scrollHeight =  crMapScroll.viewport.scrollHeight;
       	  	scrollTop = crMapScroll.viewport.scrollTop;
       		scrollMax = crMapScroll.viewport.scrollTopMax ? crMapScroll.viewport.scrollTopMax : (scrollHeight - viewPortHt);
       		dojo.style(crMapScroll.scrollUpNode, "display", "none");
			dojo.style(crMapScroll.scrollDownNode, "display", "none");
       	    if(scrollTop === scrollMax){
       	    	if(scrollHeight == viewPortHt) return;
				//scroll down
				dojo.style(crMapScroll.scrollUpNode, "display", "block");
				dojo.style(crMapScroll.scrollDownNode, "display", "none");
			} else if(scrollTop === 0 ){
				//scroll up
				dojo.style(crMapScroll.scrollUpNode, "display", "none");
				dojo.style(crMapScroll.scrollDownNode, "display", "block");
			}
			else {
				//scroll both ways
				dojo.style(crMapScroll.scrollUpNode, "display", "block");
				dojo.style(crMapScroll.scrollDownNode, "display", "block");
			}
		},
		scrollEventHandler: function (flag){
			var crMapScroll = this;
			if(flag){//scroll up
				crMapScroll.viewport.scrollTop -= 16;
			}else{//scroll down
				crMapScroll.viewport.scrollTop += 16;
			}
		}
  });
});
