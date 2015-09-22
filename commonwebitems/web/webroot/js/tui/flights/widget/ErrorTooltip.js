define("tui/flights/widget/ErrorTooltip", [
  "dojo",
  "dojo/text!tui/flights/templates/ErrorTooltip.html",
  "dojo/query",
  "dijit/registry",
  'dojo/dom-construct',
  "dojo/dom-style",
  "dojo/on",
  "dojo/keys",
  "dojo/ready",
  "dojo/dom-class",
  "dojo/dom-geometry",
  'dojo/_base/html',
  "dojo/_base/lang",
  "tui/widget/_TuiBaseWidget"], function (dojo, erroTooltipTmpl, query, registry, domConstruct, domStyle, on, keys, ready, domClass, domGeom, html, lang) {
  dojo.declare("tui.flights.widget.ErrorTooltip", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {
	  
	  errorHeaderMsg: null,
	  
	  id: null,
	  
	  tmpl: erroTooltipTmpl,
	  
	  errorMsg: null,
	  
	  connectId: "",
	  
	  constructor:function(params){
	  		var errorTooltip = this;
	  		errorTooltip.inherited(arguments);
	  		
	  		errorTooltip.connectId = params.connectId;
	  		errorTooltip.errorMsg = params.errorMsg;
	  		errorTooltip.errorHeaderMsg = params.errorHeaderMsg;
	  		errorTooltip.id = params.id;
	  		
	  		var widget = registry.byId(errorTooltip.id);
	  		if(widget){
	  			errorTooltip.destroyRecursive();
	  		}
	  		
  	  },
  	  
  	  postCreate: function(){
  		  var errorTooltip = this;
  		  errorTooltip.inherited(arguments);
  		  
  		  /* display error.. */
  		  errorTooltip.showErrorToolTip();
  		  
  		  on(document.body, "click", function(evt){
  			  evt = evt.target || evt.srcElement;
  			  if(evt.id === "whenField")return;
  			  errorTooltip.destroyErrorToolTip();
  		  });
  	  },
  	  
  	  onAfterTmplRender: function(){
  		var errorTooltip = this;
		
  	  },
  	  
  	  showErrorToolTip: function(){
  		  var errorTooltip = this;
  		  
  		  var attachpoint = query(errorTooltip.connectId)[0];
  		  var parentAttachpoint = query(attachpoint).parent()[0];
  		  
  		  var pos = html.coords(parentAttachpoint);
  		  
  		  domClass.add(attachpoint, "error-tool-tip");
	  	  
  		  var error = errorTooltip.render();
  		  
  		  domConstruct.place(error, parentAttachpoint, "last");
  		    		   		  
  		  domStyle.set(query("#" + errorTooltip.id)[0], {
	  			  "position":"absolute",
	  			  "display": "block",
	  			  "top": "33px",
	  			  "left":"0px",
	  			  "width": "244px",
	  			  "max-width": "248px"
  		  });
  		  
  	  },
  	  
  	  destroyErrorToolTip: function(){
  		var errorTooltip = this;
  		var attachpoint = query(errorTooltip.connectId)[0];
  		
  		domClass.remove(attachpoint, "error-tool-tip");
  		
  		try{
  			domConstruct.destroy(query("#" + errorTooltip.domNode.id)[0]);
  		}catch(e){}
  		
  		
  		errorTooltip.destroyRecursive();
  	  },
  	  
  	 render: function(){
  		  var errorTooltip = this;
		  var html = errorTooltip.renderTmpl(errorTooltip.tmpl);
		  return html;
	  }
	  
  });
  
  return tui.flights.widget.ErrorTooltip;
});