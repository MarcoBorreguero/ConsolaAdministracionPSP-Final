# ConsolaAdministracionPSP-Final

Última entrega yproyecto final para la asignatura de PSP.

Para su correcta ejecución, ha de incluirse una base de datos en MySQL con el siguiente script SQL:

--
-- SCRIPT SQL
--

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+3:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `bd_consola`
--

DROP DATABASE bd_consola;

CREATE DATABASE bd_consola;

USE bd_consola

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tickets`
--

CREATE TABLE IF NOT EXISTS `tickets` (
  `pk` int(12) NOT NULL AUTO_INCREMENT,
  `idAdminR` int(10) NOT NULL,
  `idCaso` int(10) NOT NULL,
  `idTicket` int(10) NOT NULL,
  `fecha` varchar(10) NOT NULL,
  `asunto` varchar(100) NOT NULL,
  `prioridad` varchar(15) NOT NULL,
  `descripcion` varchar(500) NOT NULL,
  `estado` varchar(15) NOT NULL,
  PRIMARY KEY (`pk`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=19 ;

--
-- Volcado de datos para la tabla `tickets`
--

INSERT INTO `tickets` (`pk`, `idAdminR`, `idCaso`, `idTicket`, `fecha`, `asunto`, `prioridad`, `descripcion`, `estado`) VALUES
(13, 1, 1, 1, '05/03/2019', 'Problema de red', 'MEDIA', 'Se ha encontrado un problema con la red Ethernet', 'ABIERTO'),
(14, 1, 2, 1, '05/03/2019', 'Problema de red WIFI', 'ALTA', 'Ahora el problema es con la red WIFI', 'ABIERTO'),
(15, 2, 1, 1, '05/03/2019', 'Problema con el sistema de reservas', 'ALTA', 'Se ha encontrado un error grave en la gestión del sistema de las reservas de vuelos', 'ABIERTO'),
(16, 3, 1, 1, '05/03/2019', 'Problema con el estado de los billetes a Madrid', 'MEDIA', 'No es posible cancelar billetes a Madrid para los usuarios', 'ABIERTO'),
(17, 4, 1, 1, '05/03/2019', 'Problema enel envío de mensajes', 'BAJA', 'A veces, es imposible enviar mensajes a los clientes.', 'CERRADO'),
(18, 4, 2, 1, '05/03/2019', 'Problema con los datos de los vuelos de Sevilla', 'ALTA', 'Ha ocurrido un error grave de corrupción en los datos de los vuelos de Sevilla', 'ABIERTO');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
