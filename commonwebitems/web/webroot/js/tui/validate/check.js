define(["dojo/_base/lang", "dojox/validate/check", "dojox/validate/_base"], function(lang, validateCheck, validate){

    validate.checkForm = function(form, profile){
        tui.removePlaceHolders(form);
        var validateForm = validate.check(form, profile);
        tui.resetPlaceHolders(form);
        return validateForm;
    }

	validate.evaluateConstraint = function(profile, /*Array*/constraint, fieldName, elem){
		// summary:
		//	Evaluates dojo.validate.check() constraints that are specified as array
		//	arguments
		//
		// description: The arrays are expected to be in the format of:
		//      constraints:{
		//              fieldName: [functionToCall, param1, param2, etc.],
		//              fieldName: [[functionToCallFirst, param1],[functionToCallSecond,param2]]
		//      }
		//
		//  This function evaluates a single array function in the format of:
		//      [functionName, argument1, argument2, etc]
		//
		//  The function will be parsed out and evaluated against the incoming parameters.
		//
		// profile: The dojo.validate.check() profile that this evaluation is against.
		// constraint: The single [] array of function and arguments for the function.
		// fieldName: The form dom name of the field being validated.
		// elem: The form element field.
	
 		var isValidSomething = constraint[0];
		var params = constraint.slice(1);
		params.unshift(elem.value);
		if(typeof isValidSomething != "undefined"){
			return isValidSomething.apply(null, params);
		}
		return false; // Boolean
	};

	return validate
})