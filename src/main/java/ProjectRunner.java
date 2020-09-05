public class ProjectRunner {
    private static final String sessionDataUrl = "https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=255754c5659c12f989c45a1b959f";
    private static final String postSessionUrl = "https://ptsv2.com/t/arwy8-1598815155/post";

    public static void main(String[] args) {
        SessionProfileService.buildAndPostProfiles(sessionDataUrl, postSessionUrl);
    }
}
