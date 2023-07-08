drop database if exists DBDens2021223;
create database DBDens2021223;
use DBDens2021223;

create table Pacientes(

	codigoPaciente 		int not null,
    nombresPaciente 	varchar(50) not null,
    apellidosPaciente 	varchar(50) not null,
    sexo 				char not null,
    fechaNacimiento 	date not null,
    direccionPaciente 	varchar(100) not null,
    telefonoPersonal 	varchar(8) not null,
    fechaPrimeraVisita 	date,
    
    primary key PK_codigoPaciente(codigoPaciente)

);

create table Especialidades(

	codigoEspecialidad 	int not null auto_increment,
    descripcion 		varchar(100) not null,
    
    primary key PK_codigoEspecialidad (codigoEspecialidad)

);

create table Medicamentos(

	codigoMedicamento 	int not null auto_increment,
    nombreMedicamento 	varchar(100) not null,
    
    primary key PK_codigoMedicamento (codigoMedicamento)

);

create table Doctores(

	numeroColegiado 	int not null,
    nombresDoctor 		varchar(50) not null,
    apellidosDoctor 	varchar(50) not null,
    telefonoContacto 	varchar(8) not null,
    codigoEspecialidad 	int not null,
    
    primary key PK_numeroColegiado (numeroColegiado),
    
    constraint FK_Doctores_Especialidades foreign key (codigoEspecialidad)
		references Especialidades (codigoEspecialidad)

);

create table Recetas(

	codigoReceta 		int not null auto_increment,
    fechaReceta 		date not null,
    numeroColegiado 	int not null,
    
    primary key PK_codigoReceta (codigoReceta),
    
    constraint FK_Recetas_Doctores foreign key (numeroColegiado)
		references  Doctores (numeroColegiado)

);

create table DetalleReceta(

	codigoDetalleReceta	int not null auto_increment,
    dosis 				varchar(100) not null,
    codigoReceta 		int not null,
    codigoMedicamento 	int not null,
    
    primary key PK_codigoDetalleReceta (codigoDetalleReceta),
    
    constraint FK_DetalleReceta_Receta foreign key (codigoReceta)
		references Recetas(codigoReceta),
        
	constraint FK_DetalleReceta_Medicamentos foreign key (codigoMedicamento)
		references Medicamentos(codigoMedicamento)

);

create table Citas(

	codigoCita 			int not null auto_increment,
    fechaCita 			date not null,
    horaCita 			time not null,
    tratamiento 		varchar(150),
    descripCondActual 	varchar(255) not null,
    codigoPaciente 		int not null,
    numeroColegiado 	int not null,
    
    primary key PK_codigoCita (codigoCita),
    
    constraint FK_Citas_Pacientes foreign key (codigoPaciente)
		references Pacientes (codigoPaciente),
        
	constraint FK_Citas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)

);

-- -------------------------------------------------------------------
-- PROCEDIMIENTOS ALMACENADOS

-- ----------------------------------------------------------------- PACIENTES -------------------------------------------------------------------------------

-- ----------------------------------------------------------------- AGREGAR PACIENTES -------------------------------------------------------------------------------

Delimiter $$
	Create procedure sp_AgregarPaciente(in codigoPaciente int, in nombresPaciente varchar(50), in apellidosPaciente varchar(50), in sexo char, in fechaNacimiento date, in direccionPaciente varchar(100), in telefonoPersonal varchar(8), in fechaPrimeraVisita date)
		Begin
			Insert into Pacientes (codigoPaciente, nombresPaciente, apellidosPaciente, sexo, fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita)
				values (codigoPaciente, nombresPaciente, apellidosPaciente, upper(sexo), fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita);

        end $$

Delimiter ;

