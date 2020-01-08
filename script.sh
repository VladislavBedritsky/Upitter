mvn clean install
docker-compose up

#### Run script commands

# 1. chmod +x ./script.sh && ./script.sh
# 2. wait...
# (If there is an error during initialization, run again a command - "docker-compose up" )

#### Steps for sql.scripts (after initializing springApp)

# 3. cd d:/workJava/Twitter/
# 4. docker cp ./src/main/resources/db/data.sql mysqldb:/data.sql
# 5. docker container exec -it mysqldb bash
# 6. mysql -uroot -proot
# 7. use twitter;
# 8. source data.sql
# 9. select * from users;
# 10. exit;
# 11. exit
# 12. open app http://"your_machine_IP":8080/


#### DELETE images, containers, networks
# docker stop ex && docker stop mysqldb && docker rm ex && docker rm mysqldb && docker rmi ex mysql openjdk && docker network rm springjdbctemplate_empl-mysql