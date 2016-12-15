<?php
//$con = mysql_connect("localhost","root","student");
$con = mysql_connect("localhost","root","1234");

mysql_select_db("community");

if (!$con){ die('Could not connect: ' . mysql_error());}

//mysql_select_db("users");

	
if (mysql_select_db('community', $con)) 
{
	//mysql_query("UPDATE userlist set username = 'shami' WHERE userID = 1001;");
	echo"ekade ala!";
	$userID =$_POST["userID"];
	$userID1 = (int) $userID;
	
	$insideTemp = (float)$_POST["insideTemp"];
	
	$outsideTemp = (float)$_POST["outsideTemp"];
	$setTemp = (float)$_POST["setTemp"];
	$ac_mode = $_POST["ac_mode"];
	
	$q = mysql_query("SELECT EXISTS(SELECT * FROM AC_Data WHERE userID = '$userID')");
	
	if(mysql_result($q,0) == 1)
	{
		
		//mysql_query("UPDATE userlist set username = 'abc' WHERE userID = 1001;");
		$result = mysql_query("UPDATE AC_Data set insideTemp = '$insideTemp',outsideTemp = '$outsideTemp',setTemp ='$setTemp',ac_mode = '$ac_mode' WHERE userID = '$userID';");
		
	}
	else 
	{
		echo"entered in else";
		$result = mysql_query("INSERT INTO AC_Data (userID, insideTemp, outsideTemp, setTemp, ac_mode)
		VALUES ('$userID','$insideTemp','$outsideTemp','$setTemp', '$ac_mode')");
		print("New User Inserted");
	}

	if($result)
		print("true");
	else
		print("false");
}
	
mysql_close();
?>
