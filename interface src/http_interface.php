<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

//error_reporting(E_ERROR | E_WARNING | E_PARSE);
include "libs/http_interface/function.php";
include "libs/http_interface/const.php";
include "libs/http_interface/SmartDOMDocument.class.php";

ob_start("ob_gzhandler");

if (!function_exists("curl_init")) die ("This proxy requires PHP's cURL extension. Please install/enable it on your server and try again.");

// if (empty(TARGET_URL)) die("<html><head><title>miniProxy</title></head><body><h1>Welcome to miniProxy!</h1>miniProxy can be directly invoked like this: <a href=\"" . PROXY_PREFIX . "http://google.com/\">" . PROXY_PREFIX . "http://google.com/</a><br /><br />Or, you can simply enter a URL below:<br /><br /><form onsubmit=\"window.location.href='" . PROXY_PREFIX . "' + document.getElementById('site').value; return false;\"><input id=\"site\" type=\"text\" size=\"50\" /><input type=\"submit\" value=\"Proxy It!\" /></form></body></html>");
// if (strpos(TARGET_URL, "//") === 0) TARGET_URL = "http:" . TARGET_URL; //Assume that any supplied URLs starting with // are HTTP URLs.
// if (!preg_match("@^.*://@", TARGET_URL)) TARGET_URL = "http://" . TARGET_URL; //Assume that any supplied URLs without a scheme are HTTP URLs.

$response = makeRequest(TARGET_URL);
$rawResponseHeaders = $response["headers"];
$responseBody = $response["body"];
$responseInfo = $response["responseInfo"];
/***	header	***/
//A regex that indicates which server response headers should be stripped out of the proxified response.
$header_blacklist_pattern = "/^Content-Length|^Transfer-Encoding|^Content-Encoding.*gzip/i";

//cURL can make multiple requests internally (while following 302 redirects), and reports
//headers for every request it makes. Only proxy the last set of received response headers,
//corresponding to the final request made by cURL for any given call to makeRequest().
$responseHeaderBlocks = array_filter(explode("\r\n\r\n", $rawResponseHeaders));
$lastHeaderBlock = end($responseHeaderBlocks);
$headerLines = explode("\r\n", $lastHeaderBlock);

foreach ($headerLines as $header) {
	$header = trim($header);
	
	if (preg_match($header_blacklist_pattern, $header))
		continue;
	
	//	Cookie processing
	if (stripos($header, 'Set-Cookie:') !== FALSE) {
		$cookiePath = preg_replace('/.*path=([^;]+).*/i', '$1', $header);
		$newCookiePath = str_replace(PROXY_DOMAIN, '', handleUrl($cookiePath));
		$header = str_replace($cookiePath, $newCookiePath, $header);
		
		//	Remove domain
		$header = preg_replace('/(.+)domain=.+[;]*(.*)/i', '$1$2', $header);
	}
	header($header, false);
}

$contentType = "";
if (isset($responseInfo["content_type"])) $contentType = $responseInfo["content_type"];

