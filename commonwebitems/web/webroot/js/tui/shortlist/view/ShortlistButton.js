define("tui/shortlist/view/ShortlistButton", [
    "dojo",
    "dojo/on",
    "dojo/mouse",
    "dojo/query",
    "tui/shortlist/store/ShortlistStore",
    "tui/widget/_TuiBaseWidget"], function (dojo, on, mouse, query) {

    dojo.declare("tui.shortlist.view.ShortlistButton", [tui.widget._TuiBaseWidget], {

        // ----------------------------------------------------------------------------- properties

        shortlistStore: null,

        shortlistDisabled: false,

        packageId: null,

        // ----------------------------------------------------------------------------- methods

        postCreate: function () {
            var shortlistButton = this;
            shortlistButton.inherited(arguments);
            shortlistButton.shortlistStore = shortlistButton.shortlistStore.getObservable();

            // initialise observer
            dojo.when(shortlistButton.shortlistStore.requestData(false), function(){
                shortlistButton.observeStore();
                //console.log(shortlistButton.isShortlisted(shortlistButton.packageId))
            });

            // if shortlist is full, disable button
            dojo.subscribe("tui:channel=shortlistStoreDisabled", function (isDisabled) {
                shortlistButton.shortlistDisabled = isDisabled;
                var action = isDisabled ? "addClass" : "removeClass";
                if(!shortlistButton.isShortlisted(shortlistButton.packageId)){
                    dojo[action](shortlistButton.domNode, "disabled");
                }
            });

            shortlistButton.initButtonEventListeners();
        },

        initButtonEventListeners: function () {
            var shortlistButton = this;

            // add/remove to/from shortlist on click
            on(shortlistButton.domNode, "click", function () {
                if (shortlistButton.shortlistDisabled && !shortlistButton.isShortlisted(shortlistButton.packageId)) return;
                shortlistButton.updateShortlist(shortlistButton.isShortlisted(shortlistButton.packageId), shortlistButton.packageId);
            });

            // change text on hover/focus
            on(shortlistButton.domNode, mouse.enter, function () {
                if (shortlistButton.isShortlisted(shortlistButton.packageId)) dojo.query(".text", shortlistButton.domNode).text("remove");
            });

            // change text on mouseleave/blur
            on(shortlistButton.domNode, mouse.leave, function () {
			if(shortlistButton.shortlistStore.jsonData.siteName=="firstchoice" || shortlistButton.shortlistStore.jsonData.siteName=="thomson"){
                if (shortlistButton.isShortlisted(shortlistButton.packageId)) dojo.query(".text", shortlistButton.domNode).text("shortlisted");
            }
			else
			if (shortlistButton.isShortlisted(shortlistButton.packageId)) dojo.query(".text", shortlistButton.domNode).text("saved");
			});
        },

        observeStore: function () {
            var shortlistButton = this;
            var resultSet = shortlistButton.shortlistStore.query();

            // observe shortlist and toggle button state
            resultSet.observe(function(holidayPackage, remove, add){
                shortlistButton.toggleButton(holidayPackage, remove, add);
            });
        },

        toggleButton: function (holidayPackage, remove, add) {
            var shortlistButton = this;
            var action, text, textNode = query(".text", shortlistButton.domNode)[0];

            if(holidayPackage.id !== shortlistButton.packageId) return;

            if(shortlistButton.shortlistStore.jsonData.siteName=="firstchoice" || shortlistButton.shortlistStore.jsonData.siteName=="thomson"){
                if (add > -1) {
                  action = "addClass";
                  text = "shortlisted";
                } else {
                  action = "removeClass";
                  text = "shortlist";
                }
                }
                else{
                	if (add > -1) {
                        action = "addClass";
                        text = "saved";
                      } else {
                        action = "removeClass";
                        text = "save";
                      }
                }

            dojo[action](shortlistButton.domNode, "saved");
            textNode.innerHTML = text;
        },

        isShortlisted: function (packageId) {
            var shortlistButton = this;
            return shortlistButton.shortlistStore.query({id: packageId}).total > 0;
        },

        updateShortlist: function (action, packageId) {
            // summary:
            //		Request shortlist store to add/remove package
            var shortlistButton = this;
            action = !action ? "add" : "remove";
            shortlistButton.shortlistStore.requestData(true, action, packageId);
        }

    });

    return tui.shortlist.view.ShortlistButton;
});