call sp_AgregarPaciente(1,'Juan Alejandro','Perez García','m','2000-05-15','Zona 11 Guatemala','87654321',now());
call sp_AgregarPaciente(2,'Gerson Aarón','Matta Aguilar','M','2004-09-20','Zona 11 Guatemala','12345678',now());
call sp_AgregarPaciente(3,'Juan Alejandro','Perez García','m','2000-05-15','Zona 11 Guatemala','87654321',now());
call sp_AgregarPaciente(4,'Sara Cintia','Pallares Ubeda','F','1990-04-12','Zona 12 Guatemala','56987456',now());
call sp_AgregarPaciente(5,'Chloe Iria ','Poveda Maldonado','F','1995-08-25','Zona 10 Guatemala','32165498',now());
call sp_AgregarPaciente(6,'Juan Angel','Blazquez Melendez','M','1970-05-15','Zona 11 Guatemala','87654321',now());
call sp_AgregarPaciente(7,'Luis Francisco','Frutos Tapia','M','2001-05-23','Zona 11 Guatemala','87654321',now());
call sp_AgregarPaciente(8,'Gregorio Aarón','Castaño Moreira','M','1960-07-12','Zona 11 Guatemala','87654321',now());
call sp_AgregarPaciente(9,'Maria Adriana','Padro Arce','F','1959-11-02','Zona 11 Guatemala','87654321',now());
call sp_AgregarPaciente(10,'Samira Alexia','Macia Gimeno','F','1980-12-22','Zona 11 Guatemala','87654321',now());
call sp_AgregarPaciente(11,'Jose Carlos','Gonzales Argon','M','1959-11-02','Zona 11 Guatemala','87654321',now());
call sp_AgregarPaciente(12,'Fernando Samuel','Valenzuela Alvarado','M','1980-12-22','Zona 12 Guatemala','87654321',now());

-- LISTAR PACIENTES

Delimiter $$
	Create procedure sp_ListarPacientes()
		Begin
        
			Select
				P.codigoPaciente, 
                P.nombresPaciente, 
                P.apellidosPaciente, 
                P.sexo, 
                P.fechaNacimiento, 
                P.direccionPaciente, 
                P.telefonoPersonal, 
                P.fechaPrimeraVisita
            from Pacientes P;
        
        End $$
Delimiter ;

call sp_ListarPacientes();

-- BUSCAR PACIENTE

Delimiter $$
	Create procedure sp_BuscarPaciente(in codPaciente int)
		Begin
        
			Select 
				P.codigoPaciente, 
                P.nombresPaciente, 
                P.apellidosPaciente, 
                P.sexo, 
                P.fechaNacimiento, 
                P.direccionPaciente, 
                P.telefonoPersonal, 
                P.fechaPrimeraVisita
			from Pacientes P
            where codigoPaciente = codPaciente;
        End $$
Delimiter ;

call sp_BuscarPaciente(1);

-- ELIMINAR PACIENTE

Delimiter $$
	Create procedure sp_EliminarPaciente(in codPaciente int)
		Begin
        
			Delete from Pacientes
				where codigoPaciente = codPaciente;
        
        End $$
Delimiter ;

-- call sp_EliminarPaciente(10);

-- EDITAR PACIENTE

Delimiter $$
	Create procedure sp_EditarPaciente(	in codPaciente int, in nomPaciente varchar(50), in apePaciente varchar(50), 
										in sex char, in feNacimiento date, in direPaciente varchar(100), in telPersonal varchar(8), in fePrimeraVisita date)
		Begin
        
			Update Pacientes P
				set
					P.nombresPaciente = nomPaciente,
                    P.apellidosPaciente = apePaciente,
                    P.sexo = sex,
                    P.fechaNacimiento = feNacimiento,
                    P.direccionPaciente = direPaciente,
                    P.telefonoPersonal = telPersonal,
                    P.fechaPrimeraVisita = fePrimeraVisita
                    where codigoPaciente = codPaciente;
        
        End $$
Delimiter ;

call sp_EditarPaciente(2,'Gerson Aarón','Matta Aguilar','M','2004-09-20','Zona 11 Guatemala','12345678',now());


-- ----------------------------------------------------------------- ESPECIALIDADES -------------------------------------------------------------------------------
-- --------- Agregar Especialidades ---------
Delimiter $$
	Create procedure sp_AgregarEspecialidad(in descrip varchar(100))
    Begin
		Insert into Especialidades(descripcion)
			Values(descrip);
	End $$
Delimiter ;

