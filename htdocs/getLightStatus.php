<?php
$con = mysql_connect("localhost","root","1234");
mysql_select_db("community");
if (!$con){ die('Could not connect: ' . mysql_error());}

if (mysql_select_db('community', $con))
{
	$rows=array();
	$userID=(int)$_GET["userID"];
	$q=mysql_query("SELECT * FROM light_status WHERE userID = '$userID'");

	while($e=mysql_fetch_assoc($q))
	{
		$output[]=$e;
	}
	print(json_encode($output));
}

mysql_close();
?>
