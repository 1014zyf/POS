<?php

//Adapted from http://www.php.net/manual/en/function.getallheaders.php#99814
if (!function_exists("getallheaders")) {
	function getallheaders() {
		$result = array();
		foreach($_SERVER as $key => $value) {
			if (substr($key, 0, 5) == "HTTP_") {
				$key = str_replace(" ", "-", ucwords(strtolower(str_replace("_", " ", substr($key, 5)))));
				$result[$key] = $value;
			} else {
				$result[$key] = $value;
			}
		}
		return $result;
	}
}


//Makes an HTTP request via cURL, using request data that was passed directly to this script.
function makeRequest($url) {
	//Tell cURL to make the request using the brower's user-agent if there is one, or a fallback user-agent otherwise.
	$user_agent = $_SERVER["HTTP_USER_AGENT"];
	if (empty($user_agent)) {
		$user_agent = "Mozilla/5.0 (compatible; miniProxy)";
	}
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_USERAGENT, $user_agent);
	//Proxy the browser's request headers.
	$browserRequestHeaders = getallheaders();
	//(...but let cURL set some of these headers on its own.)
	//TODO: The unset()s below assume that browsers' request headers
	//will use casing (capitalizations) that appear within them.
	unset($browserRequestHeaders["Host"]);
	unset($browserRequestHeaders["Content-Length"]);
	//Throw away the browser's Accept-Encoding header if any;
	//let cURL make the request using gzip if possible.
	unset($browserRequestHeaders["Accept-Encoding"]);
	
	if (isset($browserRequestHeaders['Referer']))
		$browserRequestHeaders['Referer'] = str_replace(PROXY_PREFIX, '', $browserRequestHeaders['Referer']);
	$browserRequestHeaders['Origin'] = preg_replace('/^(http[s]*:\/\/[^\/]+)(.*)/i', '$1', $url);
	$browserRequestHeaders['Host'] = preg_replace('/^http[s]*:\/\/([^\/]+)(.*)/i', '$1', $url);
	
	curl_setopt($ch, CURLOPT_ENCODING, "");
	//Transform the associative array from getallheaders() into an
	//indexed array of header strings to be passed to cURL.
	$curlRequestHeaders = array();
	foreach ($browserRequestHeaders as $name => $value) {
		$curlRequestHeaders[] = $name . ": " . $value;
	}
	
	curl_setopt($ch, CURLOPT_HTTPHEADER, $curlRequestHeaders);
	
	//Proxy any received GET/POST/PUT data.
	switch ($_SERVER["REQUEST_METHOD"]) {
		case "POST":
			curl_setopt($ch, CURLOPT_POST, true);
			//For some reason, $HTTP_RAW_POST_DATA isn't working as documented at
			//http://php.net/manual/en/reserved.variables.httprawpostdata.php
			//but the php://input method works. This is likely to be flaky
			//across different server environments.
			//More info here: http://stackoverflow.com/questions/8899239/http-raw-post-data-not-being-populated-after-upgrade-to-php-5-3
			curl_setopt($ch, CURLOPT_POSTFIELDS, file_get_contents("php://input"));
		break;
		case "PUT":
			curl_setopt($ch, CURLOPT_PUT, true);
			curl_setopt($ch, CURLOPT_INFILE, fopen("php://input"));
		break;
	}

	//Other cURL options.
	curl_setopt($ch, CURLOPT_HEADER, true);
	curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_FAILONERROR, true);
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	curl_setopt($ch, CURLOPT_COOKIESESSION, true);
	if (!empty($_COOKIE)) {
		foreach ($_COOKIE as $key => $value) {
			if (is_array($value)) {
				//	For PHP7, will breakdown name[key] to array
				foreach ($value as $key2 => $value2)
					$cookie[] = $key."[$key2]=$value2";
			}
			else {
				$cookie[] = "{$key}={$value}";
			}
		};
		$cookieString = implode('; ', $cookie);
		curl_setopt($ch, CURLOPT_COOKIE, $cookieString);
	}

	//Set the request URL.
	curl_setopt($ch, CURLOPT_URL, $url);

	//Make the request.
	$response = curl_exec($ch);
	$responseInfo = curl_getinfo($ch);
	$headerSize = curl_getinfo($ch, CURLINFO_HEADER_SIZE);
	
	curl_close($ch);

	//Setting CURLOPT_HEADER to true above forces the response headers and body
	//to be output together--separate them.
	$responseHeaders = substr($response, 0, $headerSize);
	$responseBody = substr($response, $headerSize);
	
	return array("headers" => $responseHeaders, "body" => $responseBody, "responseInfo" => $responseInfo);
}

