<?PHP
$hostname_localhost ="localhost";
$database_localhost ="id5395327_seguridad";
$username_localhost ="id5395327_admin";
$password_localhost ="123456";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
mysqli_set_charset($conexion,"utf8");

	$idIngresos= $_POST["idIngresos"];
	$idGuardia = $_POST["idGuardia"];
	$nombre = $_POST["nombre"];
	$apellido = $_POST["apellido"];
	$dni = $_POST["dni"];
	$motivo = $_POST["motivo"];
	date_default_timezone_set('America/Argentina/La_Rioja');
	$fechaHora = date('y-m-d H:i:s');
	$imagenRegistro = $_POST["imagenRegistro"];
	$num_aleatorio = rand(1,10000000);
	$path = "fotos/$num_aleatorio.jpg";
	//$url = "http://$hostname_localhost/seguridad/$path";
	//$url = "imagenes/".$nombre.".jpg";

	file_put_contents($path,base64_decode($imagenRegistro));
	$bytesArchivo=file_get_contents($path);

	$sql="INSERT INTO ingresos VALUES (?,?,?,?,?,?,?,?)";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('isssssss',$idIngresos,$idGuardia,$nombre,$apellido,$dni,$motivo,$bytesArchivo,$fechaHora);
		
	if($stm->execute()){
		echo "registra";
	}else{
		echo "noRegistra";
	}
	
	mysqli_close($conexion);
?>

