package com.example.videofilterapp.view;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.videofilterapp.interfaces.OnSelectShaderListener;
import com.videffects.sample.model.Shaders;

public class ShaderChooserDialog extends DialogFragment {

    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";

    private OnSelectShaderListener listener;

    public static ShaderChooserDialog newInstance(int videoViewWidth, int videoViewHeight) {
        ShaderChooserDialog dialog = new ShaderChooserDialog();
        Bundle args = new Bundle();
        args.putInt(WIDTH, videoViewWidth);
        args.putInt(HEIGHT, videoViewHeight);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int width = getArguments() != null ? getArguments().getInt(WIDTH) : 0;
        int height = getArguments() != null ? getArguments().getInt(HEIGHT) : 0;

        Shaders shaders = new Shaders(width, height); // Can take some time and may block UI

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Choose effect")
                .setItems(new String[shaders.getCount()], (dialog, which) -> {
                    if (listener != null) {
                        listener.onSelectShader(shaders.getShader(which));
                    }
                });

        builder.setOnDismissListener(dialog -> listener = null);
        return builder.create();
    }

    public void setListener(OnSelectShaderListener listener) {
        this.listener = listener;
    }
}
