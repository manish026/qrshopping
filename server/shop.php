<?php

include 'shopAPI.php';




if(isset($_GET['getShop'])){
	$json = json_decode($_GET['getShop']);

	echo login($json->customer_email,$json->customer_pass);
} 


if(isset($_GET['getshops'])) echo getAllShop();



if(isset($_GET['addshop'])){
	$json = $_GET['addshop'];
	$obj = json_decode($json);

	echo addShop($obj->customer_name,$obj->customer_pass,$obj->customer_email,$obj->customer_address,$obj->customer_dob,$obj->customer_contact);
}

?>