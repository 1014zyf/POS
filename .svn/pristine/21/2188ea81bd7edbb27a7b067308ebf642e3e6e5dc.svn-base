<?php
include_once("common_include.php");

$Gedit_key = '';
if (isset($_REQUEST['editkey']))
	$Gedit_key = $_REQUEST['editkey'];

if ($Gedit_key == '' || !isset($Gmsgs[0][$Gedit_key])) {
	echo "<body>Invalid key ... <A href=\"index.php?type=".$Gtype."\">Please go back</A>";
	echo "</body></html>";
	exit;
}	
?>

<SCRIPT LANGUAGE="Javascript">
<!--
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


function trim(strToTrim)
{
	return(strToTrim.replace(/^\s+|\s+$/g, ''));
}

function clickRenameButton()
{
	renamekey = trim(document.mainForm.newkey.value);
	if (renamekey == '') {
		alert("Missing Message Key\nPlease re-enter");
		return false;
	}

	for (i=0; i<existKeysCnt; i++) {
		if (renamekey == existKeys[i]) {
			alert("Duplicated Key : " + renamekey + "\nPlease re-enter");
			return false;
		}
	}

	document.mainForm.todo.value = 'rename';
	document.mainForm.action = "index.php#" + document.mainForm.newkey.value;
	document.mainForm.submit();

	return false;
}

function clickCancelButton()
{
	document.mainForm.submit();

	return false;
}

function loadPage()
{
	document.mainForm.newkey.focus();
}
//-->
</SCRIPT>

<body leftmargin="10" topmargin="10" marginwidth="10" marginheight="10" onload="loadPage()">
<FORM name=mainForm action="index.php" method=post target="_self">
<INPUT type="hidden" name="type" value="<?php echo $Gtype; ?>">
<INPUT type="hidden" name="todo" value="">
<INPUT type="hidden" name="editkey" value="<?php echo $Gedit_key; ?>">

<table cellspacing=2 cellpadding=2 border=0>
<tr><td colspan=2 align=center style="font-size:18px"><?php echo $Gmsg_desc; ?></td></tr>
<tr bgcolor=#AAAAAA><td>&nbsp;Key&nbsp;</td><td>&nbsp;Value&nbsp;</td></tr>
<tr><td valign=middle bgcolor=#DDDDDD>&nbsp;Original Key&nbsp;</td><td bgcolor=#DDDDDD style="font-size:18px; font-weight:bold"><?php echo $Gedit_key; ?></td></tr>
<tr><td valign=middle bgcolor=#DDDDDD>&nbsp;Rename Key&nbsp;</td><td bgcolor=#DDDDDD><input name="newkey" style="width:600px" value="<?php echo $Gedit_key; ?>"></td></tr>

</table>
<hr>
<input type="button" value=" Save " onclick="clickRenameButton();">&nbsp;&nbsp;
<input type="button" value=" Cancel " onclick="clickCancelButton();">

</FORM>
</body>
</HTML>

