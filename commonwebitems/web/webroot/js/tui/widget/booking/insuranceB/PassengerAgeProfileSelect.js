define ("tui/widget/booking/insuranceB/PassengerAgeProfileSelect",
		["dojo",
         "dojo/_base/fx",
         "dojo/dom",
         "dojo/dom-attr",
         "dojo/query",
         "dojo/dom-class",
         "dojo/has",
         "dojo/on",
         "dojo/topic",
     	 "dojo/touch",
         "tui/widget/form/SelectOption"], function(dojo, fx, dom, domAttr, query, domClass, has, on, topic, touch, SelectOption){
  	dojo.declare("tui.widget.booking.insuranceB.PassengerAgeProfileSelect", [tui.widget.form.SelectOption], {

  		  dropDownClickEvt : null,

  		  showList : function(){
  			var widget = this;
  			widget.inherited(arguments);
  			widget.dropDownClickEvt = on(dojo.body(),dojo.touch.press,function (e) {
  				    var element = e.target ? e.target : e.srcElement;
  				    var closestElement = query(element).closest(".dropdownlist");
	                if (closestElement.length) {
                    return;
  				}
	                widget.hideList();
             });
  		  },

  		  hideList : function(){
  			  var widget = this;
  			  if(widget.dropDownClickEvt){
  				widget.dropDownClickEvt.remove();
  			}
  			widget.inherited(arguments);
  		  },

  		  onChange: function(name, oldValue, newvalue){
              var passengerAgeProfileSelect = this,
     	 	 	  divClass = "",
              	  dropdown = null,
              	  parentWidget = null;
              passengerAgeProfileSelect.inherited(arguments);
		      dropdown = passengerAgeProfileSelect.domNode;
		      divClass = domAttr.get(dropdown, "data-msg-div");
		      checkBoxId = domAttr.get(dropdown, "data-check-input");
		      parentWidget = passengerAgeProfileSelect.getParent();
		      domClass.add(_.first(query(".travelling-passenger-section ." + divClass, parentWidget.individualExpanded)), "disNone");
              if(newvalue.value.toLowerCase() === "not applicable"){
  					domClass.remove(_.first(query(".travelling-passenger-section ." + divClass, parentWidget.individualExpanded)), "disNone");
  			        domAttr.set(checkBoxId,"checked", false);
  			        topic.publish("tui/widget/booking/insuranceB/insuranceFamilyCover/updateInfantCheck");
  			      _.each(passengerAgeProfileSelect.listItems, function(listItem){
  			    	  domClass.add(listItem, "disabled");
       		       });
              }
              topic.publish("tui/widget/booking/insuranceB/insuranceFamilyCover/updatePrice");
          }
  	});
  	return tui.widget.booking.insuranceB.PassengerAgeProfileSelect;
  });