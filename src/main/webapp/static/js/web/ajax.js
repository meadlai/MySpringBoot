(function(window, document, Object) {
    'use strict';

    (function(window, factory) {
        // Expose Ajax
        var define = window.define || null;

        if (typeof define === 'function' && define.amd) {
            // AMD
            define('ajax', [], function() {
                return factory();
            });
        } else {
            window.Ajax = factory();
        }
    })(window, function() {

        var ajax = function(options) {
            options = {
                type: options.type || "GET",
                url: options.url || "",
                async: options.async === false ? false : true,
                timeout: options.timeout || 5000,
                data: options.data || null,
                onSuccess: options.onSuccess || function() {
                },
                onComplete: options.onComplete || function() {
                },
                onError: options.onError || function() {
                },
                typeJson: options.typeJson || {"GET": "GET", "POST": "POST", "PUT": "PUT", "PATCH": "PATCH", "DELETE": "DELETE"},
                requestHeader: options.requestHeader || {
                    "Content-type": "application/x-www-form-urlencoded; charset=UTF-8"
                },
                overrideMimeType: options.overrideMimeType || null // "text/plain; charset=utf-8"
            };
            if (options.data && typeof options.data === "string") {
                try {
                    options.data = JSON.parse(options.data);
                } catch (e) {
                }
            }
            var xhr = getXMLHttpRequest(options.url);
            xhr.open(options.type, options.url, options.async);
            setRequestHeader(options.requestHeader, xhr);
            if (options.overrideMimeType) {
                xhr.overrideMimeType(options.overrideMimeType);
            }
            var isTimeout = false;
            setTimeout(function() {
                isTimeout = true;
            }, options.timeout);
            xhr.onreadystatechange = function() {
                if (xhr.readyState !== 4 && !isTimeout) {
                    return;
                }
                if (isSuccess(xhr)) {
                    var contentType = xhr.getResponseHeader("content-type");
                    var isXml = !options.type && contentType && contentType.indexOf("xml") >= 0;
                    var data = options.type === "xml" || isXml ? xhr.responseXML : xhr.responseText;
                    //options.onSuccess.call(this, data);
                    var jsonObj = data;
                    if (contentType.indexOf("json") > 0 || options.type.indexOf("json")) {
                        try {
                            jsonObj = JSON.parse(data);
                        } catch (e) {
                            jsonObj = data;
                        }
                    }
                    options.onSuccess(jsonObj, xhr);
                } else {
                    if (options.onError) {
                        //options.onError.call(this, xhr);
                        options.onError(xhr);
                    }
                }
                options.onComplete(xhr);
                xhr = null;
            };
            var data = formatData(options);
            window.setTimeout(function() {
                xhr.send(data);
            }, 0);
            function getXMLHttpRequest(url) {
                var xhr = null;
                try {
                    if (url && !isLocal(url)) {
                        xhr = new XDomainRequest();
                    }
                    xhr = xhr || new ActiveXObject("Msxml2.XMLHTTP");
                } catch (e1) {
                    try {
                        xhr = new ActiveXObject("Microsoft.XMLHTTP");
                    } catch (e2) {
                        xhr = new XMLHttpRequest();
                    }
                }
                return xhr;
            }
            function setRequestHeader(requestHeader, xhr) {
                for (var key in requestHeader) {
                    xhr.setRequestHeader(key, requestHeader[key]);
                }
                return this;
            }
            var isLocal = function(url) {
                var a = document.createElement('a');
                a.href = url;
                return a.hostname === window.location.hostname;
            };
            function isSuccess(xhr) {
                try {
                    return (!xhr.status && location.protocol === "file:")
                            || (xhr.status >= 200 && xhr.status < 300)
                            || xhr.status === 304
                            || (navigator.userAgent.indexOf("Safari") >= 0 && typeof xhr.status === "undefined");
                } catch (e) {
                    return false;
                }
            }

            function formatData(options) {
                if (options.data && typeof options.data === 'object') {
                    var dataStr = "";
                    options.data["_method"] = options.typeJson[options.type];
                    for (var key in options.data) {
                        dataStr += key + "=" + encodeURIComponent(options.data[key]) + "&";
                    }
                    if (dataStr) {
                        dataStr = dataStr.substr(0, dataStr.length - 1);
                    }
                    return dataStr;
                }
                return options.data;
            }
        };

        return {
            send: function(options) {
                ajax(options);
            }
        };
    });
})(window, document, Object);