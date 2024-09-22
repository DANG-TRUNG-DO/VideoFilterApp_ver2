package com.example.videofilterapp.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sample.databinding.ActivityGalleryBinding;
import com.example.videofilterapp.model.AssetsGalleryModel;
import com.example.videofilterapp.model.ViewExtensions;
import com.example.videofilterapp.view.SamplePlayerActivity;


public class AssetsGalleryActivity extends AppCompatActivity {

    private View binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        binding.recyclerView.setAdapter(new PreviewAdapter(new AssetsGalleryModel(this)));
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration());

        binding.buttonSamplePlayer.setOnClickListener(v -> startActivity(new Intent(this, SamplePlayerActivity.class)));
    }

    private static class PreviewAdapter extends RecyclerView.Adapter<PreviewHolder> {

        private final AssetsGalleryModel model;

        PreviewAdapter(AssetsGalleryModel model) {
            this.model = model;
        }

        @Override
        public PreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Activity activity = (Activity) parent.getContext();
            ImageView imageView = new ImageView(activity);
            int width = ViewExtensions.screenWidth(activity) / 2;
            int height = ViewExtensions.screenHeight(activity) / 3;
            imageView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new PreviewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(PreviewHolder holder, int position) {
            holder.setThumbnail(model.getThumbnail(position));
            holder.itemView.setOnClickListener(v -> VideoActivity.startActivity(v.getContext(), model.getAssetName(position)));
        }

        @Override
        public int getItemCount() {
            return model.getCount();
        }
    }

    private static class PreviewHolder extends RecyclerView.ViewHolder {

        PreviewHolder(View itemView) {
            super(itemView);
        }

        void setThumbnail(Drawable thumbnail) {
            ((ImageView) itemView).setImageDrawable(thumbnail);
        }
    }

    private static class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private final int space = ViewExtensions.toPx(4);

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % SPAN_COUNT;
            outRect.left = column * space / SPAN_COUNT;
            outRect.right = space - (column + 1) * space / SPAN_COUNT;
            if (position >= SPAN_COUNT) {
                outRect.top = space;
            }
        }
    }

    private static final int SPAN_COUNT = 2;
}
