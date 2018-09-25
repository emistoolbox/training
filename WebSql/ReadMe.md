# Training Tool to run SQL queries via a webbrowser. 

## Build
- execute:   `ant war`

## Installation
- copy build/websql.war into the {CATALINA_HOME}/webapps
- copy context file from conf(websql.xml to {CATALINA_HOME}/conf/Catalina/localhost/

## Configuration
- edit Context file located in conf/websql.xml
- For each different user add the database that it will use. Add a line like this:

```
<Environment name="table_testuser" value="ebookshop"  type="java.lang.String" override="false"/>       
```
The name needs to match with "table_"{username}
Value is directly the name of the database

- Add the user(s) in {CATALINA_HOME}/conf/tomcat-user.xml
By defatul the application expect role 'websqlrole' and need to be passed via Basic HTTP Authentication. Then you need to add a line like 
```
  <user username="testuser" password="test"  roles="websqlrole"/>
```

## Use
The web page that make use of the API is located in:
http://{server}:{port}/websql/index.html

The api is located in the url. Only one operation allowed [sql]
http://{server}:{port}/websql/api/sql

operation sql expects a POST with a JSON structure like this:
```
{
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


## Autofill usage

Simply add a table with class 'websql-result' and set the attribute data-sql with the valid sql query. Reload and it will fill up the table with the results of the query. For instance:

```
<table class="websql-result table table-bordered table-striped table-hover table-sm" data-sql="SELECT * FROM books;">
</table>    
```
