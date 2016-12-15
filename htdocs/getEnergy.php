<?php
$con = mysql_connect("localhost","root","1234");
mysql_select_db("community");
if (!$con){ die('Could not connect: ' . mysql_error());}

if (mysql_select_db('community', $con))
{
	$userID =(int)$_GET["userID"];
  $ApplianceID =(int)$_GET["ApplianceID"];
  $time1=(string)$_GET["time1"];
  $time2=(string)$_GET["time2"];
  $floor=$_GET["floor"];
  $room=$_GET["room"];

//echo "SELECT * FROM Energy_Consumption WHERE userID = '$userID' and ApplianceID ='$ApplianceID' and floor='$floor' and curr_time  BETWEEN '$time1' and '$time2';";
	$q=mysql_query("SELECT * FROM Energy_Consumption WHERE userID = '$userID' and ApplianceID ='$ApplianceID' and floor='$floor' and curr_time  BETWEEN '$time1' and '$time2'");

	while($e=mysql_fetch_assoc($q))
		$output[]=$e;

	print(json_encode($output));
}

//exec("java -jar EnergyConsumption.jar ", $output1);
//print(json_encode($output1));
mysql_close();
?>
