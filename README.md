# Identification backend app
This is a backend service made using Spring boot, Java, and Docker for the identification task of BiteSpeed.

## Explanation
The basic idea behind the algorithm at play here is similar to a DSU. DSU uses two operations to maintain a 
"forest of trees" in which each tree represents a set of nodes belong to the same tree, and the parent of these nodes
can be found quickly, and they can be joined together.

This is achieved by two methods
- Path Compression
- Union by rank

While union by rank isn't possible for this implementation, we use path compressions to keep trees shallow so that the 
parent node can be found quickly. Each time two nodes have to be merged, we flatten the entire joining tree, so that the
max height of **any** tree remains 2 at most. This allows us to find the parents of any record in 2 db accesses at the 
cost of slightly slower updates.

## Running
To run the backend you will need java, maven, and docker.
### Clone
```
git clone https://github.com/notSanil/identification-service
```
### Open the project
Opening the project in intellij is recommended as you can quickly setup the project.
### Start the db
The db runs in a docker container, to start it up
```
docker-compose up -d
```
### Run the project
You can create a configuration profile with the command in intellij
```cmd
spring-boot:run
```
or alternatively navigate to the project directory and run
```cmd
mvn spring-boot:run
```
Upon running the project, the contact schema will be created, along with a couple rows of data.
