<?php
$con = mysql_connect("localhost","root","1234");
mysql_select_db("community");
if (!$con){ die('Could not connect: ' . mysql_error());}

if (mysql_select_db('community', $con))
{
	$userID =(int)$_POST["userID"];
	$flag =$_POST["flag"];

	$q = mysql_query("SELECT EXISTS(SELECT * FROM tempTime WHERE userID = $userID )");
	//echo $q;
	if(mysql_result($q,0) == 1)			// USER ID Exists
	{
		$result = mysql_query("UPDATE tempTime set flag = '$flag'WHERE userID = $userID ");
	}

	else 	// USER ID does not Exists and room does not exists
	{
		$result = mysql_query("INSERT INTO tempTime (userID,flag) VALUES ('$userID','$flag')");
	}

	if($result)
		print("true");
	else
		print("false");
}

//php exec("java -jar SecurityExecMain.jar '$userID'"); 

mysql_close();
?>
