<?php
//$con = mysql_connect("localhost","root","student");
$con = mysql_connect("localhost","root","1234");

mysql_select_db("community");

if (!$con){ die('Could not connect: ' . mysql_error());}

//mysql_select_db("users");


if (mysql_select_db('community', $con))
{
	$userID =(int)$_POST["userID"];
	//$flag=$_POST["flag"];
	$lock_status =$_POST["lock_status"];
	$room = (int)$_POST["room"];

	$q = mysql_query("SELECT EXISTS(SELECT * FROM lock_status WHERE userID = '$userID')");


	if(mysql_result($q,0) == 1)		// USER ID Exists
	{
		$roomResult = mysql_query("SELECT EXISTS(SELECT * FROM lock_status WHERE room = '$room' and userID = '$userID')");

		if(mysql_result($roomResult,0) == 1) //room exists
		{
			$updateRoom = mysql_query("UPDATE lock_status set lock_status = '$lock_status' WHERE room = '$room' and userID = '$userID'");
		}
		else //room doesnt exists
		{
			$insertRoom = mysql_query("INSERT INTO lock_status (room,lock_status,userID) VALUES ('$room','$lock_status','$userID')");
		}
	}
	else 	// USER ID does not Exists and room does not exists
	{
		$useridInsert = mysql_query("INSERT INTO lock_status (room,lock_status,userID) VALUES ('$room','$lock_status','$userID')");
	}

	if($updateRoom || $insertRoom && $useridInsert)
		print("true");
	else
		print("false");
}

mysql_close();
?>
