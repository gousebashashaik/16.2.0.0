define('tui/widget/customeraccount/showmore/ShowMore', [
  'dojo',
  'dojo/dom-style',
  'dojo/text!tui/widget/customeraccount/showmore/templates/showmore.html',
  'dojo/dom-class',
  'dojo/Stateful',
  'dojo/_base/declare',
  'dojox/dtl',
  'dojox/dtl/Context',
  'tui/widget/mobile/Widget'
], function(dojo, domStyle, tmpl, domClass, Stateful, Declare) {

  var Displaying = Declare([Stateful], {
    value: 'null',
    _valueGetter: function() {
      return this.value;
    },
    _valueSetter: function(v) {
      this.value = v;
    }
  });

  dojo.declare('tui.widget.customeraccount.showmore.ShowMore', [tui.widget.mobile.Widget], {

    showing: null,
    divId:null,
    hide: function() {
      var widget = this;
      //this.showing.set('value', 'more');
      domStyle.set(widget.domNode, 'display', 'none');
    },

    show: function() {
      var widget = this;
      this.showing.set('value', 'less');
      domStyle.set(widget.domNode, 'display', 'block');
    },

    onMobile: function() {
      this.show();
    },

    onDesktop: function() {
      this.hide();
    },

    onMiniTablet: function() {
      this.hide();
    },

    onTablet:function () {
      this.hide();
    },

    toggle: function() {
      var widget = this;
	  
	  if(widget.showing.get('value') == null || widget.showing.get('value') == 'null' || widget.showing.get('value') == ""){
		widget.showing.set('value','less');
	  }
      if (widget.showing.get('value') === 'less') {
        domClass.add(widget.domNode, 'open');
        widget.showing.set('value', 'more');
      } else {	
		if(dojo.byId(widget.divId) != undefined){
			var x = dojo.byId(widget.divId).offsetLeft;
			var y = dojo.byId(widget.divId).offsetTop;		
			console.log("x:"+x+" y:"+y);
			window.scrollTo(x, y);
		}
		domClass.remove(widget.domNode, 'open');
        widget.showing.set('value', 'less');
      }
    },

    postCreate: function() {
      var widget = this;
      this.showing = new Displaying({value: null});

      this.showing.watch('value', function(name, oldValue, newValue) {
        if (newValue === 'less') {
          widget.getParent().contract();
        }

        if (newValue === 'more') {
          widget.getParent().expand();
        }
      });
      widget.inherited(arguments);

      var template = new dojox.dtl.Template(tmpl);
      widget.domNode.innerHTML = template.render(dojox.dtl.Context(widget));

      dojo.connect(widget.domNode, 'onclick', function(e) {
        dojo.stopEvent(e);
        widget.toggle();
      });
	  
	  window.document.addEventListener('orientationchange', function () {
        if (widget.showing.get('value') === 'more') {
          widget.getParent().expand();
        }
      }, false);
	  
    }
  });
});