function handleUrl($url) {
	$url = trim($url);
	if (!(stripos($url, "javascript:") === false))
		return $url;
	if (!(stripos($url, "mailto:") === false))
		return $url;
	if (!(stripos($url, "data:") === false))
		return $url;
	$result = stripos($url, "#");
	if ($result !== false && $result == 0) {
		if (strlen($url) > 1)
			return $url;
		else
			return "javascript:";
	}
	
	$result = stripos($url, "//");
	if ($result !== false && $result == 0)
		$url = "http:".$url;
	$result = stripos($url, "http");
	if ($result !== false && $result == 0)
		return PROXY_PREFIX.$url;

	$result = stripos($url, "/");
	if ($result === false) {
		return $url;	//	follow base
	}
	else if ($result == 0) {
		return PROXY_PREFIX.TARGET_DOMAIN.$url;
	}
	else {
		return $url;	//	follow base
	}
}

//Converts relative URLs to absolute ones, given a base URL.
//Modified version of code found at http://nashruddin.com/PHP_Script_for_Converting_Relative_to_Absolute_URL
// function rel2abs($rel, $base) {
	// if (empty($rel)) $rel = ".";
	// if ($rel[0] == '/') {
		// $default_domain = preg_replace('/.*(http.*\/\/[^\/]+)\/.*/i', '$1', $_SERVER['REQUEST_URI']);
		// if (!(parse_url($rel, PHP_URL_SCHEME) != "" || strpos($rel, "//") === 0))return $default_domain.$rel; //Return if is relative
	// }
	// else
		// return $rel;
	// // if (parse_url($rel, PHP_URL_SCHEME) != "" || strpos($rel, "//") === 0) return $rel; //Return if already an absolute URL
	// if ($rel[0] == "#" || $rel[0] == "?") return $base.$rel; //Queries and anchors
	// extract(parse_url($base)); //Parse base URL and convert to local variables: $scheme, $host, $path
	// $path = isset($path) ? preg_replace('#/[^/]*$#', "", $path) : "/"; //Remove non-directory element from path
	// if ($rel[0] == '/') $path = ""; //Destroy path if relative url points to root
	// $port = isset($port) && $port != 80 ? ":" . $port : "";
	// $auth = "";
	// if (isset($user)) {
		// $auth = $user;
		// if (isset($pass)) {
			// $auth .= ":" . $pass;
		// }
		// $auth .= "@";
	// }
	// $abs = "$auth$host$port$path/$rel"; //Dirty absolute URL
	// for ($n = 1; $n > 0; $abs = preg_replace(array("#(/\.?/)#", "#/(?!\.\.)[^/]+/\.\./#"), "/", $abs, -1, $n)) {} //Replace '//' or '/./' or '/foo/../' with '/'
	// return $scheme . "://" . $abs; //Absolute URL is ready.
// }

//Proxify contents of url() references in blocks of CSS text.
function proxifyCSS($css, $baseURL) {
	return preg_replace_callback(
		'/url\((.*?)\)/i',
		function($matches) use ($baseURL) {
				$url = $matches[1];
				//Remove any surrounding single or double quotes from the URL so it can be passed to rel2abs - the quotes are optional in CSS
				//Assume that if there is a leading quote then there should be a trailing quote, so just use trim() to remove them
				if (strpos($url, "'") === 0) {
					$url = trim($url, "'");
				}
				if (strpos($url, "\"") === 0) {
					$url = trim($url, "\"");
				}
				if (stripos($url, "data:") === 0) return "url(" . $url . ")"; //The URL isn't an HTTP URL but is actual binary data. Don't proxify it.
				return "url(" . handleUrl($url) . ")";
		},
		$css);
}
?>
