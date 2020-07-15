<?php
	$email=$_REQUEST['email'];
	$post=$_REQUEST['post'];
	
	include("config.php");

    $n=10;
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $generatedpassword= '';

    for ($i = 0; $i < $n; $i++) {
    $index = rand(0, strlen($characters) - 1);
    $generatedpassword .= $characters[$index];
    }

    /*Hash*/

    $salt="qscefbthm";

    $secure=sha1($generatedpassword,$salt);

    if($post=="customer"){
    $addtodatabase="UPDATE customer SET Password='$secure' WHERE Email='$email'";
    $ex = mysqli_query($con,$addtodatabase);
    }
    else if($post=="employee"){
    $addtodatabase="UPDATE employee SET Password='$secure' WHERE Email='$email'";
    $ex = mysqli_query($con,$addtodatabase);
    }
				
		date_default_timezone_set('Etc/UTC');
		
		require '/usr/share/php/libphp-phpmailer/class.phpmailer.php';
		require '/usr/share/php/libphp-phpmailer/class.smtp.php';
		
		$mail = new PHPMailer;
		$mail->setFrom('qmuzwncy@gmail.com','Restaurant Manager');
		$mail->addAddress($email);
		$mail->Subject = 'Request For New Password';
		$mail->Body = file_get_contents("http://34.93.41.224/TempletForResetPassowrd.php?password=$generatedpassword"); 
		$mail->IsSMTP();
		$mail->SMTPSecure = 'tls';
		$mail->Host = 'smtp.gmail.com';
		$mail->SMTPAuth = true;
		$mail->Port = 587;
		
		$mail->isHTML(true);                                  // Set email format to HTML
		
		$mail->Username = 'hirestaurantmanager@gmail.com';
		$mail->Password = 'Database@123';
		
		$mail->send();	
	
		
		
?>


			