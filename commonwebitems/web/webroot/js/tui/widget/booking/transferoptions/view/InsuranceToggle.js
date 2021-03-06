define("tui/widget/booking/transferoptions/view/InsuranceToggle", [
  "dojo/_base/declare",
  "dijit/_WidgetBase",
  "dojo/on",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-style",
  "dojo/dom-class",
  "dojo/dom",
  "dojo/dom-construct",
  "dojo/topic",
  "dojo/cookie",
  "dojo/_base/connect",
  'tui/widget/popup/Popup',
  "dojo/dom-class",
  "dojo/text!tui/widget/booking/transferoptions/view/Templates/InsurancePopupComponent.html",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget",
  "dojox/fx/scroll"
], function (declare, _WidgetBase, on, query, domAttr, domStyle, domClass, dom, domConstruct, topic, cookie, connect, popup,domclass,insuTmpl) {

  return declare("tui.widget.booking.transferoptions.view.InsuranceToggle",[
        tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.popup.Popup
    ], {

	  stopDefaultOnCancel: false,
		modalClick: false,
        navigate: this.jsonData.pageResponse.targetPageUrl,
		modal: true,
		includeScroll: true,
		tmpl: insuTmpl,
		smoothScroll : dojox.fx.smoothScroll,
        showPopUp: false,
        noTransferCheckValue : dojo.byId("INSURENCEPOPUP"),
        overlayEventAttached : false,


    postCreate: function () {
    	//console.log('Great');
      var widget = this,
                close = query('#checkBox-Overlay-insurance .close'),
          insuranceAddButton = query("#family .indClButton"),
          familyInsuranceAddButton = query("#individual .getQuoteFamBtn");

            widget.noTransferCheckValue.checked = cookie("passengerOption") === "true" ? true : false;
            cookie("passengerOption", "", {
                expires: 0
            });
	   if(insuranceAddButton && familyInsuranceAddButton && (insuranceAddButton.length || familyInsuranceAddButton.length)) {
		   if((insuranceAddButton.length && domAttr.get(insuranceAddButton[0],"style").indexOf("block") === -1) || (familyInsuranceAddButton.length && domAttr.get(familyInsuranceAddButton[0],"style").indexOf("block") === -1) ){
                	widget.noTransferCheckValue.disabled = true;
		   }
	   }
            connect.subscribe("tui/booking/insurance", function(message) {
                widget.noTransferCheckValue.checked = false;
                // widget.showPopUp = message;
                widget.noTransferCheckValue.disabled = !message;
	   });
	   connect.subscribe("tui/booking/insurance/openInsurancePopUp", function(){
                if (!widget.noTransferCheckValue.checked && !widget.noTransferCheckValue.disabled) {

                	 var priceBreakDown = dom.byId("priceBrk");
                     var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
                     domclass.remove(priceBreakdownSticky,"stick");
                     window.scrollTo(0,0);
                    widget.showPopUp = true;
	    		  widget.open();
                    widget.showPopUp = false;
	    	  } else {
	    		  window.location.href=widget.navigate;
	    	  }
	   });
	   if(close && close.length){
    	  on(close,"click", function (e){
    		  widget.close();
    	  });
      }
      widget.setPosOffset(widget.floatWhere);
      widget.inherited(arguments);
    },

    open: function(){
            var widget = this;
        if( widget.showPopUp){
                widget.noTransferCheckValue.checked = true;
             widget.inherited(arguments);
             widget.commonFunction();
        }
    },

    commonFunction: function(){
          var widget = this,
            	popupDomCreationTimeOut = setInterval(function(){
            	if(query('#checkBox-Overlay-insurance #checkBoxAddInsurance').length && query('#checkBox-Overlay-insurance #checkBoxArrangeInsurance').length){
            		on(query('#checkBox-Overlay-insurance #checkBoxAddInsurance'), "click", function(e) {
                        dom.byId("INSURENCEPOPUP").checked = false;
                        widget.close();
                    });

                    on(query('#checkBox-Overlay-insurance #checkBoxArrangeInsurance'), "click", function(e) {
                        cookie("passengerOption", "true", {
                            expires: 5
                        });
                        widget.close();
                        window.location.href = widget.navigate;
                    });
                    clearInterval(popupDomCreationTimeOut);
            	}
            }, 200);
    },

    close: function(){
    	var widget = this,
            	modalBackground = query(".modal");
            widget.noTransferCheckValue.checked = false;
    	widget.inherited(arguments);
            if(modalBackground && modalBackground.length){
            	domStyle.set(modalBackground[0],"display","none");
            }
    },


    refresh: function(field, response){

    }

  });


});