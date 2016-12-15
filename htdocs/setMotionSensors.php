<?php
$con = mysql_connect("localhost","root","1234");
mysql_select_db("community");
if (!$con){ die('Could not connect: ' . mysql_error());}

if (mysql_select_db('community', $con))
{
	$userID =(int)$_POST["userID"];
	$floor =$_POST["floor"];
	$status =$_POST["status"];

	$q = mysql_query("SELECT EXISTS(SELECT * FROM motion_sensors WHERE userID = $userID and floor = '$floor')");
	//echo $q;
	if(mysql_result($q,0) == 1)			// USER ID Exists
	{
		$result = mysql_query("UPDATE motion_sensors set status = '$status' WHERE userID = $userID and floor = '$floor'");
	}

	else 	// USER ID does not Exists and room does not exists
	{
		$result = mysql_query("INSERT INTO motion_sensors (userID, status, floor) VALUES ($userID,'$status', '$floor')");
	}

	if($result)
		print("true");
	else
		print("false");
}

//php exec("java -jar SecurityExecMain.jar '$userID'"); 

mysql_close();
?>
