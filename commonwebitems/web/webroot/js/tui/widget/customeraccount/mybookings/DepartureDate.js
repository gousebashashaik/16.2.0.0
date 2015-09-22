define('tui/widget/customeraccount/mybookings/DepartureDate', [
  'dojo',
  'dojo/on',
  'dojo/_base/connect',
  'dojo/dom-style',
  'dojo/dom-class',
  'tui/widget/Taggable',
  'tui/widget/mobile/Widget'
], function (dojo, on, connect, domStyle, domClass) {

  dojo.declare('tui.widget.customeraccount.mybookings.DepartureDate', [tui.widget._TuiBaseWidget , tui.widget.Taggable], {

    validate: function() {
      var validate = true;
            var widget = this;
            if (_.isEmpty(widget.criteria.when)) {
            	//var finder = dojo.getAttr('finder', 'placeholder');
    			 var finder = dojo.byId('finder').value;
    			/* Conditional statement added to find weather  get-price or holiday-finder loaded */
    			if(finder == "holidayfinder")
    				{
    					 error_header = dojo.byId('eHeader');
    					 error_from = dojo.byId('clearFrom');
    					 error_to = dojo.byId('clearTo');
    					 error_from ? error_from.innerHTML= '' : null;
    				}
    			else if(finder == "checkprice")
    				{				
    				    error_temp = dojo.query('#errorGetprice')[0];
    				    error_header = dojo.byId('eHeaderGetprice');
    				    error_to = dojo.byId('clearToGetprice');
    				    error_from = dojo.byId('clearFromGetprice');
    				    error_from ? error_from.innerHTML= '' : null;
    				}
            	
    			error_to  ? error_to.innerHTML= '' : null;        
        domClass.add(widget.domNode.parentNode, 'error');
        throw 'You need to choose a date. Try selecting from the calendar provided.';
      }
    },

    reset: function () {
      var widget = this;
      widget.domNode.value = '';
    widget.criteria.set('when', null);
    },

    removeError: function () {
      var widget = this;
      domClass.remove(widget.domNode.parentNode, 'error');
    },


    postCreate: function() {
      var widget = this;
      //widget.getParent().registerField(widget);
            widget.getParent().registerDepartureDate(widget);
            widget.criteria = widget.getParent().getCriteria();
            widget.criteria.watch('when', function (name, oldValue, newValue) {
        newValue ? widget.domNode.value = newValue.toString(): '';
      });

       // adding analytics
          widget.attachTag();
          widget.tagElement(widget.domNode ,"when");
          
      widget.inherited(arguments);
    }
  });
});
