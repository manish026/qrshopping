<?php

	include 'Response.php';
	include 'Configuration.php';


	// Creates db connection 

	function connectDb(){

	 $servername = "localhost";
	 $username = "root";
	 $password = "";
	 $DbName = $GLOBALS['databaseName'];

	 $conn = new mysqli($servername, $username, $password,$DbName);
	

		
	if ($conn->connect_error) {
		
    		die("Connection failed: " . $conn->connect_error);
		} else{
			// echo "Connected successfully";
			return $conn;
		}

	}

	 // Executes sql query 

	function executeQuery($query){
		$conn = connectDb();
		$data = $conn->query($query);
		mysqli_close($conn);
		return $data;
	}

	//Returns JSON object

	function getJSON($result){
		
		$array = array();

		if($result != null ){	
		for($x = 0; $x < $result->num_rows; $x++) {
    		$array[$x] = $result->fetch_assoc();
		}
	}

	return json_encode($array);

	}

	

	


?>