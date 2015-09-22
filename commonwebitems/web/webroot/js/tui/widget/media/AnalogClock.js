define("tui/widget/media/AnalogClock", [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/text!tui/widget/media/templates/AnalogClockTmpl.html',
  'tui/widget/mixins/Templatable',  
  'dojo/query',
  "dojo/dom-construct",
  'tui/widget/_TuiBaseWidget',
  'dojo/NodeList-traverse' ], function (dojo, on, domAttr, analogClockTmpl, query, domConstruct) {

  dojo.declare("tui.widget.media.AnalogClock", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

    //---------------------------------------------- properties

    time:null,   

    tmpl: analogClockTmpl,    

    //--------------------------------------------------------------methods

    postCreate: function () {
      var widget = this;
      widget.inherited(arguments);
	  widget.renderClock();	  
    },
	renderClock: function(){
		var widget = this;
		var IE8 = false;
		var hours = 0;
		var minutes = 0;
		if(navigator.appVersion.indexOf("MSIE 8") != -1 ||
			navigator.appVersion.indexOf("MSIE 7") != -1 ||
			navigator.appVersion.indexOf("MSIE 6") != -1 ){
			IE8 = true;
			widget.time = 0;
		}
		if(widget.time == null || widget.time == undefined){
			IE8 = true;
			widget.time = 0;
		}
		else{
			var time = (widget.time).toString() ;
			hours = time.split(/[\:\s]/)[0]; 
			minutes = time.split(/[\:\s]/)[1]; 
		}		
		
		var minute_as_degree = minutes * 6;		
		hour_as_degree = hours  * 30 ;
		hour_as_degree = hour_as_degree + Math.round((minutes / 2));
		if(hour_as_degree == null || hour_as_degree == undefined){
			hour_as_degree = 0;
		}
		if(minute_as_degree == null || minute_as_degree == undefined){
			minute_as_degree = 0;
		}
		var html = widget.renderTmpl(widget.tmpl,{hour_as_degree:hour_as_degree, minute_as_degree: minute_as_degree, IE8: IE8, tuiCdnPath: tuiCdnPath});
		if (html) { 
			dojo.place(html, widget.domNode, "only");
			dojo.parser.parse(widget.domNode);
		}
		setTimeout(function(){ 
			if(dojo.query(".minutes")[0] != undefined){
				if(minutes >= 45){
					var leftPos = parseInt(dojo.query(".minutes").style('left')[0], 10);
					dojo.query(".minutes")[0].style.left = (leftPos - 2)+"px";
				}
				if(minutes > 15 && minutes < 30){
					var leftPos = parseInt(dojo.query(".minutes").style('left')[0], 10);
					dojo.query(".minutes")[0].style.left = (leftPos + 1)+"px";
				}
				if(hours >= 3 && hours < 6){
					var leftPos = parseInt(dojo.query(".hours").style('left')[0], 10);
					dojo.query(".hours")[0].style.left = (leftPos + 2)+"px";
				}
			}		
		}, 300);
	}

  });
  return tui.widget.media.AnalogClock;
});