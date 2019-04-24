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
	
	//generate sales amount
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
		disconnectDatabase($database);
		
		connectDatabase($database, $posDbName, $dbIPAddress, $dbLogin, $dbPassword);
		for($i=0; $i<24; $i++) {
			$dSalesAmount[$i]['record_count'] = 0;
			$dSalesAmount[$i]['sales_amount'] = 0;
			$dSalesAmount[$i]['gst'] = 0;
		}
		
		//get corresponding business day record
		$bdayId = 0;
		$bdayDate = $bday->format('Ymd');
		$bdaySQL = "SELECT bday_id, bday_start_loctime, bday_end_loctime FROM ".$posDbName.".pos_business_days WHERE bday_date='".$bdayDate."' and bday_shop_id=".$shopId." and bday_olet_id=".$outletId." and bday_status<>'d'";
		$bdaySQLResult = mysql_query($bdaySQL);
		if($bdayRow = mysql_fetch_array($bdaySQLResult)) {
			$bdayId = $bdayRow['bday_id'];
			if($bdayRow['bday_start_loctime'] != null)
				$bdayCreateDay = DateTime::createFromFormat('Y-m-d H:i:s', substr($bdayRow['bday_start_loctime'], 0, 13).":00:00");
			if($bdayRow['bday_end_loctime'] != null)
				$bdayCloseDay = DateTime::createFromFormat('Y-m-d H:i:s', substr($bdayRow['bday_end_loctime'], 0, 13).":00:00");
			else
				$bdayCloseDay = DateTime::createFromFormat('Y-m-d H:i:s', date("Y-m-d")." 23:59:59");
		}else {
			//Set the number format to 2 d.p.
			for($i=0; $i<24; $i++) {
				$dSalesAmount[$i]['sales_amount'] = number_format($dSalesAmount[$i]['sales_amount'], 2, ".", "");
				$dSalesAmount[$i]['gst'] = number_format($dSalesAmount[$i]['gst'], 2, ".", "");
			}
			return;
		}
		
		$bdayDate = $bdayCreateDay;
		$dateInterval = $bdayCreateDay->diff($bdayCloseDay);
		$totalHour = $dateInterval->h + ($dateInterval->days * 24) + 1;
		/*
		if($bdayRow = mysql_fetch_array($bdaySQLResult)) {
			$bdayId = $bdayRow['bday_id'];
			if($bdayRow['bday_start_loctime'] != null) {
				$bdayDate = DateTime::createFromFormat('Y-m-d H:i:s', substr($bdayRow['bday_start_loctime'], 0, 10)." 00:00:00");
			}else {
				$bdayDate = DateTime::createFromFormat('Y-m-d H:i:s', $bday->format('Y-m-d')." 00:00:00");
			}
		}else {
			//Set the number format to 2 d.p.
			for($i=0; $i<24; $i++) {
				$dSalesAmount[$i]['sales_amount'] = number_format($dSalesAmount[$i]['sales_amount'], 2, ".", "");
				$dSalesAmount[$i]['gst'] = number_format($dSalesAmount[$i]['gst'], 2, ".", "");
			}
			return;
		}
		*/
		
		//Get the sales amount by hour
		for($hour=0; $hour<$totalHour; $hour++) {
		//for($hour=0; $hour<24; $hour++) {
			if($hour > 0)
				$bdayDate->add(new DateInterval('PT1S'));
			$startTime = $bdayDate->format('Y-m-d H:i:s');
			
			$bdayDate->add(new DateInterval('PT59M59S'));
			$endTime = $bdayDate->format('Y-m-d H:i:s');
			
			//$checkSQL = "SELECT COUNT(chks_id) AS record_count, SUM(chks_item_total) AS chks_item_total, SUM(chks_sc1) AS chks_sc1, SUM(chks_sc2) AS chks_sc2, SUM(chks_sc3) AS chks_sc3, SUM(chks_sc4) AS chks_sc4, SUM(chks_sc5) AS chks_sc5, SUM(chks_pre_disc) AS chks_pre_disc, SUM(chks_mid_disc) AS chks_mid_disc, SUM(chks_post_disc) AS chks_post_disc, SUM(chks_incl_tax_ref1) AS chks_incl_tax_ref1, SUM(chks_incl_tax_ref2) AS chks_incl_tax_ref2, SUM(chks_incl_tax_ref3) AS chks_incl_tax_ref3, SUM(chks_incl_tax_ref4) AS chks_incl_tax_ref4, SUM(chks_round_amount) AS chks_round_amount FROM ".$posDbName.".pos_checks WHERE chks_bday_id=".$bdayId." and chks_shop_id=".$shopId." and chks_olet_id=".$outletId." and chks_non_revenue='' and chks_close_loctime >= '".$startTime."' and chks_close_loctime <= '".$endTime."' and chks_status=''";
			$checkSQL = "SELECT COUNT(chks_id) AS record_count, SUM(chks_check_total) AS chks_check_total, SUM(chks_incl_tax_ref1) AS chks_incl_tax_ref1, SUM(chks_incl_tax_ref2) AS chks_incl_tax_ref2, SUM(chks_incl_tax_ref3) AS chks_incl_tax_ref3, SUM(chks_incl_tax_ref4) AS chks_incl_tax_ref4 FROM ".$posDbName.".pos_checks WHERE chks_bday_id=".$bdayId." and chks_shop_id=".$shopId." and chks_olet_id=".$outletId." and chks_non_revenue='' and chks_close_loctime >= '".$startTime."' and chks_close_loctime <= '".$endTime."' and chks_status=''";
			$checkSQLResult = mysql_query($checkSQL);
			if($checkRow = mysql_fetch_array($checkSQLResult)) {
				//$salesAmount = $checkRow['chks_item_total'] + $checkRow['chks_sc1'] + $checkRow['chks_sc2'] + $checkRow['chks_sc3'] + $checkRow['chks_sc4'] + $checkRow['chks_sc5'] + $checkRow['chks_pre_disc'] + $checkRow['chks_mid_disc'] + $checkRow['chks_post_disc'] + $checkRow['chks_round_amount'];
				$salesAmount = $checkRow['chks_check_total'];
				$gst = $checkRow['chks_incl_tax_ref1'] + $checkRow['chks_incl_tax_ref2'] + $checkRow['chks_incl_tax_ref3'] + $checkRow['chks_incl_tax_ref4'];
				
				$dSalesAmount[intval($bdayDate->format('H'))]['record_count'] += $checkRow['record_count'];
				$dSalesAmount[intval($bdayDate->format('H'))]['sales_amount'] += $salesAmount;
				$dSalesAmount[intval($bdayDate->format('H'))]['gst'] += $gst;
			}
		}
		
		//Set the number format to 2 d.p.
		for($i=0; $i<24; $i++) {
			$dSalesAmount[$i]['sales_amount'] = number_format($dSalesAmount[$i]['sales_amount'], 2, ".", "");
			$dSalesAmount[$i]['gst'] = number_format($dSalesAmount[$i]['gst'], 2, ".", "");
		}
		
		disconnectDatabase($database);
	}
	
	//write log
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
	if($setup == false ) {
		writeLog(true, "Missing setup file");
		return false;
	}
	
	if(!isset($setup['ClientID'])) {
		writeLog(true, "Missing 'ClientID' in setup file");
		$error = 1;
	}
	
	if(!isset($setup['ShopCode'])) {
		writeLog(true, "Missing 'ShopCode' in setup file");
		$error = 1;
	}else if(empty($setup['ShopCode'])) {
		writeLog(true, "Empty setup for 'ShopCode'");
		$error = 1;
	}
	
	if(!isset($setup['OutletCode'])) {
		writeLog(true, "Missing 'OutletCode' in setup file");
		$error = 1;
	}else if(empty($setup['OutletCode'])) {
		writeLog(true, "Empty setup for 'OutletCode'");
		$error = 1;
	}
	
	if(!isset($setup['PosDatabaseName'])) {
		writeLog(true, "Missing 'PosDatabaseName' in setup file");
		$error = 1;
	}else if(empty($setup['PosDatabaseName'])) {
		writeLog(true, "Empty setup for 'ShopCode'");
		$error = 1;
	}
	
	if(!isset($setup['OutletDatabaseName'])) {
		writeLog(true, "Missing 'OutletDatabaseName' in setup file");
		$error = 1;
	}else if(empty($setup['OutletDatabaseName'])) {
		writeLog(true, "Empty setup for 'OutletDatabaseName'");
		$error = 1;
	}
	
	if(!isset($setup['DBIPAddress'])) {
		writeLog(true, "Missing 'DBIPAddress' in setup file");
		$error = 1;
	}else if(empty($setup['DBIPAddress'])) {
		writeLog(true, "Empty setup for 'DBIPAddress'");
		$error = 1;
	}
	
	if(!isset($setup['DBLogin'])) {
		writeLog(true, "Missing 'DBLogin' in setup file");
		$error = 1;
	}else if(empty($setup['DBLogin'])) {
		writeLog(true, "Empty setup for 'DBLogin'");
		$error = 1;
	}
	
	if(!isset($setup['DBPassword'])) {
		writeLog(true, "Missing 'DBPassword' in setup file");
		$error = 1;
	}else if(empty($setup['DBPassword'])) {
		writeLog(true, "Empty setup for 'ShopCode'");
		$error = 1;
	}
	
	if(!isset($setup['SalesDataOutputFolder'])) {
		writeLog(true, "Missing 'SalesDataOutputFolder' in setup file");
		$error = 1;
	}else if(empty($setup['SalesDataOutputFolder'])) {
		writeLog(true, "Empty setup for 'SalesDataOutputFolder'");
		$error = 1;
	}else {
		$length = strlen($setup['SalesDataOutputFolder']);
		if(strcmp(substr($setup['SalesDataOutputFolder'], ($length-1), 1), "\\") != 0)
			$setup['SalesDataOutputFolder'] = $setup['SalesDataOutputFolder']."\\";
	}
	
	$needFtpConnection = false;
	if(isset($setup['NeedFTPConnection']) && $setup['NeedFTPConnection'] == 1)
		$needFtpConnection = true;
	
	if($needFtpConnection) {
		if(!isset($setup['FTPHost'])) {
			writeLog(true, "Missing 'FTPHost' in setup file");
			$error = 1;
		}else if(empty($setup['FTPHost'])) {
			writeLog(true, "Empty setup for 'FTPHost'");
			$error = 1;
		}
		
		if(!isset($setup['FTPLogin'])) {
			writeLog(true, "Missing 'FTPLogin' in setup file");
			$error = 1;
		}else if(empty($setup['FTPLogin'])) {
			writeLog(true, "Empty setup for 'FTPLogin'");
			$error = 1;
		}
		
		if(!isset($setup['FTPPassword'])) {
			writeLog(true, "Missing 'FTPPassword' in setup file");
			$error = 1;
		}else if(empty($setup['FTPPassword'])) {
			writeLog(true, "Empty setup for 'FTPPassword' in setup file");
			$error = 1;
		}
		
		$ftpTargetPath = "";
		if(isset($setup['FTPTargetPath'])) 
			$ftpTargetPath = $setup['FTPTargetPath'];
		
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
	}
	
	if($error)
		return;
	
	//testing connection of database
	$testingDB = null;
	if(connectDatabase($testingDB, $setup['PosDatabaseName'], $setup['DBIPAddress'], $setup['DBLogin'], $setup['DBPassword']) == false) {
		writeLog(true, "SQL error:".mysql_error());
		return;
	}
	if(connectDatabase($testingDB, $setup['OutletDatabaseName'], $setup['DBIPAddress'], $setup['DBLogin'], $setup['DBPassword']) == false) {
		writeLog(true, "SQL error:".mysql_error());
		return;
	}
	
	//get day option value - 1:One day, 2:Date range
	$dayOption = 1;
	if(isset($_POST['dayOptions']) && strcmp($_POST['dayOptions'], "dayRange") == 0)
		$dayOption = 2;
	
	//get business date
	$fromScreen = false;
	$totalDay = 1;
	
	if($dayOption == 1) {
		// single day
		if(isset($_POST['BusinessDate'])) {
			$fromScreen = true;
			$businessDate = DateTime::createFromFormat('Ymd His', $_POST['BusinessDate']." 000000");
			$dateErrors = DateTime::getLastErrors();
			if(!empty($dateErrors) && ($dateErrors['warning_count'] + $dateErrors['error_count'] > 0)) {
				echo "Invalid Business Date<br>";
				return;
			}
		}else {
			$tempBusinessDate = date('Ymd', mktime(0, 0, 0, date("m")  , date("d")-1, date("Y")));
			$businessDate = DateTime::createFromFormat('Ymd H:i:s', $tempBusinessDate." 00:00:00");
		}
		
	}else {
		// date range
		$fromScreen = true;
		$startDate = DateTime::createFromFormat('Ymd', $_POST['BeginBusinessDate']);
		$dateErrors = DateTime::getLastErrors();
		if(!empty($dateErrors) && ($dateErrors['warning_count'] + $dateErrors['error_count']) > 0) {
			echo "Invalid Begin Business Date<br>";
			return;
		}
		
		$endDate = DateTime::createFromFormat('Ymd', $_POST['EndBusinessDate']);
		$dateErrors = DateTime::getLastErrors();
		if(!empty($dateErrors) && ($dateErrors['warning_count'] + $dateErrors['error_count']) > 0) {
			echo "Invalid End Business Date<br>";
			return;
		}
		
		$dateInterval = $startDate->diff($endDate);
		if($dateInterval->invert == 1) {
			echo "End Business Date is before Begin Business Date<br>";
			return;
		}
		$totalDay = $dateInterval->days + 1;
		$businessDate = $startDate;
	}
	
	for($day=1; $day<=$totalDay; $day++) {
		if($day > 1)
			$businessDate->add(new DateInterval('P1D'));
		
		$database = null;
		$dSalesAmount = array();
		$result = getSalesAmount($database, $setup['PosDatabaseName'], $setup['OutletDatabaseName'], $setup['DBIPAddress'], $setup['DBLogin'], $setup['DBPassword'], $businessDate, $setup['ShopCode'], $setup['OutletCode'], $dSalesAmount);
		if($result == "s") {
			if($fromScreen)
				echo "Wrong shop code<br>";
			writeLog(true, "Wrong shop code");
			return;
		}else if($result == "o") {
			if($fromScreen)
				echo "Wrong outlet code<br>";
			writeLog(true, "Wrong outlet code");
			return;
		}
		
		//check folder existence
		if(!file_exists($setup['SalesDataOutputFolder']))
			mkdir($setup['SalesDataOutputFolder'], 0777);
		
		//generate the output file
		$sSalesDataFileName = "TH_".$businessDate->format('Ymd').".txt";
		$fOutputFile = fopen($setup['SalesDataOutputFolder'].$sSalesDataFileName, "w");
		for($hour=0; $hour<24; $hour++) {
			$sFileContent = $setup['ClientID']."|".$businessDate->format('Ymd')."|".str_pad($hour, 2, "0", STR_PAD_LEFT)."|".$dSalesAmount[$hour]['record_count']."|".$dSalesAmount[$hour]['sales_amount']."|".$dSalesAmount[$hour]['gst'];
			fputs($fOutputFile, $sFileContent."\r\n");
		}
		fclose($fOutputFile);
		writeLog(false, "Outlet file:".$sSalesDataFileName." has been generated");
		if($fromScreen)
			echo "Outlet file:".$sSalesDataFileName." has been generated<br>";
	}
	
	/********************************************/
	/***** Transfer output file through FTP *****/
	/********************************************/
	//testing connection of ftp
	if(!$needFtpConnection) {
		if($fromScreen)
			echo "<a href='salesData.php'>Back</a>";
		return;
	}
	
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
?>