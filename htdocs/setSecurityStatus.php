<?php
$con = mysql_connect("localhost","root","1234");
mysql_select_db("community");
if (!$con){ die('Could not connect: ' . mysql_error());}

if (mysql_select_db('community', $con))
{
	$userID =(int)$_POST["userID"];
	$security_status =$_POST["security_status"];

	$q = mysql_query("SELECT EXISTS(SELECT * FROM security_system WHERE userID = '$userID')");

	if(mysql_result($q,0) == 1)			// USER ID Exists
	{
		echo "here";
		echo $q;
		$result = mysql_query("UPDATE security_system set security_status = '$security_status' WHERE userID = '$userID'");
	}

	else 	// USER ID does not Exists and room does not exists
	{
		$result = mysql_query("INSERT INTO security_system (userID,security_status) VALUES ('$userID','$security_status')");
	}

	if($result)
		print("true");
	else
		print("false");
}

//php exec("java -jar SecurityExecMain.jar '$userID'");

mysql_close();
?>
