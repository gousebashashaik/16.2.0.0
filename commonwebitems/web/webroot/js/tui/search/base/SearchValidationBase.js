define("tui/search/base/SearchValidationBase", ["dojo"], function (dojo) {

	dojo.declare("tui.search.base.SearchValidationBase", [], {

		// ----------------------------------------------------------------------------- properties

		// Object containing error messages.
		searchErrorMessages: null,

		searchConfig: null,

		// ----------------------------------------------------------------------------- methods
		// todo: reference searchConfig

		validate: function () {
			// summary:
			//    Common search validation
			//    Sets errors to error message model using strings from i18n
			var searchValidationBase = this;

			searchValidationBase.onStartValidation();
			
			// validate that we have at least one adult in the party
			if (searchValidationBase.seniors + searchValidationBase.adults === 0) {
				searchValidationBase.searchErrorMessages.set("partyComp", {
					onePassenger: searchValidationBase.searchMessaging[dojoConfig.site].errors.onePassenger
				});
			}
			
			if ((searchValidationBase.adults + searchValidationBase.seniors + searchValidationBase.children) > searchValidationBase.searchConfig.MAX_ADULTS_NUMBER) {

                var partyLimit = dojo.replace(searchValidationBase.searchMessaging[dojoConfig.site].errors.partyLimit, {
				   maxPassengerCount: searchValidationBase.searchConfig.MAX_ADULTS_NUMBER
				            });
				searchValidationBase.searchErrorMessages.set("partyComp", {
					partyLimit: [partyLimit, searchValidationBase.searchMessaging[dojoConfig.site].errors.partyLimitHours].join("")
				});
				}
			
			// validate that we don't have only children in the party
			if (searchValidationBase.children > 0 && (searchValidationBase.seniors + searchValidationBase.adults) === 0) {
				searchValidationBase.searchErrorMessages.set("partyComp", {
					childOnly: searchValidationBase.searchMessaging[dojoConfig.site].errors.childOnly
				});
			}
			/*// validate that we don't have more than 9 persons in the party
			var children = _.filter(searchValidationBase.childAges,function (age) {
				return age > searchValidationBase.searchConfig.INFANT_AGE;
			}).length;*/

			

			
			// validate that we don't have more infants (0-1 yrs) than grownups in the party
			var infants = _.filter(searchValidationBase.childAges,function (age) {
				return age !== -1 && age < searchValidationBase.searchConfig.INFANT_AGE;
			}).length;

			if ((searchValidationBase.adults + searchValidationBase.seniors !== 0) && infants > (searchValidationBase.seniors + searchValidationBase.adults)) {
				searchValidationBase.searchErrorMessages.set("partyChildAges", {
					infantLimit: searchValidationBase.searchMessaging[dojoConfig.site].errors.infantLimit
				});
			}

			searchValidationBase.onEndValidation();

			// if we have errors return false
			for (var prop in searchValidationBase.searchErrorMessages) {
				if (!dojo.isFunction(searchValidationBase.searchErrorMessages[prop])) {
					if (searchValidationBase.searchErrorMessages.hasOwnProperty(prop) && !_.isEmpty(searchValidationBase.searchErrorMessages[prop])) {
						return false;
					}
				}
			}
			return true;
		},

		onStartValidation: function () {},

		onEndValidation: function () {}

	});
	return tui.search.base.SearchValidationBase;
});