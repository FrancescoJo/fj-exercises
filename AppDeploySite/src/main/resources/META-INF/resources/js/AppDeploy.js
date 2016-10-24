// Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
var AppDeploy = AppDeploy || {};
var com = com || {};

AppDeploy.namespace = function (namespace) {
	var nsParts = namespace.split(".");
	var parent = com;

	if (nsParts[0] === "com") {
		nsParts = nsParts.slice(1);
	}

	for (var i = 0; i < nsParts.length; i++) {
		var partname = nsParts[i];
		if (typeof parent[partname] === "undefined") {
			parent[partname] = {};
		}
		parent = parent[partname];
	}
	return parent;
};
