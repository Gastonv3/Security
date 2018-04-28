<?PHP
$hostname_localhost ="localhost";
$database_localhost ="id5395327_seguridad";
$username_localhost ="id5395327_admin";
$password_localhost ="123456";

$json=array();
	if(isset($_GET["idPersonalAutorizado"]) && isset($_GET["idGuardia"])){
		$idPersonalAutorizado=$_GET['idPersonalAutorizado'];
		$idGuardia=$_GET['idGuardia'];

		date_default_timezone_set('America/Argentina/La_Rioja');
	$fechaHora = date('y-m-d H:i:s');
		$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
		mysqli_set_charset($conexion,"utf8");

		$insert="INSERT INTO ingresosautorizados(idPersonalAutorizado, idGuardia, fechaHora) VALUES ('{$idPersonalAutorizado}','{$idGuardia}','{$fechaHora}')";
		$resultado_insert=mysqli_query($conexion,$insert);
		
		if($resultado_insert){
			$consulta="SELECT * FROM ingresosautorizados";
			$resultado=mysqli_query($conexion,$consulta);
			
			if($registro=mysqli_fetch_array($resultado)){
				$json['controles'][]=$registro;
			}
			mysqli_close($conexion);
			echo json_encode($json);
		}
		else{
			$resulta["documento"]=0;
			$resulta["nombre"]='No Registra';
			$resulta["profesion"]='No Registra';
			$json['controles'][]=$resulta;
			echo json_encode($json);
		}
		
	}
	else{
			$resulta["documento"]=0;
			$resulta["nombre"]='WS No retorna';
			$resulta["profesion"]='WS No retorna';
			$json['controles'][]=$resulta;
			echo json_encode($json);
		}
?>