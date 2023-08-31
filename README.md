# Proyecto intercorp

## 1 Introduccion

el proyecto consiste en ingresar y listar clientes

## 2 Requisitos

## 2.2 Requisitos funcionales
 
 * el api debe permitir gestionar los clientes
 
### 2.3 Herramientas

* Java 11 
* MongoDB
* Maven
* SpringToolSuite4 
* Spring WebFlux
* Spring Boot
* Git


### 2.3. Instalación

* Verificar las variables de entorno
* Importar el proyecto a SpringToolSuite4
* Verificar la ruta de mongo DB mongodb://localhost:27017/spring_boot

## 3. Ejecución

abrir el proyecto en el IDE y ejecutarlo.


## 4. las apis

* get:  http://localhost:8080/api/clientes

* get : http://localhost:8080/api/clientes/{dni}/{correo}

* post : http://localhost:8080/api/clientes      

{
        "nombre": "MARIA LUISA",
        "apellido": "PARDO ZELA",
        "email": "CORREO_PARDO@GMAIL.COM",
        "dni": "44095555",
        "fechaNacimiento": "1990-03-20"
    }





## 3. Ejecución

Basta con abrir el proyecto en el IDE y ejecutarlo.