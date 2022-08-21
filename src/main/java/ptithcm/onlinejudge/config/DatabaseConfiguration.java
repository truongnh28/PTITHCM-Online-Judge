package ptithcm.onlinejudge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
docker run -d --rm --name ptithcm_online_judge \
-e MYSQL_ROOT_PASSWORD=Truong18122001? \
-e MYSQL_USER=root \
-e MYSQL_PASSWORD=Truong18122001? \
-e MYSQL_DATABASE=ptithcm_online_judge \
-p 3309:3306 \
--volume mysql-spring-boot-tutorial-volume:/var/lib/mysql \
mysql:latest

mysql -h localhost -P 3309 --protocol=tcp -u root -p
*/
@Configuration
@EnableJpaRepositories(basePackages = {"ptithcm.onlinejudge.repository"})
public class DatabaseConfiguration {

}