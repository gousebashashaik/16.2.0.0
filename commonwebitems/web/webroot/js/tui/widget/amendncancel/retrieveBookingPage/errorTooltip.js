define('tui/widget/amendncancel/retrieveBookingPage/errorTooltip', [
  'dojo',
  'dojo/dom-class',
  'dojo/text!tui/widget/amendncancel/retrieveBookingPage/templates/errorTooltip.html',
  'tui/widget/popup/Tooltips'], function(dojo,domClass, errTooltipTmpl) {
  dojo.declare('tui.widget.amendncancel.retrieveBookingPage.errorTooltip', [tui.widget.popup.Tooltips], {

	tmpl: errTooltipTmpl,
	position: "position-top-center",
	errClass: "val-error",
	errMsgHdr: "Hold on there...",
	errMsgDesc: "Some details still haven't been entered or are currently invalid",

	formInstance: null,

	registerForm:function(form){
		this.formInstance = form;
	},

	addPopupEventListener: function () {
		var errTooltip = this;
		if (!errTooltip.domNode) return;
		errTooltip.connect(errTooltip.domNode, ["on", errTooltip.eventType].join(""), function (e) {
			if (domClass.contains(errTooltip.domNode, errTooltip.errClass)){
				dojo.stopEvent(e);
				//var formValid = errTooltip.formInstance.validate(false);
				//if(!formValid){
					errTooltip.open();
				//}
			}
		});
		
		
		if(document.addEventListener) {
			errTooltip.domNode.addEventListener('touchstart', function(e) {
			if (domClass.contains(errTooltip.domNode, errTooltip.errClass)){
				dojo.stopEvent(e);
				var formValid = errTooltip.formInstance.validate(true);
				if(!formValid){
					errTooltip.open();
				}
			}
		  });
		  
		  errTooltip.domNode.addEventListener('touchend', function(e) {
			if (errTooltip.popupDomNode){
					dojo.stopEvent(e);
					errTooltip.close();
				}
		  });
		}

		if ((errTooltip.eventType == "mouseenter") || (errTooltip.eventType == "mouseover")) {
			errTooltip.connect(errTooltip.domNode, "onmouseout", function (e) {
				if (errTooltip.popupDomNode){
					dojo.stopEvent(e);
					errTooltip.close();
				}
			});
		}
	},

	showHidePopUp:function(isValid){
		var errTooltip = this;
		if(isValid){
			if(domClass.contains(errTooltip.domNode, errTooltip.errClass)){
				domClass.remove(errTooltip.domNode, errTooltip.errClass);
			}
		}else{
			domClass.add(errTooltip.domNode, errTooltip.errClass);
		}

	}

  });
    return tui.widget.amendncancel.retrieveBookingPage.errorTooltip;
});


