define('tui/widget/booking/changeroomallocation/view/ChildAgesSelectOption', [
    'dojo',
    'dojo/query',
    'dojo/dom-class',
    'dojo/dom',
    'tui/widget/form/SelectOption',
    'tui/widget/booking/changeroomallocation/modal/RoomModel'
    
], function (dojo, query, domClass,	dom, selectoption, roomModel) {

    dojo.declare('tui.widget.booking.changeroomallocation.view.ChildAgesSelectOption', [tui.widget._TuiBaseWidget,tui.widget.form.SelectOption], {

    	
    	onChange: function (name, oldValue, newvalue)
        {
            var selectOption = this;
            var widgetDom = selectOption.domNode;
           
            
            dojo.html.set(selectOption.selectDropdownLabel, newvalue.key);
            var childsAgeId = newvalue.value;
            var roomId = dojo.attr(selectOption.selectNode, "name");
            var value = newvalue.key;
            
            //setting the value to RoomModel
            
            if (oldValue == null)
            {
            	console.log(" null condition");
                console.log(roomModel.rooms);
                 for(var i=0 ; i<roomModel.rooms.length;i++)
                 {

                     if(roomModel.rooms[i].id == roomId)
                     {
                     	roomModel.rooms[i].childAges.push(value);
                     }
                 }
               
            }
            
            if(oldValue != null)
            {
            	console.log("not null condition");
                console.log(roomModel.rooms);
                for(var i=0 ; i<roomModel.rooms.length;i++)
                {

                    if(roomModel.rooms[i].id == roomId)
                    {
                    	roomModel.rooms[i].childAges[childsAgeId] = value;
                    	
                    }
                }
            }
            
           
           
            this.inherited(arguments);
        }

		

	});

       
    return tui.widget.booking.changeroomallocation.view.ChildAgesSelectOption;
});