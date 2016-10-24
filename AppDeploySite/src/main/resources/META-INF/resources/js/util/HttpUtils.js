// Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
AppDeploy.namespace("com.github.francescojo.appdeploy.util.HttpUtils");

com.github.francescojo.appdeploy.util.HttpUtils.post = function(path, params) {
	var method = "post";
	var form = document.createElement("form");
	var createInput = function(name, value) {
		var input = document.createElement("input");
		input.setAttribute("type", "hidden");
		input.setAttribute("name", name);
		input.setAttribute("value", value);
		
		return input;
	};
	form.setAttribute("method", method);
	form.setAttribute("action", path);

	for (var key in params) {
		if (params.hasOwnProperty(key)) {
			var value = params[key];
			if (value.constructor === Array) {
				for (var i = 0, limit = value.length; i < limit; i++) {
					form.appendChild(createInput(key, value[i]));
				}
			} else {
				form.appendChild(createInput(key, value));
			}
		}
	}

	document.body.appendChild(form);
	form.submit();
}
