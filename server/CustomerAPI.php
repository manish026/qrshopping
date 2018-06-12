<?php

include 'DBconnection.php';


//  Returns JSON of Patient 

	function getCustomer($customer){
		global $customerTable,$customer_email;
		
		$sql = "SELECT * FROM $customerTable WHERE $customer_email = '$customer'";
		$result = executeQuery($sql);

		return getJSON($result);

	}

	//  Insert a patient 

	function addCustomer($name,$pass,$email,$address,$dob,$contact){
		
		global $customerTable,$customer_id,$customer_name,$customer_pass,$customer_email,$customer_address,$customer_dob,$customer_contact;

		$json = getCustomer($email);
		$obj = json_decode($json);
		if(count($obj)>0){

			$res = new Result();
			$res->status = false;
			$res->message = "User already exists";

			return json_encode($res);
		}else{
			$pass = password_hash($pass, PASSWORD_DEFAULT);
			$sql = "INSERT INTO $customerTable ($customer_name,$customer_pass,$customer_email,$customer_address,$customer_dob,$customer_contact ) VALUES ( '$name', '$pass', '$email','$address','$dob','$contact') ";
			$result = executeQuery($sql);
			$res = new Result();
			$res->status = true;
			$res->message = "Registration Succesfull";
			return json_encode($res);
		}

	}

	function login($email,$password){
		global $customer_pass;
		$json = getCustomer($email);
		$obj = json_decode($json);
		if(count($obj)>0){
			$customer = $obj[0];
			if(password_verify($password, $customer->customer_pass) == true){
				$res = new Result();
				$res->status = true;
				$res->result = $json;
				$res->message = "Login sucessful";
				return json_encode($res);
			}else{
				$res = new Result();
				$res->status = false;
				$res->message = "Incorrect Password";
				return json_encode($res);
			}
		}
		
	}

?>