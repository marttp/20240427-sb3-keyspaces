package dev.tpcoder.sbawskeyspace;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.cql.CqlTemplate;

import java.util.Objects;

@SpringBootApplication
public class SbAwsKeyspaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbAwsKeyspaceApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(UserRepository sampleUserRepository, CassandraOperations cassandraTemplate) {
        return args -> {
            CqlTemplate cqlTemplate = (CqlTemplate) cassandraTemplate.getCqlOperations();

            CqlSession session = Objects.requireNonNull(cqlTemplate.getSessionFactory()).getSession();

            int count = session.execute("SELECT * FROM system.peers").all().size();

            System.out.println("Number of hosts: " + count);

            String userName = "aws-user";

            User userIn = new User();
            userIn.setUsername(userName);
            userIn.setFname("emma");
            userIn.setLname("brie");

            sampleUserRepository.insert(userIn);

            User userOut = sampleUserRepository.findByUsername(userName);
            System.out.println("User: " + userOut.getFname() + " " + userOut.getLname());
        };
    }

}
