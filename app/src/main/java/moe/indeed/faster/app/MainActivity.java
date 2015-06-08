package moe.indeed.faster.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private static final int QRSCAN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SimpleScannerActivity.class);
                MainActivity.this.startActivityForResult(intent, QRSCAN_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == QRSCAN_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
                Intent browserIntent = new Intent(this, BrowserActivity.class);
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                String url = data.getStringExtra("url");
                browserIntent.putExtra("url", url);
                Matcher matcher = Pattern.compile("token=([a-f0-9]{40})").matcher(url);
                if (matcher.find()) {
                    String accessToken = matcher.group(1);
                    Intent registrationIntent = new Intent(this, RegistrationIntentService.class);
                    registrationIntent.putExtra("accessToken", accessToken);
                    this.startService(registrationIntent);
                }
                this.startActivity(browserIntent);
                this.finish();
            }
        }
    }

}
