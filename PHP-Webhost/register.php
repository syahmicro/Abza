<?php
	include('connection.php');
		
	$fname = addslashes($_POST['fname']);
	$lname = addslashes($_POST['lname']);
	$phone = addslashes($_POST['phone']);
	$password = addslashes($_POST['pwd']);
	
	$findPhone = mysql_query("SELECT id FROM abzaa_user WHERE phone = '".$phone."' ");
	if (mysql_num_rows($findPhone) == 0) {
		mysql_query("INSERT INTO abzaa_user (fname, lname, phone, password, date_register, status) VALUES ('".$fname."', '".$lname."', '".$phone."', '".$password."', '".date('Y-m-d H:i:s')."', 'Active')");
	
		$response['status'] = 1;
	} else {
		$response['status'] = 0;
	}
	
	echo json_encode($response);
	
?>