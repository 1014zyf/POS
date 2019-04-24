<?php
	require('DBControl.php');
	set_time_limit(600);
	
	//read setup file
	function readSetup() {
		$setupArray = array();
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
	
	//get file sequence number
	function getFileSequenceNumber($dBusinessDate) {
		$sSequenceFileName = 'sequenceNumber.txt';
		
		if(file_exists($sSequenceFileName)) {
			// file exist
			$sSequenceFile = fopen($sSequenceFileName, "r");
			$sLastSequenceContent = fgets($sSequenceFile, 1000);
			fclose($sSequenceFile);
			
			$sLastBusinessDate = "";
			
			$tok = strtok($sLastSequenceContent, ",");
			if($tok !== false)
				$sLastBusinessDate = $tok;
			$tok = strtok(",");
			if($tok !== false)
				$sSequenceNumber = $tok;
			
			if(strcmp($dBusinessDate, $sLastBusinessDate) != 0) {
				$sSequenceNumber = 65;
				$sNextSequenceNumber = 66;
			}else {
				if($sSequenceNumber < 90) 
					$sNextSequenceNumber = ($sSequenceNumber + 1);
				else if ($sSequenceNumber == 90) {
					$sNextSequenceNumber = $sSequenceNumber;
					writeLog(false, "Sequence number is reached the maximum 'Z'");
				}
			}
		}else {
			$sSequenceNumber = 65;
			$sNextSequenceNumber = 66;
		}
		
		$sFileContent = $dBusinessDate.",".$sNextSequenceNumber."\r\n";
		$sSequenceFile = fopen($sSequenceFileName, "w");
		fputs($sSequenceFile, $sFileContent);
		fclose($sSequenceFile);
		
		return $sSequenceNumber;
	}
	
	// get net sales amount
	function getNetSalesAmount(&$database, $posDbName, $outletDbName, $dbIPAddress, $dbLogin, $dbPassword, $shopCode, $outletCode, $dBusinessDate, &$gOutletId, &$gReportId, &$dNetSalesAmount) {
		// connect outlet database
		connectDatabase($database, $outletDbName, $dbIPAddress, $dbLogin, $dbPassword);
		
		// get shop information
		$shopId = 0;
		$shopSQL = "SELECT shop_id FROM ".$outletDbName.".out_shops WHERE shop_code='".$shopCode."' AND shop_status=''";
		$shopSQLResult = mysql_query($shopSQL);
		if($shopRow = mysql_fetch_array($shopSQLResult))
			$shopId = $shopRow['shop_id'];
		if($shopId == 0)
			return "s";
		
		// get outlet information
		$outletId = 0;
		$outletSQL = "SELECT olet_id FROM ".$outletDbName.".out_outlets WHERE olet_shop_id='".$shopId."' AND olet_code='".$outletCode."' AND olet_status=''";
		$outletSQLResult = mysql_query($outletSQL);
		if($outletRow = mysql_fetch_array($outletSQLResult))
			$outletId = $outletRow['olet_id'];
		if($outletId == 0)
			return "o";
		$gOutletId = $outletId;
		
		// connect pos database
		connectDatabase($database, $posDbName, $dbIPAddress, $dbLogin, $dbPassword);
		
		// get report id
		$posReportSQL = "SELECT rpts_id FROM ".$posDbName.".pos_reports WHERE rpts_filename='revenue' and rpts_status<>'d'";
		$posReportSQLResult = mysql_query($posReportSQL);
		if ($rptsRow = mysql_fetch_array($posReportSQLResult)) 
			$rptsId = $rptsRow['rpts_id'];
		
		if($rptsId == 0) 
			return "r";
		$gReportId = $rptsId;
		
		// get business day record
		$bdayId = 0;
		$bdayDate = substr($dBusinessDate, 0, 4)."-".substr($dBusinessDate, 4, 2)."-".substr($dBusinessDate, 6, 2);
		$bdaySQL = "SELECT bday_id FROM ".$posDbName.".pos_business_days WHERE bday_date='".$bdayDate."' and bday_shop_id=".$shopId." and bday_olet_id=".$outletId." and bday_status<>'d'";
		$bdaySQLResult = mysql_query($bdaySQL);
		if ($bdayRow = mysql_fetch_array($bdaySQLResult)) 
			$bdayId = $bdayRow['bday_id'];
		
		if($bdayId == 0) {
			$dNetSalesAmount = 0.0;
			$dNetSalesAmount = number_format($dNetSalesAmount, 2, ".", "");
			return $dNetSalesAmount;
		}
		
		// get the check information base on bday_id
		$checkSQL = "SELECT SUM(chks_item_total) AS chks_item_total, SUM(chks_pre_disc) AS chks_pre_disc, SUM(chks_mid_disc) AS chks_mid_disc, SUM(chks_post_disc) AS chks_post_disc FROM ".$posDbName.".pos_checks WHERE chks_bday_id=".$bdayId." and chks_shop_id=".$shopId." and chks_olet_id=".$outletId." and chks_non_revenue='' and chks_status=''";
		$checkSQLResult = mysql_query($checkSQL);
		if ($checkRow = mysql_fetch_array($checkSQLResult)) 
			$dNetSalesAmount = $checkRow['chks_item_total'] + $checkRow['chks_pre_disc'] + $checkRow['chks_mid_disc'] + $checkRow['chks_post_disc'];
		$dNetSalesAmount = number_format($dNetSalesAmount, 2, ".", "");
		
		return $dNetSalesAmount;
	}
	
	// write log
	function writeLog($error, $message) {
		$currentMonth = date('m');
		$logFileName = "salesDataGen".$currentMonth;
		
		$logFile = fopen($logFileName, 'a+');
		if($error)
			$logContent = date('Y-m-d H:i:s')." - ERROR - ".$message."\r\n";
		else
			$logContent = date('Y-m-d H:i:s')." - ".$message."\r\n";
		fputs($logFile, $logContent);
		fclose($logFile);
	}
	
	// get database url by datasource
	function getDatabaseUrl($datasource = '', $host = '', $database = ''){
		$url = '';
		switch (strtolower($datasource)){
			case 'database/sqlserver':
				$url = 'jdbc:sqlserver://' . $host . ':1433;databaseName=' . $database;
				break;
			case 'database/mysql':
				$url = 'jdbc:mysql://' . $host . '/' . $database . '?characterEncoding=UTF-8';
				break;
		}
		return $url;
	}
	
	/******************/
	/*	Main Program  */
	/******************/
	$error = 0;
	$database = null;
	
	// read setup file
	$setup = readSetup();
	if($setup == false) {
		writeLog(true, "Missing setup file");
		return;
	}
	
	if(!isset($setup['LocationCode'])) {
		writeLog(true, "Missing 'LocationCode' in setup file");
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
	}
	
	if(!isset($setup['OutletDatabaseName'])) {
		writeLog(true, "Missing 'OutletDatabaseName' in setup file");
		$error = 1;
	}
	
	if(!isset($setup['UserDatabaseName'])) {
		writeLog(true, "Missing 'UserDatabaseName' in setup file");
		$error = 1;
	}
	
	if(!isset($setup['DBIPAddress'])) {
		writeLog(true, "Missing 'DBIPAddress' in setup file");
		$error = 1;
	}
	
	if(!isset($setup['DBLogin'])) {
		writeLog(true, "Missing 'DBLogin' in setup file");
		$error = 1;
	}
	
	if(!isset($setup['DBPassword'])) {
		writeLog(true, "Missing 'DBPassword' in setup file");
		$error = 1;
	}
	
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
	
	if(!isset($setup['HEROURL'])) {
		writeLog(true, "Missing 'HEROURL' in setup file");
		$error = 1;
	}else if (strcmp($setup['HEROURL'], "") == 0) {
		writeLog(true, "Empty setup for HEROURL");
		$error = 1;
	}else {
		$length = strlen($setup['HEROURL']);
		if(strcmp(substr($setup['HEROURL'], ($length-1), 1), "/") == 0)
			$setup['HEROURL'] = substr($setup['HEROURL'], 0, ($length-1));
	}
	
	if(!isset($setup['HEROLogin'])) {
		writeLog(true, "Missing 'HEROLogin' in setup file");
		$error = 1;
	}
	
	if(!isset($setup['HEROPassword'])) {
		writeLog(true, "Missing 'HEROPassword' in setup file");
		$error = 1;
	}
	
	if(!isset($setup['ReportServerIP'])) {
		writeLog(true, "Missing 'ReportURL' in setup file");
		$error = 1;
	}
	
	if(!isset($setup['ReportServerPort']))
		$setup['ReportServerPort'] = "";
	else
		$setup['ReportServerPort'] = ":".$setup['ReportServerPort'];
	
	$gNeedFTPConnection = 0;
	if(!isset($setup['NeedFTPConnection']) && $setup['NeedFTPConnection'] == 1)
		$gNeedFTPConnection = 1;
	
	if($gNeedFTPConnection) {
		if(!isset($setup['FTPHost'])) {
			writeLog(true, "Missing 'FTPHost' in setup file");
			$error = 1;
		}
		
		if(!isset($setup['FTPLogin'])) {
			writeLog(true, "Missing 'FTPLogin' in setup file");
			$error = 1;
		}
		
		if(!isset($setup['FTPPassword'])) {
			writeLog(true, "Missing 'FTPPassword' in setup file");
			$error = 1;
		}
		
		$ftpTargetPath = "";
		if(isset($setup['FTPTargetPath'])) 
			$ftpTargetPath = $setup['FTPTargetPath'];
		
		if(!isset($setup['FileSentFolder'])) {
			writeLog(true, "Missing 'FileSendFolder' in setup file");
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
		writeLog(true, mysql_error());
		return;
	}
	
	$testingDB = null;
	if(connectDatabase($testingDB, $setup['OutletDatabaseName'], $setup['DBIPAddress'], $setup['DBLogin'], $setup['DBPassword']) == false) {
		writeLog(true, mysql_error());
		return;
	}
	
	$testingDB = null;
	if(connectDatabase($testingDB, $setup['UserDatabaseName'], $setup['DBIPAddress'], $setup['DBLogin'], $setup['DBPassword']) == false) {
		writeLog(true, mysql_error());
		return;
	}else {
		$gUserId = 0;
		$userSQL = "SELECT user_id FROM ".$setup['UserDatabaseName'].".user_users WHERE user_login='".$setup['HEROLogin']."' AND user_status=''";
		$userSQLResult = mysql_query($userSQL);
		if ($userRow = mysql_fetch_array($userSQLResult)) 
			$gUserId = $userRow['user_id'];
		else {
			writeLog(true, "Wrong user login");
			return;
		}
	}
	
	//get business date
	$fromScreen = false;
	$dBusinessDate = "";
	if(!isset($_GET['BusinessDate']) && !isset($_POST['BusinessDate']))
		$dBusinessDate = date('Ymd', mktime(0, 0, 0, date("m")  , date("d")-1, date("Y")));
	else {
		$fromScreen = true;
		if(isset($_POST['BusinessDate']))
			$dBusinessDate = $_POST['BusinessDate'];
		else
			$dBusinessDate = $_GET['BusinessDate'];
		if(strlen($dBusinessDate) != 8)
			echo "Invalid business date<br>";
		if (intval(substr($dBusinessDate, 4, 2)) <= 0  || intval(substr($dBusinessDate, 4, 2)) > 13)
			echo "Invalid Business Date<br>";
		if (intval(substr($dBusinessDate, 6, 2)) > 31)
			echo "Invalid Business Date<br>";
	}
	
	//get file sequence number
	$sFileSequence = getFileSequenceNumber($dBusinessDate);
	$sFileSequence = chr($sFileSequence);
	
	//generate output file name
	$sSalesDataFileName = $setup['LocationCode']."_".substr($dBusinessDate, 6, 2).substr($dBusinessDate, 4, 2).substr($dBusinessDate, 0, 4)."_".$sFileSequence.".txt";
	
	//get net sales amount
	$dNetSalesAmount = 0;
	$gOutletId = 0;
	$gReportId = 0;
	$sNetSalesAmountResult = getNetSalesAmount($database, $setup['PosDatabaseName'], $setup['OutletDatabaseName'], $setup['DBIPAddress'], $setup['DBLogin'], $setup['DBPassword'], $setup['ShopCode'], $setup['OutletCode'], $dBusinessDate, $gOutletId, $gReportId, $dNetSalesAmount);
	if($sNetSalesAmountResult == "s") {
		if($fromScreen)
			echo "Wrong shop code<br>";
		writeLog(true, "Wrong shop code");
		return;
	}else if($sNetSalesAmountResult == "o") {
		if($fromScreen)
			echo "Wrong outlet code<br>";
		writeLog(true, "Wrong outlet code");
		return;
	}else if($sNetSalesAmountResult == "r") {
		if($fromScreen)
			echo "No revenue slip report id<br>";
		writeLog(true, "No revenue slip report id");
		return;
	}
	
	//generate the outlet file contents
	$sOutputContent = "";
	$sOutletContent = $setup['LocationCode'].",".substr($dBusinessDate,6, 2).substr($dBusinessDate, 4, 2).substr($dBusinessDate, 0, 4).",".$dNetSalesAmount;
	
	//check output folder existence
	if(!file_exists($setup['SalesDataOutputFolder']))
		mkdir($setup['SalesDataOutputFolder'], 0777);
	
	//generate the output sales file
	$fOutputFile = fopen($setup['SalesDataOutputFolder'].$sSalesDataFileName, "w");
	fputs($fOutputFile, $sOutletContent);
	fclose($fOutputFile);
	writeLog(false, "Output sales file:".$sSalesDataFileName." has been generated");
	
	/////////////////////////////
	/* generate revenue report */
	/////////////////////////////
	$wsClient = new SoapClient($setup['HEROURL'].'/ws_interface/wsdl', array('trace' => 1));
	$result = $wsClient->__soapCall('login', array('username' => $setup['HEROLogin'], 'password' => $setup['HEROPassword']));
	if(isset($result['error']) && strcmp($result['error'], "") != 0) {
		writeLog(true, "Fail to login HERO web service with error:".$result['error']);
		return;
	}
	$sessionId = $result['sessionId'];
	
	// get all module database setup
	$wsClient->__setCookie("CAKEPHP", $sessionId);
	$dbInfoParam = array(
				'interface' => 'gm',
				'module' => 'system',
				'fcn' => 'getModuleDatabaseInfo',
				'params' => json_encode(array(
					'recursive' => '1',
				)),
			);
	$result = $wsClient->__soapCall('call', $dbInfoParam);
	if(!empty($result['reply']['moduleRegistries'])) {
		$moduleReplyJson = json_decode($result['reply'], true);	
		$moduleRegistries = $moduleReplyJson['moduleRegistries'];
		$moduleDBSettings = array();
		
		foreach($moduleRegistries as $registry) {
			$moduleValue = json_decode($registry['SysModuleRegistry']['modr_value'], true);
			
			//datasource url
			$databaseUrl = getDatabaseUrl($moduleValue['datasource'], $moduleValue['host'], $moduleValue['database']);
			$moduleDBSettings[$registry['SysModule']['modu_alias']] = $databaseUrl;
		}
		
		//build the report url path
		$dBusinessDateWithFormat = substr($dBusinessDate, 0, 4)."-".substr($dBusinessDate, 4, 2)."-".substr($dBusinessDate, 6, 2);
		$sReportURL = "http://".$setup['ReportServerIP'].$setup['ReportServerPort']."/birt/frameset?__report=".$setup['HEROURL']."/pos/reports/revenue/revenue.rptdesign&iReportId=".$gReportId."&iUserId=".$gUserId."&iIsClient=0&__locale=en&sLanguage=1";
		foreach($moduleDBSettings as $key => $moduleURL) {
			$moduleName = ucfirst($key);
			$sReportURL = $sReportURL."&ds".$moduleName."Url=".$moduleURL."&ds".$moduleName."Id=".$setup['DBLogin']."&ds".$moduleName."Pw=".$setup['DBPassword'];
		}
		$sReportURL = $sReportURL."&sDateType=specified&dBeginDate=".$dBusinessDateWithFormat."&dEndDate=".$dBusinessDateWithFormat."&sOutlets=".$gOutletId."&bShowDiscount=true&bShowGrandTotal=false&sDateFormat=yyyy-MM-dd&sDateTimeFormat=yyyy-MM-dd%20HH%3Amm%3Ass&iDecimalPlace=2&iIsClient=0&__format=pdf&bDecryptDataSource=false";
		
		//generate report file name
		$sReportFileName = $setup['LocationCode']."_".substr($dBusinessDate, 6, 2).substr($dBusinessDate, 4, 2).substr($dBusinessDate, 0, 4)."_ZRPT.pdf";
		
		//get report content
		$reportJobHeader = array('Connection: close');
		$opts = array(
				'http' => array(
					'method' => 'POST',
					'header' => $reportJobHeader,
					'timeout' => 120,	//timeout in seconds
				)
			);
		$context = stream_context_create($opts);
		$reportFile = file_get_contents($sReportURL, 0, $context);
		if($reportFile === false) {
			writeLog(true, "Fail to load revenue report");
		}else if(substr($reportFile, 0, 6) == "error:") {
			writeLog(true, "Fail to generate revenue report");
		}else {
			$fReportFile = fopen($setup['SalesDataOutputFolder'].$sReportFileName, 'w');
			fwrite($fReportFile, $reportFile);
			fclose($fReportFile);
			writeLog(false, "Output revenue report:".$sReportFileName." has been generated");
		}
	}
		
	$result = $wsClient->__soapCall('logout', array());
	if(isset($result['error']) && strcmp($result['error'], "") != 0) 
		writeLog(true, "Fail to login HERO web service with error:".$restul['error']);
	
	//////////////////////////////////////
	/* Transfer output file through FTP */
	//////////////////////////////////////
	//testing ftp connection
	if(!$gNeedFTPConnection) {
		if($fromScreen) {
			echo "Sales file(".$sSalesDataFileName.") has been generated<br>";
			echo "Revenue report file(".$sReportFileName.") has been generated<br>";
			echo "<a href='salesData.php'>Back</a>";
		}
		return;
	}
	
	$ftpConnection = ftp_connect($setup['FTPHost'], 21, 3);
	if(!$ftpConnection) {
		writeLog(true, "Cannot connect to FTP server:".$setup['FTPHost']);
		return;
	}
	ftp_close($ftpConnection);
	
	//check folder existence
	if(!file_exists($setup['FileSentFolder'])) 
		mkdir($setup['FileSentFolder']);
	
	$ftpConnection = ftp_connect($setup['FTPHost'], 21, 15);
	if($ftpConnection) {
		writeLog(false, "Connect to FTP server:".$setup['FTPHost']);
		if(ftp_login($ftpConnection, $setup['FTPLogin'], $setup['FTPPassword']) == FALSE) {
			if($fromScreen)
				echo "Fail to login FTP server<br>";
			writeLog(true, "Fail to login FTP server");
			ftp_close($ftpConnection);
			return;
		}
		
		ftp_pasv($ftpConnection, true);
		$currentFTPPath = ftp_pwd($ftpConnection);
		if(strcmp($ftpTargetPath, "") != 0)
			ftp_chdir($ftpConnection, $ftpTargetPath);
		
		$files = scandir($setup['SalesDataOutputFolder']);
		foreach($files as $fileName) {
			if(strcmp($fileName, ".") == 0 || strcmp($fileName, "..") == 0)
				continue;
			
			if(ftp_put($ftpConnection, $fileName, $setup['SalesDataOutputFolder'].$fileName, FTP_BINARY)) {
				if($fromScreen)
					echo "Success to upload file(".$fileName.") to FTP server<br>";
				writeLog(false, "Success to upload file(".$fileName.") to FTP server");
				if(file_exists($setup['FileSentFolder'].$fileName))
					unlink($setup['FileSentFolder'].$fileName);
				
				rename($setup['SalesDataOutputFolder'].$fileName, $setup['FileSentFolder'].$fileName);
			}else {
				if($fromScreen)
					echo "Fail to upload file(".$fileName.") to FTP server<br>";
				writeLog(true, "Fail to upload file(".$fileName.") to FTP server");
			}
		}
		
		ftp_close($ftpConnection);
		if($fromScreen)
			echo "<a href='salesData.php'>Back</a>";
	}else {
		if($fromScreen)
			echo "<a href='salesData.php'>Back</a>";
		writeLog(true, "Cannot connection to FTP server:".$setup['FTPHost']);
		return;
	}
?>