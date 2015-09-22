define(["dojo"], function(dojo) {
	var utils = dojo.getObject("tui.utils", true);
	return utils.ObjectUtils = {
		
		isEmptyObject: function(obj) {
			for(var name in obj ) {
				return false;
			}
			return true;
		},
		
		objectmapper: function(destination, source, mapperconfig){
			var dataMapper = this;
			for (props in mapperconfig){
				var mapper = mapperconfig[props]
				mapper.name = (mapper.name instanceof Array) ? mapper.name : [mapper.name];
				for (var i = 0; i < mapper.name.length; i++){				
					destination[props] = (mapper.fn) ? mapper.fn.apply(destination, [source[mapper.name[i]], mapper.name[i], source, props])
															: source[mapper.name[i]];
				}
			}
		},

        clone : function (obj) {
            if (obj) {
                var constructor = obj.constructor;
                constructor ? obj.constructor = function() {} : null;
                var clone = dojo.clone(obj);
                obj.constructor = constructor;
                return clone;
            }
            return obj;
        }
	}
});
