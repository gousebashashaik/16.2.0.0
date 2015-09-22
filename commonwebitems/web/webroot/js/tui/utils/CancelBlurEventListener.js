// # CancelBlurEventListener
// ## Utility module
//
// Utility module to handle cancelling blur events if needed
// for example a listener attached to the body that closes the given widget
// will respond to events on the given widget and should then be cancelled
//
// @author Maurice Morgan
define("tui/utils/CancelBlurEventListener", ["dojo"], function (dojo) {

	dojo.declare("tui.utils.CancelBlurEventListener", null, {

    /**
     * ###cancelBlurEvent
     * Array stores a single boolean which, if present, prevents the blur behaviour
     * @type {Array}
     */
		cancelBlurEvent: null,

    /**
     * ###cancelBlurTimer
     * @type {Function}
     * Internal reference to the timer
     */
    cancelBlurTimer: null,

    /**
     * ###defaultDuration
     * Default duration for the timer
     * @type {Number}
     */
    defaultDuration: 300,

    /**
     * ###constructor()
     * Initialises the utility
     * @param args
     */
		constructor: function (args) {
			var cancelBlurEventListener = this;
			cancelBlurEventListener.cancelBlurEvent = [];
		},

    /**
     * ###performAfterBlur
     * Fired by widgets in event listeners which trigger "blur" functionality
     * @param {Function} callback The callback function to execute if timer is not cancelled
     * @param {Number?} duration Optionally specify the duration for the timer
     */
		performAfterBlur: function (callback, duration) {
			var cancelBlurEventListener = this;
      // initialise and save a reference to the timer
			cancelBlurEventListener.cancelBlurTimer = setTimeout(function () {
				if (_.indexOf(cancelBlurEventListener.cancelBlurEvent, true) === -1) {
					callback();
				}
        // blur successful so reset the pointer
				cancelBlurEventListener.cancelBlurEvent = [];
			}, duration || 300);
		},

    /**
     * ###cancelBlur
     * Cancels the "blur" behaviour, and prevents `performAfterBlur`
     * from executing its callback
     */
		cancelBlur: function () {
			var cancelBlurEventListener = this;
			clearTimeout(cancelBlurEventListener.cancelBlurTimer);
			cancelBlurEventListener.cancelBlurEvent.push(true);
		}
	});

	return tui.utils.CancelBlurEventListener;
});