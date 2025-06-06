*How to run the project?*

First of all, the project has some docker files. You need to upload these docker files. To do this, go to the project folder and type the following code:

"docker-compose up --build"
After that, the containers are ready to go.

Then, enter the project and run the command

"mvn clean install" for the project to download all dependencies and perform the REST API tests.

How does the API work?

The path to the endpoint is "http://localhost:8080/calculator/{operation}?a={valueA}&b={valueB}"
The GET method type

This endpoint performs addition, subtraction, division, and multiplication to perform these calculations. In the place where this "operation" is located, you must enter one of the values ​​below:
sum = sum
sub = subtraction
mul = multiplication
div = division

And where the "value A and value B" field is, you enter the values ​​you would like to calculate.
