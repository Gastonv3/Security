<?PHP
$hostname="localhost";
$database="id5395327_seguridad";
$username="id5395327_admin";
$password="123456";
$json=array();
	if(isset($_GET["codigo"])){
		$codigo=$_GET['codigo'];
		
		$conexion=mysqli_connect($hostname,$username,$password,$database);
		mysqli_set_charset($conexion,"utf8");

		$consulta="SELECT * FROM personalautorizado WHERE codigo = '{$codigo}'";
		$resultado=mysqli_query($conexion,$consulta);

		if($consulta){
		
			if($reg=mysqli_fetch_array($resultado)){
				$json['datos'][]=$reg;
			}
			mysqli_close($conexion);
			echo json_encode($json);
		}



		else{
			$results["idpersona"]='';
			$results["codigoGuardia"]='';
			$results["user"]='';
			$results["pass"]='';
			$results["estado"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
		
	}
	else{
			$results["idpersona"]='';
			$results["codigoGuardia"]='';
			$results["user"]='';
			$results["pass"]='';
			$results["estado"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
?>