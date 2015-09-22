define(["dojo", "dojo/has"], function(dojo, has){
	
	var STR = "string",
        FN = "function"
    ;

    function typeValidates( type ){
        input.setAttribute("type", type);
        input.value = "\x01";
        return has("input-checkvalidity") && input.type == type &&
               (/search|tel/.test(type) || input.value != "\x01" || !input.checkValidity());
    }

    if(!has("dom")){ return; }

    var input = document.createElement("input");

    has.add("input-attr-placeholder", ("placeholder" in input));

 
	return has;
})