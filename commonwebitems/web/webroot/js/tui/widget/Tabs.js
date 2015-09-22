// #Tabs
// ##Tab Widget
//
// Tab widget class. Given a target selector will attach tabbable behaviour
//
// @example
// ```<div data-dojo-type='tui.widget.Tabs'>
//      <ul class='tabs'>
//        <li><a href='#tabOne'>Tab One</a></li>
//        <li><a href='#tabTwo'>Tab Two</a></li>
//        <li><a href='#tabThree'>Tab Three</a></li>
//      </ul>
//      <div id='tabOne'>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin tincidunt nisl et lacus tristique fringilla condimentum et magna. Duis gravida dolor sapien, non tincidunt augue interdum ac. Cras id turpis et orci commodo mattis. Etiam volutpat libero id magna fringilla dapibus at a dolor. In hac habitasse platea dictumst. Ut congue ligula sed egestas commodo. Proin vel tellus vel leo egestas pharetra malesuada viverra elit. Nam porttitor est sodales, ultrices nisi non, rhoncus justo. Maecenas placerat purus metus, at convallis ante elementum vel. In eu nisi eu mauris feugiat viverra. Aliquam pretium mauris turpis, a pulvinar magna tincidunt non. Maecenas eu magna in libero rhoncus venenatis blandit eget orci. Aliquam vel lacinia justo. Curabitur lorem eros, consequat lobortis sagittis ac, elementum ac sapien. Quisque vel velit non leo eleifend sagittis vel fermentum dui.</div>
//      <div id='tabTwo'>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin tincidunt nisl et lacus tristique fringilla condimentum et magna. Duis gravida dolor sapien, non tincidunt augue interdum ac. Cras id turpis et orci commodo mattis. Etiam volutpat libero id magna fringilla dapibus at a dolor. In hac habitasse platea dictumst. Ut congue ligula sed egestas commodo. Proin vel tellus vel leo egestas pharetra malesuada viverra elit. Nam porttitor est sodales, ultrices nisi non, rhoncus justo. Maecenas placerat purus metus, at convallis ante elementum vel. In eu nisi eu mauris feugiat viverra. Aliquam pretium mauris turpis, a pulvinar magna tincidunt non. Maecenas eu magna in libero rhoncus venenatis blandit eget orci. Aliquam vel lacinia justo. Curabitur lorem eros, consequat lobortis sagittis ac, elementum ac sapien. Quisque vel velit non leo eleifend sagittis vel fermentum dui.</div>
//      <div id='tabThree'>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin tincidunt nisl et lacus tristique fringilla condimentum et magna. Duis gravida dolor sapien, non tincidunt augue interdum ac. Cras id turpis et orci commodo mattis. Etiam volutpat libero id magna fringilla dapibus at a dolor. In hac habitasse platea dictumst. Ut congue ligula sed egestas commodo. Proin vel tellus vel leo egestas pharetra malesuada viverra elit. Nam porttitor est sodales, ultrices nisi non, rhoncus justo. Maecenas placerat purus metus, at convallis ante elementum vel. In eu nisi eu mauris feugiat viverra. Aliquam pretium mauris turpis, a pulvinar magna tincidunt non. Maecenas eu magna in libero rhoncus venenatis blandit eget orci. Aliquam vel lacinia justo. Curabitur lorem eros, consequat lobortis sagittis ac, elementum ac sapien. Quisque vel velit non leo eleifend sagittis vel fermentum dui.</div>
//  </div>```
//
// @extends `tui.widget._TuiBaseWidget`
// @borrows `tui.widget.mixins.Tabbable`
// @author: Maurice Morgan.

define ("tui/widget/Tabs", [
  "dojo/_base/declare",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/mixins/Tabbable"
], function(declare){
	
	return declare("tui.widget.Tabs", [tui.widget._TuiBaseWidget, tui.widget.mixins.Tabbable], {

    //---------------------------------------------------------------- properties

    /**
     * ###tabSelector
     * CSS selector for tabs
     * @type {String}
     */
		tabSelector: ".tabs li",
		
		//---------------------------------------------------------------- methods

    /**
     * ###postCreate()
     * Initialises the Tabs
     * @extends `tui.widget._TuiBaseWidget.postCreate()`
     * @borrows `tui.widget.mixins.Tabbable.attachTabbableEventListeners()`
     */
		postCreate: function(){
			var tabs = this;
			tabs.inherited(arguments);
			tabs.attachTabbableEventListeners();
		}
	});
});