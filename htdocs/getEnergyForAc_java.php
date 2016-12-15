<?php
$con = mysql_connect("localhost","root","1234");
mysql_select_db("community");
if (!$con){ die('Could not connect: ' . mysql_error());}

if (mysql_select_db('community', $con))
{
  $userID =(int)$_POST["userID"];
  $ApplianceID =(int)$_POST["ApplianceID"];
  $time1=(string)$_POST["time1"];
  $time2=(string)$_POST["time2"];
  //$floor=$_POST["floor"];
  //$room=$_POST["room"];

	$p=mysql_query("SELECT * FROM Energy_Consumption WHERE userID = '$userID' and ApplianceID ='1' and curr_time  BETWEEN '$time1' and '$time2'");

	while($e=mysql_fetch_assoc($p))
		$output[]=$e;

	print(json_encode($output));
	
}

mysql_close();
?>
