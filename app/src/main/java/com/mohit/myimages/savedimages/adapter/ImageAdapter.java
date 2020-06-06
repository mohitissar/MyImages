package com.mohit.myimages.savedimages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mohit.myimages.R;
import com.mohit.myimages.utils.pictureFacer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    public List<pictureFacer> pictureFacerList;
    private Context context;

    public ImageAdapter(List<pictureFacer> pictureFacerList, Context context) {
        this.pictureFacerList = pictureFacerList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pic_holder_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        pictureFacer image = pictureFacerList.get(position);
        Glide.with(context)
                .load(image.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.imageIv);
    }

    @Override
    public int getItemCount() {
        return pictureFacerList.size();
    }

    public void refreshAdapter(List<pictureFacer> cvTemplateList) {
        this.pictureFacerList = cvTemplateList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.image)
        ImageView imageIv;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
