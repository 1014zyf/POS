<?php
    //connect database 
    function connectDatabase(&$database, $dbName, $ipAddress, $login, $password)
    {
		//connect database server and select a specific database
        //$database = mysql_connect("localhost","root","infrasys");
		$database = mysql_connect($ipAddress, $login, $password);
      	if (!$database) {
      		return false;
			//die('Could not connect1 : ' . mysql_error());
        } else {
      		$db_selected = mysql_select_db($dbName, $database);
          
          if (!$db_selected )
          {
            //die('Could not connect2: '. mysql_error());
			return false;
          }
      	}
      	
      	//set character display to utf8
      	mysql_query("SET NAMES 'utf8'"); 
        mysql_query("SET CHARACTER_SET_CLIENT=utf8"); 
        mysql_query("SET CHARACTER_SET_RESULTS=utf8");
		return true;
    }
    
    //disconnect database
    function disconnectDatabase(&$database)
    {
        // close database server
        mysql_close($database);
    }
    
    //check the unarthorized access
    function checkingUserID($iUserID)
    {
        if ($iUserID == "")
        {
            die("<h1>Unarthorized Access</h1>");
        }
    }
    
    //compare the last activity time with current time to check the timeout
    function loginTimeoutChecking($activeTime)
    {
        $current_time = date(His);
        $timeout_second = 6000;
        $difference = $current_time - $activeTime;
        if ($difference > $timeout_second)
        {
            session_destroy();
            die("<h1>Attention</h1>You have been logged out due to inactivity.<br>
                Please <a href='login.php'>login</a> again!<br>");
        }else 
        {
            return $current_time;
        }
    }
?>
