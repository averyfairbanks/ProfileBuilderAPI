import objects.UserSessionObject;
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
import java.util.*;

/**
 * This program is intended to gather json data from url, build a profile for each unique user, and then post it to
 * another url
 */
public class SessionProfileService {
    private static final int TEN_MIN_IN_MILLISECONDS = 60000;

    public static void buildAndPostProfilesFromUrl(String url) {
        try {
            String eventDataJson = getJsonAsString(url);
            String profileDataJson = buildProfilesJson(eventDataJson);

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

    /**
     * Calls many subsequent methods but is tasked with building "sessionByUser" json
     * @param eventDataJson
     * @return
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static String buildProfilesJson(String eventDataJson) {
        HashSet<String> visitorIdsSet;
        HashMap<String, ArrayList<VisitObject>> visitsMap;

        try {
            JSONArray events = buildJsonArrayFromString(eventDataJson);
            visitorIdsSet = buildVisitorIdSet(events);
            visitsMap = buildVisitorIdMap(visitorIdsSet, events);

            sortVisitObjectArrays(visitsMap);

            JSONArray sessionArray = new JSONArray();
            JSONObject currSessionById = new JSONObject(), sessionsByUser = new JSONObject();
            for(Map.Entry<String, ArrayList<VisitObject>> entry: visitsMap.entrySet()) {
                ArrayList<String> pagesVisited = new ArrayList<>();
                UserSessionObject currSessionObject = new UserSessionObject("0", null, null);
                sessionArray.clear();

                for (VisitObject currVisit : entry.getValue()) {
                    if(currSessionObject.getStartTime() == null) {
                        currSessionObject.setStartTime(currVisit.getTimestamp());
                    }

                    if(Long.parseLong(currVisit.getTimestamp()) - Long.parseLong(currSessionObject.getStartTime()) > TEN_MIN_IN_MILLISECONDS) {
                        currSessionObject.setPages(pagesVisited);
                        JSONObject temp = new JSONObject();
                        temp.put("duration", currSessionObject.getDuration());
                        temp.put("pages", currSessionObject.getPages().toString());
                        temp.put("startTime", currSessionObject.getStartTime());
                        sessionArray.add(temp);
                        currSessionObject.setAll("0", null, null);
                        pagesVisited.clear();
                    }
                    else {
                        pagesVisited.add(currVisit.getUrlVisited());
                        currSessionObject.setDuration( Long.toString( Long.parseLong(currVisit.getTimestamp() ) - Long.parseLong(currSessionObject.getStartTime() ) ) );
                    }
                }
                currSessionById.put(entry.getKey(), sessionArray);
            }
            sessionsByUser.put("sessionsByUser", currSessionById);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *
     * @param eventDataJson
     * @return
     * @throws ParseException
     */
    private static JSONArray buildJsonArrayFromString(String eventDataJson) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONArray) ((JSONObject) parser.parse(eventDataJson)).get("events");
    }

    private static HashSet<String> buildVisitorIdSet(JSONArray events) {
        HashSet<String> idSet = new HashSet<>();

        JSONObject tempObj;
        for (Object event : events) {
            tempObj = (JSONObject) event;
            idSet.add(tempObj.get("visitorId").toString());
        }
        return idSet;
    }

    private static HashMap<String, ArrayList<VisitObject>> buildVisitorIdMap(HashSet<String> visitorIds, JSONArray events) {
        HashMap<String, ArrayList<VisitObject>> visitsMap = new HashMap<>();
        JSONObject tempObj;
        for(String id: visitorIds) {
            ArrayList<VisitObject> tempVisitArray = new ArrayList<>();

            for(Object event : events) {
                tempObj = (JSONObject) event;

                if(tempObj.get("visitorId").equals(id)) {
                    tempVisitArray.add(new VisitObject(
                                    tempObj.get("url").toString(),
                                    tempObj.get("timestamp").toString()
                            )
                    );
                }
            }
            visitsMap.put(id, tempVisitArray);
        }
        return visitsMap;
    }

    private static void sortVisitObjectArrays(HashMap<String, ArrayList<VisitObject>> visitsMap) {
        for(ArrayList<VisitObject> currArray: visitsMap.values()){
            currArray.sort(Comparator.comparing(VisitObject::getTimestamp));
        }
    }
}
