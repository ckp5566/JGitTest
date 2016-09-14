package test.jgit.android.com.androidgit;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Mist_Liao on 2016/9/14.
 */
public class GitThread extends AsyncTask<String, Integer, Boolean> {

    private static final String TAG = "JGIT";

    protected Boolean doInBackground(String... urls) {
        int count = urls.length;
        for (int i = 0; i < count; i++) {
            CloneRemoteRepository(urls[i]);
            // Escape early if cancel() is called
            if (isCancelled()) break;
        }
        return true;
    }

    void CloneRemoteRepository(String url) {

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
                        .setURI(url)
                        .setDirectory(localPath)
                        .call();
                Log.d(TAG, "Having repository: " + result.getRepository().getDirectory());
                //result.checkout();
            }
            catch (Exception e) {
                Log.e(TAG, "Error to open remote repository:" + e);
                try {
                    FileUtils.delete(localPath, FileUtils.RECURSIVE);
                } catch (IOException err){
                    Log.e(TAG, "Error to delete repository:" + err);
                }
            }
        } else {
            Log.e(TAG, "Error to create local repository dir:" + localPath.getAbsolutePath());
        }
    }
}
