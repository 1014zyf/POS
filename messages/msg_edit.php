<?php
include_once("common_include.php");
	
	class AccessTokenAuthentication {
		/*
		 * Get the access token.
		 *
		 * @param string $grantType    Grant type.
		 * @param string $scopeUrl     Application Scope URL.
		 * @param string $clientID     Application client ID.
		 * @param string $clientSecret Application client ID.
		 * @param string $authUrl      Oauth Url.
		 *
		 * @return string.
		 */
		function getTokens($grantType, $scopeUrl, $clientID, $clientSecret, $authUrl){
			try {
				//Initialize the Curl Session.
				$ch = curl_init();
				//Create the request Array.
				$paramArr = array (
					 'grant_type'    => $grantType,
					 'scope'         => $scopeUrl,
					 'client_id'     => $clientID,
					 'client_secret' => $clientSecret
				);
				//Create an Http Query.//
				$paramArr = http_build_query($paramArr);
				//Set the Curl URL.
				curl_setopt($ch, CURLOPT_URL, $authUrl);
				//Set HTTP POST Request.
				curl_setopt($ch, CURLOPT_POST, TRUE);
				//Set data to POST in HTTP "POST" Operation.
				curl_setopt($ch, CURLOPT_POSTFIELDS, $paramArr);
				//CURLOPT_RETURNTRANSFER- TRUE to return the transfer as a string of the return value of curl_exec().
				curl_setopt ($ch, CURLOPT_RETURNTRANSFER, TRUE);
				//CURLOPT_SSL_VERIFYPEER- Set FALSE to stop cURL from verifying the peer's certificate.
				curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
				//Execute the  cURL session.
				$strResponse = curl_exec($ch);
				//Get the Error Code returned by Curl.
				$curlErrno = curl_errno($ch);
				if($curlErrno){
					$curlError = curl_error($ch);
					throw new Exception($curlError);
				}
				//Close the Curl Session.
				curl_close($ch);
				//Decode the returned JSON string.
				$objResponse = json_decode($strResponse, true);
				if (isset($objResponse["error"])){
					throw new Exception($objResponse["error_description"]);
				}
				return $objResponse["access_token"];
			} catch (Exception $e) {
				echo "Exception-".$e->getMessage();
			}
		}
	}
	
	Class HTTPTranslator {
		/*
		 * Create and execute the HTTP CURL request.
		 *
		 * @param string $url        HTTP Url.
		 * @param string $authHeader Authorization Header string.
		 * @param string $postData   Data to post.
		 *
		 * @return string.
		 *
		 */
		function curlRequest($url, $authHeader) {
			//Initialize the Curl Session.
			$ch = curl_init();
			//Set the Curl url.
			curl_setopt ($ch, CURLOPT_URL, $url);
			//Set the HTTP HEADER Fields.
			curl_setopt ($ch, CURLOPT_HTTPHEADER, array($authHeader,"Content-Type: text/xml"));
			//CURLOPT_RETURNTRANSFER- TRUE to return the transfer as a string of the return value of curl_exec().
			curl_setopt ($ch, CURLOPT_RETURNTRANSFER, TRUE);
			//CURLOPT_SSL_VERIFYPEER- Set FALSE to stop cURL from verifying the peer's certificate.
			curl_setopt ($ch, CURLOPT_SSL_VERIFYPEER, False);
			//Execute the  cURL session.
			$curlResponse = curl_exec($ch);
			//Get the Error Code returned by Curl.
			$curlErrno = curl_errno($ch);
			if ($curlErrno) {
				$curlError = curl_error($ch);
				throw new Exception($curlError);
			}
			//Close a cURL session.
			curl_close($ch);
			return $curlResponse;
		}
	}

