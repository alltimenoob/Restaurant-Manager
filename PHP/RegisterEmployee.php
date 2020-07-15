<?php
	// Registration Of Employee From Admin Tools
	// Module : Admin 
	
	include("config.php");

	$username = $_REQUEST["username"];
	$name = $_REQUEST["name"];
	$email = $_REQUEST["email"];
	$contact = $_REQUEST["contact"];
	$address = $_REQUEST["address"];
	$birthday = $_REQUEST["birthday"];
	$salary = $_REQUEST["salary"];
	$rid = $_REQUEST["Rid"];
	$joindate = date("Y-m-d");
	
	
	$check = "select Username from customer where Username = '$username'";       // Search Customers
	$check1 = "select Username from employee where Username = '$username'";      // Search Employee
	$check2 = "select Username from restaurant where Username = '$username'";    // Search Restaurant    For Already Existing Users..
	
	$query = mysqli_query($con,$check);
	$query1 = mysqli_query($con,$check1);
	$query2 = mysqli_query($con,$check2);
	
	
	if($username==null || $email==null || $name==null || $contact==null || $address==null || $birthday==null || $salary==null)              // If Username & Password Is Empty...
	{
		$array = array("error"=> 0);     //Please Provide Details..
	}
	elseif(mysqli_num_rows($query) > 0)
	{
		$array = array("error"=> 3);     //User Already Exists...
	}
	elseif(mysqli_num_rows($query1) > 0)
	{
		$array = array("error"=> 3);     //User Already Exists...
	}
	elseif(mysqli_num_rows($query2) > 0)
	{
		$array = array("error"=> 3);    //User Already Exists...
	}
	else                                             // If Not Exist Then...
	{
		$n=10;
		$characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
		$generatedpassword= '';

		for ($i = 0; $i < $n; $i++) {
			$index = rand(0, strlen($characters) - 1);
			$generatedpassword .= $characters[$index];
		}
		
		$salt="qscefbthm";
		$password=sha1($generatedpassword,$salt);
		
		$query3 =  mysqli_query($con,"INSERT INTO `employee` VALUES(NULL,'$username','$name','$email','$contact','$address','$password','$birthday','$joindate','$salary','$rid')");
		
		if($query3)
		{
			date_default_timezone_set('Etc/UTC');
			
			require '/usr/share/php/libphp-phpmailer/class.phpmailer.php';
			require '/usr/share/php/libphp-phpmailer/class.smtp.php';
			
			$mail = new PHPMailer;
			$mail->setFrom('qmuzwncy@gmail.com','Restaurant Manager');
			$mail->addAddress($email);
			$mail->Subject = 'Welcome To Restaurant Manager';
			$mail->Body = file_get_contents("http://34.93.41.224/TempletForWelcomeMailEmployee.php?password=$generatedpassword&username=$username&name=$name"); 
			$mail->IsSMTP();
			$mail->SMTPSecure = 'tls';
			$mail->Host = 'smtp.gmail.com';
			$mail->SMTPAuth = true;
			$mail->Port = 587;
			
			$mail->isHTML(true);
			$mail->Username = 'hirestaurantmanager@gmail.com';
			$mail->Password = 'Database@123';
			$mail->send();
			
			$array = array("error"=> 1);
		}
		else
		{
			$array = array("error"=> 2);
		}
	}
	 echo json_encode($array);
	
	mysqli_close($con);
	
	//Error=>0  : Please Provide Details..
	//Error=>1  : User Find Sucessufully OR Login Sucessfully..
	//Error=>2  : Invalid Login Details OR No Such User Exists..
	//Error=>3  : User Already Exists...
	
?>
