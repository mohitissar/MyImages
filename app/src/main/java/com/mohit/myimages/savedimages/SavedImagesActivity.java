package com.mohit.myimages.savedimages;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohit.myimages.R;
import com.mohit.myimages.savedimages.adapter.SavedImagesAdapter;
import com.mohit.myimages.utils.imageFolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SavedImagesActivity extends AppCompatActivity {

    @BindView(R.id.images_rv)
    RecyclerView imagesRv;
    SavedImagesAdapter savedImagesAdapter;
    private List<imageFolder> savedImageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);
        ButterKnife.bind(this);

        if(ContextCompat.checkSelfPermission(SavedImagesActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(SavedImagesActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        setAdapter();
    }

    private void setAdapter() {
        savedImageList = getPicturePaths();
        RecyclerView.ItemAnimator animator = imagesRv.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        savedImagesAdapter = new SavedImagesAdapter(savedImageList, this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(imagesRv.getContext(),
                linearLayoutManager.getOrientation());
        imagesRv.addItemDecoration(dividerItemDecoration);
        imagesRv.setLayoutManager(linearLayoutManager);
        imagesRv.setAdapter(savedImagesAdapter);
    }

    /**
     * @return
     * gets all folders with pictures on the device and loads each of them in a custom object imageFolder
     * the returns an ArrayList of these custom objects
     */
    private ArrayList<imageFolder> getPicturePaths(){
        ArrayList<imageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.DATE_TAKEN};
        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, orderBy);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do{
                imageFolder folds = new imageFolder();
                int dateTakenColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                String dateToken  = cursor.getString(dateTakenColumn);
                long lDateToken = dateToken != null ? Long.parseLong(dateToken) : 0;
                folds.setlDateToken(lDateToken);
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder+"/"));
                folderpaths = folderpaths+folder+"/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addpics();
                    picFolders.add(folds);
                }else{
                    for(int i = 0;i<picFolders.size();i++){
                        if(picFolders.get(i).getPath().equals(folderpaths)){
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            }while(cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0;i < picFolders.size();i++){
            Log.d("picture folders",picFolders.get(i).getFolderName()+" and path = "+picFolders.get(i).getPath()+" "+picFolders.get(i).getNumberOfPics());
        }

        return picFolders;
    }

}