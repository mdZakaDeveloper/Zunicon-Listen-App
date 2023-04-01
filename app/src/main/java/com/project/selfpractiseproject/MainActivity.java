package com.project.selfpractiseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);



        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        Toast.makeText(MainActivity.this, "Granted Access", Toast.LENGTH_SHORT).show();
                        ArrayList <File> MySongs = FetchSongs(Environment.getExternalStorageDirectory());
                        String [] items = new String[MySongs.size()];
                        for(int i = 0; i < MySongs.size(); i++){
                            items[i] = MySongs.get(i).getName().replace(".mp3", "  ");
                        }

                        ArrayAdapter <String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, PlaySong.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList", MySongs);
                                intent.putExtra("currentSong", currentSong);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });



                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }

    public ArrayList<File> FetchSongs(File file){
        ArrayList <File> arrayList = new ArrayList<>();
        File[] songs = file.listFiles();
        if(songs != null){
            for(File myFiles: songs){
                if(!myFiles.isHidden() && myFiles.isDirectory()){
                    arrayList.addAll(FetchSongs(myFiles));
                }
                else{
                    if(myFiles.getName().endsWith(".mp3") && !myFiles.getName().startsWith(".")){
                        arrayList.add(myFiles);
                    }
                }
            }
        }
        return arrayList;
    }


}