call sp_AgregarEspecialidad('Ortodoncia');
call sp_AgregarEspecialidad('Endodoncia');
call sp_AgregarEspecialidad('Periodoncia');
call sp_AgregarEspecialidad('Prostodoncia');
call sp_AgregarEspecialidad('Protesis Maxilofacial');
call sp_AgregarEspecialidad("Odontopediatría");
call sp_AgregarEspecialidad('Cirugía oral');
call sp_AgregarEspecialidad('Cirugía maxilofacial');
call sp_AgregarEspecialidad('Cariología');
call sp_AgregarEspecialidad('Gnatologia');
call sp_AgregarEspecialidad('Patólogo Oral');



-- --------- Listar Especialidades ---------
Delimiter $$
	Create procedure sp_ListarEspecialidades()
    Begin
		Select
			E.codigoEspecialidad,
            E.descripcion
		From Especialidades E;
	End$$
Delimiter ;

call sp_ListarEspecialidades();

-- --------- Buscar Especialidades ---------
Delimiter $$
	Create procedure sp_BuscarEspecialidad(in codEspecialidad int)
    Begin
		Select
			E.codigoEspecialidad,
            E.descripcion
		From Especialidades E
			where codigoEspecialidad = codEspecialidad;
	End$$
Delimiter ;

call sp_BuscarEspecialidad(1);

-- --------- Eliminar Especialidades ---------
Delimiter $$
	Create procedure sp_EliminarEspecialidad(in codEspecialidad int)
    Begin
		Delete From Especialidades
			where codigoEspecialidad = codEspecialidad;
	End$$
Delimiter ;

-- call sp_EliminarEspecialidad(10);

-- --------- Editar Especialidades ---------
Delimiter $$
	Create procedure sp_EditarEspecialidad(in codEspecialidad int, in descrip varchar(100))
    Begin
		Update Especialidades E
			set
				E.descripcion = descrip
				where codigoEspecialidad = codEspecialidad;
	End$$
Delimiter ;

-- call sp_EditarEspecialidad(9, 'Gnatología');
call sp_ListarEspecialidades();

-- ------------------------------------------------------------------------------ MEDICAMENTOS -------------------------------------------------------------------------------
-- --------- Agregar Medicamentos ---------
Delimiter $$
	Create procedure sp_AgregarMedicamento(in nomMedicamento varchar(100))
    Begin
		Insert into Medicamentos(nombreMedicamento)
			Values(nomMedicamento);
	End$$
Delimiter ;

call sp_AgregarMedicamento('Ticarcilina');
call sp_AgregarMedicamento('Amoxicilina');
call sp_AgregarMedicamento('Sulbactam');
call sp_AgregarMedicamento('Ampicilina');
call sp_AgregarMedicamento('Penicilina sódica');
call sp_AgregarMedicamento('Piperacilina');
call sp_AgregarMedicamento('Ticarcilina');
call sp_AgregarMedicamento('Eritromicina');
call sp_AgregarMedicamento('Tetraciclina');
call sp_AgregarMedicamento('Ácido clavulánico');
call sp_AgregarMedicamento('Penicilina G sódica');

-- --------- Listar Medicamentos ---------
Delimiter $$
	Create procedure sp_ListarMedicamentos()
    Begin
		Select
			M.codigoMedicamento,
			M.nombreMedicamento
		From Medicamentos M;
	End$$
Delimiter ;

call sp_ListarMedicamentos();

-- --------- Buscar Medicamentos ---------
Delimiter $$
	Create procedure sp_BuscarMedicamento(in codMedicamento int)
    Begin
		Select
			M.codigoMedicamento,
			M.nombreMedicamento
		From Medicamentos M
			where codigoMedicamento = codMedicamento;
	End$$
Delimiter ;

call sp_BuscarMedicamento(1);

-- --------- Eliminar Medicamentos ---------
Delimiter $$
	Create procedure sp_EliminarMedicamento(in codMedicamento int)
	Begin
		Delete from Medicamentos
			where codigoMedicamento = codMedicamento;
	End$$
Delimiter ;



-- --------- Editar Medicamentos ---------
Delimiter $$
	Create procedure sp_EditarMedicamento(in codMedicamento int, in nomMedicamento varchar(100))
    Begin
		Update Medicamentos M
			set
				nombreMedicamento = nomMedicamento
			where codigoMedicamento = codMedicamento;
    End$$
Delimiter ;

