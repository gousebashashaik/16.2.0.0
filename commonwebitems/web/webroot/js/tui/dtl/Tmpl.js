define(["dojo"], function(dojo) {
	
	var dtl = dojo.getObject("tui.dtl", true);
	
	return dtl.Tmpl = {
		createTmpl : function(context, tmpl) {
			var tmpl = new dojox.dtl.Template(tmpl);
			context  = new dojox.dtl.Context(context);
			return tmpl.render(context);
		}
	}
});