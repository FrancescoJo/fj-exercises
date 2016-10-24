// Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
function onClickDelete(noSelectedAlertMsg, unexpectedResponseAlertMsg) {
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

	var ajaxClient = new com.github.francescojo.appdeploy.net.AjaxClient("POST", "./app/checkDelete");
	ajaxClient.onCompleteResponse = function(code, headers, responseText) {
		var responseObject;
		try {
			responseObject = JSON.parse(responseText);
		} catch(e) {
			responseObject = null;
		}

		if (code >= 400) {
			var alertMsg;
			if (responseObject.hasOwnProperty("text")) {
				alertMsg = responseObject.text;
			} else {
				alertMsg = unexpectedResponseAlertMsg;
			}
			alert(alertMsg);
			return false;
		}

		var proceedToDelete = function(codes) {
			var HttpUtils = com.github.francescojo.appdeploy.util.HttpUtils;
			HttpUtils.post("app/delete", { "appIds" : codes });
		}

		if (code >= 300 && code <= 399) {
			if (!responseObject.hasOwnProperty("text")) {
				alert("Unexpected response. Please try again later.");
				return false;
			}

			var deleteConfirmed = confirm(responseObject.text);
			if (deleteConfirmed) {
				proceedToDelete(codes.join(","));
			}
		}

		if (code >= 200 && code <= 299) {
			proceedToDelete(codes.join(","));
		}
	}
	ajaxClient.request({ 
		"appIds" : codes.join(",")
	});
}

function checkAppAdd(alertMsg) {
	var HttpUtils = com.github.francescojo.appdeploy.util.HttpUtils;
	var extractCheckboxes = function(node) {
		var checkBoxes = [];
		for (var i = 0, limit = node.length; i < limit; i++) {
			checkBoxes.push(node[i].getElementsByTagName("input")[0]);
		}
		
		return checkBoxes;
	};

	var stageCheckTop = document.getElementById("stageCheckBoxes");
	var platformCheckTop = document.getElementById("platformCheckboxes");
	var stageChecks = extractCheckboxes(stageCheckTop.children);
	var platformChecks = extractCheckboxes(platformCheckTop.children);
	var appName = document.getElementById("appNameInput").value;
	var appDescription = document.getElementById("appDescTextArea").value;
	var isChecked = function(checkBoxes) {
		for (var i = 0, limit = checkBoxes.length; i < limit; i++) {
			var checkBox = checkBoxes[i];
			if(checkBox.checked) {
				return true;
			}
		}
		
		return false;
	};
	var getIds = function(checkBoxes) {
		var ids = [];
		
		for (var i = 0, limit = checkBoxes.length; i < limit; i++) {
			var checkBox = checkBoxes[i];
			if(checkBox.checked) {
				ids.push(checkBox.getAttribute("data-id"));
			}
		}
		
		return "" + ids;
	};
	
	if (!isChecked(stageChecks) || !isChecked(platformChecks)) {
		alert(alertMsg);
		return false;
	}

	return true;
}