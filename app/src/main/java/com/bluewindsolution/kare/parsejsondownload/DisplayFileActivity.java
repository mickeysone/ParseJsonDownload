package com.bluewindsolution.kare.parsejsondownload;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class DisplayFileActivity extends AppCompatActivity {

    private String appId = "";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DisplayFileAdapter displayFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_file);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.display_file_rcv_files);
        layoutManager = new LinearLayoutManager(DisplayFileActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        appId = getIntent().getExtras().getString("appID");

        if (getDataSet().size() > 0) {
            displayFileAdapter = new DisplayFileAdapter(getDataSet(), this);
            recyclerView.setAdapter(displayFileAdapter);
            displayFileAdapter.SetOnItemClickListener(new DisplayFileAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(String path) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String mimeType = MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(MimeTypeMap
                                    .getFileExtensionFromUrl(path));
                    intent.setDataAndType(Uri.fromFile(new File(path)), mimeType);
                    startActivity(Intent.createChooser(intent, "Open file with"));
                }
            });
        } else {
            displayFileAdapter = new DisplayFileAdapter(new ArrayList<DisplayFileObject>(), this);
            recyclerView.setAdapter(displayFileAdapter);
            Toast.makeText(this, "file Not Found.", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<DisplayFileObject> getDataSet() {
        ArrayList<DisplayFileObject> allPaths = new ArrayList<DisplayFileObject>();
        File allFilePathInText = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/" + appId);
        if (allFilePathInText.exists()) {
            try {
                File appIdTextFile = new File(allFilePathInText.toString() + "/" + appId + ".txt");
                if (appIdTextFile.exists()) {
                    String[] arrAllPath = GameDataManager.getStringFromFile(appIdTextFile.toString());
                    for (String path: arrAllPath) {
                        if (path.endsWith(".jpg") || path.endsWith(".png")) {
                            String[] pathComp =  path.split("/");
                            String fileName = pathComp[pathComp.length - 1];
                            DisplayFileObject displayFileObject = new DisplayFileObject(fileName, "IMAGE", path);
                            allPaths.add(displayFileObject);
                        } else if (path.endsWith(".mp3")) {
                            String[] pathComp =  path.split("/");
                            String fileName = pathComp[pathComp.length - 1];
                            DisplayFileObject displayFileObject = new DisplayFileObject(fileName, "SOUND", path);
                            allPaths.add(displayFileObject);
                        }  else if (path.endsWith(".mp4")) {
                            String[] pathComp =  path.split("/");
                            String fileName = pathComp[pathComp.length - 1];
                            DisplayFileObject displayFileObject = new DisplayFileObject(fileName, "VIDEO", path);
                            allPaths.add(displayFileObject);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allPaths;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
