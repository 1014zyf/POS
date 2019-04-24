<?php
include_once("common_include.php");

$todo = '';
if (isset($_REQUEST['todo']))
	$todo = trim($_REQUEST['todo']);
if ($todo == 'add') {
	while (1) {
		$new_key = '';
		if (isset($_REQUEST['newkey']))
			$new_key = trim($_REQUEST['newkey']);
		if ($new_key == '' || isset($Gmsgs[0][$new_key])) 
			break;

		for ($i=0; $i<$Gnum_lang; $i++) {
			if (!isset($_REQUEST['msg'.$i])) 
				continue;
			$Gmsgs[$i][$new_key] = stripslashes(trim($_REQUEST['msg'.$i]));
			ksort($Gmsgs[$i]);
			writeMsgFile($i);
		}

		break;
	}
}
else if ($todo == 'delete') {
	while (1) {
		$edit_key = '';
		if (isset($_REQUEST['editkey']))
			$edit_key = trim($_REQUEST['editkey']);
		if ($edit_key == '' || !isset($Gmsgs[0][$edit_key])) 
			break;

		for ($i=0; $i<$Gnum_lang; $i++) {
			unset($Gmsgs[$i][$edit_key]);
			ksort($Gmsgs[$i]);
			writeMsgFile($i);
		}

		break;
	}
}
else if ($todo == 'save') {
	while (1) {
		$edit_key = '';
		if (isset($_REQUEST['editkey']))
			$edit_key = $_REQUEST['editkey'];
		if ($edit_key == '' || !isset($Gmsgs[0][$edit_key])) 
			break;

		for ($i=0; $i<$Gnum_lang; $i++) {
			if (!isset($_REQUEST['msg'.$i])) 
				continue;
			$Gmsgs[$i][$edit_key] = stripslashes($_REQUEST['msg'.$i]);
			writeMsgFile($i);
		}

		break;
	}
}
else if ($todo == 'saveall') {
	if(isset($_REQUEST['targetLanguageIndex']) && isset($_REQUEST['editkeyCnt']) && $_REQUEST['editkeyCnt'] > 0) {
		$languageIndex = $_REQUEST['targetLanguageIndex'];
		for($i=1; $i<=$_REQUEST['editkeyCnt']; $i++) {
			if(isset($_REQUEST['editkey'.$i]) && isset($_REQUEST['editkeyValue'.$i])) {
				$edit_key = $_REQUEST['editkey'.$i];
				$edit_value = $_REQUEST['editkeyValue'.$i];
				if(isset($Gmsgs[$languageIndex][$edit_key]))
					$Gmsgs[$languageIndex][$edit_key] = stripslashes($edit_value);
			}
		}
		
		writeMsgFile($languageIndex);
	}
}
else if ($todo == 'rename') {
	while (1) {
		$edit_key = '';
		if (isset($_REQUEST['editkey']))
			$edit_key = trim($_REQUEST['editkey']);
		if ($edit_key == '' || !isset($Gmsgs[0][$edit_key])) 
			break;
			
		$new_key = '';
		if (isset($_REQUEST['newkey']))
			$new_key = trim($_REQUEST['newkey']);
		if ($new_key == '' || isset($Gmsgs[0][$new_key])) 
			break;
		
		for ($i=0; $i<$Gnum_lang; $i++) {
			$Gmsgs[$i][$new_key] = $Gmsgs[$i][$edit_key];
			unset($Gmsgs[$i][$edit_key]);
			ksort($Gmsgs[$i]);
			writeMsgFile($i);
		}
		
		break;
	}
}

?>

<SCRIPT LANGUAGE="Javascript">
<!--
var internalUse = <?php if(isset($_GET['internalUse']) && $_GET['internalUse'] == 1) echo 1; else echo 0;?>;

function clickEditRecord(keyvalue)
{
	document.mainForm.editkey.value = keyvalue;
	if(internalUse == 1)
		document.mainForm.action = "msg_edit.php?internalUse=1";
	else
		document.mainForm.action = "msg_edit.php";
	document.mainForm.submit();

	return false;
}

