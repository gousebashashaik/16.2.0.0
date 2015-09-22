define('tui/widget/booking/changeroomallocation/view/ChangeRoomAllocation', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  "dojo/dom-construct",
  "dojo/dom",
  "dojo/on",
  "dijit/registry",
  "dojo/Evented",
  "dojo/dom-attr",
  "dojo/topic",
  "dojo/number",
  'tui/widget/booking/changeroomallocation/modal/RoomModel',
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/text!tui/widget/booking/changeroomallocation/view/Templates/ChangeTypeAllocationTmpl.html",
  "dojo/_base/lang",
  "tui/widget/booking/changeroomallocation/view/RoomTypeAllocation",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "tui/widget/_TuiBaseWidget",
  'tui/widget/mixins/Templatable'], function(dojo, query, domClass,domConstruct,dom,on,registry,Evented,domAttr,topic,number,roomModel,BookflowUrl,changeTypeAllocationTmpl, lang) {

dojo.declare('tui.widget.booking.changeroomallocation.view.ChangeRoomAllocation', [tui.widget._TuiBaseWidget,tui.widget.mixins.Templatable,tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable], {
    handles: [],
    url:BookflowUrl.changeroomsurl,
    dataPathRef:"",
    addButtonHandles:[],
    okButtonHandles:[],
    addButtonCount: null,
    childsCount:null,
    adultsCount:null,
    tempjsonData:null,
    refTotalAdultsCount: null,
    refTotalChildsCount: null,
    refChildsAge: [],
    TotalAdultsCount:null,
    TotalChildsCount: null,
    tmpl: changeTypeAllocationTmpl,
    changeRoomOverlay: null,
    postCreate: function() {
        var widget = this;
         var widgetDom = widget.domNode;

         widget.initBookflowMessaging();
         widget.errorDisplayContent = widget.bookflowMessaging[dojoConfig.site].roomErrorTitle;
         widget.roomRef = widget.bookflowMessaging[dojoConfig.site].roomTitle;
         widget.roomStaticText = widget.bookflowMessaging[dojoConfig.site].roomStaticText;
         topic.subscribe("some/topic", function(){
            widget.labelDisplay();
        });
         topic.subscribe("some/topic/childAgeTitleDisplay", function(){
            widget.childAgeTitleDisplay();
        });



         
         roomModel.accomIndexVal = widget.accomIndexVal;

         widget.commonFunction();
          this.inherited(arguments);
         widget.tagElements(query('button.addroom'),"addroom");
         widget.tagElements(query('a.cancel'),"cancelSelectRooms");
         widget.tagElements(query('button.submit'),"continueSelectRooms");
         widget.tagElements(query('a.close'),"closeSelectRooms");
         var roomrowDom=query('.room_row',widgetDom);
         for(var index=0;index < roomrowDom.length;index++){
             widget.tagElement(roomrowDom[index],"room"+(index+1)+"interaction");
         }

         topic.subscribe("tui/widget/booking/changeRoomAllocation/displayAddRoomButton", function(){
           widget.displayAddRoomButton();
        });
         topic.subscribe("some/topic/displayErrorMsg", function(fieldName){       
        	 if (fieldName == "changeRoom"){ 
        		   widget.errorMsgDisplay();
        	 }     
        });

         var controller=null;
         widget.controller= dijit.registry.byId("controllerWidget");
         var tempVar =widget.controller.registerView(widget);

         topic.subscribe("tui/widget/ChangeRoomAllocationOverlay/close", function(){
             widget.canceButtonClick();
        });
         widget.clickEventForRemoveTag();
         widget.addButton();
         widget.okButton();
         widget.inherited(arguments);

         var RoomData = widget.triggerFunction(widget.dataPathRef, widget.jsonData);

         widget.accomData = dijit.registry.byId("controllerWidget").refData(widget.jsonData.packageViewData.accomViewData, widget.accomIndexVal);
   	 	 widget.roomViewOptionViews = widget.triggerFunction(widget.refDataPath,widget.accomData);




         console.log(RoomData);
         console.log(widget.sellingCodeRefPath)

         //jsp data-dojo-props index filght1 accom0
         indexVal = 1;
         var refData = _.find(widget.roomViewOptionViews, function(item,index){ if(index == indexVal) return item });
         console.log(refData);

         widget.dataRefContainer(RoomData);
         
        
         
     },
     
     dataRefContainer: function(RoomData){
    	 
    	 var widget = this;
    	 roomModel.roomSellingCode = RoomData[0][widget.sellingCodeRefPath][0].sellingCode;
    	 
     },
     
     triggerFunction: function(dataPathRef , contextObj){
        return lang.getObject(dataPathRef, false ,contextObj ) ;
     },
     canceButtonClick: function(){

         var widget = this;
         var widgetDom = widget.domNode;

         var container= dojo.query("#room_row_content",widgetDom)[0];
         var roomOptions =  dojo.query('.room_row',container);

         var viewRoomCount = roomOptions.length;
         var jsonDataRoomCount = widget.roomViewOptionViews.length;
         var errorDisplayBlock = query(".errorMsg",widgetDom)[0];
         if(errorDisplayBlock != null){
             domClass.add(errorDisplayBlock,"disNone");
         }

            if( viewRoomCount == jsonDataRoomCount){

                widget.commonFunction();
                widget.clickEventForRemoveTag();
            }else if(viewRoomCount != jsonDataRoomCount){

                widget.commonFunction();
                widget.clickEventForRemoveTag();

            }





     },
     hasFunction: function(contentBlock){

         var widget =this;
         var widgetDom = widget.domNode;
         var container= dojo.query("#room_row_content",widgetDom)[0];
         var roomOptions =  dojo.query('.room_row',contentBlock);
         _.each(roomOptions, function(room){

             var roomId = domAttr.get(room, "id");
             var domnode= dom.byId(roomId);
             var roomVal = roomId.slice(5);
             var roomValRef = roomVal-1;

             if( widget.roomViewOptionViews.hasOwnProperty(roomValRef)){

                    var refchilAdultcount = widget.roomViewOptionViews[roomValRef].noOfAdults;
                    var refchilCount = widget.roomViewOptionViews[roomValRef].totalChildrenCount;

                     dojo.forEach(dojo.query(".adultCount", contentBlock), function (item, i) {

                         for(var j=0 ; j<item.options.length;j++)
                             {

                             if(item.options[j].innerHTML == refchilAdultcount)
                                 {
                                 item.options[j].selected = true;
                                 item.options[j].label = refchilAdultcount;
                                 }
                             }
                     });

                     if(refchilCount != 0){
                         dojo.forEach(dojo.query(".childCount", contentBlock), function (item, i) {

                               for(var j=0 ; j<item.options.length;j++)
                                   {

                                   if(item.options[j].innerHTML == refchilCount)
                                       {
                                       item.options[j].selected = true;
                                       item.options[j].label = refchilCount;
                                       }
                                   }

                           });
                     }else{

                    	 dojo.forEach(dojo.query(".childCount", contentBlock), function (item, i) {

                             for(var j=0 ; j<item.options.length;j++)
                                 {

                                 if(item.options[j].innerHTML == "0")
                                     {
                                     item.options[j].selected = true;
                                     item.options[j].label = "0";
                                     }
                                 }

                         });

                         /*	Remove the child content block
                          *
                          * dojo.forEach(dojo.query(".childCount", contentBlock), function (item, i) {

                             domConstruct.destroy(item);

                            });*/
                     }



                }else{


                    }

         });

     },

     labelDisplay: function()
     {
         var widget =this;
         var widgetDom = widget.domNode;
         var container= query("#room_row_content",widgetDom)[0];
         var roomOptions =  query('.room_row',container);
         var count = 1;
         _.each(roomOptions, function(room){
             var string = widget.roomStaticText+count;
             domClass.add(room,"dashed-border");
             var roomNo = query('.room_no',room)[0];
             var closeRoom=  query('.close_row',room)[0];
             var selectTags= query('select',room);

             var removeTags = query(".close_row",widgetDom);
             
             _.each(removeTags, function(removeTag){
            	 removeTag.innerHTML = "Remove "+widget.roomStaticText;
             });
             

             _.each(selectTags, function(selectTag){

                 var selecTagId= widget.roomStaticText+" "+count;
                 domAttr.set(selectTag, "name", selecTagId);

             });

             var roomNumString= widget.roomStaticText+" "+count;
             roomNo.innerHTML =  roomNumString ;
             domAttr.set(closeRoom, "id", roomNumString);
             domAttr.set(room, "id", roomNumString);
             if(roomNumString == widget.roomStaticText+" 1")
                 {

                 domClass.add(closeRoom,"dispnone");

                 }
             if(roomNumString == widget.roomStaticText+" 5")
             {

             domClass.remove(room,"dashed-border");

             }
             count++;
         });

         widget.childAgeTitleDisplay();


     },
     //Code for to display the ages title on conditional basis.
     childAgeTitleDisplay: function(){
         var widget = this;
         var widgetDom= widget.domNode;
         var container= query("#room_row_content",widgetDom)[0];
         var roomOptions =  query('.room_row',container);
         var displayAgeTitle = false;
         for(var i=0 ; i<roomModel.rooms.length;i++)
         {
             if    (roomModel.rooms[i].noOfChildren == "-" ||  roomModel.rooms[i].noOfChildren =="0")
                 {
                 displayAgeTitle= false;
                 continue;
                 }else{
                     displayAgeTitle = true;
                     break;
                 }

         }

         var childAgeTitle = query('.child_age_title',container)[0];

             if(typeof childAgeTitle != 'undefined')
         {

         if(displayAgeTitle == false)
             {

             domClass.add(childAgeTitle,"disNone");

             }else{
                 domClass.remove(childAgeTitle,"disNone");
             }
         }

     },
     removeFunction: function(roomId)
     {
         var widget = this;
         var widgetDom = widget.domNode;
         domConstruct.destroy(roomId);

         var container= query("#room_row_content",widgetDom)[0];
         var roomOptions =  query('.room_row',container);

         var actualNoRooms = roomOptions.length;
             for(var i=0 ; i<roomModel.rooms.length;i++)
             {

                 if(roomModel.rooms[i].id == roomId)
                 {
                   roomModel.rooms.splice([i],1);
                 }
             }

         for(i=0; i<actualNoRooms; i++)
             {
             var tempRoomId =widget.roomStaticText+" "+[i+1];
             roomModel.rooms[i].id = tempRoomId;
             }



         widget.labelDisplay();

         widget.displayAddRoomButton();



     },
    displayAddRoomButton: function(){
        var widget = this;
        var widgetDom = widget.domNode;
        if(widget.addButtonCount < 6)
        {

            var addButton = query(".addroom" ,widgetDom)[0];
            domClass.remove(addButton,"dispnone");
            widget.addButtonCount = widget.addButtonCount - 1;

        }
    },
    clickEventForRemoveTag: function()
     {
         var widget = this;
         var widgetDom = widget.domNode;
         var removeTags = query(".close_row",widgetDom);
             _.each(removeTags, function(removeTag){
                 widget.handles.push(on(removeTag, 'click', function(e) {
                 var errorDisplayBlock = query(".error-notation",widgetDom)[0];
                 var roomId = domAttr.get(removeTag, "id");
                 widget.removeFunction(roomId);
                 var errorBlock = query(".errorMsg",widgetDom)[0];
                 domClass.add(errorBlock,"disNone");
                 domClass.add(errorDisplayBlock,"disNone");
                 }));
             });


     },
     generateRequest : function(ajaxObj) {

             var field = "changeRoom";
            var widget= this;
            var request={roomSelectionCriteria: dojo.toJson(ajaxObj)};
            var url = widget.url;
            widget.controller.generateRequest(field,url,request);
            return request;

    	/* var request={roomSelectionCriteria: dojo.toJson(ajaxObj)};
    	 console.log(request);
    	 this.errorMsgDisplay();*/

        },
        commonFunction: function()
        {
        var widget = this;
        var widgetDom = widget.domNode;
        widget.addButtonCount = 0;
         widget.TotalAdultsCount =0;
         widget.TotalChildsCount=0;
         widget.childsCount = [];
        widget.adultsCount= [];
        roomModel.rooms = [];
        widget.refChildsAge=[];
        widget.childRefId=[];
        var addButton = query(".addroom" ,widgetDom)[0];
        var contentBlock = dom.byId("room_row_content");
        roomModel.jsonData =dojo.clone(widget.jsonData);
        widget.tempjsonData = dojo.clone(widget.jsonData);
        domClass.remove(addButton,"dispnone");
        widget.refTotalAdultsCount = widget.jsonData.packageViewData.paxViewData.noOfAdults + widget.jsonData.packageViewData.paxViewData.noOfSeniors;
        widget.refTotalChildsCount = widget.jsonData.packageViewData.paxViewData.noOfChildren + widget.jsonData.packageViewData.paxViewData.noOfInfants ;
        var errorDisplayBlock = query(".error-notation",widgetDom)[0];
        domClass.add(errorDisplayBlock,"disNone");

        for (var i = 0 ; i< widget.tempjsonData. packageViewData.passenger.length ; i++){

            if(widget.tempjsonData. packageViewData.passenger[i].type == "CHILD" || widget.tempjsonData. packageViewData.passenger[i].type == "INFANT"){

                widget.childRefId.push(widget.tempjsonData. packageViewData.passenger[i].identifier);

            }

        }
        for(var i=0 ; i< widget.tempjsonData.packageViewData.paxViewData.childAges.length ; i++ )
            {
            widget.refChildsAge.push(widget.jsonData.packageViewData.paxViewData.childAges[i]);
            }
        for ( var i = 1 ;i<=widget.refTotalChildsCount; i++){
            widget.childsCount.push(i)
       }
        for ( var i = 1 ;i<=widget.refTotalAdultsCount; i++){
             widget.adultsCount.push(i)
        }

        widget.childsCount.splice(0,0,"0");
        widget.childsCount.splice(0,0,"-");
        if(widget.jsonData.inventoryType == "TRACS"){

        	 widget.adultsCount.splice(0,0,"-");
        }else{
        	widget.adultsCount.splice(0,0,"0");
        	 widget.adultsCount.splice(0,0,"-");
        }



            var html = widget.renderTmpl(widget.tmpl, widget);

            domConstruct.place(html, contentBlock, 'only');
         var roomViewData = widget.roomViewOptionViews;
         for(var i=0;i<roomViewData.length;i++)
             {
             var item={id:widget.roomStaticText+" "+[i+1],noOfAdults: 0,noOfChildren: 0,noOfInfants:0,childAges:[]};
             roomModel.rooms.push(item);
             widget.addButtonCount = widget.addButtonCount + 1;
             }
         if(widget.addButtonCount === 5)
            {
            domClass.add(addButton,"dispnone");
            }
          widget.labelDisplay();
        dojo.parser.parse(contentBlock);
    },
    addButton: function()
    {
        var widget = this;
        var widgetDom = widget.domNode;
        var addButton = query(".addroom" ,widgetDom)[0];

         widget.addButtonHandles.push(dojo.connect(addButton, 'click', dojo.hitch(this, function(e) {

                 /*var tmplSection = query(".roomTmpl" ,widgetDom)[0];*/

                  var widget = this;
                  var errorBlock = query(".errorMsg",widgetDom)[0];
                  var errorDisplayBlock = query(".error-notation",widgetDom)[0];
                  domClass.add(errorBlock,"disNone");
                 domClass.add(errorDisplayBlock,"disNone");
                 if(widget.addButtonCount < 6)
                  {
                  var newInstance = new tui.widget.booking.changeroomallocation.view.RoomTypeAllocation({'jsonData': jsonData,'roomViewOptionViews':widget.roomViewOptions, 'roomOptionsViewDataRef' : widget.roomOptionsViewData});

                  var count = roomModel.rooms.length;
                  var item={id:widget.roomStaticText+" "+[roomModel.rooms.length+1],noOfAdults: 0,noOfChildren: 0,noOfInfants:0,childAges:[]};

                 roomModel.rooms.push(item);
                 var data = dojo.toJson(roomModel);

                 var container= query("#room_row_content",widgetDom)[0];
                 domConstruct.place(newInstance.domNode, container, 'last');

                 widget.labelDisplay();
                 dojo.forEach(widget.handles, dojo.disconnect);
                 widget.hasFunction(newInstance.domNode);
                 widget.clickEventForRemoveTag();

                 dojo.parser.parse(newInstance.domNode);
                 widget.addButtonCount = widget.addButtonCount + 1;

                  }
                 if(widget.addButtonCount === 5)
                     {
                     domClass.add(addButton,"dispnone");
                     }
             })));
    },
    okButton: function()
    {
        var widget = this;
        var widgetDom = widget.domNode;
         var okButton = query(".submit",widgetDom)[0];
         var errorDisplayBlock = query(".error-notation",widgetDom)[0];
         var errorBlock = query(".errorMsg",widgetDom)[0];
            widget.okButtonHandles.push(dojo.connect(okButton, 'click', function(e) {
                var childAgeRef=[];
                var childAgeMismatchFlag = true;
                var successAjax = true;
                widget.TotalAdultsCount= 0;
                widget.TotalChildsCount=0;
                 domClass.add (errorBlock,"disNone");
                for(var i = 0; i<widget.refChildsAge.length; i++)
                    {
                    childAgeRef.push(widget.refChildsAge[i]);
                    }


                for (var i=0; i< roomModel.rooms.length; i++)
                    {
                    //invalid entry validation

                    var regex = /^[\-]+$/
                    var childstr = roomModel.rooms[i].noOfChildren;
                    var adultstr = roomModel.rooms[i].noOfAdults;
                    var roomId = roomModel.rooms[i].id;
                    
                    if (adultstr == 0 && childstr == 0 )
                    {
                    successAjax = false;
                    domClass.remove(errorDisplayBlock,"disNone");
                    domAttr.set(errorDisplayBlock, "innerHTML", roomId +" "+"Invalid entries");

                    break;
                    }
                    
                    if (regex.test(adultstr) == true || regex.test(childstr) == true )
                        {
                        successAjax = false;
                        domClass.remove(errorDisplayBlock,"disNone");
                        domAttr.set(errorDisplayBlock, "innerHTML", roomId +" "+"Invalid entries");

                        break;
                        }


                    if(roomModel.rooms[i].childAges.length != 0)
                    {
                    for (var j=0; j<roomModel.rooms[i].childAges.length; j++)
                        {
                        var childAgeStr = roomModel.rooms[i].childAges[j];
                        if(regex.test(childAgeStr) == true)
                            {
                            successAjax = false;
                            domClass.remove(errorDisplayBlock,"disNone");
                            domAttr.set(errorDisplayBlock, "innerHTML", roomId +" "+"Invalid child entries");
                            break;
                            }

                        if(childAgeRef.length != 0)
                        {
                        for (var l = 0; l<childAgeRef.length;l++)
                            {
                            if(childAgeRef[l] != roomModel.rooms[i].childAges[j])
                                {
                                childAgeMismatchFlag = false;

                                }else
                                    {
                                    childAgeMismatchFlag = true;
                                    childAgeRef.splice([l],1);
                                    break;
                                    }
                            }
                        if(childAgeMismatchFlag == false)
                            {
                            successAjax = false;
                            domClass.remove(errorDisplayBlock,"disNone");
                            domAttr.set(errorDisplayBlock, "innerHTML", roomId+" "+"Invalid child entry");

                            }

                        }else
                        {
                            if (roomModel.rooms[i].childAges.length != 0)
                                {
                                successAjax = false;
                                domClass.remove(errorDisplayBlock,"disNone");
                                domAttr.set(errorDisplayBlock, "innerHTML", roomId+" "+"Invalid child entry");

                            break;
                                }
                        }
                        }

                    }
                    // party composition validation

                    widget.TotalAdultsCount = widget.TotalAdultsCount + number.parse(roomModel.rooms[i].noOfAdults);
                    widget.TotalChildsCount = widget.TotalChildsCount + number.parse(roomModel.rooms[i].noOfChildren);

                    }

              //party composition validation

                if (successAjax == true)
                {
                    if (widget.refTotalAdultsCount == widget.TotalAdultsCount && widget.refTotalChildsCount == widget.TotalChildsCount)
                    {



                    var ajaxObj = {  roomCode: "",sellingCode: "", roomIndex: 0,rooms:[]}

                    ajaxObj.sellingCode = roomModel.roomSellingCode;

                    for(var i = 0; i<roomModel.rooms.length; i++)
                        {
                        var roomId = i+1;
                        var string = ''+roomId;
                        var item = {id:string,noOfAdults: number.parse(roomModel.rooms[i].noOfAdults), noOfChildren: number.parse(roomModel.rooms[i].noOfChildren), childAges: [ ],childIds:[]}
                        for(var m = 0 ; m<roomModel.rooms[i].childAges.length;m++)
                            {
                            item.childAges.push(number.parse(roomModel.rooms[i].childAges[m]));
                            var childIds = widget.childIdentifiers(number.parse(roomModel.rooms[i].childAges[m]));
                            item.childIds.push(childIds);
                            }
                        ajaxObj.rooms.push(item);

                        }

                     domClass.add(dom.byId("mask"),'mask-interactivity')
                     domClass.add(dom.byId("changeRoom-Allocation"),'updating');

                  domClass.add (errorBlock,"disNone");
                  domClass.add(errorDisplayBlock,"disNone");
                    widget.generateRequest(ajaxObj);

                        }                  else{

                        successAjax = false;
                        domClass.remove(errorDisplayBlock,"disNone");
                        domAttr.set(errorDisplayBlock, "innerHTML", "The number of people you have selected does not match your original party size");

                    }

                }

            }))
    },

        childIdentifiers: function(childAges){
            var widget = this;


            var childIds= null;

        var uniFlag = false;


                for (var j = 0 ; j< widget.jsonData. packageViewData.passenger.length ; j++){
                    if(widget.jsonData. packageViewData.passenger[j].type == "CHILD" || widget.jsonData. packageViewData.passenger[j].type == "INFANT"){
                        if(childAges == widget.tempjsonData.packageViewData.passenger[j].age){
                            for(var l=0;l<widget.childRefId.length;l++){
                                if(widget.childRefId[l] == widget.tempjsonData.packageViewData.passenger[j].identifier)
                                    {
                                    childIds = widget.jsonData.packageViewData.passenger[j].identifier;
                                    widget.childRefId.splice([l],1)
                                    uniFlag = true;
                                    }else{
                                        continue;
                                    }
                            }
                            if(uniFlag){
                                break;
                            }

                        }else{
                            continue;
                        }
                    }

                }


            return childIds;

        },

        refresh: function(field,response)
        {
            var widget = this;

            domClass.remove(dom.byId("mask"),'mask-interactivity')
            domClass.remove(dom.byId("changeRoom-Allocation"),'updating');
            var RoomData = widget.triggerFunction(widget.dataPathRef, response);
            if(RoomData != null){
            	 widget.dataRefContainer(RoomData);
            }
            if (field == "changeRoom")
                {
                widget.jsonData = response;
                if(response== null){
                this.errorMsgDisplay();
                }
                widget.accomData = dijit.registry.byId("controllerWidget").refData(widget.jsonData.packageViewData.accomViewData, widget.accomIndexVal);
          	 	 widget.roomViewOptionViews = widget.triggerFunction(widget.refDataPath,widget.accomData);


                topic.publish("some/topic/closeOverlay");

                }

        },
        errorMsgDisplay: function()
        {
            var widget = this;
            widgetDom = widget.domNode;
            if(widget.childRefId.length != 0){

            }else{
                for (var i = 0 ; i< widget.tempjsonData. packageViewData.passenger.length ; i++){

                    if(widget.tempjsonData. packageViewData.passenger[i].type == "CHILD" || widget.tempjsonData. packageViewData.passenger[i].type == "INFANT"){

                        widget.childRefId.push(widget.tempjsonData. packageViewData.passenger[i].identifier);

                    }

                }
            }
            domClass.remove(dom.byId("mask"),'mask-interactivity')
            domClass.remove(dom.byId("changeRoom-Allocation"),'updating');
            var errorBlock = query(".errorMsg",widgetDom)[0];

            domAttr.set(errorBlock, "innerHTML", widget.errorDisplayContent);

             domClass.remove(errorBlock,"disNone");
        }

    });
    return tui.widget.booking.changeroomallocation.view.ChangeRoomAllocation;
});