if(isset($_GET['translateAll']) && $_GET['translateAll'] == 1) {
	//edit all with chosen language
	$Gchosen_lang = '';
	$Gchosen_lang_index = -1;
	$Gchosen_slot = 0;
	if (isset($_REQUEST['chosenlang'])) 
		$Gchosen_lang = $_REQUEST['chosenlang'];
	
	if (isset($_REQUEST['chosenslot'])) 
		$Gchosen_slot = $_REQUEST['chosenslot'];
	
	if ($Gchosen_lang == '') {
		echo "<body>Invalid language ... <A href=\"index.php?internalUse=1&type=".$Gtype."\">Please go back</A>";
		echo "</body></html>";
		exit;
	}
	
	if ($Gchosen_slot <= 0) {
		echo "<body>Invalid slot ... <A href=\"index.php?internalUse=1&type=".$Gtype."\">Please go back</A>";
		echo "</body></html>";
		exit;
	}
	
	for ($i=0; $i<$Gnum_lang; $i++) {
		if($Gmsg_files[$i][2] == $Gchosen_lang)
			$Gchosen_lang_index = $i;
	}
	
	if($Gchosen_lang_index < 0) {
		echo "<body>Invalid language ... <A href=\"index.php?internalUse=1&type=".$Gtype."\">Please go back</A>";
		echo "</body></html>";
		exit;
	}
	
}else {
	//edit one key
	$Gedit_key = '';
	if (isset($_REQUEST['editkey'])) {
		$_REQUEST['editkey'] = javaStrSpecialHandling($_REQUEST['editkey'], 1);
		$Gedit_key = $_REQUEST['editkey'];
	}

	if ($Gedit_key == '' || !isset($Gmsgs[0][$Gedit_key])) {
		if(isset($_GET['internalUse']) && $_GET['internalUse'] == 1)
			echo "<body>Invalid key ... <A href=\"index.php?internalUse=1&type=".$Gtype."\">Please go back</A>";
		else
			echo "<body>Invalid key ... <A href=\"index.php?type=".$Gtype."\">Please go back</A>";
		echo "</body></html>";
		exit;
	}
}
?>

<!--<script type="text/javascript" src="http://www.google.com/jsapi"></script>-->
<script type="text/javascript" src="js/jquery/jquery-1.7.2.js"></script>
<script type="text/javascript">

	/*
	google.load("language", "1");
	//	Translate Language (Google Translate API V1)(not available anymore)
	function translate(id, destLang, srcLang, fromId) {
		var inText = document.getElementById('msg' + fromId).value;
		inText = trim(inText);
		if (inText == "") {
			alert("Missing source language for translation");
			return false;
		}
		google.language.translate(inText, srcLang, destLang, function(result) {
			if (!result.error) {
				var container = document.getElementById('msg' + id);
				container.value = result.translation;
			}
		});
	}
	*/
	
    //	Translate Language (Microsoft Translator API V2)
    var currentId = 0;
	
	function translateWord(id, destLang, srcLang, fromId) {
		var inText = document.getElementById('msg' + fromId).value;
		inText = trim(inText);
		if (inText == "") {
			alert("Missing source language for translation");
			return false;
		}
		
		currentId = id;
        var p = {};
		//	Bing App ID, Pls refer to https://hk.ssl.bing.com/webmaster/Developers/AppIds
		p.appid = 'EC54DDB6D591688E05FDB9DB4C448CDA02F509BC';
		p.to = destLang;
		p.from = srcLang;
		p.text = inText;
		$.ajax({
			url: 'http://api.microsofttranslator.com/V2/Ajax.svc/Translate',
			data: p,
			dataType: 'jsonp',
			jsonp: 'oncomplete',
			jsonpCallback: 'ajaxTranslateCallback',
			complete: function(request, status) {
//				alert('complete: '+status);
			},
			success: function(data, status) {
//				alert('success: data-'+data+',status-'+status);
			},
			error: function(request, status, error) {
//				alert('error: status-'+status+',desc-'+error);
			}
		});
	}
		
	function ajaxTranslateCallback(response) {
		var container = document.getElementById('msg' + currentId);
		container.value = response;
	}
	
	function trim(strToTrim) {
		return(strToTrim.replace(/^\s+|\s+$/g, ''));
	}

	function clickSaveButton() {
		document.mainForm.todo.value = 'save';
		document.mainForm.submit();

		return false;
	}
	
	function clickSaveAllButton() {
		document.mainForm.todo.value = 'saveall';
		document.mainForm.submit();

		return false;
	}

	function clickCancelButton() {
		document.mainForm.submit();

		return false;
	}

	function clickDeleteButton() {
		if (!confirm("Confirm to delete this message ?"))
			return false;
		document.mainForm.todo.value = 'delete';
		document.mainForm.action = "index.php";
		document.mainForm.submit();

		return false;
	}

	function clickRenameButton() {
		document.mainForm.action = "msg_rename.php";
		document.mainForm.submit();

		return false;
	}
