define("tui/widget/form/flights/DealsExpandable", [
   "tui/widget/expand/SimpleExpandable",
   "dojo",
   "dojo/on",
   "dojo/text!tui/widget/form/flights/templates/dealsOverlayTmpl.html",
   "dojo/text!tui/flightdeals/templates/ICanFlyOut.html",
   "dojo/_base/fx"
   ],function(SimpleExpandable,dojo,on,dealsOverlayTmpl,ICanFlyOutTmpl,fx){

		dojo.declare("tui.widget.form.flights.DealsExpandable",[SimpleExpandable],{
			// ----------------------------------------------------------------------------- properties

	    	tmpl: dealsOverlayTmpl,

	    	// ----------------------------------------------------------------------------- methods
	    	postCreate: function () {
	    		var DealsExpandable = this;
	    		DealsExpandable.inherited(arguments);
	    	},
	    	attachOpenEvent: function () {
    	      var DealsExpandable = this;
    	      on(DealsExpandable.domNode, "click", function (event) {

    	        if (DealsExpandable.expandableDom === null || !DealsExpandable.isShowing(DealsExpandable.expandableDom)) {
    	          dojo.publish("tui.searchPanel.view.DestinationGuide.closeExpandable");
    	          setTimeout(function () {
    	        	  DealsExpandable.openExpandable();
    	          }, 350);
    	        } else {
    	        	DealsExpandable.closeExpandable();
    	        }
    	      });
    	    },
    	    openExpandable: function(){
    	    	var DealsExpandable = this,
    	    	height = Math.floor(dojo.position(DealsExpandable.domNode).h);
    	    	DealsExpandable.inherited(arguments);
	    		dojo.style(DealsExpandable.domNode,"border-bottom","0px");
	    		//if(DealsExpandable.expandableDom) dojo.style(DealsExpandable.expandableDom,"visibility","visible");
	    		dojo.style(DealsExpandable.domNode,"height","54px");


    	    },
    	    closeExpandable: function(){
    	    	var DealsExpandable = this;
	    		DealsExpandable.inherited(arguments);
	    		height = Math.floor(dojo.position(DealsExpandable.domNode).h);
	    		//dojo.style(DealsExpandable.expandableDom,"visibility","hidden");
    	    },
    	    onAfterTmplRender: function () {
    	    	var DealsExpandable = this;
	    		DealsExpandable.inherited(arguments);

	    		console.log(DealsExpandable.id);
	    		switch(DealsExpandable.id){
	    			case "dealsExpandable":
	    				target = dojo.query(".deals-wrapper",DealsExpandable.expandableDom)[0];
	    	    		dojo.html.set(target,ICanFlyOutTmpl,{
	    					parseContent: true
	    				});
	    	    		DealsExpandable.attachFlyOutEvents();
	    				break;
	    			case "flyFromDealsExpandable":
	    				target = dojo.query(".deals-wrapper",DealsExpandable.expandableDom)[0];
	    	    		dojo.place(ICanFlyOutTmpl, target, "last");
	    				break;
					case "dealsExpandable2":
						target = dojo.query(".deals-wrapper",DealsExpandable.expandableDom)[0];
			    		dojo.place(ICanFlyOutTmpl, target, "last");
	    				break;
	    		}
    	    },
    	    attachFlyOutEvents: function(){
    	    	var DealsExpandable = this,labelNode;
    	    	dojo.query(".labelHover").on("mouseover,mouseout",function(evt){
    	    		labelNode = dojo.query(evt.target).prev('.dijitRadio')[0] || dojo.query(evt.target).prev('.dijitCheckBox')[0];
    	    		if(evt.type=="mouseover"){

    	    			dojo.query(evt.target).addClass("per-radiospl");
    	    			dojo.query(evt.target).prev('.dijitRadio').addClass("dijitHover dijitRadioHover");
    	    			if(dojo.hasClass(labelNode,"dijitRadioChecked")){
    	    				dojo.query(evt.target).prev('.dijitRadio').addClass("dijitRadioCheckedHover")
    	    			}
    	    			/*if(dojo.hasClass(labelNode,"dijitCheckBoxChecked")){
    	    				dojo.query(evt.target).prev('.dijitCheckBox').addClass("dijitCheckBoxCheckedHover")
    	    			}*/
    	    			dojo.query(evt.target).prev('.dijitCheckBox').addClass("dijitHover dijitCheckBoxHover");
    	    		} else{
    	    			dojo.query(evt.target).removeClass("per-radiospl");
    	    			dojo.query(evt.target).prev('.dijitRadio').removeClass("dijitHover dijitRadioHover");
    	    			dojo.query(evt.target).prev('.dijitCheckBox').removeClass("dijitHover dijitCheckBoxHover");
    	    			if(dojo.hasClass(labelNode,"dijitRadioChecked")){
    	    				dojo.query(evt.target).prev('.dijitRadio').removeClass("dijitRadioCheckedHover")
    	    			}
    	    			/*if(dojo.hasClass(labelNode,"dijitCheckBoxChecked")){
    	    				dojo.query(evt.target).prev('.dijitCheckBox').removeClass("dijitCheckBoxCheckedHover")
    	    			}*/
    	    		}

    	    	});
    	    	dojo.query(".dijitRadio").on("mouseover,mouseout",DealsExpandable.hoverOndijit);
    	    	dojo.query(".dijitCheckBox").on("mouseover,mouseout",DealsExpandable.hoverOndijit);
    	    },
    	    hoverOndijit: function(evt){
    	    	if(evt.type=="mouseover"){
	    			dojo.query(evt.target).closest("span").children("label").addClass("per-radiospl");
	    		} else {
	    			dojo.query(evt.target).closest("span").children("label").removeClass("per-radiospl");
	    		}
    	    },
    	    showWidget: function (element) {
                var DealsExpandable = this,
                    elementToShow = (element || DealsExpandable.domNode),
                    wrapper = dojo.query('.deals-wrapper', DealsExpandable.expandableDom)[0],
                    setHeight = DealsExpandable.expandableProp === 'expand-vertical',
                    height = setHeight ? dojo.position(wrapper).h : 0;
	                setTimeout(function () {
	                	dojo.style(DealsExpandable.expandableDom,"visibility","visible");
	                	if(setHeight){
	                		fx.animateProperty({
	                			node:elementToShow,
	                			duration:1000,
	                			 properties: {
	                				 maxHeight: {end:height,start:0}
	                			 	}
	                		}).play();
	                	}
	                    dojo.addClass(elementToShow, "open");
	                }, 1);
                on.once(DealsExpandable.expandableDom, "transitionend, webkitTransitionEnd, mozTransitionEnd, otransitionend, oTransitionEnd", function(){
                    dojo.addClass(elementToShow, "open-anim-done");
                });
            },
            hideWidget: function (element) {
            	var DealsExpandable = this,
                	elementToShow = (element || DealsExpandable.domNode),
                	setHeight = DealsExpandable.expandableProp === 'expand-vertical';

            	if(setHeight){
            		fx.animateProperty({
            			node:elementToShow,
            			//duration:500,
            			 properties: {
            				 maxHeight: 0
            			   },
            			onEnd:function(){
            				dojo.style(DealsExpandable.expandableDom,"visibility","hidden");
            				dojo.style(DealsExpandable.domNode,"border-bottom","1px solid #82B8E4");
            	    		dojo.style(DealsExpandable.domNode,"height","42px");
            			}
            		}).play();
            	}
                dojo.removeClass(elementToShow, "open");
                dojo.removeClass(elementToShow, "open-anim-done");
            },
    	    place: function (html) {
    	        // summary:
    	        //		Override place method from simpleExpandable,
    	        //		place the airport guide in the main search container.
    	    	var DealsExpandable = this,
    	            target = DealsExpandable.domNode.parentNode;
    	        return dojo.place(html, target, "last");
    	      },

		});
		return tui.widget.form.flights.DealsExpandable;
})