<?PHP
$hostname_localhost ="localhost";
$database_localhost ="id5395327_seguridad";
$username_localhost ="id5395327_admin";
$password_localhost ="123456";

$json=array();
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
		mysqli_set_charset($conexion,"utf8");

		$consulta="select * from lugares";
		$resultado=mysqli_query($conexion,$consulta);
		
		while($registro=mysqli_fetch_array($resultado)){
			$result["idLugares"]=$registro['idLugares'];
			$result["nombreLugar"]=$registro['nombreLugar'];
			$result["ubicacion"]=$registro['ubicacion'];			
			$result["emails"]=$registro['emails'];
			$result["estado"]=$registro['estado'];
			$result["imagenLugar"]=base64_encode($registro['imagenLugar']);
			$json['lugares'][]=$result;
			//echo $registro['id'].' - '.$registro['nombre'].'<br/>';
		}
		mysqli_close($conexion);
		echo json_encode($json);
?>