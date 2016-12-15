<?php
$con = mysql_connect("localhost","pma","1234");

if (!$con){ die('Could not connect: ' . mysql_error());}

mysql_query("DROP SCHEMA USERS");
