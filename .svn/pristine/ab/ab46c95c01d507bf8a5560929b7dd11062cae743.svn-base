<?php
include_once("common_include.php");
?>
<!--<script type="text/javascript" src="http://www.google.com/jsapi"></script>-->
<script type="text/javascript" src="js/jquery/jquery-1.7.2.js"></script>
<script type="text/javascript">
	var existKeys = [
		<?php
			$cnt = 0;
			foreach ($Gmsgs[0] as $existkey=>$value) {
				echo "\"".$existkey."\",\n";
				$cnt ++;
			}
		?>
	];
	var existKeysCnt = <?php echo $cnt; ?>;

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

	function clickAddButton() {
		addkey = trim(document.mainForm.newkey.value);
		if (addkey == '') {
			alert("Missing Message Key\nPlease re-enter");
			return false;
		}

		for (i=0; i<existKeysCnt; i++) {
			if (addkey == existKeys[i]) {
				alert("Duplicated Key : " + addkey + "\nPlease re-enter");
				return false;
			}
		}

		for (i=0; i<addkey.length; i++) {
			ch = addkey.charAt(i);
			if (ch == ' ') {
				alert("Not allow spaces in Message Key : " + addkey + "\nPlease re-enter");
				return;
			}
		}

		document.mainForm.todo.value = 'add';
		document.mainForm.action = "index.php#" + document.mainForm.newkey.value;
		document.mainForm.submit();

		return false;
	}

	function clickCancelButton() {
		document.mainForm.submit();

		return false;
	}

	function presetValue(to_element) {
		e = document.mainForm;

		if (e.elements[to_element].value == "" &&
			e.msg0.value != "") {
			e.elements[to_element].value = e.msg0.value;
			e.elements[to_element].select();
			e.elements[to_element].focus();
		}
	}

	function loadPage() {
		document.mainForm.newkey.focus();
	}
</script>

<body leftmargin="10" topmargin="10" marginwidth="10" marginheight="10" onload="loadPage()">
<FORM name=mainForm action="index.php" method=post target="_self">
<INPUT type="hidden" name="type" value="<?php echo $Gtype; ?>">
<INPUT type="hidden" name="todo" value="">

<table cellspacing=2 cellpadding=2 border=0>
<tr><td colspan=2 align=center style="font-size:18px"><?php echo $Gmsg_desc; ?></td></tr>
<tr bgcolor=#AAAAAA><td>&nbsp;Key&nbsp;</td><td>&nbsp;Value&nbsp;</td></tr>
<tr><td valign=middle bgcolor=#DDDDDD>&nbsp;Key&nbsp;</td><td bgcolor=#DDDDDD><input name="newkey" style="width:600px" value=""></td></tr>
<?php
	for ($i=0; $i<$Gnum_lang; $i++) {
		echo '<tr>';
		echo '<td valign=middle bgcolor=#DDDDDD nowrap>&nbsp;'.$Gmsg_files[$i][1].'&nbsp;</td>';
		echo '<td valign=middle bgcolor=#EEEEEE nowrap><input id="msg'.$i.'" name="msg'.$i.'" style="width:600px;" value=""';
		if ($i > 0)
			echo ' onfocus="presetValue(\'msg'.$i.'\')"';
		echo '> ';
		echo '<input type="button" value="Translate" onclick="translateWord('.$i.', \''.$Gmsg_files[$i][2].'\', \''.$Gmsg_files[$i][3].'\', '.$Gmsg_files[$i][4].');" />';
		echo '</td>';
		echo '</tr>';
	}
?>
</table>
<hr>
<input type="button" value=" Save " onclick="clickAddButton();">&nbsp;&nbsp;
<input type="button" value=" Cancel " onclick="clickCancelButton();">

</FORM>
</body>
</HTML>

