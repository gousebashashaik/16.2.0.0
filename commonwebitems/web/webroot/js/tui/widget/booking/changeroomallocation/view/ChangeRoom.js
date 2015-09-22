define('tui/widget/booking/changeroomallocation/view/ChangeRoom', [
  'dojo',
  "dojo/on",
  "dojo/topic",
  'dojo/query',
  'dojo/_base/lang',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/text!tui/widget/booking/changeroomallocation/view/Templates/ChangeRoomTmpl.html',
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  'tui/widget/mixins/Templatable'


], function(dojo, on, topic, query, lang, domClass,domConstruct,ChangeRoomTmpl) {

dojo.declare('tui.widget.booking.changeroomallocation.view.ChangeRoom', [tui.widget._TuiBaseWidget,tui.widget.mixins.Templatable,tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable], {

	tmpl: ChangeRoomTmpl,
	refDataPath:"",

	 postCreate: function() {

		var widget = this;
	 	var widgetDom = widget.domNode;
	 	//fetching the data


	 	//Registering of the view to the controller.
	 	var controller=null;
	 	console.log(dijit.registry.byId("controllerWidget"));
	 	widget.controller= dijit.registry.byId("controllerWidget").registerView(widget);
		widget.accomData = dijit.registry.byId("controllerWidget").refData(widget.jsonData.packageViewData.accomViewData, widget.accomIndexVal);
	 	 widget.roomViewOptionViews = widget.getDataFunc(widget.refDataPath,widget.accomData);






	 	widget.initBookflowMessaging();
	 	widget.roomTile=_.isUndefined(widget.jsonData.roomOptionsStaticContentViewData.roomContentMap.Room_SelectRooms_CompHeader)? this.bookflowMessaging[dojoConfig.site].roomTitle: widget.jsonData.roomOptionsStaticContentViewData.roomContentMap.Room_SelectRooms_CompHeader;





	 	widget.indexRef = 0;
	 	widget.headerTitle =  _.isUndefined(widget.jsonData.roomOptionsStaticContentViewData.roomContentMap.Room_NumberOfRooms_CompHeader)? this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].changeCabinHeader: widget.jsonData.roomOptionsStaticContentViewData.roomContentMap.Room_NumberOfRooms_CompHeader;

	 	 widget.displayChangeButton = this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].roomSectionMetaData.changeRoomDisplFlag;

	    var html = widget.renderTmpl(widget.tmpl, widget);
	    domConstruct.place(html, widget.domNode, 'only');
	    dojo.parser.parse(widget.domNode);
	    this.inherited(arguments);
        widget.tagElements(dojo.query('button.large'),"Number of rooms component");
     widget.labelUpdat();
	 },
	 getDataFunc: function(dataPathRef, contextData){
			return lang.getObject(dataPathRef, false, contextData);
	 },
  labelUpdat : function(){
   var widget = this;
    var widgetDom = widget.domNode;
    var adultCount = query(".adultCount",widgetDom)[0];
    var roomButton  = query(".roomButton ",widgetDom)[0];
    var noOfRooms = query("li",widgetDom);
    var lastLi = noOfRooms[noOfRooms.length-1];
    var h3flag = false;
    var h2flag = false;
    if(lastLi) {
    domClass.add(lastLi,"bgNone");
    }
    for(var i = 0 ; i< noOfRooms.length ; i++)
    {
      var header  = query("h5",noOfRooms[i])[0];
      var spanCount = query("span",header);
      console.log(spanCount.length);
      if(spanCount.length == 3){
       h3flag = true;
        h2flag= false;
        break;
      } else if(spanCount.length == 2){
          h2flag= true;
          continue;
      }else{

      }

    }
    if(h3flag == true)
    {
      for(var i = 0 ; i< noOfRooms.length ; i++)
      {
        var header  = query("h5",noOfRooms[i])[0];
        domClass.add(header,"height70");
      }
    }
    else if(h2flag == true)
    {
      for(var i = 0 ; i< noOfRooms.length ; i++)
      {
        var header  = query("h5",noOfRooms[i])[0];
        domClass.add(header,"height50");
      }
    } else{
      for(var i = 0 ; i< noOfRooms.length ; i++)
      {
        var header  = query("h5",noOfRooms[i])[0];
        domClass.add(header,"height30");
      }
    }
    if(noOfRooms.length == 5){
      if(h3flag == true)
      {
        domClass.add(adultCount,"height190");
        if(roomButton != null){
        	domClass.add(roomButton,"roomButton5Rooms");
        }

      }else if(h2flag == true)
      {
        domClass.add(adultCount,"height170");
        if(roomButton != null){
	domClass.add(roomButton,"roomButton5Rooms");
        }

      } else{
        domClass.add(adultCount,"height150");
        if(roomButton != null){
	   domClass.add(roomButton,"roomButton5Rooms");
        }

      }

    }else{
      domClass.remove(adultCount,"height190");
      domClass.remove(adultCount,"height170");
      domClass.remove(adultCount,"height150");
      if(roomButton != null){
    	  domClass.remove(roomButton,"roomButton5Rooms");
      }

    }

  },
	 refresh : function(field,response)
		{

			 if(field == "changeRoom")
				 {
            var widget = this;
            var widgetDom = widget.domNode;
            widget.jsonData = response;
            widget.accomData = dijit.registry.byId("controllerWidget").refData(widget.jsonData.packageViewData.accomViewData, widget.accomIndexVal);
   	 	 widget.roomViewOptionViews = widget.getDataFunc(widget.refDataPath,widget.accomData);

            var html = widget.renderTmpl(widget.tmpl, widget);
            domConstruct.place(html, widget.domNode, 'only');
            dojo.parser.parse(widget.domNode);
            widget.labelUpdat();
				 }

		}


	});
	return tui.widget.booking.changeroomallocation.view.ChangeRoom;
});