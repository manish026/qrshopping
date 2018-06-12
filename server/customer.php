<?php

include 'CustomerAPI.php';



if(isset($_GET['getCustomer'])){
	$json = json_decode($_GET['getCustomer']);

	echo login($json->customer_email,$json->customer_pass);
} 






if(isset($_GET['addcustomer'])){
	$json = $_GET['addcustomer'];
	$obj = json_decode($json);
	echo addCustomer($obj->customer_name,$obj->customer_pass,$obj->customer_email,$obj->customer_address,$obj->customer_dob,$obj->customer_contact);
}

?>