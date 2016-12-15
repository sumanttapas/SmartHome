<?php
$con = mysql_connect("localhost","root","1234");
mysql_select_db("community");
if (!$con){ die('Could not connect: ' . mysql_error());}

if (mysql_select_db('community', $con))
{
  
  $q=mysql_query("SELECT energy FROM Energy");
  while($e=mysql_fetch_assoc($q))
		$output[]=$e;

	print(json_encode($output));
}
mysql_close();
?>
