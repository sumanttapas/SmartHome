<?php
//$con = mysql_connect("localhost","root","student");
$con = mysql_connect("localhost","root","1234");

mysql_select_db("community");

if (!$con){ die('Could not connect: ' . mysql_error());}

$q = mysql_query("SELECT EXISTS(SELECT * FROM lock_status WHERE userID = '$userID')");

if (mysql_select_db('community', $con)) 
{
	
	$userID =$_POST["userID"];
	$flag=$_POST["flag"];	
	$lock_status =$_POST["lock_status"];
	$room = (int)$_POST["room"];
	
	if(mysql_result($q,0) == 1) 	//user id exists
	{
		$z = mysql_query("SELECT EXISTS(SELECT * FROM lock_status WHERE room = '$room' and userID = '$userID')");
		
		if(mysql_result($z,0) == 1) //room exists
		{		
			if($flag == 1)		//flag 1 for setting
			{
				$setResult = mysql_query("UPDATE lock_status set lock_status = '$lock_status' WHERE userID = '$userID' and room = '$room';");
			}
			else		//flag 0 for getting
			{
				$getResult=mysql_query("SELECT * FROM lock_status WHERE userID = '$userID'");
			}
		}
		else //room doesnt exists
		{
			if($flag == 1)		//flag 1 for setting
			{
				$roomInsert = mysql_query("INSERT INTO lock_status (room,lock_status,userID) VALUES ('$room','$lock_status','$userID')"); 
			}
			else		//flag 0 for getting
			{
				$getResult=mysql_query("SELECT * FROM lock_status WHERE userID = '$userID'");
			}
			
		}
	else 
	{
		echo"entered in else";
		$useridInsertResult = mysql_query("INSERT INTO lock_status (userID) VALUES ('$userID')");
		
		$z = mysql_query("SELECT EXISTS(SELECT * FROM lock_status WHERE room = '$room' and userID = '$userID')");
		
		if(mysql_result($z,0) == 1) //room exists
		{		
			if($flag == 1)		//flag 1 for setting
			{
				$setResult = mysql_query("UPDATE lock_status set lock_status = '$lock_status' WHERE userID = '$userID' and room = '$room';");
			}
			else		//flag 0 for getting
			{
				$getResult=mysql_query("SELECT * FROM lock_status WHERE userID = '$userID'");
			}
		}
		else //room doesnt exists
		{
			$roomInsert = mysql_query("INSERT INTO lock_status (room) VALUES ('$room')");
			
			if($flag == 1)		//flag 1 for setting
			{
				$setResult = mysql_query("INSERT INTO lock_status (lock_status) VALUES('$lock_status');"); 
			}
			else		//flag 0 for getting
			{
				$getResult=mysql_query("SELECT * FROM lock_status WHERE userID = '$userID'");
			}
			
		}
	}

}
mysql_close();
?>
