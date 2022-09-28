
# Performance Enhancement

Performance of the App is best when utilizing multiple custom MySQL Indexes for each of the Database Entities.
This project was tested with a dataset of 400,000 Tracks before and after the implimentation of an Index
and the performance via API call response reflects the improved Indexing system; organizing the data not only by
its Primary Key but also by the multiple ways data is queried from the Database. Using this Indexing system has dropped
the access time from an average of 200 Milliseconds to an average of 20 Milliseconds when working with the listed dataset
of 400,000 entities. This improvement was measured via API calls through Postman and their implemented response timer and a
Java StopWatch instance both before and after the MySQL Index was implemented.

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

### Indexes:

For optimized access to the database, it is recommended that the following Indexes be created on the specified tables

Record:
``` MySQL
CREATE INDEX record_name_idx
ON record (name);
```

Artist:
``` MySQL
CREATE INDEX artist_name_idx
ON artist (artist_name);
```

Track:
``` MySQL
CREATE INDEX track_title_idx
ON track (title);
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




## Discogs API

The Vinyl Hub has accessability for its Admin users to utilize Discogs internal

search engine and Database via their OAuth system.

Below are the requirements:

### Discogs

To utilize Discogs routes, an API key and API secret must be obtained through

a Discogs account on [Discogs.com](https://www.discogs.com/). With an account created navigate to Settings

then to Developers, and finally to Create Application. You will then be given values

to add to your Application.Properties inside this application.

### Application.Properties

These fields must be created within Application.Properties for the

DiscogsApiController to operate. Their values will need to come from the

Discogs Developer Application tab unless provided herein:

 ```
recorddatabase.app.consumerKey=<Discogs_Consumer_Key>
recorddatabase.app.consumerSecret=<Discogs_Consumer_Secret>
recorddatabase.app.requestTokenURL=https://api.discogs.com/oauth/request_token
recorddatabase.app.authorizeURL=https://www.discogs.com/oauth/authorize
recorddatabase.app.accessTokenURL=https://api.discogs.com/oauth/access_token
recorddatabase.app.callbackURL=<Frontend_Address>/login
recorddatabase.app.testURL=https://api.discogs.com/releases/249504
recorddatabase.app.searchReleaseURL=https://api.discogs.com/database/search?format=album&release_title=
recorddatabase.app.searchArtistURL=https://api.discogs.com/database/search?title=
recorddatabase.app.artistInfoPageURL=https://api.discogs.com/artists/
 ```

### Frontend Navigation

The Discogs API Oauth Flow can be interfaced through an unlisted route on the

Frontend, through use of the `<Frontend_Address>/admin/create_access_account`.

This route will allow a user to be created as normal, but will also navigate the user to

the Discogs site to allow for generation of an OAuth Token to be stored within the

Database. This token is used for interfacing with the Discogs API in the Admin only

Frontend option for Discogs Search.