
# WebTechnikon Project
### CodeHub - European Dynamics

This is a Java web application built using Jakarta EE 10 with Hibernate as the persistence provider. The project is managed using Maven, and it's packaged as a WAR file for deployment. It includes RESTful web services to manage property repairs and owners.

## Technologies and Tools

- **Jakarta EE 10**: Java EE API for developing enterprise-level applications.
- **Hibernate**: ORM tool used for database persistence.
- **Maven**: Project build tool for managing dependencies and build lifecycle.
- **WildFly**: Application server used for deploying the WAR file and testing the services.
- **Postman**: Used for testing REST API endpoints.
- **Jackson**: Library for converting Java objects to and from JSON.


## Setting Up the Project

1. Clone the repository.
2. Ensure Maven and Java 11 (or higher) are installed.
3. Configure your MySQL datasource in WildFly as `java:/MySqlDS`.
4. Use `mvn clean install` to build the project.
5. Deploy the generated WAR file to WildFly.
6. Use Postman to test the endpoints.

### Example Endpoints

- **Search Repairs by Date Range**: `GET /resources/propertyRepairs/searchByDateRange?startDate=2023-01-01&endDate=2023-12-31`
- **Update Repair**: `PUT /resources/propertyRepairs/update`

## Testing

Postman can be used to test the REST API endpoints. Ensure that WildFly is running, and the WAR file is deployed before executing any requests.


## Future Improvements

- Add more comprehensive validation on inputs.
- Improve exception handling with more detailed error responses.
- Add more tests, including integration and unit tests.

## Authors

- John Morellas