call sp_EditarMedicamento(2, "Antiinflamatorios ");
call sp_ListarMedicamentos();

-- ------------------------------------------------------------------------------ DOCTORES -------------------------------------------------------------------------------
-- --------- Agregar Doctores ---------
Delimiter $$
	Create procedure sp_AgregarDoctor(in numColegiado int, in nomDoctor varchar(50), in apeDoctor varchar(50),
										in telContacto varchar(8), in codEspecialidad int)
	Begin
		Insert into Doctores(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad)
			Values(numColegiado, nomDoctor, apeDoctor, telContacto, codEspecialidad);
    End$$
Delimiter ; 

call sp_AgregarDoctor(1001, 'José Carlos', 'Fernandez Gomez', '98765432', 2);
call sp_AgregarDoctor(1002, 'Victor Mario', 'Herrera Flores', '32145678', 2);
call sp_AgregarDoctor(1003, 'Julio Hector', 'Perez Ramirez', '65983214', 3);
call sp_AgregarDoctor(1004, 'Martin Nicolás', 'Suarez Garcia', '78945612', 4);
call sp_AgregarDoctor(1005, 'Claudio Jorge', 'Ruiz Perez', '45781236', 5);
call sp_AgregarDoctor(1006, 'Liliana Marta', 'Gutiérrez Alvarez', '78451265', 3);
call sp_AgregarDoctor(1007, 'Mirta Ana', 'Diaz Sosa', '85967412', 2);
call sp_AgregarDoctor(1008, 'Susana Lucia', 'Herrera Acosta', '12657865', 3);
call sp_AgregarDoctor(1009,'Luisa Andrea', 'Rodriguez Juarez', '98786545', 4);
call sp_AgregarDoctor(1010, 'Fabiola Paty', 'Robles Campos', '65457898', 5);
call sp_AgregarDoctor(1011, 'Jose Fabricio', 'Salazar Rosales', '65457898', 5);
call sp_AgregarDoctor(1000,'Fabiola Paty', 'Robles Campos', '65457898', 5);


-- --------- Listar Doctores ---------
Delimiter $$
	Create procedure sp_ListarDoctores()
	Begin
		Select
			D.numeroColegiado,
            D.nombresDoctor,
            D.apellidosDoctor,
            D.telefonoContacto,
            D.codigoEspecialidad
		From Doctores D;
    End$$
Delimiter ;

call sp_ListarDoctores();

-- ---------- Listar Doctores Reporte ---------------

Delimiter $$
	Create procedure sp_ListarDoctoresReporte()
		Begin
        
			select D.numeroColegiado as Codigo, D.nombresDoctor as Nombres, D.apellidosDoctor as Apellidos, D.telefonoContacto as Telefono, E.descripcion as Descripcion 
				from Doctores D
					inner join Especialidades E on E.codigoEspecialidad=D.codigoEspecialidad;
        
        End $$
Delimiter ;
call sp_ListarDoctoresReporte();

-- --------- Buscar Doctores ---------
Delimiter $$
	Create procedure sp_BuscarDoctor(in numColegiado int)
    Begin
		Select
			D.numeroColegiado,
            D.nombresDoctor,
            D.apellidosDoctor,
            D.telefonoContacto,
            D.codigoEspecialidad
		From Doctores D 
			where numeroColegiado = numColegiado;
    End$$
Delimiter ;

call sp_BuscarDoctor(1000);

-- --------- Eliminar Doctores ---------
Delimiter $$
	Create procedure sp_EliminarDoctor(in numColegiado int)
    Begin
		Delete from Doctores
			where numeroColegiado = numColegiado;
    End$$
Delimiter ;

-- call sp_EliminarDoctor(1000);

-- --------- Editar Doctores ---------
Delimiter $$
	Create procedure sp_EditarDoctor(in numColegiado int, in nomDoctor varchar(50), in apellDoctor varchar(50),
										in telContacto varchar(8))
	Begin
		Update Doctores D
			set
				D.numeroColegiado = numeroColegiado,
                D.nombresDoctor = nomDoctor,
                D.apellidosDoctor = apellDoctor,
                D.telefonoContacto = telContacto
				where numeroColegiado = numColegiado;
    End$$
Delimiter ;