</script>

<body leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<?php
	if(isset($_GET['internalUse']) && isset($_GET['translateAll']) && $_GET['internalUse'] == 1 && $_GET['translateAll'] == 1) {
?>
		<FORM name=mainForm action="index.php?internalUse=1" method=post target="_self">
		<INPUT type="hidden" name="type" value="<?php echo $Gtype; ?>">
		<INPUT type="hidden" name="todo" value="">
		<INPUT type="hidden" name="targetLanguageIndex" value="<?php echo $Gchosen_lang_index;?>">
		
<?php
		$startIndex = (($Gchosen_slot-1)*400) + 1;
		$endIndex = $Gchosen_slot*400;
		
		$authenticationResult = true;
		try {
			 //Client ID of the application.
			$clientID       = "MicrosoftTranslateForWeb";
			//Client Secret key of the application.
			$clientSecret = "QpWUVMcc78OLk9IjIGXgW13zVFD9injNRMSg8xStqeE=";
			//OAuth Url.
			$authUrl      = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13/";
			//Application Scope Url
			$scopeUrl     = "http://api.microsofttranslator.com";
			//Application grant type
			$grantType    = "client_credentials";

			//Create the AccessTokenAuthentication object.
			$authObj      = new AccessTokenAuthentication();
			//Get the Access token.
			$accessToken  = $authObj->getTokens($grantType, $scopeUrl, $clientID, $clientSecret, $authUrl);
			//Create the authorization Header string.
			$authHeader = "Authorization: Bearer ". $accessToken;
			
			//Set the translate param
			$fromLanguage = $Gmsg_files[$Gchosen_lang_index][3];
			$toLanguage   = $Gmsg_files[$Gchosen_lang_index][2];
			$contentType  = 'text/plain';
			$category     = 'general';
			
		}catch (Exception $e){
			$authenticationResult = false;
			echo "Fail to authenticate the translation tool<br>";
			echo "Exception: " . $e->getMessage() . PHP_EOL;
			return;
		}
		
		if($authenticationResult == true) {
			set_time_limit(3600);	//set the maximum execution time to 1 hr as it may be take long time to translate whole file
			$cnt = 1;
			$translateStrList = array();
?>
			<table cellspacing=2 cellpadding=2>
				<tr>
					<td><INPUT type="button" name="Save" value="Save All Messages" onclick="clickSaveAllButton();"></td>
					<td><INPUT type="button" name="Cancel" value="Cancel" onclick="clickCancelButton();"></td>
				</tr>
			</table>
			
			<p>Range: <?php echo $startIndex;?> ~ <?php echo $endIndex;?></p>
			
			<table cellspacing=2 cellpadding=2 border=1>
<?php
			$messageCnt = 0;
			foreach ($Gmsgs[$Gchosen_lang_index] as $msgKey => $value) {
				if($cnt < $startIndex || $cnt > $endIndex) {
					$cnt++;
					continue;
				}
				
				$translatedStrList[$msgKey] = "";
				 //Set the params.//
				$inputStr = $value;
				
				$params = "text=".urlencode($inputStr)."&to=".$toLanguage."&from=".$fromLanguage;
				$translateUrl = "http://api.microsofttranslator.com/v2/Http.svc/Translate?$params";
			
				//Create the Translator Object.
				$translatorObj = new HTTPTranslator();
			
				//Get the curlResponse.
				$curlResponse = $translatorObj->curlRequest($translateUrl, $authHeader);
			
				//Interprets a string of XML into an object.
				$xmlObj = simplexml_load_string($curlResponse);
				foreach((array)$xmlObj[0] as $val)
					$translatedStr = $val;
				$translatedStrList[$msgKey] = $translatedStr;
				
				echo "<tr class=msg><td bgcolor=#DDDDDD>".$cnt."</td><td bgcolor=#DDDDDD>".$msgKey."</td><td>".showStr($translatedStr)."</td>";
				$cnt++;
				$messageCnt++;
			}
		}
?>
		<INPUT type="hidden" name="editkeyCnt" value='<?php echo $messageCnt;?>'>
<?php
		$tCnt = 1;
		foreach($translatedStrList as $key => $value) {
			echo '<INPUT type="hidden" name="editkey'.$tCnt.'" value="'.$key.'">';
			echo '<INPUT type="hidden" name="editkeyValue'.$tCnt.'" value="'.$value.'">';
			$tCnt++;
		}
?>
		</table>	
<?php
	}else {
?>
	<!-- Edit one key -->
	<?php if(isset($_GET['internalUse']) && $_GET['internalUse'] == 1) {?>
		<FORM name=mainForm action="index.php?internalUse=1#<?php echo $Gedit_key; ?>" method=post target="_self">
	<?php }else {?>
		<FORM name=mainForm action="index.php#<?php echo $Gedit_key; ?>" method=post target="_self">
	<?php }?>
	<INPUT type="hidden" name="type" value="<?php echo $Gtype; ?>">
	<INPUT type="hidden" name="todo" value="">
	<INPUT type="hidden" name="editkey" value="<?php echo $Gedit_key; ?>">

	<table cellspacing=2 cellpadding=2 border=0>
	<tr><td colspan=2 align=center style="font-size:18px"><?php echo $Gmsg_desc; ?></td></tr>
	<tr bgcolor=#AAAAAA><td>&nbsp;Key&nbsp;</td><td>&nbsp;Value&nbsp;</td></tr>
	<tr><td colspan=2 bgcolor=#DDDDDD style="font-size:18px; font-weight:bold">&nbsp;<?php echo $Gedit_key; ?>&nbsp;</td></tr>
	<?php
		for ($i=0; $i<$Gnum_lang; $i++) {
			echo '<tr>';
			echo '<td valign=middle bgcolor=#DDDDDD nowrap>&nbsp;'.$Gmsg_files[$i][1].'&nbsp;</td>';
			echo '<td valign=middle bgcolor=#EEEEEE nowrap><input id="msg'.$i.'" name="msg'.$i.'" style="width:600px;" value="'.showStr($Gmsgs[$i][$Gedit_key]).'"> ';
			if(isset($_GET['internalUse']) && $_GET['internalUse'] == 1)
				echo '<input type="button" value="Translate" onclick="translateWord('.$i.', \''.$Gmsg_files[$i][2].'\', \''.$Gmsg_files[$i][3].'\', '.$Gmsg_files[$i][4].');" />';
			echo '</td>';
			echo '</tr>';
		}
	?>
	</table>
	<hr>
	<input type="button" value=" Save " onclick="clickSaveButton();">&nbsp;&nbsp;
	<!-- <input type="button" value=" Delete " onclick="clickDeleteButton();">&nbsp;&nbsp; -->
	<!-- <input type="button" value=" Rename Key " onclick="clickRenameButton();">&nbsp;&nbsp; -->
	<input type="button" value=" Cancel " onclick="clickCancelButton();">

	</FORM>
<?php }?>
	
</body>
</HTML>

