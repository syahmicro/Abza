<?php
	include('connection.php');
	
	$fname = addslashes($_POST['fname']);
	$lname = addslashes($_POST['lname']);
	$id = $_POST['id'];
	
	mysql_query("UPDATE abzaa_user SET fname = '".$fname."', lname = '".$lname."' WHERE id = '".$id."' ") or die($response['error'] = mysql_error());
	
	$response['status'] = 1;
	$response['fname'] = $fname;
	$response['lname'] = $lname;
	$response['id'] = $id;
	
	echo json_encode($response);	
?>