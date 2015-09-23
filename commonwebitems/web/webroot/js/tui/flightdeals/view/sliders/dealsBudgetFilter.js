define("tui/flightdeals/view/sliders/dealsBudgetFilter",[
"dojo/_base/declare",
"dojo/_base/lang",
"dojo/_base/connect",
"dojo/topic",
"dojo/dom-attr",
"dojo/query",
"dojo/on",
"dojo/dom-class",
"dojo/dom-style",
"tui/flightdeals/model/DealsPanelModel",
"tui/filterPanel/view/sliders/SlidingSelect"],function(declare, lang, connect, topic,domAttr, query, on, domClass, domStyle){

	return declare("tui.flightdeals.view.sliders.dealsBudgetFilter",[tui.filterPanel.view.sliders.SlidingSelect],{

		 // ----------------------------------------------------------------------------- properties
		valueTmpl:  dojoConfig.currency + "${value}",

		  // ----------------------------------------------------------------------------- methods
	    postCreate: function () {
	            var widget = this;
	            widget.inherited(arguments);
	     },
	     drawIfData: function (model) {
	        	 var widget = this;
	             if (model) {
	                widget.drawSlider(lang.mixin({step: model.values}, model));
	             }
	     },

	     drawSlider: function () {
	            var widget = this;
	            widget.inherited(arguments);
	      },
	      //updating the steps with corresponding price
	      setMarkers: function(){
	          var widget = this, value, valueHolder, marker = null;
	          for (var i = 0; i <= widget.steps; i++){
	            marker = widget.trackLists()[i];
	            valueHolder = query('span', marker)[0];
	            if (i === 0){
	              value = widget.min;
	              valueHolder ? valueHolder.innerHTML = widget.renderValue(value) : '';
	              domAttr.set(marker, 'data-value', value);
	            }
	            else if (i > 0 && i < widget.steps){
	              value = widget.min + (i * widget.stepSize);
	              value = Math.ceil(value / 10) * 10;
	              valueHolder ? valueHolder.innerHTML = widget.renderValue(value) : '';
	              domAttr.set(marker, 'data-value', value);
	            } else{
	              value = widget.max;
	              valueHolder ? valueHolder.innerHTML = widget.renderValue(value + " +") : '';
	              domAttr.set(marker, 'data-value', value);
	              domClass.add(marker,"selected");
	            }
	          }
	       },
	      attachTrackEvents: function() {
	          var widget = this, pill = query('ul > li', widget.domNode)[0];

	          _.each(widget.trackLists(), function(pill, i) {
	            on(pill, 'click', function(e) {
	              if(domClass.contains(pill, 'disabled') || domClass.contains(pill, 'selected')) {
	                return;
	              }
	              dojo.stopEvent(e);
	              query("ul > li.selected", widget.domNode).removeClass("selected");

	              widget.updateSlider(i);
	              domClass.add(pill, 'selected');
	             // var criteria = dojo.fromJson(dojo.cookie("retainCriteria"));
	              if(domAttr.get(pill, 'data-value') != null){
	            	  if(pill.dataset != undefined){
	            		  /*if(i<4){
	            			  widget.dealsPanelModel.maxPrice = pill.dataset.value;
	            		  } else {
	            			  widget.dealsPanelModel.maxPrice ="";
	            			  domClass.add(pill, 'disabled');
	            		  }*/

	            		 // widget.dealsPanelModel.defaultBugetDomIndex = i;
	            		 // dojo.cookie("retainCriteria",dojo.toJson(criteria));
	            		  //widget.dealsPanelModel.SetDealsSavedSearch();
	            	  }
	            	  else{
	            		  /*if(i<4){
	            			  widget.dealsPanelModel.maxPrice = pill.getAttribute("data-value");
	            		  } else {
	            			  widget.dealsPanelModel.maxPrice = "";
	            			  domClass.add(pill, 'disabled');
	            		  }*/
	            		 // widget.dealsPanelModel.defaultBugetDomIndex = i;
	            		//  dojo.cookie("retainCriteria",dojo.toJson(criteria));
	            		//  widget.dealsPanelModel.SetDealsSavedSearch();
	            	  }

        			  query("#dealSearchPanelContainer .dealsApplyButton").removeClass("disabled").addClass("cta");

	              }


	            })
	          });

	        },
	        updateSlider: function(i) {
	            var slider = this, width = 0, pill = query('ul > li', slider.domNode)[0];
	            slider.current = i;
	            slider.stepWidth = slider.trackLists()[0].offsetWidth || slider.stepWidth;
	             //TODO: add the midrange functionality to max range slider as well!!
	              width = (i + 1) * slider.stepWidth;
	              domStyle.set(slider.slider, 'width', width + 'px');
	              _.each(slider.trackLists(), function(pill, index) {
	                if(!domClass.contains(pill, 'disabled')) {
	                  i < index ? domClass.add(pill, 'selectable') : [domClass.contains(pill, 'selectable') ? domClass.remove(pill, 'selectable') : ''];
	                }
	              })
	        },

	        updateSliderWithDom: function(slider,i){
	        	 var slider = slider, width = 0, pill = query('ul > li', slider.domNode)[0];
		            slider.current = i;
		            slider.stepWidth = slider.trackLists()[0].offsetWidth || slider.stepWidth;
		             //TODO: add the midrange functionality to max range slider as well!!
		              width = (i + 1) * slider.stepWidth;
		              domStyle.set(slider.slider, 'width', width + 'px');
		              _.each(slider.trackLists(), function(pill, index) {
		                if(!domClass.contains(pill, 'disabled')) {
		                  i < index ? domClass.add(pill, 'selectable') : [domClass.contains(pill, 'selectable') ? domClass.remove(pill, 'selectable') : ''];
		                }
		              })
	        }

	});
});