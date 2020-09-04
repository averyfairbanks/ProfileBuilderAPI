public class ProjectRunner {
    private static final String sessionDataUrl = "https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=255754c5659c12f989c45a1b959f";

    public static void main(String[] args) {
        SessionProfileService.buildAndPostProfilesFromUrl(sessionDataUrl);
    }
}
