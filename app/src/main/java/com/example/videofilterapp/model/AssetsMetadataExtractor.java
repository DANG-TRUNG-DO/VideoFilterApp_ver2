package com.example.videofilterapp.model;

import android.content.res.AssetFileDescriptor;

import com.example.videffects.model.Metadata;
import com.example.videffects.model.MetadataExtractor;

import java.io.IOException;

public class AssetsMetadataExtractor extends MetadataExtractor {

    public Metadata extract(AssetFileDescriptor assetFileDescriptor) throws IOException {
        retriever.setDataSource(
                assetFileDescriptor.getFileDescriptor(),
                assetFileDescriptor.getStartOffset(),
                assetFileDescriptor.getLength()
        );
        return extractMetadata();
    }

}
