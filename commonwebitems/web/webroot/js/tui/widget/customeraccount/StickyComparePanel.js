define('tui/widget/customeraccount/StickyComparePanel', [
  'dojo', 
  'dojo/dom-class',
  'dojo/dom-style',
  'dojo/dom-attr',
  'dojo/has',
  'dojo/_base/connect',
  'dojo/dom-construct',  
  'tui/widget/mobile/Widget',
  'tui/common/TuiUnderscore'], function (dojo, domClass, domStyle, domAttr, has) {


  dojo.declare('tui.widget.customeraccount.StickyComparePanel', [tui.widget.mobile.Widget], {

    disabled: false,


    reset: function () {
      domAttr.remove(this.scrollableElement(), 'style');
    },


    onMobile: function () {
      this.reset();
      this.disable();
    },

    onDesktop: function () {
      this.enable();
      this.reset();
    },

    onMiniTablet: function () {
      this.enable();
      this.reset();
    },

    onTablet: function () {
      this.enable();
      this.reset();
    },

    disable: function () {
      this.disabled = true;
    },

    enable: function () {
      this.disabled = false;
    },

    isDisabled: function () {
      return this.disabled;
    },

    isEnabled: function () {
      return _.not(this.isDisabled)();
    },

    scrollableElement: _.once(function () {
      return _.first(dojo.query('#compare-panel'));
    }),

    stick: function (scrollLength, stickyRange) {
      //domStyle.set(this.scrollableElement(), 'top', _.pixels(scrollLength - _.first(stickyRange)));
	 dojo.query('#compare-panel').attr("style","position:fixed;top:0;left:"+this.left+"px;");
	  
    },

    unstick: function () {     
	  //dojo.query("#compare-panel").attr("style","position:absolute;");
	  dojo.query("#compare-panel").attr("style","position:absolute;");
    },
    getDifferencePosition: function () {
            var initialHeight = 0;
            if (dojo.query("#account-bar")[0] != undefined) {
                initialHeight += dojo.query("#account-bar")[0].offsetHeight;
            }
            if (dojo.query("#header")[0] != undefined) {
                initialHeight += dojo.query("#header")[0].offsetHeight;
            }
            if (dojo.query("#nav")[0] != undefined) {
                initialHeight += dojo.query("#nav")[0].offsetHeight;
            }
            if (dojo.query("#wishCountSpan")[0] != undefined) {
                initialHeight += dojo.query("#wishCountSpan")[0].offsetHeight;
            }
			if (dojo.query(".heading-with-controls")[0] != undefined) {
                initialHeight += (dojo.query(".heading-with-controls")[0].offsetHeight) + (dojo.query(".heading-with-controls")[0].offsetHeight);
            }
            return initialHeight;
        },
    stickyRange: function () {
	  if(!this.botPos){
	  var pix = dojo.position(dojo.query(".remove_all_saved_holidays")[0]).h;
	  this.botPos = (dojo.coords(dojo.query(".shortlisted-hols")[0]).h - pix);		
	  this.bpos = parseInt(this.botPos,10);
	  }
	  if(!this.top){
	  this.top = this.getDifferencePosition();
	  }
      return [this.top, this.bpos];
    },

    shouldStick: function (scrollPosition, stickyRange) {
      return _.isBetween(scrollPosition, stickyRange);
    },

    postCreate: function () {
      var widget = this;
	  this.compStyle = dojo.query("#compare-panel").attr("style");
	  this.left = dojo.position(dojo.query("#compare-panel")[0]).x;
	  var eventname = "onscroll";
	  if(has("touch")){
		//eventname = "touchmove";
	  }
      dojo.connect(window, eventname, function () {
       
          widget.shouldStick(dojo._docScroll().y, widget.stickyRange()) ? widget.stick(dojo._docScroll().y, widget.stickyRange()) : widget.unstick();
       
      });
      widget.inherited(arguments);
    },
	CustomPostCreate: function () {
      var widget = this;
	  this.compStyle = dojo.query("#compare-panel").attr("style");
	  this.left = dojo.position(dojo.query("#compare-panel")[0]).x;
	  var eventname = "onscroll";
	  if(has("touch")){
		//eventname = "touchmove";
	  }
      dojo.connect(window, eventname, function () {
       
          widget.shouldStick(dojo._docScroll().y, widget.stickyRange()) ? widget.stick(dojo._docScroll().y, widget.stickyRange()) : widget.unstick();
       
      });
      widget.inherited(arguments);
    }

  });
  return tui.widget.customeraccount.StickyComparePanel;
});
