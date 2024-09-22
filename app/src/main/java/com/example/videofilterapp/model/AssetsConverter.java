package com.example.videofilterapp.model;

import android.content.res.AssetFileDescriptor;

import com.example.videffects.model.Converter;
import com.example.videffects.model.Metadata;


import java.io.IOException;

public class AssetsConverter extends Converter {

    public AssetsConverter(AssetFileDescriptor assetFileDescriptor) throws IOException {
        setMetadata(assetFileDescriptor);
        try {
            videoExtractor.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            audioExtractor.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMetadata(AssetFileDescriptor assetFileDescriptor) throws IOException {
        Metadata metadata = new AssetsMetadataExtractor().extract(assetFileDescriptor);
        if (metadata != null) {
            width = (int) metadata.getWidth();
            height = (int) metadata.getHeight();
            bitrate = metadata.getBitrate();
        }
    }
}
