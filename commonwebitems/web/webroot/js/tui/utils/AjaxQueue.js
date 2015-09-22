// # AjaxQueue
// ## Utility module
//
// Provides an AJAX queue mechanism allows queuing of ajax requests to be sent synchronously
// which permits us to limit the number of requests and cancel the queue if needed
//
// @author Maurice Morgan, adapted for Dojo by Laurent Mignot
define("tui/utils/AjaxQueue", [
  "dojo/_base/lang",
  "dojo/_base/xhr"], function (lang, xhr) {

  /**
   * Internal object stores requestGroups and current ajaxRequest being processed
   * @type {{requestGroup: {}, ajaxRequest: null}}
   */
  var queue = {
    requestGroup: {},
    ajaxRequest: null
  };

  return {

    /**
     * Queues and executes request
     * @param {String?} queueGroupName Name for this queue group
     * @param {dojo._base.xhr} options xhr request options
     * @param {String} reqMethod Request method (get|post)
     */
    send: function (queueGroupName, options, reqMethod) {
      if (typeof queue.requestGroup[queueGroupName] === "undefined") {
        queue.requestGroup[queueGroupName] = [];
      }
      if (!options) return;

      // Store reference to callback function
      var originalCompleteCallback = options.load;
      options = lang.clone(options);

      // add options to closure, will be executed when response returned from server
      (function (p_originalCompleteCallback, p_queueGroupName, p_reqMethod, p_options) {
        // when response is returned
        options.load = function (request) {
          // remove the request from the queue
          queue.requestGroup[p_queueGroupName].shift();
          queue.ajaxRequest = null;

          // if callback fn specified, execute the callback with given arguments
          if (p_originalCompleteCallback) {
            p_originalCompleteCallback(request, p_options);
          }
          // and set the next request if any in the queue to be fired
          if (queue.requestGroup[p_queueGroupName].length > 0) {
            queue.ajaxRequest = xhr[p_reqMethod](queue.requestGroup[p_queueGroupName][0]);
          }
        };
      })(originalCompleteCallback, queueGroupName, reqMethod, options);

      // push this ajax request object into the queue for future execution
      queue.requestGroup[queueGroupName].push(options);

      // fire the request
      if (queue.requestGroup[queueGroupName].length == 1) {
        queue.ajaxRequest = xhr[reqMethod](options);
      }
    },

    /**
     * ###stopActiveRequest()
     * Cancels active request
     */
    stopActiveRequest: function () {
      if (queue.ajaxRequest) {
        queue.ajaxRequest.cancel();
      }
    },

    /**
     * ###clearQueue
     * Clears the ajax Queue
     * @param {String} queueGroupName Name of queue to clear
     */
    clearQueue: function (queueGroupName) {
      (typeof queueGroupName === "undefined") ? queue.requestGroup = {} : queue.requestGroup[queueGroupName] = [];
    },

    /**
     * ###getQueue
     * Retrieves an Ajax queue by name
     * @param {String} queueGroupName Name of queue to return
     * @returns group from ajax queue
     */
    getQueue: function (queueGroupName) {
      return queue.requestGroup[queueGroupName];
    }

  };

});