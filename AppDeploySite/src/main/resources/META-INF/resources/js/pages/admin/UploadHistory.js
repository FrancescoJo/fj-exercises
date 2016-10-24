// Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
function onClickDelete(noSelectedAlertMsg) {
	var HttpUtils = com.github.francescojo.appdeploy.util.HttpUtils;
	var checkboxes = document.getElementsByName("deleteCheck");
	var codes = [];

	for (var i = 0, limit = checkboxes.length; i < limit; i++) {
		var checkbox = checkboxes[i];
		if (checkbox.checked) {
			codes.push(checkbox.value);
		}
	}

	if (codes.length === 0) {
		alert(noSelectedAlertMsg);
		return false;
	}

	HttpUtils.post(window.location.href + "/delete", { "codes" : codes });
}
