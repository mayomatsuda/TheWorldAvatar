package uk.ac.cam.cares.jps.agent.gfaagent;

import uk.ac.cam.cares.jps.base.agent.JPSAgent;
import uk.ac.cam.cares.jps.base.exception.JPSRuntimeException;
import com.cmclinnovations.stack.clients.ontop.OntopClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.nio.file.Path;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(urlPatterns = {"/calculation", "/calculationwithodba", "/cost", "/costwithodba"})
public class GFAAgent extends JPSAgent{
       
    private EndpointConfig endpointConfig = new EndpointConfig();

    
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String ontopUrl;

    public synchronized void init() {
        String dbName;
        dbName = endpointConfig.getDbName();
        this.dbUrl = endpointConfig.getDbUrl(dbName);
        this.dbUser = endpointConfig.getDbUser();
        dbPassword = endpointConfig.getDbPassword();       
        this.ontopUrl = endpointConfig.getOntopUrl();
    }

    @Override
    public JSONObject processRequestParameters(JSONObject requestParams, HttpServletRequest request) {
        return processRequestParameters(requestParams);
    }

    @Override
    public JSONObject processRequestParameters(JSONObject requestParams) {

        try {
            GFACalculation gfaCalculation = new GFACalculation(dbUrl, dbUser, dbPassword);
            String paremString = requestParams.getString("requestUrl");
            if(paremString.contains("/calculation")){            
                //calculate GFA 1. query footpring 2. query height (if no height, estimate 3.2m/floor) 3. calculate 4. store
                gfaCalculation.calculationGFA();
                if (paremString.contains("withodba")){
                    Path obdaFile = Path.of("/resources/gfa.obda");
                    uploadODBA(obdaFile);
                }
            }else if (paremString.contains("/cost")){
                CostCalculation costCalculation = new CostCalculation(dbUrl, dbUser, dbPassword, ontopUrl);
                costCalculation.calculationCost();
                if (paremString.contains("withodba")){
                    Path obdaFile = Path.of("/resources/cost.obda");
                    uploadODBA(obdaFile);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new JPSRuntimeException(e);
        }

        return requestParams;
    }

    public void uploadODBA (Path obdaFile) {
        try {
            OntopClient ontopClient = OntopClient.getInstance();
            ontopClient.updateOBDA(obdaFile);
        } catch (Exception e) {
            System.out.println("Could not retrieve isochrone .obda file.");
        }

    }
}