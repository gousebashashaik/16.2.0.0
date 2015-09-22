define('tui/widget/CookieNotifier', [
    'dojo',
    'dojo/on',
    'dojo/cookie',
    'tui/widget/_TuiBaseWidget'
], function (dojo, on, cookie) {

    dojo.declare('tui.widget.CookieNotifier', [tui.widget._TuiBaseWidget], {

        timeout: 10,

        postCreate: function () {
            var cookieNotifier = this;

            var COOKIE_NAME = 'consent';

            if (cookie(COOKIE_NAME)) {
                return;
            }

            cookieNotifier.inherited(arguments);
            cookieNotifier.showWidget();
            cookieNotifier.addTimer(cookieNotifier.timeout);

            dojo.connect(dojo.query('.button', cookieNotifier.domNode)[0], 'onclick', function () {
                cookie(COOKIE_NAME, '1', {
                    expires: 1095,
                    path: '/'
                });
                cookieNotifier.hideWidget();
                cookieNotifier.setStyle('removeClass');
            });

            on.once(dojo.body(), '*:click', function () {
                cookieNotifier.hideWidget();
                cookieNotifier.setStyle('removeClass');
            });

            if (cookieNotifier.isShowing(cookieNotifier.domNode)) {
                cookieNotifier.setStyle('addClass');
            }
        },

        setStyle: function (action) {
            dojo[action]('footer', 'tall');
        },

        addTimer: function (timeout) {
            var cookieNotifier = this;

            var timing = timeout * 1000;
            setTimeout(function () {
              cookieNotifier.hideWidget();
              cookieNotifier.setStyle('removeClass');
            }, timing);
        }
    });

    return tui.widget.CookieNotifier;
});
