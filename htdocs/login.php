<?php
//$con = mysql_connect("localhost","root","student");
mysql_connect("localhost","root","1234");
mysql_select_db("users");

$usr=$_POST["username"];
$pwdIn=$_POST["password"];
$q=mysql_query("SELECT password,userID FROM userlist WHERE username = '$usr' AND password = '$pwdIn'");

while($e=mysql_fetch_assoc($q)){
	$output[]=$e;
	$pwd = $e['password'];
}
if($pwd == $pwdIn)
	print(json_encode($output));
else
	print("false");

mysql_close();
?>