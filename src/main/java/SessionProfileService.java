import objects.VisitObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This program is intended to gather json data from url, build a profile for each unique user, and then post it to
 * another url
 */
public class SessionProfileService {

    public static void buildAndPostProfilesFromUrl(String url) {
        try {
            String sessionDataJson = getJsonAsString(url);
            String profileDataJson = buildProfilesJson(sessionDataJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getJsonAsString(String url) {
        String jsonString = "";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet getJsonRequest = new HttpGet(url);
            getJsonRequest.addHeader("accept", "application/json");

            HttpResponse response = client.execute(getJsonRequest);
            jsonString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static String buildProfilesJson(String sessionDataJson) {
        HashSet<String> visitorIds = new HashSet<>();
        HashMap<String, VisitObject> visits = new HashMap<String, VisitObject>();
        try {
            JSONArray events = buildJsonArrayFromString(sessionDataJson);

            //HELL CODE STARTS HERE, object is to build profiles based on specifications
            JSONObject tempObj;
            for (Object event : events) {
                tempObj = (JSONObject) event;
                visitorIds.add(tempObj.get("visitorId").toString());
            }

            for(String id: visitorIds) {
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static JSONArray buildJsonArrayFromString(String sessionDataJson) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONArray) ((JSONObject) parser.parse(sessionDataJson)).get("events");
    }
}
