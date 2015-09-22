define('tui/widget/mobile/TabHeader', [
  'dojo',
  'dojo/dom-class',
  'dojo/query',
  'dojo/_base/event',
  'dojo/dom-attr',
  'tui/widget/Taggable',
  'tui/widget/mobile/Widget'], function(dojo, domClass, query, event, domAttr) {


  dojo.declare('tui.widget.mobile.TabHeader', [tui.widget.mobile.Widget , tui.widget.Taggable], {

	  onMobile: function () {
          var widget = this;
          domClass.remove(widget.domNode, 'open');
       },
      
    onDesktop: function() {
      var widget = this;
      domClass.add(widget.domNode, 'open');

    },

    onMiniTablet: function() {
      var widget = this;
      domClass.add(widget.domNode, 'open');

    },

    addListeners: function() {
      var widget = this;
      query('.active', widget.domNode).forEach(function(element) {
    	 
        dojo.connect(element, 'onclick', function(e) {
          e.preventDefault();
          domClass.toggle(widget.domNode, 'open');
        });
      });
      var i = 1;
      query("#navTabs li").forEach(function(node) {
    	  if(dojo.hasClass(node, "active")){
    	      /* it does */
    		  domAttr.set(node, "data-mob-position", "1");
    	   }
    	  else{
    		  i = i+1;
    		  domAttr.set(node, "data-mob-position", i);
    	  }
      });
    },

    postCreate: function() {
      var widget = this;
      widget.inherited(arguments);
      
      //Adding analytics
      widget.attachTag();
      widget.tagElements(dojo.query('#navTabs li a', widget.domNode), function(DOMElement) {
          var returnText = DOMElement.textContent || DOMElement.innerText;
          if(returnText == "Essential Info"){
        	  returnText = "Essential Information";
          } 
          return returnText;
        });
      widget.addListeners();
    }

  });
});


