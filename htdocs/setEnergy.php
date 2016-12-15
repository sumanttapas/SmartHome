<?php
$con = mysql_connect("localhost","root","1234");
mysql_select_db("community");
if (!$con){ die('Could not connect: ' . mysql_error());}

if (mysql_select_db('community', $con))
{
	$userID =(int)$_POST["userID"];
	$energy =(float)$_POST["energy"];
        $ApplianceID = $_POST["ApplianceID"];

	$q = mysql_query("SELECT EXISTS(SELECT * FROM Energy WHERE userID = '$userID')");

	if(mysql_result($q,0) == 1)			// USER ID Exists
	{
		$result = mysql_query("UPDATE Energy set energy = '$energy', ApplianceID = '$ApplianceID' WHERE userID = '$userID'");
	}

	else 	// USER ID does not Exists and room does not exists
	{
		$result = mysql_query("INSERT INTO Energy (userID,energy,ApplianceID) VALUES ('$userID','$energy','$ApplianceID')");
	}

	if($result)
		print("true");
	else
		print("false");
}
//include 'getEnergyOutput.php';
mysql_close();
?>
