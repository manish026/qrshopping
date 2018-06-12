<?php

include 'DBconnection.php';


//  Returns JSON of Patient 

	function getProduct($product,$shopid){
		
		$json = getShopProduct($product,$shopid);
		$res = new Result();
		$res->status = true;
		$res->result = $json;

		$obj = json_decode($json);
		// echo "".$obj;
		
		if(count($obj)>0){
			$product = $obj[0];
			$product->product_quantity = $product->product_quantity - 1;
			$sql = "Update products set product_quantity = ".$product->product_quantity." Where ".$GLOBALS['shop_id']." = '".$product->product_owner."' AND ".$GLOBALS['product_id']." = ".$product->product_id;
			$result = executeQuery($sql);
		}

		return json_encode($res);

	}

	function getShopProduct($product,$shopid){

		$sql = "SELECT * FROM ".$GLOBALS['productTable']." WHERE ".$GLOBALS['product_id']." = ".$product. " AND ".$GLOBALS['shop_id']." = '".$shopid."'";
		// echo "$sql";
		$result = executeQuery($sql);
		return getJSON($result);

	}

	//  Insert a patient 

	function addProduct($id,$name,$price,$shopid){
		
		$json = getShopProduct($id,$shopid);
		$obj = json_decode($json);
		
		
		if(count($obj)>0){
			$product = $obj[0];
			$product->product_quantity = $product->product_quantity + 1;
			$sql = "Update products set product_quantity = ".$product->product_quantity." Where ".$GLOBALS['shop_id']." = '".$product->product_owner."' AND ".$GLOBALS['product_id']." = ".$product->product_id;
			$result = executeQuery($sql);
			$res = new Result();
			$res->status = true;
			$res->message = "Product added succesfull";

			// return json_encode($res);
		}else{
			$sql = "INSERT INTO ".$GLOBALS['productTable'].
			" (".$GLOBALS['product_id'].", ".$GLOBALS['product_name'].", ".$GLOBALS['product_price'].", ".$GLOBALS['shop_id']." 
			) VALUES ('".$id."', '".$name."', '".$price."', '".$shopid."') ";
			$result = executeQuery($sql);
			$res = new Result();
			$res->status = true;
			$res->message = "Product added succesfull";
			return json_encode($res);
		}

	}

	//  Returns JSON of products id 

	function getAllProducts($shopid){
		global $productTable,$shop_id;
		
		$sql = "SELECT * FROM $productTable Where $shop_id = ".$shopid ;
		$result = executeQuery($sql);
		$res = new Result();
		$res->status = true;
		$res->result = getJSON($result);
		return json_encode($res);

	}

?>