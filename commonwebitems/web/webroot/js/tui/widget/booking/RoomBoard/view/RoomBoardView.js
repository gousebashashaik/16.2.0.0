define("tui/widget/booking/RoomBoard/view/RoomBoardView",["dojo/_base/declare",
                                           "dijit/_WidgetBase",
                                           "tui/widget/mixins/Templatable",
                                           "dojox/dtl/_Templated",
                                           'dojo',
                                           "dojo/on",
                                           'dojo/query',
                                   		   'dojo/dom-class',
                                   		   "dojo/dom-style",
                                   		   "dojo/_base/lang",
                                   		   "dojo/dom",
                                   		   "dojo/dom-construct",
                                   		   "dojo/text!tui/widget/booking/RoomBoard/view/Templates/RoomBoardBasis.html",
                                   		   "dojo/topic",
                                   		   "dojo/html",
                                           "dojox/dtl",
                                           "dojox/dtl/Context",
                                           "tui/widget/expand/Expandable",
                                           "tui/widget/_TuiBaseWidget",
                                           "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable"
                                           ],

function(declare, _WidgetBase, Templatable,dtlTemplated,dojo,on, query, domClass,domStyle,lang,dom,domConstruct,RoomBoardBasisTmpl,topic,html){
return declare("tui.widget.booking.RoomBoard.view.RoomBoardView",[tui.widget._TuiBaseWidget,_WidgetBase,dtlTemplated,Templatable,tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable],{

	 tmpl: RoomBoardBasisTmpl,
	 templateString: "",

	 buildRendering: function(){
		this.initBookflowMessaging();
		this.toolTipClassName = this.bookflowMessaging[dojoConfig.site].roomBoard.toolTipClassName;

		this.headerTitle = this.bookflowMessaging[dojoConfig.site].roomBoard.title;
	 	this.headerArray = this.bookflowMessaging[dojoConfig.site].roomBoard.header;
	 	//temp fix for cruise will be reverted back once content is available for cruise

	 	this.alternateBoardBasisData = this.jsonData.alternateBoardBasis;


	 	this.currentPage = this.jsonData.summaryViewData.currentPage;

	 	if(this.currentPage == "roomoptions"){

	 	}else{
	 		this.disNoneClass = _.isUndefined(this.bookflowMessaging[dojoConfig.site].roomBoard.displayNoneClass) ? " " : this.bookflowMessaging[dojoConfig.site].roomBoard.displayNoneClass;
		 	this.spanClass = _.isUndefined(this.bookflowMessaging[dojoConfig.site].roomBoard.spanClass) ? "disNone" : this.bookflowMessaging[dojoConfig.site].roomBoard.spanClass;

	 		this.alternateBoardBasisData = this.staticContentMap(this.alternateBoardBasisData);
	 	}



	    this.templateString = this.renderTmpl(this.tmpl, this);
	    delete this._templateCache[this.templateString];
   		this.inherited(arguments);
	 },

	 staticContentMap: function(alternateBoardBasisData){

		 _.each(alternateBoardBasisData, function(item){

		 		item["staticContentIntro"] = _.isUndefined(this.jsonData.roomOptionsStaticContentViewData.roomContentMap[item.boardBasisCode+"_Description"]) ? " " : this.jsonData.roomOptionsStaticContentViewData.roomContentMap[item.boardBasisCode+"_Description"];
		 	});

		 return alternateBoardBasisData
	 },

     postCreate: function () {
    	var widget = this;
	 	var widgetDom = widget.domNode;
	 	var controller=null;
	 	widget.controller= dijit.registry.byId("controllerWidget").registerView(widget);
	 	console.log(widget.jsonData);
	 	dojo.parser.parse(widget.domNode);


		this.inherited(arguments);

     },

     refresh : function(field,response) {
    	 if(field == "boardBasis" || field == "Rooms")
    	 {
	    	 var widget = this;
	    	 var widgetDom = widget.domNode;
	    	 widget.jsonData = response;
	    	 widget.alternateBoardBasisData = widget.jsonData.alternateBoardBasis;

	    	 widget.alternateBoardBasisData = widget.staticContentMap(widget.alternateBoardBasisData);

	    	 var html = widget.renderTmpl(widget.tmpl, widget);
	         domConstruct.place(html, widget.domNode,'only');
			 dojo.parser.parse(widget.domNode);
    	 }
     }

});
});