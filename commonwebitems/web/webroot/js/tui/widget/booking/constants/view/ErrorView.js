define("tui/widget/booking/constants/view/ErrorView", [
  "dojo",
  "dojo/_base/declare",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/constants/view/templates/ErrorTmpl.html",
  "dojo/on",
  "dojo/_base/lang",
  "dojo/dom"
 
  ], function (dojo,declare, _TuiBaseWidget, dtlTemplate, Templatable,ErrorTmpl,on,lang,dom) {
	 
	return declare('tui.widget.booking.constants.view.ErrorTmpl', [_TuiBaseWidget, dtlTemplate, Templatable], {
		
    tmpl: ErrorTmpl,
    templateString: "",
  
    
      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },
      
      postCreate: function () {
    	  this.inherited(arguments);
    	  this.attachEvents();
      },
      
      attachEvents: function () {
          on(this.okButton, "click", lang.hitch(this, this.handleOkButton));
      },
      
      handleOkButton: function () {
    	  dojo.removeClass(dom.byId("altflight-overlay"), 'error-overlay')
      }
     
		      
	});
});