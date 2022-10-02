package pl.wit.shop.example;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/test")
@RequestScoped
public class TestResource {

    @Named("shopDataSource")
    private final DataSource testDataSource;

    @Inject
    public TestResource(DataSource testDataSource) {
        this.testDataSource = testDataSource;
    }

    @GET
    @Path("tables")
    @Produces("text/plain")
    public String getTableNames() throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Connection connection = this.testDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT s.table_name  FROM information_schema.tables s WHERE s.table_schema = 'public'");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sb.append(rs.getString(1)).append("\n");
            }
        }
        return sb.toString();
    }
}
