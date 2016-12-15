<?php
//$con = mysql_connect("localhost","root","student");
$con = mysql_connect("localhost","root","1234");

mysql_select_db("community");

if (!$con){ die('Could not connect: ' . mysql_error());}

//mysql_select_db("users");


if (mysql_select_db('community', $con))
{
	//mysql_query("UPDATE userlist set username = 'shami' WHERE userID = 1001;");
	//echo"ekade ala!";
	$userID =$_POST["userID"];
	//$userID1 = (int) $userID;

	$insideTemp = (float)$_POST["insideTemp"];

	$outsideTemp = (float)$_POST["outsideTemp"];
	$setTemp = (float)$_POST["setTemp"];
	$ac_mode = $_POST["ac_mode"];
	$floor = (int)$_POST["floor"];
	$fan = $_POST["fan"];
	$q = mysql_query("SELECT EXISTS(SELECT * FROM AC_Data WHERE userID = '$userID')");

	if(mysql_result($q,0) == 1)		//user exists
	{

		$floorResult = mysql_query("SELECT EXISTS(SELECT * FROM AC_Data WHERE floor = '$floor' and userID = '$userID')");
		if(mysql_result($floorResult,0) == 1) // floor exists
		{
			mysql_query("INSERT INTO Energy_Consumption (userID,ApplianceID, Appliance_status, Outside_temp, Set_temp, floor) VALUES ('$userID','1', '$ac_mode','$outsideTemp','$setTemp','$floor')");
			$result = mysql_query("UPDATE AC_Data set insideTemp = '$insideTemp',outsideTemp = '$outsideTemp',setTemp ='$setTemp',ac_mode = '$ac_mode',fan = '$fan' WHERE userID = '$userID' and floor = '$floor'");
		}
		else
		{
			mysql_query("INSERT INTO Energy_Consumption (userID,ApplianceID, Appliance_status, Outside_temp, Set_temp, floor) VALUES ('$userID','1', '$ac_mode','$outsideTemp','$setTemp','$floor')");
			$result = mysql_query("INSERT INTO AC_Data (userID, floor,insideTemp, outsideTemp, setTemp, ac_mode,fan)
			VALUES ('$userID','$floor','$insideTemp','$outsideTemp','$setTemp', '$ac_mode','$fan')");
		}
	}
	else            //user does not exist
	{
		//echo"entered in else";
		mysql_query("INSERT INTO Energy_Consumption (userID,ApplianceID, Appliance_status, Outside_temp, Set_temp,floor) VALUES ('$userID','1', '$ac_mode','$outsideTemp','$setTemp','$floor')");
		$result = mysql_query("INSERT INTO AC_Data (userID, insideTemp, outsideTemp, setTemp, ac_mode,fan)
		VALUES ('$userID','$insideTemp','$outsideTemp','$setTemp','$ac_mode','$fan')");
		//print("New User Inserted");
	}

	/*if($result)
		print("true");
	else
		print("false");*/
}

mysql_close();
?>
