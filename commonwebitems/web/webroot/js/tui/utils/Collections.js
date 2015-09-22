define([ "dojo" ], function(dojo) {
	return {
		select : function(collection, predicate) {
			var selected = null;
			_.forEach(collection, function(entry, i) {
				if (predicate(entry))
					selected = entry;
			});
			return selected;
		}

	};
});
