
# The Vinyl Hub Backend

A Spring Web routing system for connecting the frontend to a MySQL
Server and providing Authentication through JWT Tokens.

  To Run, please have MySQL installed and configured on whatever
  device you will be running this application on. For more on that
  please reference the Running Locally section.

For best results, also download [The Vinyl Hub Frontend](https://github.com/MGreco2112/RecordCollectionFrontend)
## Run Locally

Clone the project

```
  git clone https://github.com/MGreco2112/record_collection_jpa_pp
```

### MySQL:

Create Database

``` MySQL
CREATE DATABASE <database_name>;
```

Create user

``` MySQL
CREATE USER '<user_name>'@'%' IDENTIFIED BY <password>;
```

Grant Permissions

``` MySQL
GRANT all ON <database_name>.* TO '<username>'@'%';
```


### JAVA

Open project

### Application.Properties:

Standard Spring configs
```
Spring.jpa.hibernate.ddl-auto=update
Spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

MySQL Configs: (local server info in <>)
```
Spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
Spring.datasource.username=<username>
Spring.datasource.password=<password>
```

JWT Configs: (user config in <>)
```
#application properties
recorddatabase.app.jwtSecret=<secret_string>

```


## API Reference

### Authentication

#### Sign Up

```http
  POST /api/auth/signup
```

This creates a new User in the MySQL database. Logging in will grant access to the authenticated routes.

| Body      | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `signup_request` | `object` | **Required** {username: <username>, password: <password>} |

#### Sign in

```http
  POST /api/auth/signin
```

This returns a JWT Token which can be used as a Bearer token to access authenticated routes

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `signin_request` | `object` | **Required** {username: <username>, password: <password>} |


## Collector Requests

### Create Collector

This attaches a new Collector Object to the logged in User

``` HTTP
POST /api/collectors
```

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `collector` | `object` | **Required** {name: <collector_name>} |


### Get Logged In Collector

This returns the Collector of the current logged in User

``` HTTP
GET /api/collectors/currentCollector
```

### Modify Collector

Submit elements of a Collector that you wish to modify/update

``` HTTP
PUT /api/collectors
```

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `collector` | `object` | **Required** {<key_to_update>: <update_value>, <optional_keys_&_values>} |


## Record Requests

### Post new Record

Post a new Record to the MySQL database

``` HTTP
POST /api/records
```

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `record` | `object` | **Required** {name: <record_name>, nameFormatted: <record name with " " replaced for "_">, releaseYear: <year of release>, numberOfTracks: <number of tracks on record>, tracks: [<array of track names as string>], imageLink: <url to image file of record>} |


### Modify Record

Submit elements of a Record that you wish to modify/update

``` HTTP
PUT /api/records
```

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `record` | `object` | **Required** {<key_to_update>: <update_value>, <optional_keys_&_values>} |

