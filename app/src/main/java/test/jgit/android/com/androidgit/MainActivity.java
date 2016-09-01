package test.jgit.android.com.androidgit;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.eclipse.jgit.api.Git;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String REMOTE_URL = "https://github.com/github/testrepo.git";
    private static final String TAG = "JGIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CloneRemoteRepository();
    }

    void CloneRemoteRepository() {

        // prepare a new folder for the cloned repository
        File localPath = new File(Environment.getExternalStorageDirectory() + File.separator + "TestGitRepository");
        boolean success = true;
        if (!localPath.exists()) {
            Log.d(TAG, "create folder");
            success = localPath.mkdir();
        }
        if (success) {
            try {
                Git result = Git.cloneRepository()
                        .setURI(REMOTE_URL)
                        .setDirectory(localPath)
                        .call();
                Log.d(TAG, "Having repository: " + result.getRepository().getDirectory());
                //result.checkout();
            }
            catch (Exception e) {
                Log.e(TAG, "Error to open remote repository");
            }
        } else {
            Log.e(TAG, "Error to create local repository dir:" + localPath.getAbsolutePath());
        }
    }
}