call sp_EditarDoctor(1001, 'José Carlos', 'Fernandez Toledo', '98765432');
call sp_ListarDoctores();

-- ------------------------------------------------------------------------------ RECETAS ------------------------------------------------------------------------------
-- --------- Agregar Recetas ---------
Delimiter $$
	Create procedure sp_AgregarReceta(in fecReceta date, in numColegiado int)
    Begin
		Insert into Recetas (fechaReceta, numeroColegiado)
			Values(fecReceta, numColegiado);
	End $$
Delimiter ;

call sp_AgregarReceta('2013-04-15',1001);
call sp_AgregarReceta('2013-03-15',1002);
call sp_AgregarReceta('2014-04-02',1003);
call sp_AgregarReceta('2015-05-01',1004);
call sp_AgregarReceta('2016-06-15',1005);
call sp_AgregarReceta('2017-10-07',1006);
call sp_AgregarReceta('2018-08-21',1007);
call sp_AgregarReceta('2019-09-11',1008);
call sp_AgregarReceta('2020-10-07',1009);
call sp_AgregarReceta('2021-11-21',1010);
call sp_AgregarReceta('2022-12-23',1011);

-- --------- Listar Recetas ---------
Delimiter $$
	Create procedure sp_ListarRecetas()
    Begin
		Select
			R.codigoReceta,
			R.fechaReceta,
            R.numeroColegiado
		from Recetas R;
    End$$
Delimiter ;

call sp_ListarRecetas();

-- ---------- Listar Recetas Reporte ---------------

Delimiter $$
	Create procedure sp_ListarRecetasReporte()
		Begin
        
			select 	R.codigoReceta as Codigo, 
					R.fechaReceta as Fecha, 
                    D.numeroColegiado as Numero_Colegiado, 
                    D.nombresDoctor as NombresDoctor, 
                    D.apellidosDoctor as ApellidosDoctor 
				from Recetas R
					inner join Doctores D on D.numeroColegiado = R.numeroColegiado;
        
        End$$
Delimiter ;

call sp_ListarRecetasReporte();


-- --------- Buscar Recetas ---------

Delimiter $$
	Create procedure sp_BuscarReceta(in codReceta int)
    Begin
		Select
			R.codigoReceta,
			R.fechaReceta,
            R.numeroColegiado
		from Recetas R
			where codigoReceta = codReceta;
    End$$
Delimiter ;

call sp_BuscarReceta(1);

-- --------- Eliminar Recetas ---------
Delimiter $$
	Create procedure sp_EliminarReceta(in codReceta int)
    Begin
		Delete from Recetas
			where codigoReceta = codReceta;
    End$$
Delimiter ;

-- call sp_EliminarReceta(11);

-- --------- Editar Recetas ---------
Delimiter $$
	Create procedure sp_EditarReceta(in fechaRec date, in codReceta int)
    Begin
		Update Recetas R
			set
				R.fechaReceta = fechaRec
				where codigoReceta = codReceta;
    End$$
Delimiter ;

call sp_EditarReceta('2021-06-25', 2);
call sp_ListarRecetas();

-- ------------------------------------------------------------------------------ DETALLE RECETA ------------------------------------------------------------------------------
-- --------- Agregar Detalle Receta ---------
Delimiter $$
	Create procedure sp_AgregarDetalleReceta(in dosi varchar(100), in codReceta int, in codMedicamento int)
    Begin
		Insert into DetalleReceta (dosis, codigoReceta, codigoMedicamento)
			Values(dosi, codReceta, codMedicamento);
    End $$
Delimiter ;

call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 5 horas', 1, 1);
call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 6 horas', 2, 2);
call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 7 horas', 3, 3);
call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 8 horas', 4, 4);
call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 9 horas', 5, 5);
call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 10 horas', 6, 6);
call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 12 horas', 7, 7);
call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 14 horas', 8, 8);
call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 15 horas', 9, 9); 
call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 16 horas', 10, 10);
call sp_AgregarDetalleReceta('Ingerir en un lapso de tiempo de cada 17 horas', 11, 11);

-- --------- Listar Detalle Receta ---------
Delimiter $$
	Create procedure sp_ListarDetalleRecetas()
    Begin
		Select
			DR.codigoDetalleReceta,
			DR.dosis,
            DR.codigoReceta,
            DR.codigoMedicamento
		from DetalleReceta DR;
    End $$
