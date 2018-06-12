<?php

include 'ProductAPI.php';


if(isset($_GET['GetProduct'])){

	$json = $_GET['GetProduct'];
	$obj = json_decode($json);
	echo getProduct($obj->id,$obj->shopid);
}

if(isset($_GET['AddProduct'])){
	
	$json = $_GET['AddProduct'];
	$obj = json_decode($json);
	echo addProduct($obj->id,$obj->name,$obj->price,$obj->shopid);
}

if(isset($_GET['AllProduct'])){

	$json = $_GET['AllProduct'];
	$obj = json_decode($json);
	echo getAllProducts($obj->shopid);
}


?>