define("tui/searchResults/view/CruiseSticky", [
  "dojo/dom-geometry",
  "tui/widget/common/Sticky"], function (domGeom) {

	return dojo.declare("tui.searchResults.view.CruiseSticky", [tui.widget.common.Sticky], {


		 postCreate: function () {
	      var widget = this;
	      widget.inherited(arguments);
	      dojo.subscribe("tui.searchResults.view.CruiseCalendarSearchResultsComponent.openMonth", function(){

		       _.when(widget.isEnabled(), function () {
		    	   widget.shouldStick(domGeom.docScroll().y, widget.stickyRange()) ? widget.stick(domGeom.docScroll().y, widget.stickyRange()) : widget.unstick();
		      });
	      });
	     },

	     isEnabled: function () {
	         return !this.disabled;
	     },

	     shouldStick: function (scrollPosition, stickyRange) {
	       	return scrollPosition > stickyRange[0] && scrollPosition < stickyRange[1];
	     },

	     relativeNodeGeom: function(){
	    	 return domGeom.position(dojo.query(".result-view .product-list")[0]);
	     },

	     stickyRange: function () {
	    	 var relativeNodeGeom = this.relativeNodeGeom();
	    	 var stickyGeom = domGeom.position(this.domNode);
	         return [300, relativeNodeGeom.h - stickyGeom.h];
	     },
	     stick: function (scrollLength, stickyRange) {
	    	 var widget = this;
	    	 var filterNode = dojo.query(".filter-section", widget.domNode)[0];
	    	 dojo.setStyle ( filterNode, {marginTop: _.pixels(scrollLength - _.first(stickyRange)) == "0px" ? "15px" : "-15px"});
	    	 widget.inherited(arguments);
	       },

	     unstick: function(){
	    	 var widget = this;
	    	 var filterNode = dojo.query(".filter-section", this.domNode)[0];
	    	 var range = widget.stickyRange();
	    	 var reverseRange = range.reverse()
	    	 var relativeNodeGeom = this.relativeNodeGeom();
	    	 var stickyGeom = domGeom.position(filterNode);
	    	 var median = (relativeNodeGeom.h - stickyGeom.h) / 2 * -1;
	    	 var scrollTop = (document.documentElement && document.documentElement.scrollTop) || document.body.scrollTop;
	    	 var win_h = (window.innerHeight) ? window.innerHeight : document.body.clientHeight;    // gets window height
	    	  // gets current vertical scrollbar position
	    	 var scrl_pos = window.pageYOffset ? window.pageYOffset : document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
	    	 if( widget.getDocHeight() == widget.getScrollXY()[1] + window.innerHeight ){
	    		 //Scrolled down using CTRL + END key press
	    		 widget.stick((_.first(reverseRange)*2) - stickyGeom.h + 12 , reverseRange)
	    	 }else  if( scrollTop === 0 ){
	    		 		//Scrolled down using CTRL + HOME key press
	    		 		 widget.stick(_.first(range), range);
		    	 	}else{
		    		 filterNode.offsetTop * -1 < median ? widget.stick((_.first(reverseRange)*2) - stickyGeom.h + 12 , reverseRange) :  widget.stick(_.first(range), range);
		    	 	}
	     },

	     getScrollXY: function() {
	    	    var scrOfX = 0, scrOfY = 0;
	    	    if( typeof( window.pageYOffset ) == 'number' ) {
	    	        //Netscape compliant
	    	        scrOfY = window.pageYOffset;
	    	        scrOfX = window.pageXOffset;
	    	    } else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
	    	        //DOM compliant
	    	        scrOfY = document.body.scrollTop;
	    	        scrOfX = document.body.scrollLeft;
	    	    } else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
	    	        //IE6 standards compliant mode
	    	        scrOfY = document.documentElement.scrollTop;
	    	        scrOfX = document.documentElement.scrollLeft;
	    	    }
	    	    return [ scrOfX, scrOfY ];
	    	},

	    getDocHeight: function() {
	    	    var D = document;
	    	    return Math.max(
	    	        D.body.scrollHeight, D.documentElement.scrollHeight,
	    	        D.body.offsetHeight, D.documentElement.offsetHeight,
	    	        D.body.clientHeight, D.documentElement.clientHeight
	    	    );
	    	}
  });
});

