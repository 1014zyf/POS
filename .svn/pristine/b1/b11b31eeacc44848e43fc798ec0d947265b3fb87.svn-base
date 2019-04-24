<?php
	require ('DBControl.php');

	//read setup file
	function readSetup() {
		$setup = array();
		$sSetupFileName = "setup.txt";
		
		if (!file_exists($sSetupFileName))
			return false;
		
		$fSetupFile = fopen($sSetupFileName, "r");
		if($fSetupFile != NULL) {
			while(!feof($fSetupFile)) {
				$setupText = fgets($fSetupFile);
				$tok = strtok($setupText, "=\r\n");
				if($tok !== false) {
					$setupName = $tok;
					$tok = strtok("=\r\n");
					if($tok !== false) {
						$setup[$setupName] = $tok;
					}
				}
			}
		}
		fclose($fSetupFile);
		
		return $setup;
	}
	
	//get file serial number
	function getFileSerialNumber($bday) {
		$iSerialNumber = 0;
		$serialNumberArray = array();
		//$sSerialNumberFileName = substr($bday, 0, 6).".txt";
		$sSerialNumberFileName = "serialNumber.txt";
		
		if (file_exists($sSerialNumberFileName)) {
			$fSerialNumberFile = fopen($sSerialNumberFileName, "r");
			if($fSerialNumberFile != NULL) {
				if (!feof($fSerialNumberFile)) {
					$serialNumberText = fgets($fSerialNumberFile);
					$tok = strtok($serialNumberText, ",\r\n");
					if ($tok !== false) {
						$iSerialNumber = $tok;
					}
				}
				fclose($fSerialNumberFile);
				/*while (!feof($fSerialNumberFile)) {
					$serialNumberText = fgets($fSerialNumberFile);
					$tok = strtok($serialNumberText, ",\r\n");
					if ($tok !== false) {
						$date = $tok;
						$tok = strtok(",\r\n");
						$dateSerialNumber = $tok;
						
						$serialNumberArray[$date] = $dateSerialNumber;
					}
				}*/
			}
		}
		
		/*if(isset($serialNumberArray[$bday])) {
			$iSerialNumber = $serialNumberArray[$bday];
			$serialNumberArray[$bday] = $iSerialNumber+1;
		}else
			$serialNumberArray[$bday] = 1;*/
		
		$fSerialNumberFile = fopen($sSerialNumberFileName, "w");
		/*foreach($serialNumberArray as $date => $dateSerialNumber)
			fputs($fSerialNumberFile, $date.",".$dateSerialNumber."\r\n");*/
		if($iSerialNumber == 999)
			fputs($fSerialNumberFile, "0\r\n");
		else
			fputs($fSerialNumberFile, ($iSerialNumber+1)."\r\n");
		fclose($fSerialNumberFile);
		
		return $iSerialNumber;
	}
	
	//get the check total (not include discount amount and inclusive tax)
	function getSalesAmount(&$database, $posDbName, $outletDbName, $dbIPAddress, $dbLogin, $dbPassword, $bday, $shopCode, $outletCode, &$dSalesAmount) {
		connectDatabase($database, $outletDbName, $dbIPAddress, $dbLogin, $dbPassword);
		//get shop information
		$shopId = 0;
		$shopSQL = "SELECT shop_id FROM ".$outletDbName.".out_shops WHERE shop_code='".$shopCode."' and shop_status=''";
		$shopSQLResult = mysql_query($shopSQL);
		if ($shopRow = mysql_fetch_array($shopSQLResult))
			$shopId = $shopRow['shop_id'];
		
		if ($shopId == 0)
			return "s";
		
		//get outlet information
		$outletId = 0;
		$outletSQL = "SELECT olet_id FROM ".$outletDbName.".out_outlets WHERE olet_shop_id=".$shopId." and olet_code='".$outletCode."' and olet_status=''";
		$outletSQLResult = mysql_query($outletSQL);
		if ($outletRow = mysql_fetch_array($outletSQLResult)) {
			$outletId = $outletRow['olet_id'];
		}
		
		if ($outletId == 0)
			return "o";
		
		connectDatabase($database, $posDbName, $dbIPAddress, $dbLogin, $dbPassword);
		//get corresponding business day record
		$bdayId = 0;
		$bdayDate = substr($bday, 0, 4)."-".substr($bday, 4, 2)."-".substr($bday, 6, 2);
		$bdaySQL = "SELECT bday_id FROM ".$posDbName.".pos_business_days WHERE bday_date='".$bdayDate."' and bday_shop_id=".$shopId." and bday_olet_id=".$outletId." and bday_status<>'d'";
		$bdaySQLResult = mysql_query($bdaySQL);
		if ($bdayRow = mysql_fetch_array($bdaySQLResult)) 
			$bdayId = $bdayRow['bday_id'];
		
		if ($bdayId == 0) {
			$dSalesAmount = 0;
			$dSalesAmount = number_format($dSalesAmount, 2, ".", "");
			return $dSalesAmount;
		}
		
		//get the check information base on bdayId
		$checkSQL = "SELECT SUM(chks_item_total) AS chks_item_total, SUM(chks_sc1) AS chks_sc1, SUM(chks_sc2) AS chks_sc2, SUM(chks_sc3) AS chks_sc3, SUM(chks_sc4) AS chks_sc4, SUM(chks_sc5) AS chks_sc5, SUM(chks_pre_disc) AS chks_pre_disc, SUM(chks_mid_disc) AS chks_mid_disc, SUM(chks_post_disc) AS chks_post_disc, SUM(chks_incl_tax_ref1) AS chks_incl_tax_ref1, SUM(chks_incl_tax_ref2) AS chks_incl_tax_ref2, SUM(chks_incl_tax_ref3) AS chks_incl_tax_ref3, SUM(chks_incl_tax_ref4) AS chks_incl_tax_ref4 FROM ".$posDbName.".pos_checks WHERE chks_bday_id=".$bdayId." and chks_shop_id=".$shopId." and chks_olet_id=".$outletId." and chks_non_revenue='' and chks_status=''";
		$checkSQLResult = mysql_query($checkSQL);
		if ($checkRow = mysql_fetch_array($checkSQLResult)) {
			$dSalesAmount = $checkRow['chks_item_total'] + $checkRow['chks_sc1'] + $checkRow['chks_sc2'] + $checkRow['chks_sc3'] + $checkRow['chks_sc4'] + $checkRow['chks_sc5'] + $checkRow['chks_pre_disc'] + $checkRow['chks_mid_disc'] + $checkRow['chks_post_disc'] - $checkRow['chks_incl_tax_ref1'] - $checkRow['chks_incl_tax_ref2'] - $checkRow['chks_incl_tax_ref3'] - $checkRow['chks_incl_tax_ref4'];
		}
		$dSalesAmount = number_format($dSalesAmount, 2, ".", "");
		
		return $dSalesAmount;
	}
	
	function writeLog($error, $message) {
		$currentMonth = date('m');
		$logFileName = "salesDataGen.".$currentMonth;
		
		$logFile = fopen($logFileName, "a+");
		if($error)
			$logContent = date('Y-m-d H:i:s')." - ERROR - ".$message."\r\n";
		else
			$logContent = date('Y-m-d H:i:s')." - ".$message."\r\n";
		fputs($logFile, $logContent);
		fclose($logFile);
	}
	
	//main program
	$error = 0;
	$database = null;
	
	$setup = readSetup();
	if($setup == false) {
		writeLog(true, "Missing setup file");
		return;
	}
	
	if(!isset($setup['MachineID'])) {
		writeLog(true, "Missing 'MachineID' in setup file");
		$error = 1;
	}else {
		if(strlen($setup['MachineID']) < 7) {
			$len = strlen($setup['MachineID']);
			for($i=($len+1); $i<=7; $i++)
				$setup['MachineID'] = $setup['MachineID']."0";
		}else if(strlen($setup['MachineID']) > 7)
			$error = 1;
	}
	
	if(!isset($setup['ShopCode'])) {
		writeLog(true, "Missing 'ShopCode' in setup file");
		$error = 1;
	}
	
	if(!isset($setup['OutletCode'])) {
		writeLog(true, "Missing 'OutletCode' in setup file");
		$error = 1;
	}
	
	if(!isset($setup['PosDatabaseName'])) {
		writeLog(true, "Missing 'PosDatabaseName' in setup file");
		$error = 1;
	}else if(strcmp($setup['PosDatabaseName'], "") == 0) {
		writeLog(true, "Empty setup for PosDatabaseName");
		$error = 1;
	}
	
	if(!isset($setup['OutletDatabaseName'])) {
		writeLog(true, "Missing 'OutletDatabaseName' in setup file");
		$error = 1;
	}else if(strcmp($setup['OutletDatabaseName'], "") == 0) {
		writeLog(true, "Empty setup for OutletDatabaseName");
		$error = 1;
	}
	
	if(!isset($setup['DBIPAddress'])) {
		writeLog(true, "Missing 'DBIPAddress' in setup file");
		$error = 1;
	}else if(strcmp($setup['DBIPAddress'], "") == 0) {
		writeLog(true, "Empty setup for DBIPAddress");
		$error = 1;
	}
	
	if(!isset($setup['DBLogin'])) {
		writeLog(true, "Missing 'DBLogin' in setup file");
		$error = 1;
	}else if(strcmp($setup['DBLogin'], "") == 0) {
		writeLog(true, "Empty setup for DBLogin");
		$error = 1;
	}
	
	if(!isset($setup['DBPassword'])) {
		writeLog(true, "Missing 'DBPassword' in setup file");
		$error = 1;
	}else if(strcmp($setup['DBPassword'], "") == 0) {
		writeLog(true, "Empty setup for DBPassword");
		$error = 1;
	}
	
	if(!isset($setup['FTPHost'])) {
		writeLog(true, "Missing 'FTPHost' in setup file");
		$error = 1;
	}else if(strcmp($setup['FTPHost'], "") == 0) {
		writeLog(true, "Empty setup for FTPHost");
		$error = 1;
	}
	
	if(!isset($setup['FTPLogin'])) {
		writeLog(true, "Missing 'FTPLogin' in setup file");
		$error = 1;
	}else if(strcmp($setup['FTPLogin'], "") == 0) {
		writeLog(true, "Empty setup for FTPLogin");
		$error = 1;
	}
	
	if(!isset($setup['FTPPassword'])) {
		writeLog(true, "Missing 'FTPPassword' in setup file");
		$error = 1;
	}else if(strcmp($setup['FTPPassword'], "") == 0) {
		writeLog(true, "Empty setup for FTPPassword");
		$error = 1;
	}
	
	$ftpTargetPath = "";
	if(isset($setup['FTPTargetPath'])) 
		$ftpTargetPath = $setup['FTPTargetPath'];
	
	if(!isset($setup['SalesDataOutputFolder'])) {
		writeLog(true, "Missing 'SalesDataOutputFolder' in setup file");
		$error = 1;
	}else if(strcmp($setup['SalesDataOutputFolder'], "") == 0) {
		writeLog(true, "Empty setup for SalesDataOutputFolder");
		$error = 1;
	}else {
		$length = strlen($setup['SalesDataOutputFolder']);
		if(strcmp(substr($setup['SalesDataOutputFolder'], ($length-1), 1), "\\") != 0)
			$setup['SalesDataOutputFolder'] = $setup['SalesDataOutputFolder']."\\";
	}
	
	if(!isset($setup['FileSentFolder'])) {
		writeLog(true, "Missing 'FileSentFolder' in setup file");
		$error = 1;
	}else if(strcmp($setup['FileSentFolder'], "") == 0) {
		writeLog(true, "Empty setup for FileSentFolder");
		$error = 1;
	}else {
		$length = strlen($setup['FileSentFolder']);
		if(strcmp(substr($setup['FileSentFolder'], ($length-1), 1), "\\") != 0)
			$setup['FileSentFolder'] = $setup['FileSentFolder']."\\";
	}
	
	if($error)
		return;
	
	//testing connect of database
	$testingDB = null;
	if (connectDatabase($testingDB, $setup['PosDatabaseName'], $setup['DBIPAddress'], $setup['DBLogin'], $setup['DBPassword']) == false) {
		writeLog(true, mysql_error());
		return;
	}
	$testingDB = null;
	if (connectDatabase($testingDB, $setup['OutletDatabaseName'], $setup['DBIPAddress'], $setup['DBLogin'], $setup['DBPassword']) == false) {
		writeLog(true, mysql_error());
		return;
	}
	
	//get day options value ~ 1:One day, 2:Day Range
	$dayOption = 1;
	if(isset($_POST['dayOptions'])) {
		if(strcmp($_POST['dayOptions'], 'dayRange') == 0)
			$dayOption = 2;
	}
	
	//get business date
	$fromScreen = false;
	$totalDay = 1;
	if($dayOption == 1) {
		$dBusinessDate = "";
		if(!isset($_GET['BusinessDate']) && !isset($_POST['BusinessDate']))
			$dBusinessDate = date('Ymd', mktime(0, 0, 0, date("m")  , date("d")-1, date("Y")));
		else {
			$fromScreen = true;
			if(isset($_POST['BusinessDate']))
				$dBusinessDate = $_POST['BusinessDate'];
			else
				$dBusinessDate = $_GET['BuinessDate'];
			if (strlen($dBusinessDate) != 8)
				echo "Invalid Business Date<br>";
			if (intval(substr($dBusinessDate, 4, 2)) <= 0  || intval(substr($dBusinessDate, 4, 2)) > 13)
				echo "Invalid Business Date<br>";
			if (intval(substr($dBusinessDate, 6, 2)) > 31)
				echo "Invalid Business Date<br>";
		}
	}else {
		$fromScreen = true;
		$beginDate = DateTime::createFromFormat('Ymd', $_POST['BeginBusinessDate']);
		$dateErrors = DateTime::getLastErrors();
		if(!empty($dateErrors) && ($dateErrors['warning_count'] + $dateErrors['error_count'] > 0)) {
			echo "Invalid Begin Business Date<br>";
			return;
		}
		
		$endDate = DateTime::createFromFormat('Ymd', $_POST['EndBusinessDate']);
		$dateErrors = DateTime::getLastErrors();
		if(!empty($dateErrors) && ($dateErrors['warning_count'] + $dateErrors['error_count'] > 0)) {
			echo "Invalid End Business Date<br>";
			return;
		}
		
		$dateInterval = $beginDate->diff($endDate);
		$totalDay = $dateInterval->days + 1;
		$dBusinessDate = $beginDate->format('Ymd');
	}
	
	//loop to generate the sales data file
	$tempDate = null;
	if($dayOption == 2)
		$tempDate = $beginDate;
	for($dateIndex=1; $dateIndex<=$totalDay; $dateIndex++) {
		if($dateIndex > 1) {
			$tempDate->add(new DateInterval('P1D'));
			$dBusinessDate = $tempDate->format('Ymd');
		}
		
		//get file serial number
		$sFileSerialNumber = getFileSerialNumber($dBusinessDate);
		if(strlen($sFileSerialNumber) != 3) {
			for($i=(strlen($sFileSerialNumber) + 1); $i<=3; $i++)
				$sFileSerialNumber = "0".$sFileSerialNumber;
		}
		
		//generate output file name
		$sSalesDataFileName = "D".$setup['MachineID'].".".$sFileSerialNumber;
		
		//get the sales amount
		$dSalesAmount = 0;
		$sSalesAmount = getSalesAmount($database, $setup['PosDatabaseName'], $setup['OutletDatabaseName'], $setup['DBIPAddress'], $setup['DBLogin'], $setup['DBPassword'], $dBusinessDate, $setup['ShopCode'], $setup['OutletCode'], $dSalesAmount);
		if($sSalesAmount == "s") {
			if($fromScreen)
				echo "Wrong shop code<br>";
			writeLog(true, "Wrong shop code");
			return;
		}else if ($sSalesAmount == "o") {
			if($fromScreen)
				echo "Wrong outlet code<br>";
			writeLog(true, "Wrong outlet code");
			return;
		}else if ($sSalesAmount == "b") {
			if($fromScreen)
				echo "No business day record found<br>";
			writeLog(true, "No business day record found");
			return;
		}
		
		//generate outlet file contents
		$sOutputContent = "";
		if(strlen($sSalesAmount) != 11) {
			for($i=(strlen($sSalesAmount) + 1); $i<=11; $i++)
				$sSalesAmount = "0".$sSalesAmount;
		}
		$sOutletContent = "D".$setup['MachineID'].$dBusinessDate.$sSalesAmount;
		
		//check folder existence
		if(!file_exists($setup['SalesDataOutputFolder']))
			mkdir($setup['SalesDataOutputFolder'], 0777);
		
		//generate output file
		$fOutputFile = fopen($setup['SalesDataOutputFolder'].$sSalesDataFileName, "w");
		fputs($fOutputFile, $sOutletContent);
		fclose($fOutputFile);
		writeLog(false, "Output file:".$sSalesDataFileName." has been generated");
	}
	
	/********************************************/
	/***** Transfer output file through FTP *****/
	/********************************************/
	//testing connection of ftp
	$ftpConnection = ftp_connect($setup['FTPHost'], 21, 3);
	if (!$ftpConnection) {
		writeLog(true, "Cannot connect to FTP server:".$setup['FTPHost']);
		return;
	}
	ftp_close($ftpConnection);
	
	//check folder existence
	if(!file_exists($setup['FileSentFolder']))
		mkdir($setup['FileSentFolder']);
	
	$ftpConnection = ftp_connect($setup['FTPHost'], 21, 15);
	if ($ftpConnection) {
		writeLog(false, "Connect to FTP server:".$setup['FTPHost']);
		if (ftp_login($ftpConnection, $setup['FTPLogin'], $setup['FTPPassword']) == FALSE) {
			if($fromScreen)
				echo "Fail to login to FTP Server";
			writeLog(true, "Fail to login to FTP Server");
			ftp_close($ftpConnection);
			return;
		}
		
		//ftp_pasv($ftpConnection, true);
		$currentFTPPath = ftp_pwd($ftpConnection);
		if (strcmp($ftpTargetPath, "") != 0)
			ftp_chdir($ftpConnection, $ftpTargetPath);
	
	
		$files = scandir($setup['SalesDataOutputFolder']);
		foreach($files as $fileName) {
			if(strcmp($fileName, ".") == 0 || strcmp($fileName, "..") == 0)
				continue;
			
			//if(ftp_put($ftpConnection, $setup['SalesDataOutputFolder'].$sSalesDataFileName, $sSalesDataFileName, FTP_BINARY)) {
			if(ftp_put($ftpConnection, $fileName, $setup['SalesDataOutputFolder'].$fileName, FTP_BINARY)) {
				if($fromScreen)
					echo "Succcess to upload file to FTP server<br>";
				writeLog(false, "Success to upload file(".$fileName.") to FTP server");
				if(file_exists($setup['FileSentFolder'].$fileName))
					unlink($setup['FileSentFolder'].$fileName);
				rename($setup['SalesDataOutputFolder'].$fileName, $setup['FileSentFolder'].$fileName);
			}else {
				if($fromScreen)
					echo "Fail to upload file to FTP server<br>";
				writeLog(true, "Fail to update file(".$fileName.") to FTP server");
			}
		}
		
		ftp_close($ftpConnection);
		if($fromScreen)
			echo "<a href='salesData.php'>Back</a>";
	}else {
		if($fromScreen)
			echo "<a href='salesData.php'>Back</a>";
		writeLog(true, "Cannot connect to FTP server:".$setup['FTPHost']);
		return;
	}
	
	/*$ftpConnection = ftp_connect($setup['FTPHost'], 21, 15);
	if ($ftpConnection) {
		writeLog(false, "Connect to FTP server:".$setup['FTPHost']);
		if (ftp_login($ftpConnection, $setup['FTPLogin'], $setup['FTPPassword']) == FALSE) {
			if($fromScreen)
				echo "Fail to login to FTP Server";
			writeLog(true, "Fail to login to FTP Server");
			ftp_close($ftpConnection);
			return;
		}
		
		ftp_pasv($ftpConnection, true);
		$currentFTPPath = ftp_pwd($ftpConnection);
		if (strcmp($ftpTargetPath, "") != 0)
			ftp_chdir($ftpConnection, $ftpTargetPath);
		if (ftp_put($ftpConnection, $sSalesDataFileName, $sSalesDataFileName, FTP_BINARY)) {
			if($fromScreen)
				echo "Succcess to upload file to FTP server<br>";
			writeLog(false, "Success to upload file to FTP server");
		}else {
			if($fromScreen)
				echo "Fail to upload file to FTP server<br>";
			writeLog(true, "Fail to update file to FTP server");
		}
		
		ftp_close($ftpConnection);
		
		if($fromScreen)
			echo "<a href='salesData.php'>Back</a>";
	}else {
		if($fromScreen)
			echo "<a href='salesData.php'>Back</a>";
		writeLog(true, "Cannot connect to FTP server:".$setup['FTPHost']);
		return;
	}*/
	
?>