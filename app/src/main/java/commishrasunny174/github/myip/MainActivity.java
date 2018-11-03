package commishrasunny174.github.myip;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar = null;
    private TextView ipTextView = null;
    private TextView countryTextView = null;
    private Button getIPButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        ipTextView = (TextView) findViewById(R.id.ip_text_view);
        getIPButton = (Button) findViewById(R.id.get_ip_button);
        countryTextView = (TextView) findViewById(R.id.country_text_view);
        getIPButton.setOnClickListener((view) -> {
            new GetIP().execute();
        });
    }

    private class GetIP extends AsyncTask<Void, Void, Void>{

        private String ip = null;
        private String country = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ip = null;
            country = null;
            ipTextView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            countryTextView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            URLConnection connection;
            String data="";
            InputStream inputStream;
            JSONObject object = null;
            int temp;
            try {
                url  = new URL("https://api.myip.com/");
                connection = url.openConnection();
                inputStream = connection.getInputStream();
                while((temp = inputStream.read()) != -1) {
                    data += (char)temp;
                }
                object = new JSONObject(data);
                ip = object.getString("ip");
                country = object.getString("country");
            } catch (MalformedURLException mue) {
                Log.wtf("Error",mue.getMessage());
            } catch (IOException ioe) {
                Log.e("Error",ioe.getMessage());
                ip = getString(R.string.no_connection_error);
            } catch (JSONException jsone) {
                Log.wtf("Error", jsone.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            ipTextView.setVisibility(View.VISIBLE);
            ipTextView.setText(ip);
            if(country!=null) {
                countryTextView.setText(country);
                countryTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
