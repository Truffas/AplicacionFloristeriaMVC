CREATE DATABASE if not exists floristeria;
--
USE floristeria;
--
CREATE TABLE IF NOT EXISTS contactos (
idcontacto int auto_increment primary key,
nombre varchar(50) not null,
apellidos varchar(150) not null,
fechanacimiento date,
pais varchar(50));
--
CREATE TABLE IF NOT EXISTS ceremonias (
idceremonia int auto_increment primary key,
tipo varchar(100)  not null,
otro varchar(100),
fechaentrega date,
lugarentrega varchar(20),
direccion varchar(500));
--
CREATE TABLE IF NOT EXISTS adornos (
idadorno int auto_increment primary key,
tipo varchar(100)  not null,
otro varchar(100),
tipoflores varchar(100),
opciones varchar(50),
mensaje varchar(500));
--
CREATE TABLE IF NOT EXISTS pedidos (
idpedido int auto_increment primary key,
idcontacto int not null,
idceremonia int not null,
idadorno int not null,
fecha date,
comentario varchar(40),
precio float not null);
--
alter table pedidos
add foreign key (idcontacto) references contactos(idcontacto),
add foreign key (idceremonia) references ceremonias(idceremonia);
add foreign key (idadorno) references adornos(idadorno);
--
delimiter ||
create function existeContacto (f_name varchar(50))
returns bit
begin
	declare i int;
	set i=0;
	while (i<(select max(idcontacto) from contactos)) do
	if ((select concat(apellidos,', ',nombre) from contactos
		where idcontacto = (i+1)) like f_name)
	then return 1;
	end if;
	set i=i+1;
	end while;
	return 0;
end; ||
delimiter ;


