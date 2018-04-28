<?PHP
$hostname="localhost";
$database="id5395327_seguridad";
$username="id5395327_admin";
$password="123456";

$json=array();
	if(isset($_GET["idControles"])){
		$idControles=$_GET['idControles'];


		$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
		mysqli_set_charset($conexion,"utf8");

		$sql="DELETE FROM controles WHERE idControles= ?";
		$stm=$conexion->prepare($sql);
		$stm->bind_param('i',$idControles);
			
		if($stm->execute()){
			echo "elimina";
		}else{
			echo "noElimina";
		}
		
		mysqli_close($conexion);
	}
	else{
		echo "noExiste";
	}