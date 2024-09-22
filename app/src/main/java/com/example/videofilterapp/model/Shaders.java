package com.videffects.sample.model;

import android.graphics.Color;
import com.example.videffects.*;
import com.example.videffects.filter.AutoFixFilter;
import com.example.videffects.filter.GrainFilter;
import com.example.videffects.filter.HueFilter;

// TODO: Rewrite to factory
public class Shaders {

    private static final String SUFFIX = "Effect";

    private final Object[] shaders;

    public Shaders(int width, int height) {
        shaders = new Object[]{
                // Filters
                new AutoFixFilter(),
                new GrainFilter(width, height),
                new HueFilter(),

                // Effects
                new AutoFixEffect(0.0F),
                new BlackAndWhiteEffect(),
                new BrightnessEffect(0.5F),
                new ContrastEffect(0.5F),
                new CrossProcessEffect(),
                new DocumentaryEffect(),
                new DuotoneEffect(),
                new FillLightEffect(0.5F),
                new GammaEffect(1.0F),
                new GreyScaleEffect(),
                new InvertColorsEffect(),
                new LamoishEffect(),
                new PosterizeEffect(),
                new SaturationEffect(0.5F),
                new SepiaEffect(),
                new SharpnessEffect(0.5F),
                new TemperatureEffect(0.5F),
                new TintEffect(Color.GREEN),
                new VignetteEffect(0F)
        };
    }

    public int getCount() {
        return shaders.length;
    }

    public Object getShader(int index) {
        return shaders[index];
    }

    public String getShaderName(int index) {
        return shaders[index].getClass().getSimpleName().replace(SUFFIX, "");
    }
}