Delimiter ;

call sp_ListarDetalleRecetas();

-- --------- Listar Detalle Receta Reporte---------
Delimiter $$
	Create procedure sp_ListarDetalleRecetasReporte()
    Begin
		Select
			DR.codigoDetalleReceta as Codigo_Detalle_Receta,
			R.codigoReceta as Codigo_Receta,
            R.fechaReceta as Fecha_Receta,
            P.nombresPaciente as Nombres,
            P.apellidosPaciente as Apellidos,
            P.sexo as Sexo,
            P.fechaNacimiento as Fecha_Nacimiento,
            M.nombreMedicamento as Medicamento,
			DR.dosis as Dosis,
            R.numeroColegiado as Numero_Colegiado
            
			from DetalleReceta DR
				inner join Recetas R on R.codigoReceta = DR.codigoReceta
                inner join Medicamentos M on M.codigoMedicamento = DR.codigoMedicamento
                inner join Doctores D on D.numeroColegiado = R.numeroColegiado
                inner join Citas C on C.numeroColegiado = D.numeroColegiado
                inner join Pacientes P on P.codigoPaciente = C.codigoPaciente;
    End $$
Delimiter ;

call sp_ListarDetalleRecetasReporte();

-- --------- Buscar Detalle Receta ---------
Delimiter $$
	Create procedure sp_BuscarDetalleReceta(in codDetalleReceta int)
    Begin
		Select
			DR.codigoDetalleReceta,
			DR.dosis,
            DR.codigoReceta,
            DR.codigoMedicamento
		from DetalleReceta DR
			where codigoDetalleReceta = codDetalleReceta;
	End $$
Delimiter ;

call sp_BuscarDetalleReceta(1);

-- --------- Eliminar Detalle Receta ---------
Delimiter $$
	Create procedure sp_EliminarDetalleReceta(in codDetalleReceta int)
    Begin
		Delete from DetalleReceta
			where codigoDetalleReceta = codDetalleReceta;
    End$$
Delimiter ;

-- call sp_EliminarDetalleReceta(11);

-- --------- Editar Detalle Receta ---------
Delimiter $$
	Create procedure sp_EditarDetalleReceta(in dosi varchar(100), in codDetalleReceta int)
    Begin
		Update DetalleReceta DR
			set
				DR.dosis = dosi
			where codigoDetalleReceta = codDetalleReceta;
    End $$
Delimiter ;

call sp_EditarDetalleReceta('Ingerir en un lapso de tiempo de cada 32 horas', 6);
call sp_ListarDetalleRecetas();



-- ------------------------------------------------------------------------------ CITAS ------------------------------------------------------------------------------
-- --------- Agregar Citas ---------
Delimiter $$
	Create procedure sp_AgregarCita(in fecCita date, in horCita time, in tratamien varchar(150), in descripCondActual varchar(150),
		in codPaciente int, in numColegiado int)
    Begin
		Insert into Citas (fechaCita, horaCita, tratamiento, descripCondActual, codigoPaciente,numeroColegiado)
			Values (fecCita, horCita, tratamien, descripCondActual, codPaciente, numColegiado);
	End $$
Delimiter ;

call sp_AgregarCita('2022-02-11', '11:05:00', 'Diagnóstico', 'Ligero dolor', 1, 1001);
call sp_AgregarCita('2022-02-12', '12:05:00', 'Diagnóstico', 'Ligero dolor', 2, 1002);
call sp_AgregarCita('2022-03-13', '13:10:00', 'Diagnóstico', 'Ligero dolor', 3, 1003);
call sp_AgregarCita('2022-04-14', '14:15:00', 'Diagnóstico', 'Ligero dolor', 4, 1004);
call sp_AgregarCita('2022-05-15', '15:20:00', 'Diagnóstico', 'Ligero dolor', 5, 1005);
call sp_AgregarCita('2022-06-16', '16:25:00', 'Diagnóstico', 'Ligero dolor', 6, 1006);
call sp_AgregarCita('2022-07-17', '17:30:00', 'Diagnóstico', 'Ligero dolor', 7, 1007);
call sp_AgregarCita('2022-08-18', '18:35:00', 'Diagnóstico', 'Ligero dolor', 8, 1008);
call sp_AgregarCita('2022-09-19', '19:45:00', 'Diagnóstico', 'Ligero dolor', 9, 1009);
call sp_AgregarCita('2022-09-20', '19:45:00', 'Diagnóstico', 'Ligero dolor', 10, 1010);
call sp_AgregarCita('2022-09-20', '19:45:00', 'Diagnóstico', 'Ligero dolor', 11, 1011);

