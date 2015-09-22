define(["dojo/_base/lang"], function(lang){

	var tui = dojo.getObject("tui", true);

	dojo.setObject('widgetFx.TransitionFactory', {
		getTransition: function(transitionType, options){
			return new tui.widgetFx[transitionType](options);
		}
	}, tui);
})
