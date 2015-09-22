define('tui/widget/customeraccount/TopEntities', [
  'dojo',
  'dojo/dom-geometry',
  'tui/widget/customeraccount/mixins/Expandable',
  'tui/widget/Taggable',
  'tui/widget/mobile/Widget'
], function(dojo, domGeometry) {

  function calculateHeight(refNode, elements) {
    var height = 0;
    elements.forEach(function(element) {
      var domElement = dojo.query(element, refNode)[0];
      if (!domElement) {
        return;
      }
      height = height + domGeometry.getMarginBox(domElement).h;
	  
    });
    return height;
  }

  dojo.declare('tui.widget.customeraccount.TopEntities', [tui.widget.mobile.Widget, tui.widget.customeraccount.mixins.Expandable, tui.widget.Taggable], {

  continuouslyAdapt: true,
   columns:0,
    expand: function() { 
      var widget = this;
      var newHeight = calculateHeight(widget.domNode, ['> ul > li']);
      var lastElement = _.last(dojo.query('> ul > li', widget.domNode));

      if (newHeight <= 0) {
        return;
      }

      //newHeight += domGeometry.getMarginBox(lastElement).t;
	  
	  var liHeight = dojo.query(".radiolist li", widget.domNode);
	  var newHeight = widget.computeULHeight(liHeight,widget.columns);
	  
	  //fix for DE15743
	  var ulElement = _.last(dojo.query('> ul', widget.domNode));
	  dojo.setStyle(widget.domNode, 'height','');
	  dojo.setStyle(ulElement, 'margin-bottom','20px');
      //dojo.setStyle(widget.domNode, 'height', newHeight + 'px');
    },
    computeULHeight: function(listElement,columns) {
	  var heightValue = 0;
	  for(var i = 0;i < listElement.length; i++) {
		heightValue = ( heightValue + listElement[i].offsetHeight );
	  }
	  if(columns == 1){
	  var perCentage = (29/100*heightValue)+25 ;
	  }
	  else{
	  var perCentage = (29/100*heightValue)+55;
	  }
	 
	  var total = Math.round((heightValue / columns )+perCentage);	
	  
	  return total;
	},
    contract: function() {
      var widget = this;
      var newHeight = calculateHeight(widget.domNode, ['> ul > li', '> a'])+ 100 ;

      if (newHeight <= 0) {
        return;
      }  
	
      dojo.setStyle(widget.domNode, 'height', newHeight + 'px');
    },

    onMobile: function() {
      var widget = this;
      widget.contract();
      dojo.addClass(widget.domNode, 'animated');
    },

    onMiniTablet: function (){
      var widget = this;
      //widget.expand();
      dojo.removeAttr(widget.domNode, 'style');
      dojo.removeClass(widget.domNode, 'animated');
    },

    onTablet: function (){
      this.onMiniTablet();
    },

    onDesktop: function (){
      var widget = this;
      //widget.expand();
      dojo.removeAttr(widget.domNode, 'style');
      dojo.removeClass(widget.domNode, 'animated');
    },

    postCreate: function() {
      var widget = this;
      widget.inherited(arguments);
      widget.attachTag();
	  //default contrasted
      if(navigator.userAgent.indexOf("Mobile") == -1){	 
		  dojo.query(".show-text").attr("style","display:none;");
	  }
	  else{
		  widget.contract();
	  }
      dojo.addClass(widget.domNode, 'animated');
	  
      _.each(dojo.query('.product', widget.domNode), function (contentDom) {  
          widget.tagElements(dojo.query('.ensLinkTrack', contentDom), function(DOMElement) {   
        	  var textDom = dojo.query('.show-hover', contentDom)[0];
             return (textDom.textContent || textDom.innerText);          
           });
         });
         widget.tagElements(dojo.query('.top-resorts .show-text'), "moreResorts");
         widget.tagElements(dojo.query('.top-hotels .show-text'), "moreHotels");
    }
  });
});
