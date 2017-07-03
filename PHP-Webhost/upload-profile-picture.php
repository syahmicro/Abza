<?php
	if ($_SERVER['REQUEST_METHOD']=='POST'){
		
		$userId = $_POST['userId']; 
		$imageName= $userId.'-'.date('Ymd-His');

		include 'connection.php';
		$image = $_POST['image'];
		$path = "profile-picture/" . $imageName. ".jpg";
		mysql_query("
		 	UPDATE abzaa_user SET picture = '".$imageName.".jpg' WHERE id = '".$userId."' 
		");
		 
		$done = file_put_contents($path, base64_decode($image));
		$response['status'] = 1;
		$response['picture'] = $imageName.'.jpg';		
		echo json_encode($response);
	} else {
	 	$response['status'] = 2;
		
		echo json_encode($response);
	}

?>