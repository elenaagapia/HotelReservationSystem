## Wantsome - Hotel Reservation System

### 1. Description

This is an application which allows the receptionist of a hotel to create for the hotel's clients.

Supported actions:
 - add/update/delete reservations without overlapping 
 - add/update/delete clients 
 - add/update/delete rooms
 - view all reservations or only the active one, sorted by start date
 - view all existing room types 

---
### 2. Setup

No setup needed, just start the application. If the database is missing
(like on first startup), it will create a new database (of type SQLite,
stored in a local file named 'hotel_reservation_project.db'), and use it to save the future data.

Once the web app starts, navigate with a web browser at url: <http://localhost:4567>

---
### 3. Technical details

__User interface__

The project only one type of user interface which is a web app(starts with Main class)

__Technologies__

- main code is written in Java (minimum version: 8)
- it uses a small embedded database of type SQLite, using SQL and JDBC to
  connect the Java code to it
- it uses Spark micro web framework (which includes an embedded web server, Jetty)
- web pages: using the Velocity templating engine, to separate the UI code 
  from Java code; UI code consists of basic HTML and CSS code (and a little Javascript)
- web services interface: uses REST principles to define the API, and JSON to
  encode requests/responses (using Gson library)
  

__Code structure__

- java code is organized in packages by its role, on layers:
  - db - database part, including DTOs and DAOs, as well as the code to init
    and connect to the db
  - ui - code related to the interface/presentation layer
  - root package - the main class for the interface

- web resources are found in `main/resources` folder:
  - under `/public` folder - static resources to be served by the web server
    directly (images, css files)
  - all other (directly under `/resources`) - the Velocity templates
  
Note: the focus of this project is on the back-end part, not so much on the 
front-end part.

 
