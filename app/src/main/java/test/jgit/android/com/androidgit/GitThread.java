package test.jgit.android.com.androidgit;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RebaseCommand;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.transport.FetchResult;
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
            File gitPath = new File(localPath.getAbsolutePath() + File.separator + ".git");
            if (!gitPath.exists()) {
                // If git repository is not exist, then clone
                try {
                    Git result = Git.cloneRepository()
                            .setURI(url)
                            .setDirectory(localPath)
                            .call();
                    Log.d(TAG, "Having repository: " + result.getRepository().getDirectory());
                }
                catch (Exception e) {
                    Log.e(TAG, "Error to clone remote repository:" + e);
                }
            } else {
                // If git repository is exist already, then sync latest code base
                try {
                    Git git = Git.open(localPath);
                    FetchCommand fetchCmd = git.fetch();
                    FetchResult fResult = fetchCmd.setRemote("origin").call();
                    Log.d(TAG, "Fetch result:" + fResult);
                    RebaseCommand rebaseCmd = git.rebase();
                    rebaseCmd.setUpstream("refs/heads/master");
                    RebaseResult rResult = rebaseCmd.call();
                    // if there are merge conflicts (rebase interactive) - reset the repository
                    if (!rResult.getStatus().isSuccessful()) {
                        git.rebase().setOperation(RebaseCommand.Operation.ABORT).call();
                        Log.e(TAG, "Fail to rebase.");
                    } else {
                        Log.d(TAG, "Success to rebase");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "fail to sync latest codes");

                }
            }
        } else {
            Log.e(TAG, "Error to create local repository dir:" + localPath.getAbsolutePath());
        }
    }
}
