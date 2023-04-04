package com.example.carlistingassingment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView makeAutoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeAutoCompleteTextView = findViewById(R.id.make);

        new FetchMakesTask().execute();
    }

    private class FetchMakesTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> makes = new ArrayList<>();
            try {
                // Create a URL object for the API endpoint
                URL url = new URL("https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json");

                // Open an HTTP connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Get the response code and response message
                int responseCode = connection.getResponseCode();
                String responseMessage = connection.getResponseMessage();
                System.out.println("Response Code : " + responseCode);
                System.out.println("Response Message : " + responseMessage);

                // Read the JSON response from the API endpoint
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response and extract the make names
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray makesArray = jsonResponse.getJSONArray("Results");
                for (int i = 0; i < makesArray.length(); i++) {
                    JSONObject makeObject = makesArray.getJSONObject(i);
                    String makeName = makeObject.getString("Make_Name");
                    makes.add(makeName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return makes;
        }

        @Override
        protected void onPostExecute(ArrayList<String> makes) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, makes);
            makeAutoCompleteTextView.setAdapter(adapter);
        }
    }
}

