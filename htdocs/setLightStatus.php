<?php
//$con = mysql_connect("localhost","root","student");
$con = mysql_connect("localhost","root","1234");

mysql_select_db("community");

if (!$con){ die('Could not connect: ' . mysql_error());}

//mysql_select_db("users");
//echo"entered into Database";

if (mysql_select_db('community', $con))
{
	//echo"entered into Database";

	$userID =$_POST["userID"];
	//$userID1 = (int) $userID;
	$room = (int)$_POST["room"];

	$light_status = $_POST["light_status"];
	$dimmer_status = (int)$_POST["dimmer_status"];

	$q = mysql_query("SELECT EXISTS(SELECT * FROM light_status WHERE userID = '$userID')");

	if(mysql_result($q,0) == 1)   //user exists
	{
		$resultRoom = mysql_query("SELECT EXISTS(SELECT * FROM light_status WHERE userID = '$userID' and room = '$room')");
		if(mysql_result($resultRoom,0) == 1)    //room exists
		{
			$energyResult = mysql_query("INSERT INTO Energy_Consumption (userID, ApplianceID,Appliance_status, DimmerStatus,room) VALUES ('$userID','3','$light_status','$dimmer_status','$room')");
			$result = mysql_query("UPDATE light_status set light_Status = '$light_status',dimmer_status ='$dimmer_status' WHERE userID = '$userID' and room = '$room'");
		//	echo"entered in if";
		}
		else
		{
			$energyResult = mysql_query("INSERT INTO Energy_Consumption (userID, ApplianceID,Appliance_status, DimmerStatus,room) VALUES ('$userID','3','$light_status','$dimmer_status','$room')");
			$result = mysql_query("INSERT INTO light_status (userID,room, light_status, dimmer_status)
			VALUES ('$userID','$room','$light_status','$dimmer_status')");
		}
	}
	else  							// user does not exist
	{
		//echo"entered in else";
		$energyResult = mysql_query("INSERT INTO Energy_Consumption (userID, ApplianceID,Appliance_status, DimmerStatus,room) VALUES ('$userID','3','$light_status','$dimmer_status','$room')");
		$result = mysql_query("INSERT INTO light_status (userID,room, light_status, dimmer_status)
		VALUES ('$userID','$room','$light_status','$dimmer_status')");
		//print("New User Inserted");
	}

	if($result)
		print("true");
	else
		print("false");
}
//echo exec('whoami');
//php exec("java -jar LightExecMain.jar '$userID'",$output);
//echo $output;
mysql_close();
//include '/opt/lampp/htdocs/getLightStatus.php'
//$output = exec("java -jar LightExecMain.jar '$userID'");
//exec("java -jar LightExecMain.jar '$userID'",$output);
//$result_array = explode(' ',$output);

//print_r($output);
//print json_decode('',$result_array);
//var_dump($output);

?>
