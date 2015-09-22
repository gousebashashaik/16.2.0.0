define("tui/shortlist/view/ShortlistSummary", [
  "dojo",
  "dojo/on",
  "dojo/query",
  "dojo/window",
  "dojo/dom-style",
  "dojo/dom-class",
  "dojo/_base/connect",
  "dojo/dom-construct",
  "tui/shortlist/store/ShortlistStore",
  "tui/widget/_TuiBaseWidget"], function (dojo, on, query, win, domStyle, domClass, connect, domConstruct) {

	function getIndex(node) {
	    var parent = node.parentElement || node.parentNode, i = -1, child;
	    while(parent && (child = parent.childNodes[++i])) if(child == node) return i;
	    return -1;
	  }

	function actualLocation(element) {
	    return {
	      'element': element,
	      'parent': element.parentElement || element.parentNode,
	      'index': getIndex(element)
	    };
	}

  dojo.declare("tui.shortlist.view.ShortlistSummary", [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- properties

    shortlistStore: null,

    shortlistCount: 0,

    linkNode: null,

    countNode: null,

    hideNode: null,

    winListeners: null,

    actualDomLocation: null,

    sticked: false,

    newShortlistUnit : false,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var shortlistSummary = this,
      	  controller = null;

      shortlistSummary.inherited(arguments);
      shortlistSummary.winListeners = [];

      // initialise observer
      dojo.when(shortlistSummary.shortlistStore.getObservable().requestData(false), function () {
        shortlistSummary.observeStore();
      });

      shortlistSummary.initSummary();

      shortlistSummary.actualDomLocation = actualLocation(shortlistSummary.domNode);

      if(shortlistSummary.hideNode) {
	      on(shortlistSummary.hideNode, "click", function(){
	  		domClass.add(shortlistSummary.domNode,"hide");
	  	  });
      }

      connect.subscribe("tui/filterPanel/filterSticked", function(sticked){
    	  shortlistSummary.reposition(sticked);
      });

    },



    reposition: function(sticked){
    	var shortlistSummary = this;
    	sticked ? shortlistSummary.stick() : shortlistSummary.unStick();
    	shortlistSummary.newShortlistUnit = true;
    },

    stick: function(){
         var shortlistSummary = this,
         	 filterStickyComponent = query("#sticky-filter"),
             shortListSection = null;
         if(shortlistSummary.shortlistCount && filterStickyComponent && filterStickyComponent.length){
        	 shortListSection = query(".shortlisted-h",shortListSection);
        	 if(shortListSection && shortListSection.length){
        		 domConstruct.place(shortlistSummary.domNode,shortListSection[0], "last");
        	 }
         }
         shortlistSummary.sticked = true;
    },

    unStick: function(){
    	var shortlistSummary = this;
    	domConstruct.place(shortlistSummary.domNode,shortlistSummary.actualDomLocation.parent, shortlistSummary.actualDomLocation.index);
    	shortlistSummary.sticked = false;
    	domClass.remove(shortlistSummary.domNode,"hide");
    },

    initSummary: function () {
      var shortlistSummary = this,
          action = null,
          winTop = 0,
          winBoxWidth = 0,
          posRight = 0,
          pageWidth = 1000,
          summaryBox = dojo.position(shortlistSummary.domNode),
          summaryBottom = shortlistSummary.domNode.offsetTop + summaryBox.h;

      shortlistSummary.linkNode = query("a", shortlistSummary.domNode)[0];
      shortlistSummary.countNode = query(".count", shortlistSummary.domNode)[0];
      shortlistSummary.textNode = query(".text", shortlistSummary.domNode)[0];
      shortlistSummary.hideNode = query(".hideShortList", shortlistSummary.domNode)[0];


      var loadScrollTop = (window.pageYOffset !== undefined) ? window.pageYOffset :
          (document.documentElement || document.body.parentNode || document.body).scrollTop;

      // add fixed class on page load if items are shortlisted and page was scrolled down previously (refresh)
      if (loadScrollTop >= summaryBottom && shortlistSummary.shortlistCount > 0) {
        dojo.addClass(shortlistSummary.domNode, "fixed");
      }

      // handle scrolling
      shortlistSummary.winListeners.push(on(window, "scroll", function () {
        winTop = (window.pageYOffset !== undefined) ? window.pageYOffset : (document.documentElement || document.body.parentNode || document.body).scrollTop;
        // add/remove fixed class if scrolled past
        action = (winTop >= summaryBottom && shortlistSummary.shortlistCount > 0) ? "add" : "remove";
        // calculate position
        winBoxWidth = win.getBox().w;
        posRight = (winTop >= summaryBottom && shortlistSummary.shortlistCount > 0) ? ((winBoxWidth - pageWidth) / 2) : 0;

        if (shortlistSummary.domNode) {
          domClass[action](shortlistSummary.domNode, "fixed");
          domStyle.set(shortlistSummary.domNode, 'right', _.pixels(posRight));
        }
      }));

      // handle window resize
      shortlistSummary.winListeners.push(on(window, "resize", function(){
        if(!domClass.contains(shortlistSummary.domNode, "fixed")) return;
        // calculate position
        winBoxWidth = win.getBox().w;
        posRight = (winTop >= summaryBottom && shortlistSummary.shortlistCount > 0) ? ((winBoxWidth - pageWidth) / 2) : 0;
        if (shortlistSummary.domNode) {
          domStyle.set(shortlistSummary.domNode, 'right', _.pixels(posRight));
        }
      }));

      // summary link handler
      on(shortlistSummary.linkNode, "click", function (event) {
        if (shortlistSummary.shortlistCount === 0) {
          dojo.stopEvent(event);
        }
      });

      query('.close-this', shortlistSummary.domNode).on('click', function(){
        domStyle.set(shortlistSummary.domNode, 'right', 0);
        domClass.remove(shortlistSummary.domNode, "fixed");
        shortlistSummary.removeListeners();
      });
    },

    removeListeners: function () {
      var shortlistSummary = this;
      if(shortlistSummary.winListeners.length) {
        _.each(shortlistSummary.winListeners, function(listener){
          listener.remove();
        });
      }
    },

    observeStore: function () {
      var shortlistSummary = this;
      var resultSet = shortlistSummary.shortlistStore.query();

      // observe shortlist count and update summary display
      resultSet.observe(function () {
        shortlistSummary.shortlistCount = shortlistSummary.shortlistStore.getCount();
        shortlistSummary.updateSummary();
      });
    },

    updateSummary: function () {
      var shortlistSummary = this;
      shortlistSummary.countNode.innerHTML = shortlistSummary.shortlistCount;
      shortlistSummary.textNode.innerHTML = (shortlistSummary.shortlistCount > 1) ? 'holidays shortlisted' : 'holiday shortlisted';
      var action = (shortlistSummary.shortlistCount > 0) ? "addClass" : "removeClass";
      dojo[action](shortlistSummary.linkNode, "packages-shortlisted");
      if(shortlistSummary.newShortlistUnit){
    	switch(shortlistSummary.shortlistCount)  {
    		case 0: if(shortlistSummary.sticked){
    			shortlistSummary.reposition(false);
    			shortlistSummary.sticked = true;
    		}
    			break;
    		case 1: if(shortlistSummary.sticked){
    			shortlistSummary.reposition(true);
    		}
    			break;
    	}
      }
    }

  });

  return tui.shortlist.view.ShortlistSummary;
});