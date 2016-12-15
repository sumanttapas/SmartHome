<?php
$con = mysql_connect("localhost","root","1234");
mysql_select_db("community");
if (!$con){ die('Could not connect: ' . mysql_error());}

if (mysql_select_db('community', $con))
{
	$userID =(int)$_POST["userID"];
	$door_status =$_POST["door_status"];
	$door_type =$_POST["door_type"];

	$q = mysql_query("SELECT EXISTS(SELECT * FROM garage_doors WHERE userID = $userID and door_type = '$door_type')");
	//echo $q;
	if(mysql_result($q,0) == 1)			// USER ID Exists
	{
		$result = mysql_query("UPDATE garage_doors set door_status = '$door_status' WHERE userID = $userID and door_type = '$door_type'");
	}

	else 	// USER ID does not Exists and room does not exists
	{
		$result = mysql_query("INSERT INTO garage_doors (userID, door_status, door_type) VALUES ($userID,'$door_status', '$door_type')");
	}

	if($result)
		print("true");
	else
		print("false");
}

//php exec("java -jar SecurityExecMain.jar '$userID'"); 

mysql_close();
?>
