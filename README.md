# atipiera_project

## Prerequisites
Java, Maven, Internet access, 8080 port available

## How to run?
Run these two command in the project's root directory:

```
mvn clean package
java -jar target/atipiera-project-0.0.1-SNAPSHOT.jar
```

You can also build and run the project using the Intellij IDE instead. The application runs on port 8080.

## Endpoints
There is one available endpoint /repos. It requires a path variable:

```
/repos/{login}
```

The endpoint returns a list of given user's repositories. In a case of some errors, it can also return:
- Status 403, when API request rate limit is exceeded. 
- Status 404, when given user does not exist.
- Status 406, when Accept header does not include JSONs.
- Status 500, when some unexpected error occurs.
Appropriate error JSONs are also included into the response.

## Tests
There are 4 integration tests. They cover the scope of the above endpoint.
