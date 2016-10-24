// Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
function onClickDelete(alertMsg) {
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
		alert(alertMsg);
	} else {
		HttpUtils.post("platform/delete", { "codes" : codes });
	}
}

function onPresetSelected(selection, confirmMsg) {
	var ObjectUtils = com.github.francescojo.appdeploy.util.ObjectUtils;
	var idx = selection.selectedIndex;
	if (idx === 0) {
		return false;
	}
	
	var name = document.getElementById("platformNameInput");
	var desc = document.getElementById("platformDescInput");
	var adminUrl = document.getElementById("platformAdminUrlInput");
	var selectOption = selection.options[selection.selectedIndex];
	var setText = function(option) {
		name.value = option.getAttribute("data-name");
		desc.value = option.getAttribute("data-description");
		adminUrl.value = option.getAttribute("data-adminUrl");
	}

	if (ObjectUtils.isEmptyString(name.value) &&
		ObjectUtils.isEmptyString(desc.value) &&
		ObjectUtils.isEmptyString(adminUrl.value)) {
		setText(selectOption);
		return true;
	} else {
		if (confirm(confirmMsg)) {
			setText(selectOption);
			return true;
		} else {
			selection.value = selection.oldValue;
			return false;
		}
	}
}
