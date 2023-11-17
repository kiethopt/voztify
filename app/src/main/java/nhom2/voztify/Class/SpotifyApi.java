package nhom2.voztify.Class;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class SpotifyApi {
    private static final String CLIENT_ID = "2c74da9640de4e0b92f86e29c2ac1112";
    private static final String CLIENT_SECRET = "1bc44876654a40f2b655ee7cc1c22464";

    public static String getAccessToken() {
        String authString = CLIENT_ID + ":" + CLIENT_SECRET;
        String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write("grant_type=client_credentials".getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON and get access token
                JSONObject jsonObject = new JSONObject(response.toString());
                return jsonObject.getString("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

