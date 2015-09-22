define("tui/widget/booking/alertMessage/view/AlertsMessage",["dojo/_base/declare",
                                           "dijit/_WidgetBase",
                                           "tui/widget/mixins/Templatable",
                                           "dojox/dtl/_Templated",
                                           'dojo',
                                           'dojo/query',
                                   		'dojo/dom-class',
                                   		"dojo/dom-style",
                                   		"dojo/dom",
                                   		"dojo/dom-construct",
                                   		"dojo/text!tui/widget/booking/alertMessage/view/template/AlertsMessageTmpl.html",
                                   		"dojo/html",
                                           "dojox/dtl",
                                           "dojox/dtl/Context",
                                           "tui/widget/_TuiBaseWidget"],
		function(declare, _WidgetBase, Templatable,dtlTemplated,dojo, query, domClass,domStyle,dom,domConstruct,AlertsMessage,html){
	    return declare("tui.widget.booking.alertMessage.view.AlertsMessage",[tui.widget._TuiBaseWidget,_WidgetBase,dtlTemplated,Templatable],{

	    	  tmpl:AlertsMessage,
	    	  templateString: "",
	    	  widgetsInTemplate : true,
	    	  templateView:false,



	    	 buildRendering: function(){

	    		  	if(jsonData.alertMessages)
	    		  		{

	    		  		this.templateView = true;
	    		  		this.templateString = this.renderTmpl(this.tmpl, this);
	    		  		delete this._templateCache[this.templateString];
	    		  		}else{
	    		  			this.templateString = this.renderTmpl(this.tmpl, this);
		    		  		delete this._templateCache[this.templateString];
	    		  		}
		       		this.inherited(arguments);
		       	},

		        postCreate: function () {
		        	var widget = this;
		    	 	var widgetDom = widget.domNode;
		    	 	 var controller=null;
		    		 widget.controller = dijit.registry.byId("controllerWidget");
		    		 var testvar = widget.controller.registerView(widget);

		        	  var hideLink= query('.closeLink',widgetDom)[0];
		  		 	dojo.connect(hideLink,'click',function(e){
		  		 		widget.hideRemoveText();
		  		 		});
		  		    this.inherited(arguments);
		  		    var alertDom=query('.info-section');
		  		    for(var index=0;index<alertDom.length;index++){
		  		    	if(alertDom[index].id=="The price of your hoilday has changed"){
		  		      widget.tagElement(alertDom[index],"holPriceChange");
		  		    	}
		  		    	else if (alertDom[index].id=="Your holiday is now even cheaper!"){
		  		    widget.tagElement(alertDom[index],"holPriceDown");
		  		    	}
		  		    	else if (alertDom[index].id=="The price of an extra has changed"){
				  		    widget.tagElement(alertDom[index],"extraPriceChange");
				  		    	}
		  		    	else if(alertDom[index].id=="A selected extra is no longer available"){
				  		    widget.tagElement(alertDom[index],"extraNotAvailable");
				  		    	}

		  		    }
			      },

			      hideRemoveText: function() {
			    		var getSection = query(".alert-infosection");
			    		//			alert(getSection[0]);
			    		getSection[0].style.display = "none";

			    	},

	    	  constructor: function () {

		        },
		        refresh : function(field,response)
				{

				    this.jsonData = response;


	    		  	if(jsonData.alertMessages != null)
    		  		{

    		  		this.templateView = true;
    		  		this.templateString = this.renderTmpl(this.tmpl, this);

    		  		domConstruct.place(this.templateString, this.domNode, "only");

    		  		}else{
    		  			this.templateView = false;
    		  			this.templateString = this.renderTmpl(this.tmpl, this);

        		  		domConstruct.place(this.templateString, this.domNode, "only");
    		  		}
	    		  	var widget = this;
		    	 	var widgetDom = widget.domNode;
	    		  	 var hideLink= query('.closeLink',widgetDom)[0];
			  		 	dojo.connect(hideLink,'click',function(e){
			  		 		widget.hideRemoveText();
			  		 		});


				}
	});

});