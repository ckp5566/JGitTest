package test.jgit.android.com.androidgit;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private static final String REMOTE_URL = "https://github.com/MistLiao/jgitlib.git";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GitThread().execute(REMOTE_URL);
    }


}
