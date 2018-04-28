<?PHP
$hostname="localhost";
$database="id5395327_seguridad";
$username="id5395327_admin";
$password="123456";
$json=array();

		$conexion=mysqli_connect($hostname,$username,$password,$database);
		
		$consulta="SELECT c.idControles, c.idGuardia, c.idLugares, c.latitud, c.longitud, l.emails 
		FROM controles c inner join lugares l on c.idLugares = l.idLugares 
		ORDER BY c.idControles DESC LIMIT 1";
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
			$results["codigo_guarda"]='';
			$results["login"]='';
			$results["password"]='';
			$results["estado"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
		
	
?>