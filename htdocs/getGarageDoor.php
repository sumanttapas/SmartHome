<?php
//$con = mysql_connect("localhost","root","student");
mysql_connect("localhost","root","1234");
mysql_select_db("community");

$userID = $_GET["userID"];
//$rows = array();
$q=mysql_query("SELECT * FROM garage_doors WHERE userID = $userID");

while($e=mysql_fetch_assoc($q)){
	$rows[] = $e;
}
echo json_encode($rows);


mysql_close();
?>