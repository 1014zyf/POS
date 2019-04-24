<?php
define("PROXY_DOMAIN", "http" . (isset($_SERVER['HTTPS']) ? "s" : "") . "://" . $_SERVER["SERVER_NAME"] . ($_SERVER["SERVER_PORT"] != 80 ? ":" . $_SERVER["SERVER_PORT"] : ""));
define("PROXY_PREFIX", "http" . (isset($_SERVER['HTTPS']) ? "s" : "") . "://" . $_SERVER["SERVER_NAME"] . ($_SERVER["SERVER_PORT"] != 80 ? ":" . $_SERVER["SERVER_PORT"] : "") . $_SERVER["SCRIPT_NAME"] . "/");
define("TARGET_URL", str_replace(PROXY_PREFIX, '', substr($_SERVER["REQUEST_URI"], strlen($_SERVER["SCRIPT_NAME"]) + 1)));
define("TARGET_DOMAIN", preg_replace('/([^\/]*\/\/[^\/]*)\/.+/', '$1', TARGET_URL));
define("IMPORT_SCRIPT", '(function() {
	if (window.XMLHttpRequest) {
		function handleUrl(url) {
			newUrl = url;
			if (newUrl.indexOf("http") == 0)
				newUrl = "' + PROXY_PREFIX + '" + newUrl;
			return newUrl;
		}

		var proxied = window.XMLHttpRequest.prototype.open;
		window.XMLHttpRequest.prototype.open = function() {
			if (arguments[1] !== null && arguments[1] !== undefined) {
				var url = arguments[1];
				arguments[1] = handle(url);
			}
			return proxied.apply(this, [].slice.call(arguments));
		};
	}

})();');
?>