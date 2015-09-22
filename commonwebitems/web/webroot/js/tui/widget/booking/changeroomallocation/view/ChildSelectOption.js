define('tui/widget/booking/changeroomallocation/view/ChildSelectOption', [
    'dojo',
    'dojo/dom',
    'dojo/query',
    'dojo/topic',
    'dojo/dom-class',
    'dojo/_base/lang',
    'dojo/dom-construct',
    'tui/widget/booking/changeroomallocation/modal/RoomModel',
    'dojo/text!tui/widget/booking/changeroomallocation/view/Templates/ChildAgesSelectionTemplate.html',
    'tui/widget/mixins/Templatable'


], function (dojo, dom, query, topic, domClass,lang,domConstruct, roomModel,childAgesSelectionTemplate) {



    dojo.declare('tui.widget.booking.changeroomallocation.view.ChildSelectOption', [tui.widget._TuiBaseWidget,tui.widget.form.SelectOption,tui.widget.mixins.Templatable],
    		{

    	childAges: null,

    	templateView: null,

    	tmpl: childAgesSelectionTemplate,

    	tempjsonData: null,

    	onChange: function (name, oldValue, newvalue)
        {
            var selectOption = this;
            var widgetDom = selectOption.domNode;
            dojo.html.set(selectOption.selectDropdownLabel, newvalue.key);
            var roomId = dojo.attr(selectOption.selectNode, "name");
            var value = newvalue.value;
            if (oldValue == null)
            {
                	selectOption.childAges=[];
					var diff = newvalue.value;
					for (var i = 0; i < diff; i++)
					{
						selectOption.childAges.push(-1);
					}

               //creating clone of jsonData.
                selectOption.tempjsonData = dojo.clone(roomModel.jsonData);
                //To populate the child ages dropDown.
            	selectOption.tempjsonData.packageViewData.paxViewData.childAges.splice(0,0,"-");
                selectOption.jsonData = selectOption.tempjsonData;
                selectOption.templateView= "false";
                if(newvalue.value != "-" && newvalue.value != "0")
                {
                    selectOption.templateView= "true";
                }

                var html = selectOption.renderTmpl(selectOption.tmpl, selectOption);
                var domnode= dom.byId(roomId);
                var roomVal = roomId.slice(-1);

                var roomValRef = roomVal-1;


                var childAgesDomNode = dojo.query(".right_section" ,domnode)[0];
                domConstruct.place(html, childAgesDomNode, 'only');

                this.controller = dijit.registry.byId("controllerWidget");
                var accomIndex = roomModel.accomIndexVal;
                this.accomData = dijit.registry.byId("controllerWidget").refData(this.jsonData.packageViewData.accomViewData,accomIndex);
                this.accomViewData = this.triggerFunction("roomViewData",this.accomData);


                if( this.accomViewData.hasOwnProperty(roomValRef)){

                	var refchilAges = this.accomViewData[roomValRef].childAges;
                	 dojo.forEach(dojo.query(".child-age", childAgesDomNode), function (item, i) {

                     	for(var j=0 ; j<item.options.length;j++)
                     		{
                     		item.options[j].text;
                     		item.options[j].selected;

                     		if(refchilAges.hasOwnProperty(i)){

                     		if(item.options[j].text == refchilAges[i])
                     			{
                     			item.options[j].selected = true;
                     			item.options[j].text = refchilAges[i];
                     			}
                     		}else{

                     			if(item.options[j].text == "-")
                     			{
                     			item.options[j].selected = true;
                     			}

                     		}

                     		}

                     });

                }else{


                	}






                //Popuate the RoomModel.
                for(var i=0 ; i<roomModel.rooms.length;i++)
                {
                    if(roomModel.rooms[i].id == roomId)
                    {
                      roomModel.rooms[i].childAges;
                    }
                }
                topic.publish("some/topic");
                dojo.parser.parse(childAgesDomNode);

                 for(var i=0 ; i<roomModel.rooms.length;i++)
                 {
                     if(roomModel.rooms[i].id == roomId)
                     {
                     	roomModel.rooms[i].noOfChildren = value;
                     }
                 }

                 topic.publish("some/topic/childAgeTitleDisplay");
            }

            if(oldValue != null)
            {
    			 var domnode= dom.byId(roomId);
                 var childAgesDomNode = dojo.query(".right_section" ,domnode)[0];
                 var childNode = dojo.query(".children_age_content",childAgesDomNode)[0];

                 //Destroy the childNodes(Child ages template is destroyed).
                 dojo.destroy(childNode);

                 selectOption.childAges=[];
                 var diff = newvalue.value;
					for (var i = 0; i < diff; i++) {
						selectOption.childAges.push(-1);
					}
				selectOption.jsonData = selectOption.tempjsonData;
                selectOption.templateView= "true";
                if(newvalue.value == "-" || newvalue.value == "0")
                {
                    selectOption.templateView= "false";
                }
               var html = selectOption.renderTmpl(selectOption.tmpl, selectOption);
               var domnode= dom.byId(roomId);
               var roomVal = roomId.slice(5);
               var roomValRef = roomVal-1;
               domConstruct.place(html, childAgesDomNode, 'only');

               if(this.accomIndexValue == null){

            	   indexvalue = 0;

               }else{

            	   indexvalue = this.accomIndexValue;
               }

               this.accomData = dijit.registry.byId("controllerWidget").refData(this.jsonData.packageViewData.accomViewData,  indexvalue);
               this.accomViewData = this.triggerFunction("roomViewData",this.accomData);



               if( this.accomViewData.hasOwnProperty(roomValRef)){

               	var refchilAges = this.accomViewData[roomValRef].childAges;
               	 dojo.forEach(dojo.query(".child-age", childAgesDomNode), function (item, i) {

                    	for(var j=0 ; j<item.options.length;j++)
                    		{
                    		item.options[j].text;
                    		item.options[j].selected;

                    		if(refchilAges.hasOwnProperty(i)){

                    		if(item.options[j].text == refchilAges[i])
                    			{
                    			item.options[j].selected = true;
                    			item.options[j].text = refchilAges[i];
                    			}
                    		}else{

                    			if(item.options[j].text == "-")
                    			{
                    			item.options[j].selected = true;
                    			}

                    		}

                    		}

                    });

               }else{

               	}



               for(var i=0 ; i<roomModel.rooms.length;i++)
               {
                 if(roomModel.rooms[i].id == roomId)
                 {
                   roomModel.rooms[i].childAges = [];
                 }

               }
               topic.publish("some/topic");
               dojo.parser.parse(childAgesDomNode);
               for(var i=0 ; i<roomModel.rooms.length;i++)
                {

                    if(roomModel.rooms[i].id == roomId)
                    {
                    	roomModel.rooms[i].noOfChildren = newvalue.value;

                    }

                }
               topic.publish("some/topic/childAgeTitleDisplay");

            }

            this.inherited(arguments);
        },
        triggerFunction: function(dataPathRef , contextObj){
            return lang.getObject(dataPathRef, false ,contextObj ) ;
         }
	});


    return tui.widget.booking.changeroomallocation.view.ChildSelectOption;
});