define('tui/widget/IntroExpandable', [
                                     'dojo',
                                     'tui/widget/expand/LinkExpandable',
                                     'dojo/html',
                                     'dojo/NodeList-manipulate'
                                   ],function(dojo){
	dojo.declare('tui.widget.IntroExpandable', [tui.widget.expand.LinkExpandable],{
		
		transitionType: 'WipeInHeight',

	    defaultTxt: null,

	    openTxt: '',

	    itemSelector: '',

	    targetSelector: '.link-hide-show',

	    itemContentSelector: '.copy',

	    copyShowHide: null,
		
		postCreate:function(){
			var introExpandable =this;
			var targetLink = dojo.query(introExpandable.targetSelector, introExpandable.domNode);
			var contentParagraph = dojo.query(introExpandable.itemContentSelector, introExpandable.domNode);
			introExpandable.copyShowHide = dojo.query('.copy-show-hide', contentParagraph[0])[0];
			if(targetLink.length){
				var coords = dojo.coords(targetLink[0]);
		        var top = [coords.t + coords.h, 'px'].join('');
		        dojo.style(contentParagraph[0], 'height', top);
			}
	        dojo.mixin(introExpandable.transitionOptions, {
	            onClick: function(transition, itemContent, item, wipe, target) {
	              var coords = dojo.coords(target);
	              if (!introExpandable.start || introExpandable.start === coords.t + coords.h) {
	                introExpandable.start = introExpandable.start || coords.t + coords.h;
	                dojo.query(target).remove();
	                dojo.style(introExpandable.copyShowHide, 'display', 'inline');
	                dojo.query(target).insertAfter(introExpandable.copyShowHide);
	                coords = dojo.coords(target);
	                introExpandable.end = introExpandable.end || coords.t + coords.h;
	                arguments[3] = 'wipeIn';
	                Array.prototype.push.call(arguments, {end: introExpandable.end, start: introExpandable.start});
	              } else {
	                arguments[3] = 'wipeOut';
	                Array.prototype.push.call(arguments, {end: introExpandable.start, start: introExpandable.end});
	              }
	              this.inherited(arguments);
	            }
	          });
	          introExpandable.inherited(arguments);
	          introExpandable.connect(introExpandable.transition,
	            'onWidgetTransitionEnd', function(transition, itemContent, item, wipe, target) {
	              if (wipe === 'wipeOut') {
	                dojo.query('.dots', introExpandable.domNode).style('display', 'inline');
	                dojo.style(introExpandable.copyShowHide, 'display', 'none');
	                dojo.query(target).remove().insertBefore(introExpandable.copyShowHide);
	              }
	            });
			
		}
		
	});	
	return tui.widget.IntroExpandable;
});