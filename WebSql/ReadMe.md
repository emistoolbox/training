# Training Tool to run SQL queries via a webbrowser. 

## Build
- execute:   `ant war`

## Installation
- copy build/websql.war into the {CATALINA_HOME}/webapps
- edit Context file located in conf/websql.xml
- copy context file to {CATALINA_HOME}/conf/Catalina/localhost/

## Use
The web page that make use of the API is located in:
http://{server}:{port}/websql/index.html

The api is located in the url. Only one operation allowed [sql]
http://{server}:{port}/websql/api/sql

operation sql expects a POST with a JSON structure like this:
```
{
	"user":"testuser",
	"password":"littleballoffur",
	"sql":"select * from books order by price desc;"
}
```
if user or password are not defined in the request, then the application will use the credentials defined in the Context.

response is a JSON structure like this
```
{
  headers[h1,h2,...],
  rows[[a1,a2],[b1,b2],...],
  log: [],
  errorMessage: "xxx"
}
```
where:
`log` contains some information about the execution of the query (number of rows affected, if any) and the stacktrace in case of error.
`errorMessage` will contain the error message in case of any error during the SQL execution, if any.

