<?php
	include_once("common_include.php");
	
	$keySet = $_GET['keys'];
	$bbSet = $_GET['bb'];
	$catSet = $_GET['category'];	
	
	$keys = trim($keySet);
	$keyArray = explode(' ', $keys);
	
	if ($bbSet)
		$category = '[b][u]'.$catSet.'[/u][/b]';
	else
		$category = $catSet;
	
	$section = $category . '<br />';
	
	if (count($keyArray)) {
		foreach ($keyArray as $key) {			
			if (isset($Gmsgs[0][$key])) {			
				$section .= 'Key : ' . $key . '<br />';
				$section .= 'English : ' . showStr($Gmsgs[0][$key]) . '<br />';
				$section .= 'Traditional Chinese : ' . showStr($Gmsgs[1][$key]) . '<br />';
				$section .= 'Simplified Chinese : ' . showStr($Gmsgs[2][$key]) .  '<br /><br />';	
			}
		}
	}

?>
<body>
<?php echo $section; ?>
</body>
</html>