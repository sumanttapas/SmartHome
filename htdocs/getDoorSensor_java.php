<?php
//$con = mysql_connect("localhost","root","student");
mysql_connect("localhost","root","1234");
mysql_select_db("community");

$userID = $_POST["userID"];
//$rows = array();
$q=mysql_query("SELECT * FROM door_sensors WHERE userID = $userID");

while($e=mysql_fetch_assoc($q)){
	$output[] = $e;
}

print(json_encode($output));
//echo json_encode($rows);

mysql_close();
?>
