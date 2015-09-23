define("tui/widget/form/flights/Expandable", [
   "tui/widget/expand/SimpleExpandable",
   "dojo",
   "dojo/on",
   "dojo/text!tui/widget/form/flights/templates/dealsOverlayTmpl.html",
   "dojo/_base/fx",
   "dijit/registry",
   "dojo/query",
   "dojo/dom-class",
   "dojo/has",
   "dojo/dom-construct",
   "dojox/widget/Standby",
   "dojox/dtl/Context",
   "dojo/NodeList-traverse",
   "dojo/_base/sniff"
   ],function(SimpleExpandable,dojo,on,dealsOverlayTmpl,fx,registry,query,domClass,has,domConstruct,Standby){

		dojo.declare("tui.widget.form.flights.Expandable",[SimpleExpandable],{
			tmpl: dealsOverlayTmpl,

		postCreate: function () {
    		var DealsExpandable = this;
    		DealsExpandable.inherited(arguments);
    	},
    	attachOpenEvent: function () {
	      var DealsExpandable = this;
	      on(DealsExpandable.domNode, "click", function (event) {
	    	  if(domClass.contains(DealsExpandable.domNode,"disabled")) return;
	    	  if(DealsExpandable.ie8support()) return
	        if (DealsExpandable.expandableDom === null || !DealsExpandable.isShowing(DealsExpandable.expandableDom)) {
	          //dojo.publish("tui.searchPanel.view.DestinationGuide.closeExpandable");
	          setTimeout(function () {
	        	  DealsExpandable.openExpandable();
	          }, 350);
	        } else {
	        	setTimeout(function () {
	        		DealsExpandable.closeExpandable();
	        	 }, 350);
	        }
	      });

	    },
	    openExpandable: function(){
	    	var DealsExpandable = this;
	    	//height = Math.floor(dojo.position(DealsExpandable.domNode).h);
	    	DealsExpandable.inherited(arguments);

    		DealsExpandable.onOpenExpandable();

	    },
	    closeExpandable: function(){
	    	var DealsExpandable = this;
    		DealsExpandable.inherited(arguments);
    		//height = Math.floor(dojo.position(DealsExpandable.domNode).h);
    		if(dijit.byId("monthSelect")!=undefined){
        	var monthDropdown=dijit.byId("monthSelect").listElement;
      			dojo.setStyle(monthDropdown,"display","none");
    		}
    		//dojo.style(DealsExpandable.expandableDom,"visibility","hidden");
	    },
	    attachExpandableEvent: function(){
	    	var DealsExpandable = this,labelNode;
	    	var touchSupport = dojo.hasClass(query("html")[0], "touch");
	    		if(!touchSupport){
	    			dojo.query(".labelHover",DealsExpandable.expandableDom).on("mouseover,mouseout",function(evt){

	    				labelNode = dojo.query(evt.target).prev('.dijitRadio')[0] || dojo.query(evt.target).prev('.dijitCheckBox')[0];
		    			if(domClass.contains(labelNode,"dijitDisabled")) return;
		    			if(evt.type=="mouseover"){
		    				DealsExpandable.selectOneway(evt,labelNode);
		    			}else{
		    				DealsExpandable.unSelectOneway(evt,labelNode);
		    			}
	    			});
	    			dojo.query(".dijitRadio").on("mouseover,mouseout",DealsExpandable.hoverOndijit);
	    			dojo.query(".dijitCheckBox").on("mouseover,mouseout",DealsExpandable.hoverOndijit);
	    		}

	    		dojo.query("a.close",DealsExpandable.expandableDom).on("click",function(){
	    		DealsExpandable.hideWidget(DealsExpandable.expandableDom);
	    	});

    		DealsExpandable.attachBodyEvent();

	    },
	    selectOneway:function(evt,labelNode){
	    	dojo.query(evt.target).addClass("per-radiospl");
			dojo.query(evt.target).prev('.dijitRadio').addClass("dijitHover dijitRadioHover");
			if(dojo.hasClass(labelNode,"dijitRadioChecked")){
				dojo.query(evt.target).prev('.dijitRadio').addClass("dijitRadioCheckedHover")
			}
			dojo.query(evt.target).prev('.dijitCheckBox').addClass("dijitHover dijitCheckBoxHover");
	    },
	    unSelectOneway:function(evt,labelNode){
	    	dojo.query(evt.target).removeClass("per-radiospl");
			dojo.query(evt.target).prev('.dijitRadio').removeClass("dijitHover dijitRadioHover");
			dojo.query(evt.target).prev('.dijitCheckBox').removeClass("dijitHover dijitCheckBoxHover");
			if(dojo.hasClass(labelNode,"dijitRadioChecked")){
				dojo.query(evt.target).prev('.dijitRadio').removeClass("dijitRadioCheckedHover")
			}
	    },
	    hoverOndijit: function(evt){
	    	if(evt.type=="mouseover"){
    			dojo.query(evt.target).closest("span").children("label").addClass("per-radiospl");
    		} else {
    			dojo.query(evt.target).closest("span").children("label").removeClass("per-radiospl");
    		}
	    },
	    attachBodyEvent: function(){
	    	var DealsExpandable = this,sortByFilter,isSortByFilter;
	    	on(document.body, "click", function(evt){
	    		dealsGuideDom = dojo.query(evt.target).closest(".deals-guide")[0]
	    		//sortByFilterNode = dojo.byId("sortByFilter");
	    		parentTargetElement = dojo.query(evt.target).closest('#sortByFilter');
	    		//if user clicks on "sortbyfilter" dropdown stop closing of domnode
	    		if(parentTargetElement.length>0){
	    			isSortByFilter = true;
	    		}
	    		else{
	    			//if user clikcs other than sortbyfilter dropdown allow dom node to close
	    			isSortByFilter = false;

	    		}
	    		if(dealsGuideDom == undefined && DealsExpandable.domNode != evt.target && !isSortByFilter){
	    			if(dijit.byId("monthSelect") !== undefined && dijit.byId("monthSelect").listShowing) return
	    			DealsExpandable.closeExpandable();
	    		}
	    	});
	    },

	    attachEventListeners: function(){},

	    showWidget: function (element) {
            var DealsExpandable = this,
                elementToShow = (element || DealsExpandable.domNode),
                wrapper = dojo.query('.deals-wrapper', DealsExpandable.expandableDom)[0],
                setHeight = DealsExpandable.expandableProp === 'expand-vertical',
                height = setHeight ? dojo.position(wrapper).h+16 : 0;
                if(DealsExpandable.isShowing()) return;
                setTimeout(function () {
                	dojo.style(DealsExpandable.expandableDom,"visibility","visible");
                	if(setHeight){
                			dojo.style(DealsExpandable.domNode,"border-bottom","0px");
                			if(dojo.isIE == 8) DealsExpandable.domNode.style.borderBottom="0px";
                    		//if(DealsExpandable.expandableDom) dojo.style(DealsExpandable.expandableDom,"visibility","visible");
                    		dojo.style(DealsExpandable.domNode,"height","59px");
                    		dojo.style(DealsExpandable.domNode,"border-bottom-right-radius",0)
                    		dojo.style(DealsExpandable.domNode,"border-bottom-left-radius",0)
                			fx.animateProperty({
                    			node:elementToShow,
                    			 properties: {
                    				 maxHeight: {end:height,start:0}
                    			 	}
                    		}).play();

                	}
                    dojo.addClass(elementToShow, "open");
                }, 50);
            on.once(DealsExpandable.expandableDom, "transitionend, webkitTransitionEnd, mozTransitionEnd, otransitionend, oTransitionEnd", function(){
                dojo.addClass(elementToShow, "open-anim-done");
            });
        },
	    hideWidget: function (element) {
        	var DealsExpandable = this,

            	elementToShow = (element || DealsExpandable.domNode),
            	setHeight = DealsExpandable.expandableProp === 'expand-vertical';

        	if(setHeight){
        		/*dojo.style(DealsExpandable.expandableDom,"visibility","hidden");
				dojo.style(DealsExpandable.domNode,"border-bottom","1px solid #82B8E4");
	    		dojo.style(DealsExpandable.domNode,"height","42px");
	    		dojo.style(DealsExpandable.domNode,"border-bottom-right-radius","3px");
	    		dojo.style(DealsExpandable.domNode,"border-bottom-left-radius","3px");
	    		dojo.style(elementToShow,"max-height","0px")*/
        			fx.animateProperty({
            			node:elementToShow,
            			 properties: {
            				 maxHeight: 0
            			   },
            			onEnd:function(){
            				dojo.style(DealsExpandable.expandableDom,"visibility","hidden");
            				dojo.style(DealsExpandable.domNode,"border-bottom","1px solid #82B8E4");
            				if(dojo.isIE == 8) DealsExpandable.domNode.style.borderBottom="1px solid #82B8E4";
            	    		dojo.style(DealsExpandable.domNode,"height","42px");
            	    		dojo.style(DealsExpandable.domNode,"border-bottom-right-radius","3px");
            	    		dojo.style(DealsExpandable.domNode,"border-bottom-left-radius","3px");
            			}
            		}).play();

        	}
            dojo.removeClass(elementToShow, "open");
            dojo.removeClass(elementToShow, "open-anim-done");
        },
	    doXhrPost: function(url){
	    	var DealsExpandable = this;
	    	var jsonData = DealsExpandable.dealsPanelModel.createQueryObjForServerCall();
			return dojo.xhrPost({
				url: url,
				handleAs: "json",
				content: {
					"jsonData":jsonData
				}
			});

	    },

	    place: function (html) {
	        // summary:
	        //		Override place method from simpleExpandable,
	        //		place the airport guide in the main search container.
	    	var DealsExpandable = this,
	            target = DealsExpandable.domNode.parentNode;
	        return dojo.place(html, target, "last");
	      },
	      onOpenExpandable: function(){
	    	  // this should overriden in child classes
	      },
	      ie8support: function(){
	    	  if(has("ie") < 9) {
	    		  if(query("#container .section-overlay").length > 0 ){
		    		  domClass.remove(query("#container.container")[0],"disableAll");
						query(".section-overlay",dojo.byId("container")).remove();
						var element = domConstruct.create("div",{"class":"section-overlay"},dojo.byId("searchPanelSubtle"),"first");
						 domClass.add(dojo.byId("searchPanelSubtle"),"disableAll")
		    		  return true;
		    	  }
	    	  }
	    	  return false;
	      },
	      standBy : function(node){
		    	var standby = new Standby({target: node, image:require.toUrl(dojoConfig.paths.webRoot+"/images/loader-maps.gif").toString(),color:"#fff"});
		    	   document.body.appendChild(standby.domNode);
		    	   standby.startup();
		    	   return standby;
	    }
	});
	return tui.widget.form.flights.Expandable;
})