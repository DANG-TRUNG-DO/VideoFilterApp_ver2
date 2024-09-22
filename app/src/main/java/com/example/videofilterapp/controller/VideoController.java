package com.example.videofilterapp.controller;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.SeekBar;
import androidx.annotation.RequiresApi;
import com.example.videffects.filter.AutoFixFilter;
import com.example.videffects.filter.GrainFilter;
import com.example.videffects.filter.HueFilter;
import com.example.videffects.filter.NoEffectFilter;
import com.example.videffects.interfaces.ConvertResultListener;
import com.example.videffects.interfaces.Filter;
import com.example.videffects.interfaces.ShaderInterface;
import com.example.videffects.model.Metadata;
import com.example.videofilterapp.interfaces.ProgressChangeListener;

import com.example.videofilterapp.model.AssetsConverter;
import com.example.videofilterapp.model.AssetsMetadataExtractor;
import com.example.videofilterapp.model.ViewExtensions;
import com.example.videofilterapp.view.ShaderChooserDialog;
import com.videffects.sample.view.VideoActivity;
import java.io.File;
import java.io.IOException;

public class VideoController {

    private static final String TAG = "VideoController";

    private VideoActivity activity;
    private Filter filter = new NoEffectFilter();
    private AssetFileDescriptor assetFileDescriptor;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Metadata metadata;

    private ProgressChangeListener intensityChangeListener = new ProgressChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (filter instanceof GrainFilter) {
                ((GrainFilter) filter).setIntensity(ViewExtensions.transformGrain(progress));
            } else if (filter instanceof HueFilter) {
                ((HueFilter) filter).setIntensity(ViewExtensions.transformHue(progress));
            } else if (filter instanceof AutoFixFilter) {
                ((AutoFixFilter) filter).setIntensity(ViewExtensions.transformAutofix(progress));
            } else {
                if (activity != null) {
                    activity.showToast("Changing intensity not implemented for selected effect in this demo");
                }
            }
        }
    };

    public VideoController(VideoActivity activity, String filename) throws RuntimeException, IOException {
        this.activity = activity;

        if (activity != null) {
            this.assetFileDescriptor = activity.getAssets().openFd(filename);
        } else {
            throw new RuntimeException("Asset not found");
        }

        this.metadata = new AssetsMetadataExtractor().extract(assetFileDescriptor);

        setupMediaPlayer();
        setupView();
    }

    private void setupMediaPlayer() {
        mediaPlayer.setLooping(true);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
        } catch (Exception e) {
            Log.d(TAG, "Error setting data source: " + e.getMessage());
        }

        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.d(TAG, "OnError! What: " + what + "; Extra: " + extra);
            return false;
        });

        mediaPlayer.setOnCompletionListener(mp -> Log.d(TAG, "OnComplete!"));
    }

    private void setupView() {
        if (metadata != null && activity != null) {
            activity.setupVideoSurfaceView(mediaPlayer, metadata.getWidth(), metadata.getHeight());
            activity.setupSeekBar(intensityChangeListener);
        }
    }

    public void chooseShader() {
        if (metadata == null || activity == null) return;

        int videoWidth = (int) metadata.getWidth();
        int videoHeight = (int) metadata.getHeight();

        ShaderChooserDialog dialog = ShaderChooserDialog.newInstance(videoWidth, videoHeight);
        dialog.setListener(shader -> {
            if (shader instanceof ShaderInterface) {
                filter = new NoEffectFilter();
                activity.onSelectShader((ShaderInterface) shader);
            } else if (shader instanceof Filter) {
                filter = (Filter) shader;
                activity.onSelectFilter((Filter) shader);
            }
        });

        if (activity != null) {
            dialog.show(activity.getSupportFragmentManager(), ShaderChooserDialog.class.getSimpleName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveVideo() throws IOException {
        if (filter instanceof NoEffectFilter) {
            if (activity != null) {
                activity.showToast("Saving will work only with Filters.");
            }
            return;
        }

        File parent = activity != null ? activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) : null;
        if (parent == null) {
            throw new RuntimeException("Activity is destroyed!");
        }

        String child = "out.mp4";
        String outPath = new File(parent, child).toString();

        AssetConverterThread assetConverterThread = new AssetConverterThread(
                new AssetsConverter(assetFileDescriptor),
                filter,
                outPath,
                new ConvertResultListener() {
                    @Override
                    public void onSuccess() {
                        if (activity != null) {
                            activity.onFinishSavingVideo("Video successfully saved at " + outPath);
                        }
                    }

                    @Override
                    public void onFail() {
                        if (activity != null) {
                            activity.onFinishSavingVideo("Video wasn't saved. Check log for details.");
                        }
                    }
                }
        );

        if (activity != null) {
            activity.onStartSavingVideo();
        }
        assetConverterThread.start();
    }

    public void onPause() {
        mediaPlayer.pause();
    }

    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        try {
            assetFileDescriptor.close();
        } catch (Exception e) {
            Log.d(TAG, "Error closing AssetFileDescriptor: " + e.getMessage());
        }
        activity = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static class AssetConverterThread extends Thread {

        private AssetsConverter assetsConverter;
        private Filter filter;
        private String outPath;
        private ConvertResultListener listener;

        AssetConverterThread(AssetsConverter assetsConverter, Filter filter, String outPath, ConvertResultListener listener) {
            this.assetsConverter = assetsConverter;
            this.filter = filter;
            this.outPath = outPath;
            this.listener = listener;
        }

        @Override
        public void run() {
            super.run();
            assetsConverter.startConverter(filter, outPath, listener);
        }
    }
}
