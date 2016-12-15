<?php
//$con = mysql_connect("localhost","root","student");
$con = mysql_connect("localhost","root","1234");

mysql_select_db("community");

if (!$con){ die('Could not connect: ' . mysql_error());}

//mysql_select_db("users");


if (mysql_select_db('community', $con))
{

	$userID =(int)$_GET["userID"];
	$q=mysql_query("SELECT * FROM lock_status WHERE userID = '$userID'");

	while($e=mysql_fetch_assoc($q))
		$output[]=$e;

	print(json_encode($output));
}

mysql_close();
?>
