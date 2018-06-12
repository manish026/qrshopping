<?php

include 'DBconnection.php';


//  Returns JSON of Patient 

	function getShop($shop){
		global $shopTable,$shop_email;
		
		$sql = "SELECT * FROM $shopTable WHERE $shop_email = '$shop'";
		$result = executeQuery($sql);

		return getJSON($result);

	}

	function getAllShop(){
		global $shopTable,$shop_email;
		
		$sql = "SELECT * FROM $shopTable";
		$result = executeQuery($sql);

		return getJSON($result);
	}

	//  Insert a patient 

	function addShop($name,$pass,$email,$address,$dob,$contact){
		
		global $shopTable,$shop_name,$shop_pass,$shop_email,$shop_address,$shop_dob,$shop_contact;

		$json = getShop($email);
		$obj = json_decode($json);
		if(count($obj)>0){

			$res = new Result();
			$res->status = false;
			$res->message = "User already exists";

			return json_encode($res);
		}else{
			$pass = password_hash($pass, PASSWORD_DEFAULT);
			$sql = "INSERT INTO $shopTable ($shop_name,$shop_pass,$shop_email,$shop_address,$shop_dob,$shop_contact ) VALUES ('$name', '$pass', '$email','$address','$dob','$contact') ";
			
			$result = executeQuery($sql);
			$res = new Result();
			$res->status = true;
			$res->message = "Registration Succesfull";
			return json_encode($res);
		}

	}

	function login($email,$password){
		global $shop_pass;
		$json = getShop($email);

		$obj = json_decode($json);
		if(count($obj)>0){
			$shop = $obj[0];
			if(password_verify($password, $shop->shop_pass) == true){
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
		}else{
			$res = new Result();
				$res->status = false;
				$res->message = "User does not exists";
				return json_encode($res);
		}
		
	}

?>