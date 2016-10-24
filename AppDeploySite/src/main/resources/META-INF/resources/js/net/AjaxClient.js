// Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
AppDeploy.namespace("com.github.francescojo.appdeploy.net");

com.github.francescojo.appdeploy.net.AjaxClient = function(httpMethod, requestPath) {
	var ObjectUtils = com.github.francescojo.appdeploy.util.ObjectUtils;
	this.onConnectionOpened = null;
	this.onHeadersReceived = null;
	this.onLoadingContents = null;
	this.onCompleteResponse = null;

	this.request = function(requestParams) {
		var xhr = new XMLHttpRequest();
		xhr["onConnectionOpened"] = this.onConnectionOpened;
		xhr["onHeadersReceived"] = this.onHeadersReceived;
		xhr["onLoadingContents"] = this.onLoadingContents;
		xhr["onCompleteResponse"] = this.onCompleteResponse;
		xhr.onreadystatechange = function() {
			var StringUtils = com.github.francescojo.appdeploy.util.StringUtils; 
			var headers = this.getAllResponseHeaders().split("\n");
			var headersMap = {};
			for (var i = 0, limit = headers.length; i < limit; i++) {
				var header = headers[i];
				if (header.length > 0) {
					var matches = /[a-zA-Z\-]+:/g.exec(header);
					var group = matches[0];
					var key = header.substring(0, group.length - 1).trim();
					var value = header.substring(group.length).trim();
					headersMap[key] = value;
				}
			}

			switch(this.readyState) {
			case 1:
				if (this.onConnectionOpened instanceof Function) {
					this.onConnectionOpened();
				}
				break;
			case 2:
				if (this.onHeadersReceived instanceof Function) {
					this.onHeadersReceived(this.status, headersMap);
				}
				break;
			case 3:
				if (this.onLoadingContents instanceof Function) {
					this.onLoadingContents(this.status, headersMap);
				}
				break;
			case 4:
				if (this.onCompleteResponse instanceof Function) {
					// headers, string
					this.onCompleteResponse(this.status, headersMap, this.responseText);
				}
				break;
			}
		}
		xhr.open(httpMethod, requestPath);

		var isPostRequest = /post/i.test(httpMethod) && !ObjectUtils.isEmptyObject(requestParams);
		if (isPostRequest) {
			xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			var params = "";
			var keys = Object.keys(requestParams);
			for (var i = 0, limit = keys.length; i < limit; i++) {
				var key = keys[i];
				var value = requestParams[key];
				
				params += key + "=" + value;
				if (i < limit - 1) {
					params += "&";
				}
			}
			xhr.send(params);
		} else {
			xhr.send();
		}
	}
}