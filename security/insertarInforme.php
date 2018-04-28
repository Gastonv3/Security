<?PHP
$hostname_localhost ="localhost";
$database_localhost ="id5395327_seguridad";
$username_localhost ="id5395327_admin";
$password_localhost ="123456";


$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
mysqli_set_charset($conexion,"utf8");

	$idinforme= $_POST["idinforme"];
	$idControles = $_POST["idControles"];
	$tituloInforme = $_POST["tituloInforme"];
	$informe = $_POST["informe"];

/*date_default_timezone_set('America/Argentina/La_Rioja');
	$fecha_hora = date('y-m-d H:i:s');*/
	$imagenInforme = $_POST["imagenInforme"];
	$num_aleatorio = rand(1,10000000);
	$path = "fotos/$num_aleatorio.jpg";

	//$url = "http://$hostname_localhost/seguridad/$path";
	//$url = "imagenes/".$nombre.".jpg";

	file_put_contents($path,base64_decode($imagenInforme));
	$bytesArchivo=file_get_contents($path);
	//mysql_query("SET NAMES 'utf8'");

	$sql="INSERT INTO informes VALUES (?,?,?,?,?)";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('issss',$idinforme,$idControles,$tituloInforme,$informe,$bytesArchivo);
		
	if($stm->execute()){
		echo "registra";
	}else{
		echo "noRegistra";
	}
	
	mysqli_close($conexion);
?>

