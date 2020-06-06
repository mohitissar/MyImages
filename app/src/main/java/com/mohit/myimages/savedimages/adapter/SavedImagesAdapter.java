package com.mohit.myimages.savedimages.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohit.myimages.R;
import com.mohit.myimages.savedimages.model.Image;
import com.mohit.myimages.utils.GridSpacingItemDecoration;
import com.mohit.myimages.utils.Utils;
import com.mohit.myimages.utils.imageFolder;
import com.mohit.myimages.utils.pictureFacer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedImagesAdapter extends RecyclerView.Adapter<SavedImagesAdapter.ViewHolder> {

    private List<imageFolder> savedImageList;
    private List<Image> imageList;
    private Context context;
    ImageAdapter imageAdapter;
    public SavedImagesAdapter(List<imageFolder> savedImageList, Context context) {
        this.savedImageList = savedImageList;
        this.imageList = imageList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        imageFolder savedImage = savedImageList.get(position);
        String text = "" + savedImage.getFolderName();
        String count = ""+savedImage.getNumberOfPics();
        holder.tvNavigationName.setText(Utils.getInstance(context).getDate(savedImage.getlDateToken(),"MMM d yyyy"));
        holder.tvCountName.setText(count);
        if (savedImage.getNumberOfPics() >= 6) {
            setAdapter(holder, savedImage.getPath(), savedImage.getFolderName(), 6);
        } else {
            setAdapter(holder, savedImage.getPath(), savedImage.getFolderName(), 3);
        }
    }

    @Override
    public int getItemCount() {
        return savedImageList.size();
    }

    public void refreshAdapter(ArrayList<imageFolder> data) {
        savedImageList.clear();
        savedImageList.addAll(data);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tvNavigationName)
        TextView tvNavigationName;
        @Nullable
        @BindView(R.id.tv_count_name)
        TextView tvCountName;
        @BindView(R.id.image_rv)
        RecyclerView imageRv;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setAdapter(ViewHolder holder, String path, String folderName, int maxSize) {
        RecyclerView.ItemAnimator animator = holder.imageRv.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        imageAdapter = new ImageAdapter(getAllImagesByFolder(path, maxSize), context);
        holder.imageRv.setLayoutManager(new GridLayoutManager(context, 3));
        holder.imageRv.setAdapter(imageAdapter);
    }

    public ArrayList<pictureFacer> getAllImagesByFolder(String path, int maxSize) {
        ArrayList<pictureFacer> images = new ArrayList<>();
        Uri allVideosuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        String sortOrder = String.format("%s limit "+maxSize, BaseColumns._ID);
        Cursor cursor = context.getContentResolver().query( allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[] {"%"+path+"%"}, sortOrder);
        try {
            cursor.moveToFirst();
            do{
                pictureFacer pic = new pictureFacer();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<pictureFacer> reSelection = new ArrayList<>();
            for(int i = images.size()-1;i > -1;i--){
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

}