-- --------- Listar Citas ---------
Delimiter $$
	Create procedure sp_ListarCitas()
	Begin
		Select
			C.codigoCita,
			C.fechaCita,
			C.horaCita,
			C.tratamiento,
			C.descripCondActual,
			C.codigoPaciente,
			C.numeroColegiado
		from Citas C;
	End$$
Delimiter ;

call sp_ListarCitas();

-- --------- Listar Citas Reporte---------
Delimiter $$
	Create procedure sp_ListarCitasReporte()
	Begin
    
		Select
			C.codigoCita,
			C.fechaCita,
			C.horaCita,
			C.tratamiento,
			C.descripCondActual,
			P.nombresPaciente,
            P.apellidosPaciente,
            P.fechaNacimiento,
            P.telefonoPersonal,
            P.fechaPrimeraVisita,
            D.numeroColegiado,
			D.nombresDoctor,
            D.apellidosDoctor
		from Citas C
        inner join Pacientes P on P.codigoPaciente = C.codigoPaciente
        inner join Doctores D on D.numeroColegiado = C.numeroColegiado;
        
	End$$
Delimiter ;

call sp_ListarCitasReporte();

-- --------- Buscar Citas ---------
Delimiter $$
	Create procedure sp_BuscarCita(in codCita int)
	Begin
		Select
			C.codigoCita,
			C.fechaCita,
			C.horaCita,
			C.tratamiento,
			C.descripCondActual,
			C.codigoPaciente,
			C.numeroColegiado
		from Citas C
			where codigoCita = codCita;
	End$$
Delimiter ;

call sp_BuscarCita(1);

-- --------- Eliminar Citas ---------
Delimiter $$
	Create procedure sp_EliminarCita(in codCita int)
	Begin
		Delete from Citas
			where codigoCita = codCita;
	End$$
Delimiter ;

-- call sp_EliminarCita(8);

-- --------- Editar Citas ---------
Delimiter $$
	Create procedure sp_EditarCita(in codCita int ,in fechCita date, in horCita time, in trata varchar(150), in descripCondicionActual varchar(150) )
	Begin
		Update Citas C
			set
				C.fechaCita = fechCita,
				C.horaCita = horCita,
				C.tratamiento = trata,
				C.descripCondActual = descripCondicionActual
			where codigoCita = codCita;
	End$$
Delimiter ;

call sp_EditarCita(1,'2022-09-19', '19:45:00', 'Diagnóstico', 'Dolor grave');
call sp_ListarCitas();

-- ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'admin';

create table Usuario(

	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    
    primary key PK_codigoUsuario (codigoUsuario)

);

-- ---------------------------------- USUARIO---------------------------------------
-- --------- Agregar Usuario ---------
Delimiter $$
	Create procedure sp_AgregarUsuario( in nombreUsuario varchar(100), in apellidoUsuario varchar(100), in usuarioLogin varchar(50), in contrasena varchar(50))
    Begin
    
		Insert into Usuario( nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
			values ( nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
    
    End$$
Delimiter ;

call sp_AgregarUsuario('Gerson','Matta','gmatta','123');
call sp_AgregarUsuario('Aarón','Aguilar','aaguilar','asd123');

-- --------- Listar Usuario ---------
-- drop procedure if exists sp_ListarUsuarios();

Delimiter $$
	Create procedure sp_ListarUsuarios()
    Begin
    
		Select 
			U.codigoUsuario, 
            U.nombreUsuario, 
            U.apellidoUsuario, 
            U.usuarioLogin, 
            U.contrasena
		from Usuario U;
    End$$
Delimiter ;

call sp_ListarUsuarios();

create table Login(

	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    
    primary key PK_usuarioMaster (usuarioMaster)

);