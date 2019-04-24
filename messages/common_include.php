<?php
if(isset($_GET['internalUse']) && $_GET['internalUse'] == 1)
	include_once("config_internal.php");
else
	include_once("config.php");

header("Cache-Control: no-cache, must-revalidate, post-check=0, pre-check=0");
header("Pragma: no-cache");
header("Expires: 0");

/////////////////////////////////////////////////////////////////
// Get the type of the message files
reset($Gall_msgs);
$arr = each($Gall_msgs);
$Gtype = $arr['key'];
if (isset($_REQUEST['type'])) 
	$Gtype = trim($_REQUEST['type']);

if ($Gtype == '' || !isset($Gall_msgs[$Gtype])) {
	echo "Invalid format<BR>";
	exit;
}

/////////////////////////////////////////////////////////////////
//	Load the the message files into memory
$Gmsg_desc = $Gall_msgs[$Gtype]['desc'];
$Gmsg_path = $Gall_msgs[$Gtype]['path'];
$Gmsg_files = $Gall_msgs[$Gtype]['files'];

$Gmsgs = array();
$Gnum_lang = count($Gmsg_files);

for ($i=0; $i<$Gnum_lang; $i++) {
	$type = 0;
	$translations = array();
	$translationKey = "";
	
	$fp = fopen($Gmsg_path.$Gmsg_files[$i][0], "r");
	while (!feof($fp)) {
		
		$line = trim(fgets($fp, 1024));
		if ($line == "" || $line[0] == "#") {
			continue;
		}

		if (preg_match("/msgid[[:space:]]+\"(.+)\"$/i", $line, $regs)) {
			$type = 1;
			$translationKey = stripcslashes($regs[1]);
		} 
		elseif (preg_match("/msgid[[:space:]]+\"\"$/i", $line, $regs)) {
			$type = 2;
			$translationKey = "";
		}
		elseif (preg_match("/^\"(.*)\"$/i", $line, $regs) && ($type == 1 || $type == 2 || $type == 3)) {
			$type = 3;
			$translationKey .= stripcslashes($regs[1]);
		}		
		elseif (preg_match("/msgstr[[:space:]]+\"(.+)\"$/i", $line, $regs) && ($type == 1 || $type == 3) && $translationKey) {
			$type = 4;
			$translations[$translationKey] = stripcslashes($regs[1]);
		}
		elseif (preg_match("/msgstr[[:space:]]+\"\"$/i", $line, $regs) && ($type == 1 || $type == 3) && $translationKey) {
			$type = 4;
			$translations[$translationKey] = "";
		}	
		elseif (preg_match("/^\"(.*)\"$/i", $line, $regs) && $type == 4 && $translationKey) {
			$translations[$translationKey] .= stripcslashes($regs[1]);
		}	
		else {
			unset($translations[$translationKey]);
			$type = 0;
			$translationKey = "";
		}

    }
       
    $Gmsgs[$i] = $translations;
    ksort($Gmsgs[$i]);
	unset($translations);

    fclose($fp);
}

////////////////////////////////////////////////////////////////
//	Function to protect string
function showStr($text)
{
    $text = htmlspecialchars($text, ENT_QUOTES);
    return $text;
}

////////////////////////////////////////////////////////////////
//	Function to write the message
function writeMsgFile($idx)
{
	global $Gmsg_path, $Gmsg_files, $Gmsgs;

	$fd = fopen($Gmsg_path.$Gmsg_files[$idx][0], 'w+');
	if (!$fd) 
		return;

	fwrite($fd, "msgid \"\"\r\n");
	fwrite($fd, "msgstr \"\"\r\n");
	fwrite($fd, "\"Project-Id-Version: PACKAGE VERSION\\n\"\r\n");
	fwrite($fd, "\"Report-Msgid-Bugs-To: \\n\"\r\n");
	fwrite($fd, "\"POT-Creation-Date: 2013-04-09 14:53+0800\\n\"\r\n");
	fwrite($fd, "\"PO-Revision-Date: YEAR-MO-DA HO:MI+ZONE\\n\"\r\n");
	fwrite($fd, "\"Last-Translator: FULL NAME <EMAIL@ADDRESS>\\n\"\r\n");
	fwrite($fd, "\"Language-Team: LANGUAGE <LL@li.org>\\n\"\r\n");
	fwrite($fd, "\"MIME-Version: 1.0\\n\"\r\n");
	fwrite($fd, "\"Content-Type: text/plain; charset=utf-8\\n\"\r\n");
	fwrite($fd, "\"Content-Transfer-Encoding: 8bit\\n\"\r\n");
		
	foreach ($Gmsgs[$idx] as $editkey=>$value) {
		$value = str_replace("\\", "\\\\", $value); 
		$value = str_replace("\"", "\\\"", $value);
		fwrite($fd, "msgid\t\"".$editkey."\"\r\n");
		fwrite($fd, "msgstr\t\"".$value."\"\r\n");
	}
	fclose($fd);
}

////////////////////////////////////////////////////////////////
//	Function to write the message
function javaStrSpecialHandling($string, $replaceBack)
{
	if (!$replaceBack)
		$returnString = str_replace("'", "<SINGLEQUOTATION>", $string);
	else
		$returnString = str_replace("<SINGLEQUOTATION>", "'", $string);
	
	return $returnString;
}

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Gourmate Multi-Language Message File</title>
<meta http-equiv="Content-Language" content="zh-us">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../css/cake.webtms.css" />
</head>

<STYLE REL="stylesheet" TYPE="text/css">
TR.msg {
	text-align : left;
	vertical-align : top;
	FONT-SIZE: 12px;
	COLOR : #000000;
}
</STYLE>