function clickEditAllRecord()
{
	var language = document.getElementById("language");
	var chosenLang = language.options[language.selectedIndex].value;
	var slot = document.getElementById("slot");
	var chosenSlot = slot.options[slot.selectedIndex].value;
	document.mainForm.chosenlang.value = chosenLang;
	document.mainForm.chosenslot.value = chosenSlot;
	document.mainForm.action = "msg_edit.php?internalUse=1&translateAll=1";
	document.mainForm.submit();
	
	return false;
}

function clickAddRecord()
{
	document.mainForm.action = "msg_add.php";
	document.mainForm.submit();

	return false;
}

function checkUnusedRecord()
{
	document.mainForm.action = "check_translation.php";
	document.mainForm.submit();

	return false;
}

function gererateRecord() {
	var keySet = document.getElementById('keys').value;
	var bbObj = document.getElementById('bbcode');
	var catSet = document.getElementById('category').value;
	
	var bbSet = 1;	
	if (!bbObj.checked)
		bbSet = 0; 
	
	if (keySet != '')	
		location.href = 'format.php?keys='+escape(keySet)+'&bb='+bbSet+'&category='+catSet+'&type=default';	
	else  
		document.getElementById('keys').focus();
}

function clickChangeType()
{
	document.mainForm.type.value = mainForm.select_type.value;
	document.mainForm.submit();

	return false;
}
//-->
</SCRIPT>

<body leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<FORM name=mainForm action="index.php" method=post target="_self">
<INPUT type="hidden" name="type" value="<?php echo $Gtype?>">
<INPUT type="hidden" name="todo" value="">
<INPUT type="hidden" name="editkey" value="">
<INPUT type="hidden" name="chosenlang" value="">
<INPUT type="hidden" name="chosenslot" value="">

<?php if(isset($_GET['internalUse']) && $_GET['internalUse'] == 1) {?>
	<table cellspacing=2 cellpadding=2 style="border: 3px dotted;border-color:#FF0000;">
		<tr><td colspan=3>Special Operation</td></tr>
		<tr><td>Please Chose Language:</td><td>Please chosen the range of language file:</td><td></td></tr>
		<tr>
			<td>
				<select id="language">
				<?php for ($i=0; $i<$Gnum_lang; $i++) {?><option value="<?php echo $Gmsg_files[$i][2];?>"><?php echo $Gmsg_files[$i][1];?></option><?php }?>
				</select>
			</td>
			<td>
				<?php 
					$msgCount = count($Gmsgs[0]);
					$slot = ceil($msgCount/400);
				?>
				<select id="slot">
				<?php for ($i=1; $i<=$slot; $i++) {?><option value="<?php echo $i;?>"><?php echo (($i-1)*400+1)."~".($i*400);?></option><?php }?>
				</select>
			</td>
			<td><INPUT type="button" name="Translate all messages" onclick="clickEditAllRecord();" value="Translate All Messages"></td>
		</tr>
	</table>
<?php }?>

<table cellspacing=2 cellpadding=2 border=1>
<?php
	$cnt=1;
	echo "<tr bgcolor=#AAAAAA><td>&nbsp;</td><td>&nbsp;</td><td>Key</td>";
	for ($i=0; $i<$Gnum_lang; $i++) {
		echo "<td>".$Gmsg_files[$i][1]."</td>";
	}
	echo "</tr>\n";
	
	foreach ($Gmsgs[0] as $editkey => $value) {
		$bgColor = "#DDDDDD";
		
		if (strcmp($editkey, "") != 0 && strcmp($value, "") == 0)
			$bgColor = "#FF3333";
		
		echo "<tr class=msg><A name='".$editkey."'></A><td bgcolor=#AAAAAA><A href='javascript:void(null);' onclick='clickEditRecord(\"".javaStrSpecialHandling($editkey, 0)."\")'><img src='images/button_edit.gif' width=12 height=13 border=0></A></td><td bgcolor=#AAAAAA>".$cnt."</td><td bgcolor=".$bgColor.">".$editkey."</td><td>".showStr($value)."</td>";
		
		for ($i=1; $i<$Gnum_lang; $i++) {
			echo "<td>".showStr($Gmsgs[$i][$editkey])."</td>";
		}
		echo "</tr>\n";

		$cnt++;
	}
?>
</table>

</FORM>
</body>
</html>

