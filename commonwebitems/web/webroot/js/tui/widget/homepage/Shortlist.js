define('tui/widget/homepage/Shortlist', [
  'dojo',
  'dojo/cookie',
  'dojo/text!tui/widget/homepage/templates/shortlist.html',
  'tui/widget/_TuiBaseWidget'
], function(dojo, cookie, tmpl) {

  dojo.declare('tui.widget.homepage.Shortlist', [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

    // ----------------------------------------------------------------------------- properties

    error: false,

    data: [],

    holidayNumber: 0,

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var shortlist = this;

      shortlist.inherited(arguments);

      shortlist.getShortlist();
    },

    getShortlist: function() {
      var shortlist = this;
      var shortlistCookie = cookie('shortlist-firstchoice');

      if (!shortlistCookie) {
        shortlist.setDefaultContent();
        return;
      }

      // We set the loading class when the Ajax is called.
      dojo.addClass(shortlist.domNode, 'loading');

      var xhrArgs = {
        url: tui.widget.homepage.Shortlist.END_POINT,
        content: {
          'shortlistCookieID': shortlistCookie
        },
        handleAs: 'json',
        timeout: 0
      };

      var deferred = dojo.xhrGet(xhrArgs);

      deferred.then(
        // Success
        function(data) {
          if (!data) {
            shortlist.displayErrorMessage("An unexpected error occurred");
            return;
          }

          dojo.removeClass(shortlist.domNode, 'loading');

          shortlist.data = shortlist.processRawData(data);

          var html = shortlist.renderTmpl(tmpl);
          dojo.place(html, shortlist.domNode, 'only');

          console.log(data);
          console.log(shortlist.data);
        },
        // Error
        function(error) {
          console.log("A shortlist error occurred", error);
          shortlist.displayErrorMessage();
        }
      );
    },

    displayErrorMessage: function() {
      var shortlist = this;

      shortlist.error = true;

      var html = shortlist.renderTmpl(tmpl);
      dojo.place(html, shortlist.domNode, 'only');
      dojo.removeClass(shortlist.domNode, 'loading');
      dojo.addClass(shortlist.domNode, 'error');
    },

    processRawData: function(data) {
      var shortlist = this;

      shortlist.holidayNumber = data.length;
      data = data.slice(0, 4);
      return _.map(data, function(holiday) {
        var priceChange = holiday.pricing.toPricePerPersonExDefExtras.amount - holiday.pricing.fromPricePerPersonExDefExtras.amount;
        return {
          name: holiday.accommodation.accommodationName,
          imageURL: 'http://media.thomson.co.uk' + holiday.accommodation.accommodationImageURL,
          url: holiday.bookmarkURL,
          price: holiday.pricing.toPricePerPersonExDefExtras.amount,
          difference: priceChange,
          type: holiday.soldOut ? 'soldOut' :
            priceChange === 0 ? 'noChange' :
              priceChange > 0 ? 'increase' :
                'decrease'
        };
      });
    },

    setDefaultContent: function() {
      //mboxCreate('F_Shortlist_Content');
    }
  });

  // ----------------------------------------------------------------------------- constants

  tui.widget.homepage.Shortlist.END_POINT = '/fcsun/page/shortlist/phoenixviewshortlist.page';
  //tui.widget.homepage.Shortlist.END_POINT = '/holiday/js/tui/widget/homepage/response.js';

  return tui.widget.homepage.Shortlist;
});