/***	body	***/
//This is presumably a web page, so attempt to proxify the DOM.
if (strpos($responseBody, 'proxyHandleUrl') === false) {
	$responseBody = preg_replace('/((window.(top.)?)?location(.href)?[\s]*=[\s]*)([^;]+);/i', '
	function proxyHandleUrl(proxyUrl) {
		var result = proxyUrl.indexOf("//");
		if (result == 0)
			proxyUrl = "http:" + proxyUrl;
		result = proxyUrl.indexOf("http");
		if (result == 0)
			return "'.PROXY_PREFIX.'" + proxyUrl;

		result = proxyUrl.indexOf("/");
		if (result === -1) {
			return proxyUrl;	//	follow base
		}
		else if (result == 0) {
			return "'.PROXY_PREFIX.TARGET_DOMAIN.'" + proxyUrl;
		}
		else {
			return proxyUrl;	//	follow base
		}
	}
	$1proxyHandleUrl($5);
	', $responseBody);
}

//	For parameter with url
$responseBody = preg_replace('/=(http[s]*:\/\/)/i', '='.PROXY_PREFIX.'$1', $responseBody);

if (stripos($contentType, "javascript") !== false) {
	//	Handle location in html and javascript
	// $responseBody = preg_replace('/((?:location)window.location(?:.href)?[\s]*=[\s]*http)/i', '$1http'.'"'.PROXY_PREFIX.'" + ', $responseBody);
	
	die($responseBody);
}
else if (stripos($contentType, "text/html") !== false) {
	//Attempt to normalize character encoding.
	$detectedEncoding = mb_detect_encoding($responseBody, "UTF-8, ISO-8859-1");
	if ($detectedEncoding) {
		$responseBody = mb_convert_encoding($responseBody, "HTML-ENTITIES", $detectedEncoding);
	}
	
	$lastBase = '';
	$baseDomain = preg_replace('/(http[s]*:\/\/.*\/)([^\/]*\/)/', '$1', TARGET_URL.((substr(TARGET_URL, -1) != '/') ? '/' : ''));
	$baseFile = preg_replace('/(http[s]*:\/\/.*\/)([^\/]*\/)/', '$2', TARGET_URL.((substr(TARGET_URL, -1) != '/') ? '/' : ''));
	if (strpos($baseFile, '.') === FALSE)
		$lastBase = $baseDomain.$baseFile;
	else
		$lastBase = $baseDomain;
	// die($lastBase);
	preg_match_all('/(<base[^>]*>)/i', $responseBody, $matches);
	if (!empty($matches[0])) {
		$lastBaseSeg = $matches[0][sizeof($matches[0]) - 1];
		//	Remove all base tag
		for ($i=0; $i < sizeof($matches[0]); $i++)
			$responseBody = str_replace($matches[0][$i], '', $responseBody);
	
		//	Get base (if exist)
		preg_match('/href\s*=["\'](.+)["\']/i', $lastBaseSeg, $matches);
		if (!empty($matches[1]))
			$lastBase = $matches[1];
	}
	
	if (stripos($lastBase, PROXY_PREFIX) === false || stripos($lastBase, PROXY_PREFIX) > 0)
		$lastBase = PROXY_PREFIX.$lastBase;
	
	//	take away '/' at last
	// if (substr($lastBase, -1) == '/')
		// $lastBase = substr($lastBase, 0, strlen($lastBase) - 1);
	
	//	Replace all action attr.
	preg_match_all('/[\s\.]+(action|src|href)[\s]*=[\s]*["\']([^"\']+)["\']/i', $responseBody, $matches);
	for ($i=0; $i<sizeof($matches[0]); $i++) {
		$actionSegment = $matches[0][$i];
		$actionUrl = $matches[2][$i];
		$newActionUrl = empty($actionUrl) ? TARGET_URL : handleUrl($actionUrl);
		$newActionSegment = str_replace($actionUrl, $newActionUrl, $actionSegment);
		$responseBody = str_replace($actionSegment, $newActionSegment, $responseBody);
		
		$matches[3][$i] = $newActionUrl;
	}
	
	//	Replace all background-image:url
	preg_match_all('/background-image:url\((.+)\)/i', $responseBody, $matches);
	for ($i=0; $i<sizeof($matches[0]); $i++) {
		$orgImageUrl = $matches[1][$i];
		$newImageUrl = handleUrl($orgImageUrl);
		$responseBody = str_replace('background-image:url('.$orgImageUrl.')', 'background-image:url('.$newImageUrl.')', $responseBody);
	}
	
	//	Add <base> tag
	$responseBody = preg_replace('/<head>/i', '<head><base href="'.$lastBase.'">', $responseBody);
	
	//	Add script
	$responseBody = preg_replace('/<\/head>/i', '</head><script>'.IMPORT_SCRIPT.'</script>', $responseBody);

	//	Handle location
	// $responseBody = preg_replace('/(window.location(?:.href)?[\s]*=)/i', '$1'.'"'.PROXY_PREFIX.'" + ', $responseBody);
	// $responseBody = preg_replace('/(\slocation(?:.href)?[\s]*=)/i', '$1'.'"'.PROXY_PREFIX.'" + ', $responseBody);
	
	
	//	Handle location in html and javascript
	// $responseBody = preg_replace('/(window.location(?:.href)?[\s]*=[\s]*http)/i', '$1http'.'"'.PROXY_PREFIX.'" + ', $responseBody);
	// $responseBody = preg_replace('/(\slocation(?:.href)?[\s]*=[\s]*http)/i', '$1http'.'"'.PROXY_PREFIX.'" + ', $responseBody);
	
	die($responseBody);
	// $action = empty($action) ? TARGET_URL : handleUrl($action);
	
	//Parse the DOM.
	// $doc = new SmartDOMDocument();
	// @$doc->loadHTML($responseBody);
	// $xpath = new DOMXPath($doc);
	
	//	Mark processing
	// $lastBase = preg_replace('/(.*)\/.+/', '$1/', TARGET_URL);
	// foreach($xpath->query('//base') as $form) {
		// $baseHref = $form->getAttribute("href");
		// $lastBase = $baseHref;
		// $form->parentNode->removeChild($form);
	// }
	
	// $head = $xpath->query('//head')->item(0);
	// $body = $xpath->query('//body')->item(0);
	// $prependElem = $head != NULL ? $head : $body;
	// $baseElem = $doc->createElement('base', '');
    // $baseElem->setAttribute("href", $lastBase);
    // $prependElem->appendChild($baseElem);

	//Rewrite forms so that their actions point back to the proxy.
	foreach($xpath->query('//form') as $form) {
		$method = $form->getAttribute("method");
		$action = $form->getAttribute("action");
		//If the form doesn't have an action, the action is the page itself.
		//Otherwise, change an existing action to an absolute version.
		$action = empty($action) ? TARGET_URL : handleUrl($action);
		//Rewrite the form action to point back at the proxy.
		$form->setAttribute("action", $action);
	}
	//Profixy <style> tags.
	foreach($xpath->query('//style') as $style) {
		$style->nodeValue = proxifyCSS($style->nodeValue, TARGET_URL);
	}
	//Proxify tags with a "style" attribute.
	foreach ($xpath->query('//*[@style]') as $element) {
		$element->setAttribute("style", proxifyCSS($element->getAttribute("style"), TARGET_URL));
	}
	//Proxify any of these attributes appearing in any tag.
	$proxifyAttributes = array("href", "src");
	foreach($proxifyAttributes as $attrName) {
		foreach($xpath->query('//*[@' . $attrName . ']') as $element) { //For every element with the given attribute...
			$attrContent = $element->getAttribute($attrName);
			if ($attrName == "href" && (stripos($attrContent, "javascript:") === 0 || stripos($attrContent, "mailto:") === 0)) continue;
			$element->setAttribute($attrName, handleUrl($attrContent));
		}
	}


	//Attempt to force AJAX requests to be made through the proxy by
	//wrapping window.XMLHttpRequest.prototype.open in order to make
	//all request URLs absolute and point back to the proxy.
	//The rel2abs() JavaScript function serves the same purpose as the server-side one in this file,
	//but is used in the browser to ensure all AJAX request URLs are absolute and not relative.
	//Uses code from these sources:
	//http://stackoverflow.com/questions/7775767/javascript-overriding-xmlhttprequest-open
	//https://gist.github.com/1088850
	//TODO: This is obviously only useful for browsers that use XMLHttpRequest but
	//it's better than nothing.

	$head = $xpath->query('//head')->item(0);
	$body = $xpath->query('//body')->item(0);
	$prependElem = $head != NULL ? $head : $body;

	//Only bother trying to apply this hack if the DOM has a <head> or <body> element;
	//insert some JavaScript at the top of whichever is available first.
	//Protects against cases where the server sends a Content-Type of "text/html" when
	//what's coming back is most likely not actually HTML.
	//TODO: Do this check before attempting to do any sort of DOM parsing?
	if ($prependElem != NULL) {
		$scriptElem = $doc->createElement("script", IMPORT_SCRIPT);
		$scriptElem->setAttribute("type", "text/javascript");
		$prependElem->insertBefore($scriptElem, $prependElem->firstChild);
	}
	
	preg_match('/(.+http.+\/\/[^\/]+)/', $_SERVER['REQUEST_URI'], $match);
	$proxyDomain = $match[0];	//	no ending	"/"
	
	$htmlResult = $doc->saveHTMLExact();
	$htmlResult = str_replace('*=*</script></div>', '*=*', $htmlResult);
	// $htmlResult = preg_replace('/(<\/[^<>]+>)/', "$1\r\n", $htmlResult);

	echo "<!-- Proxified page constructed by miniProxy -->\n" . $htmlResult;
} 
else if (stripos($contentType, "text/css") !== false) { //This is CSS, so proxify url() references.
	echo proxifyCSS($responseBody, TARGET_URL);
} 
else { //This isn't a web page or CSS, so serve unmodified through the proxy with the correct headers (images, JavaScript, etc.)
	header("Content-Length: " . strlen($responseBody));
	echo $responseBody;
}

?>
