define([
	"dojo/_base/lang",
	"dojo/_base/array",
	"dojox/string/tokenize",
	"dojox/string/sprintf",
	"dojox/dtl/filter/htmlstrings",
	"dojox/dtl/_base"
], function(lang,array,Tokenize,Sprintf,htmlstrings,dd){	
	
	lang.mixin(dd.filter.strings, {
			truncatewords: function(value, arg){
			// summary: Truncates a string after a certain number of words
			// arg: Integer
			//		Number of words to truncate after
			arg = parseInt(arg, 10);
			if(!arg){
				return value;
			}

			for(var i = 0, j = value.length, count = 0, current, last; i < value.length; i++){
				current = value.charAt(i);
				if(dojox.dtl.filter.strings._truncatewords.test(last)){
					if(!dojox.dtl.filter.strings._truncatewords.test(current)){
						++count;
						if(count == arg){
							return value.substring(0, j + 1) + "...";
						}
					}
				}else if(!dojox.dtl.filter.strings._truncatewords.test(current)){
					j = i;
				}
				last = current;
			}
			return value;
		}
	})
});