package uk.ac.cam.cares.jps.agent.travellingsalesmanagent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgis.PGgeometry;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.ac.cam.cares.jps.base.exception.JPSRuntimeException;
import uk.ac.cam.cares.jps.base.query.RemoteRDBStoreClient;

public class NearestNodeFinder {

    String tableName; // default value

    public NearestNodeFinder(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Pass POI_tsp in arrays and finds the nearest nodes based on routing_ways road
     * data.
     * 
     * @param remoteRDBStoreClient
     * @param jsonArray            POI_tsp in array format
     */
    public void insertPoiData(RemoteRDBStoreClient remoteRDBStoreClient, JSONArray jsonArray) {

        try (Connection connection = remoteRDBStoreClient.getConnection()) {

            String initialiseTable = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                    + "poi_tsp_iri VARCHAR, "
                    + "poi_tsp_type VARCHAR, "
                    + "nearest_node BIGINT, "
                    + "geom geometry, "
                    + "is_flooded BOOLEAN, "
                    + "name VARCHAR"
                    + ")";

            executeSql(connection, initialiseTable);
            System.out.println("Initialized " + tableName + " table.");

            String sql = "INSERT INTO " + tableName
                    + " (poi_tsp_iri, poi_tsp_type, nearest_node, geom, is_flooded, name) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject poiTSP = jsonArray.getJSONObject(i);
                String poiTSPIri = poiTSP.getString("poi_iri");
                String poiTSPType = poiTSP.getString("poi_type");
                String poiName = poiTSP.getString("name");
                // Remove the prefix from poiTSPIri, poiTSPType
                poiTSPIri = poiTSPIri.replace("https://www.theworldavatar.com/kg/", "");
                poiTSPType = poiTSPType.replace("https://www.theworldavatar.com/kg/", "");

                String geometry = poiTSP.getString("geometry");
                PGgeometry pgGeometry = new PGgeometry(geometry);
                pgGeometry.getGeometry().setSrid(4326);

                String nearestNode = findNearestNode(connection, geometry);

                preparedStatement.setString(1, poiTSPIri);
                preparedStatement.setString(2, poiTSPType);
                preparedStatement.setInt(3, Integer.parseInt(nearestNode));
                preparedStatement.setObject(4, pgGeometry);
                preparedStatement.setBoolean(5, false);
                preparedStatement.setString(6, poiName);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

            System.out.println("Table " + tableName + " created.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new JPSRuntimeException(e);
        }
    }

    /**
     * Finds the nearest node of the POI_tsp from routing_ways table
     * 
     * @param connection
     * @param geom
     * @return
     * @throws SQLException
     */
    private String findNearestNode(Connection connection, String geom) throws SQLException {

        String geomConvert = "ST_GeometryFromText('" + geom + "', 4326)";

        String findNearestNodeSql = "SELECT id, ST_Distance(the_geom, " + geomConvert + ") AS distance\n" +
                "FROM routing_ways_vertices_pgr\n" +
                "ORDER BY the_geom <-> " + geomConvert + "\n" +
                "LIMIT 1;\n";

        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(findNearestNodeSql)) {
                if (resultSet.next()) {
                    // Assuming 'id' and 'distance' are columns in your query result
                    int id = resultSet.getInt("id");

                    return Integer.toString(id);
                } else {
                    // No results found
                    return null;
                }
            }
        }
    }

    /**
     * Create connection to remoteStoreClient and execute SQL statement
     * 
     * @param connection PostgreSQL connection object
     * @param sql        SQl statement to execute
     */
    private void executeSql(Connection connection, String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

}
