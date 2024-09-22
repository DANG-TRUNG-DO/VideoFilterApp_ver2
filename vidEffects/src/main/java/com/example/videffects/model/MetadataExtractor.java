package com.example.videffects.model;

import static android.media.MediaMetadataRetriever.METADATA_KEY_BITRATE;
import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT;
import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;
import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH;

import android.media.MediaMetadataRetriever;

import java.io.IOException;

/**
 * Class for extracting metadata from video file.
 * Default implementation work with videos from file system,
 * but class can be easily extended for working with any kind of data source.
 */
public class MetadataExtractor {

    protected MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    public Metadata extract(String path) throws IOException {
        retriever.setDataSource(path);
        return extractMetadata();
    }

    protected Metadata extractMetadata() throws IOException {
        String rotationString = retriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION);
        String bitrateString = retriever.extractMetadata(METADATA_KEY_BITRATE);
        String widthString = retriever.extractMetadata(METADATA_KEY_VIDEO_WIDTH);
        String heightString = retriever.extractMetadata(METADATA_KEY_VIDEO_HEIGHT);

        Metadata metadata = null;

        try {
            int rotation = Integer.parseInt(rotationString);
            int bitrate = Integer.parseInt(bitrateString);
            double width = Double.parseDouble(widthString);
            double height = Double.parseDouble(heightString);

            if (rotation == 90 || rotation == 270) {
                metadata = new Metadata(height, width, bitrate);
            } else {
                metadata = new Metadata(width, height, bitrate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        retriever.release();
        return metadata;
    }
}
