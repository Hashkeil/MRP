# development report for MRP

GitHub Repo: **https://github.com/Hashkeil/MRP.git**


## Steps to setup local postgres database using docker-compose
1- open docker desktop

2- where .yml file is located run command 
 --> docker-compose up -d
   
3- docker exec -it mrp-postgres-1 psql -U postgres -d mrp_db

4- sql commands to create tables **(schema.sql)**

5- . \d to see tables

6- . q to quit psql

7-  to stop the container run command
 --> docker-compose down


- to enter psql again run command
 --> docker exec -it mrp-postgres-1 psql -U postgres -d mrp_db