<?php
	if ($_SERVER['REQUEST_METHOD']=='POST'){
		
		$groupId = $_POST['groupId']; 
		$imageName= $groupId.'-'.date('Ymd-His');
		
		include 'connection.php';
		$image = $_POST['image'];
		$path = "group-picture/" . $imageName. ".jpg";
		mysql_query("
		 	UPDATE abzaa_group SET picture = '".$imageName.".jpg' WHERE id = '".$groupId."' 
		");
		 
		$done = file_put_contents($path, base64_decode($image));
		$response['status'] = 1;
		echo json_encode($response);
	} else {
	 	$response['status'] = 2;	
		echo json_encode($response);
	}

?>