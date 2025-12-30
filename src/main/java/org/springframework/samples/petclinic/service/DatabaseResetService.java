package org.springframework.samples.petclinic.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
public class DatabaseResetService {

    private final DataSource dataSource;

    public DatabaseResetService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void reset() {
        try (Connection conn = dataSource.getConnection()) {

            conn.createStatement().execute("DROP ALL OBJECTS");

            ScriptUtils.executeSqlScript(conn,
                new ClassPathResource("db/h2/schema.sql"));

            ScriptUtils.executeSqlScript(conn,
                new ClassPathResource("db/h2/data.sql"));

        } catch (Exception e) {
            throw new IllegalStateException("Database reset failed", e);
        }
    }
}
