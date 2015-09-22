define(["dojo","dojo/_base/lang"], function(dojo, lang) {
	
	var widget = lang.getObject("tui.widget", true);
	var ignore = [dojo.keys.TAB, dojo.keys.CLEAR, dojo.keys.ENTER, dojo.keys.SHIFT, dojo.keys.CTRL, dojo.keys.ALT, 
				  dojo.keys.META, dojo.keys.PAUSE, /*dojo.keys.CAPS_LOCK,*/ dojo.keys.ESCAPE, dojo.keys.SPACE, dojo.keys.PAGE_UP, dojo.keys.PAGE_DOWN,
				  dojo.keys.END, dojo.keys.HOME, dojo.keys.LEFT_ARROW, dojo.keys.UP_ARROW, dojo.keys.RIGHT_ARROW, dojo.keys.DOWN_ARROW, dojo.keys.INSERT, 
				  dojo.keys.DELETE, dojo.keys.HELP, dojo.keys.LEFT_WINDOW, dojo.keys.RIGHT_WINDOW, dojo.keys.SELECT, /*dojo.keys.NUMPAD_0,
				  dojo.keys.NUMPAD_1, dojo.keys.NUMPAD_2, dojo.keys.NUMPAD_3, dojo.keys.NUMPAD_4, dojo.keys.NUMPAD_5, dojo.keys.NUMPAD_6, 
				  dojo.keys.NUMPAD_7, dojo.keys.NUMPAD_8, dojo.keys.NUMPAD_9,*/ dojo.keys.NUMPAD_MULTIPLY, dojo.keys.NUMPAD_PLUS,
				  dojo.keys.NUMPAD_ENTER, dojo.keys.NUMPAD_MINUS, dojo.keys.NUMPAD_PERIOD, dojo.keys.NUMPAD_DIVIDE, dojo.keys.F1, dojo.keys.F2, dojo.keys.F3, 
				  dojo.keys.F4, dojo.keys.F5, dojo.keys.F6, dojo.keys.F7,dojo.keys.F8,dojo.keys.F9,dojo.keys.F10,dojo.keys.F11,dojo.keys.F12,
				  dojo.keys.F13,dojo.keys.F14,dojo.keys.F15,dojo.keys.NUM_LOCK,dojo.keys.SCROLL_LOCK];

	widget.Growable = function(element){

        // regular expanding
		dojo.connect(element, "onkeydown", function(event){
            var size, defaultIncr = 2;
			if (event.keyCode === dojo.keys.BACKSPACE){
				size = parseInt(dojo.attr(element, "size"), 10);
				size = (size === 1) ?  1 : size - 1;
				dojo.attr(element, "size", size);
			} else if (_.indexOf(ignore, event.keyCode) === -1) {
                if(event.shiftKey) {
                    defaultIncr = 5;
                }
				dojo.attr(element, "size", element.value.length + defaultIncr);
			}
		});

        // handle caps lock
        dojo.connect(element, "onkeypress", function(event){
            if(isNaN(event.charOrCode) && event.charOrCode === event.charOrCode.toUpperCase()) {
                dojo.attr(element, "size", element.value.length + 5);
            }
        });

        // handle paste events
        dojo.connect(element, "onkeyup", function(event){
            if(event.ctrlKey && event.keyCode === 86) {
                dojo.attr(element, "size", element.value.length + 2);
            }
        });
	
		return {
			reset: function(){
				dojo.attr(element, "size", 1);
			}
		}
	
	};
		
	return tui.widget.Growable;
});