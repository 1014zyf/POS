<?php
	include_once("common_include.php");
	
	set_time_limit(0);

	define('TEMP_FILENAME', 'tmpMergeFile.txt');
	
	$todo = "";
	if (isset($_GET['todo']))
		$todo = trim($_GET['todo']);
?>

<SCRIPT LANGUAGE="Javascript">
<!--
function goBack()
{
	location.href = 'index.php';	

	return false;
}

function deleteUnusedRecord()
{
	location.href = 'check_translation.php?todo=delete';	

	return false;
}
//-->
</SCRIPT>

<?php
	function listdir($dir='.') {
		if (!is_dir($dir)) {
			return false;
		}
	   
		$files = array();
		listdiraux($dir, $files);

		return $files;
	}

	function listdiraux($dir, &$files) {
		$handle = opendir($dir);
		while (($file = readdir($handle)) !== false) {
			if ($file == '.' || $file == '..') {
				continue;
			}
			$filepath = $dir == '.' ? $file : $dir . '/' . $file;
			if (is_link($filepath))
				continue;
			if (is_file($filepath))
				$files[] = $filepath;
			else if (is_dir($filepath))
				listdiraux($filepath, $files);
		}
		closedir($handle);
	}

	function loadFile($sFilename/*, $sCharset = 'UTF-8'*/)
	{
		if (floatval(phpversion()) >= 4.3) {
			$sData = file_get_contents($sFilename);
		} else {
			if (!file_exists($sFilename)) return -3;
			$rHandle = fopen($sFilename, 'r');
			if (!$rHandle) return -2;

			$sData = '';
			while(!feof($rHandle))
				$sData .= fread($rHandle, filesize($sFilename));
			fclose($rHandle);
		}
		if (mb_detect_encoding($sData, "UTF-8") != "UTF-8")
			$sData = utf8_encode($sData);
		return $sData;
	}

	//	Merge all *.php, *.ctp file
	//	Write to TEMP_FILE for fast concat
	$fh = fopen(TEMP_FILENAME, 'w') or die("can't open file");

	foreach($Gall_msgs[$Gtype]['checkDirs'] as $checkDir) {
		$files = listdir($checkDir);
		//sort($files, SORT_LOCALE_STRING);
		foreach ($files as $f) {
			//echo  $f, "<br />";
			if (preg_match('/.php$/i', $f) || preg_match('/.ctp$/i', $f)) {
				fwrite($fh, loadFile($f));
			}
		}
	}
	fclose($fh);

	
	//	Read TEMP_FILE
	$mergeFile = loadFile(TEMP_FILENAME);

	//	Remove TEMP_FILE
	unlink(TEMP_FILENAME);
	
	//	Extract all string encapsulated by quot
	$regex_pattern = "/['\"]([a-zA-Z_]*)['\"]/";
	preg_match_all($regex_pattern, $mergeFile, $matches);
	
	$allMatches = array();
	foreach ($matches as $match) {
		$allMatches = array_merge($allMatches, $match);
	}
	
	//	Trim off quot sign
	for ($i=0; $i<count($allMatches); $i++) {
		$allMatches[$i] = str_replace(array('"', "'"), array("", ""), $allMatches[$i]);
	}
	
	//	Remove duplicates and empty
	//	Output: 	$langKeysInUse	-	All string in all *.php, *.ctp 
	$allMatches = array_unique($allMatches);
	$langKeysInUse = array();
	foreach ($allMatches as $allMatch) {
		if (!empty($allMatch)) {
			$langKeysInUse[] = $allMatch;
		}
	}
	
	//	Read the 1st language files
	$langFile = loadFile($Gmsg_path.$Gmsg_files[0][0]);
	
	//	Extract existing language key
	$regex_pattern = "/msgid	\"([a-zA-Z_]*)\"/";
	preg_match_all($regex_pattern, $langFile, $langKeys);
	for ($i=0; $i<count($langKeys); $i++) {
		$langKeys[$i] = str_replace(array("msgid	", "\""), array("", ""), $langKeys[$i]);
	}
	$langKeys = $langKeys[0];
	
	//	Find unuse language key
	$uselessLangKeys = array();
	foreach ($langKeys as $langKey) {
		if (!in_array($langKey, $langKeysInUse)) {
			$uselessLangKeys[] = $langKey;
		}
	}
	
	echo '<pre>';
	foreach ($uselessLangKeys as $uselessLangKey) {
		echo $uselessLangKey."\r\n";
		if ($todo == 'delete') {
			for ($i=0; $i<$Gnum_lang; $i++) {
				unset($Gmsgs[$i][$uselessLangKey]);
				ksort($Gmsgs[$i]);
				writeMsgFile($i);
				echo "**** Deleted!!!! ****\r\n\r\n";
			}
		}
	}
	echo '</pre>';
	
	if (empty($uselessLangKeys))
		echo '<div style="color:#dd0000;padding:10px;">** No Unused Message! **</div>';
		
	echo '<input type="button" value=" Back " onclick="goBack();">';
	
	if (!empty($uselessLangKeys) && $todo != 'delete')
		echo '&nbsp;<input type="button" value=" Delete All Unused Message(s) " onclick="deleteUnusedRecord();">';
?>