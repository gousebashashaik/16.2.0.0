define("tui/widget/DeckPlanHorizontalScroll", ["dojo",
    "dojo/dom-style",
    "dojo/aspect",
	"tui/widget/ScrollHorizontal"], function (dojo, domStyle, aspect) {

	dojo.declare("tui.widget.DeckPlanHorizontalScroll", [tui.widget.ScrollHorizontal], {

		touchSupport: !!dojo.query("html.touch").length,

		postCreate: function () {
			var scrollPanel = this;

			if( scrollPanel.touchSupport ){
				var vport = dojo.query(".viewport", scrollPanel.domNode)[0];
				dojo.removeClass(vport, "viewport");
				//dojo.setStyle(vport, {width: "1000px"});

				if( dojo.byId("deckOverlay") ){
					dojo.setStyle(dojo.byId("deckOverlay"), {display: "block"});
					dojo.setStyle(dojo.query(".deck-SVG", scrollPanel.domNode)[0], {overflow: "hidden"});
				}
				var myScroll = new IScroll(dojo.query(".deck-SVG", scrollPanel.domNode)[0], {
					mouseWheel: true,
				    scrollX: true,
				    eventPassthrough: true,
				    indicators: {
				        el: dojo.query(".hz-track", scrollPanel.domNode)[0],
				        interactive: true,
				        fade: false,
				        ignoreBoundaries: false,
				        resize: true,
				        shrink: false
				    }
				});

				dojo.byId("deckOverlay") && dojo.setStyle(dojo.byId("deckOverlay"), {display: "none"});

			} else{
				scrollPanel.inherited(arguments);
				domStyle.set(dojo.byId("ipad-scroll-pad"), {display: "block"});
			}

			dojo.subscribe("tui/cruise/deck/view/DeckInteractiveSVG/onSvgLoad", function(domNode){
				if( scrollPanel.touchSupport ){
					myScroll.refresh();
					var scrlPadWidth = _.first(myScroll.indicators).indicatorWidth;
					myScroll.scrollTo((950 - scrlPadWidth) * -1, 0);

				}else{
					domNode.parentNode.scrollLeft = 400
				}
			});

			/*scrollPanel.iOS = ( navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false );
			if(scrollPanel.iOS)
			{
				dojo.query(".cabin-image .viewport").removeClass("viewport");
				dojo.query(".cabin-image .hz-handle").addClass("ipad-center");
				scrollPanel.attachEvents();

			}else{
				scrollPanel.inherited(arguments);
				domStyle.set(dojo.byId("ipad-scroll-pad"), {display: "block"});
			}*/

		},

		update: function () {
			var scrollPanel = this;
			if(scrollPanel.touchSupport)
			{
				scrollPanel.domNode.scrollLeft = 510;
			}else{
				scrollPanel.inherited(arguments);
			}
		}

		/*attachEvents: function(){
			var scrollPanel = this;
			dojo.connect(scrollPanel.domNode, "onscroll", function(e){
				dojo.query(".hz-track", scrollPanel.domNode)[0].style.height = "0";
			});

			aspect.after(scrollPanel.domNode, "onscroll", function(e){
				dojo.query(".hz-track", scrollPanel.domNode)[0].style.height = "0";
			});

			dojo.subscribe("tui/cruise/deck/controller/DeckSVGController/afterToggle", function(domNode, state){
				var node = dojo.query(".cabin-image", domNode)[0];
				node.scrollLeft = 410;
			});
			dojo.subscribe("tui/cruise/deck/view/DeckInteractiveSVG/onSvgLoad", function(domNode, state){
				var node = dojo.query(domNode).closest(".cabin-image")[0];
				node.scrollLeft = 410;
			});

			dojo.subscribe("tui/widget/popup/cruise/DeckPopup/deckSVG",function(e){
				//var node = dojo.query(".cabin-image", scrollPanel.domNode)[0];
				scrollPanel.domNode.scrollLeft = 510;
			});
		}*/
	})

	return tui.widget.DeckPlanHorizontalScroll;
})