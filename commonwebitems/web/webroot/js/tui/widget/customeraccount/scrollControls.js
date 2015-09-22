define("tui/widget/customeraccount/scrollControls", [
    "dojo",
    "dojo/dom",
    "dojo/dom-attr",
    "dojo/on",
    "dojo/cookie",
    "dojo/query",
    "dojo/has",
    "dojox/validate/web",
    "tui/validate/check",
    "dojo/_base/array",
    "dojo/dom-style",
    "dijit/focus",
    "dojo/dom-construct",
    "tui/widget/customeraccount/UserRepository",
    "dojo/Stateful",
    "dojo/dom-geometry",
    "dojo/_base/fx",
    "tui/widget/_TuiBaseWidget"

], function (dojo, dom, domAttr, on, cookie, query, has, validate, check, arrayUtil, domStyle, focusUtil, domConstruct, userRepo, Stateful, domGeom,fx) {

    dojo.declare("tui.widget.customeraccount.scrollControls", [tui.widget._TuiBaseWidget], {
        bpos: null,
        postCreate: function () {
            var widget = this;
            widget.initCompareScroll();
            widget.inherited(arguments);
        },
        initCompareScroll: function () {
            var widget = this;
            var setTop, setHeight, controls = widget.domNode;
            var node = dojo.byId("content");
            var containerHt = dojo.getComputedStyle(node).height;

            if (widget.compareViewScroll == 'true' || widget.compareViewScroll == true) {
                setTimeout(function(){
					    widget.vScrollCompare();
				},1000);
            } else {
                if (dojo.query(".spaceBlocker")[0] != undefined) {
                    var spaceBlocker = dojo.query(".spaceBlocker")[0];
                    dojo.connect(window, 'onscroll', function (event) {
                        var scrollTop = dojo._docScroll().y;
                        if (scrollTop > widget.getDifferencePosition()) {
                            dojo.addClass(controls, "fixed sticky");
                            setHeight = {
                                height: controls.offsetHeight + 15 + "px"
                            };
                            dojo.setAttr(spaceBlocker, "style", setHeight);

                        } else {
                            dojo.removeClass(controls, "fixed sticky");
                            setHeight = {
                                height: "0px"
                            };
                            dojo.setAttr(spaceBlocker, "style", setHeight);
                        }
                    });
                }
            }
        },
        getDifferencePosition: function () {
            var initialHeight = 0;
            if (dojo.query("#account-bar")[0] != undefined) {
                initialHeight += dojo.query("#account-bar")[0].offsetHeight;
            }
            if (dojo.query("#header")[0] != undefined) {
                initialHeight += dojo.query("#header")[0].offsetHeight;
            }
            if (dojo.query("#nav")[0] != undefined) {
                initialHeight += dojo.query("#nav")[0].offsetHeight;
            }
            if (dojo.query("#wishCountSpan")[0] != undefined) {
                initialHeight += dojo.query("#wishCountSpan")[0].offsetHeight;
            }
            return initialHeight;
        },
        vScrollCompare:function(){
        	window.scrollTo(0,0);
        	 var widget = this;
        	 	holInfo=dojo.position(dojo.query(".holiday-info")[1]),
        	 	topSummary=dojo.position(dojo.query('.pinnedit .top-summary')[0]),
        	 	tpos = window.innerWidth < 959 ? parseInt(topSummary.y,10) : parseInt(holInfo.y,10),
        	 	pin=dojo.query(".pinning"),
        	 	pinChildren=pin.children('li'),
        	 	pinit = dojo.position(dojo.query('.pinnedit')[0]),
        	 	booknw = dojo.position(dojo.query('.book-now')[0]),
        	 	bpos = (parseInt(tpos, 10) + (parseInt(holInfo.h, 10)- parseInt(booknw.h,10))) - parseInt(pinit.h, 10);
        	 	
        	 	pinChildren.style({'background':'#ffffff'});
        	 	dojo.connect(window, 'onscroll,touchmove', function (e){
        	 		var ypos=dojo._docScroll().y;
        	 		if(ypos > tpos && ypos < bpos){
        	 			var nwpos=ypos-tpos;
        	 			pin.style({"display":"block"});
        	 		}else{
        	 			pin.style("display","none");
        	 			
					}
        	 	});
        },
        scrollCompare: function () {
        	window.scrollTo(0,0);
            var widget = this;
				topPos = dojo.position(dojo.query(".holiday-info")[0]).y,		
				tpos = parseInt(topPos,10),
				pinned = dojo.query(".pinned"),
				cvht = dojo.position(dojo.query('.holiday-info')[0]).h,
				pinHt = dojo.position(dojo.query('.pinnedit')[0]).h,
				bookHt = dojo.position(dojo.query('.book-now')[0]).h,
				bpos = (parseInt(tpos, 10) + (parseInt(cvht, 10)- parseInt(bookHt,10))) - parseInt(pinHt, 10),
				pinHtpx = parseInt(dojo.position(dojo.query('.pinnedit')[0]).h, 10)+"px",
				contWdth = dojo.position(dojo.query(".hol1")[0]).w+"px",
				IE=navigator.userAgent.indexOf("IE")!=-1 ? true: false;
				
				dojo.connect(window, 'onscroll', function (event){
					var ypos=dojo._docScroll().y;
					if(ypos > tpos && ypos<bpos){
						var nwpos=ypos-tpos;
						pinned.style("display","block");
						if(IE){
							pinned.style({
								"position":"fixed",
								"top":0+"px",
								"width":contWdth
							});
						}else{
							pinned.style({
								"-webkit-transform":"translate(0px, "+nwpos+"px) translateZ(0px)",
								"transform:":"translate(0px, "+nwpos+"px)",
								"-moz-transform":"translate(0px, "+nwpos+"px)",
								"-o-transform":"translate(0px, "+nwpos+"px)",
								"-ms-transform":"translate(0px, "+nwpos+"px)"
							});
						}
						
					}else{
						pinned.style("display","none");
					}
					
				});
         

        },
        getPosition: function (element) {
            var xPosition = 0;
            var yPosition = 0;

            while (element) {
                xPosition += (element.offsetLeft - element.scrollLeft + element.clientLeft);
                yPosition += (element.offsetTop - element.scrollTop + element.clientTop);
                element = element.offsetParent;
            }
            return {
                x: xPosition,
                y: yPosition
            };
        }
    });
    return tui.widget.customeraccount.scrollControls;
});