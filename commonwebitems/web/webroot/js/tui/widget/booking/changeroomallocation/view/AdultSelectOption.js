define('tui/widget/booking/changeroomallocation/view/AdultSelectOption', [
    'dojo',
    'dojo/query',
    'dojo/dom-class',
    'dojo/dom',
    'tui/widget/form/SelectOption',
    'tui/widget/booking/changeroomallocation/modal/RoomModel'
    
], function (dojo, query, domClass,dom,selectoption,roomModel) {

    dojo.declare('tui.widget.booking.changeroomallocation.view.AdultSelectOption', [tui.widget._TuiBaseWidget,tui.widget.form.SelectOption], {

    	
    	onChange: function (name, oldValue, newvalue)
        {
            var selectOption = this;
            var widgetDom = selectOption.domNode;
            dojo.html.set(selectOption.selectDropdownLabel, newvalue.key);
            var roomId = dojo.attr(selectOption.selectNode, "name");
            var value = newvalue.value;
            
            //To update RoomModel for adults count.
            
            if (oldValue == null)
            {
                 for(var i=0 ; i<roomModel.rooms.length;i++)
                 {

                     if(roomModel.rooms[i].id == roomId)
                     {
                     	roomModel.rooms[i].noOfAdults = value;
                     	
                     }
                 }
               
            }
            
            if(oldValue != null)
            {
                for(var i=0 ; i<roomModel.rooms.length;i++)
                {

                    if(roomModel.rooms[i].id == roomId)
                    {
                    	roomModel.rooms[i].noOfAdults = newvalue.value;
                    	
                    }
                }
            }
             
            this.inherited(arguments);
        }

		

	});

       
    return tui.widget.booking.changeroomallocation.view.AdultSelectOption;
});