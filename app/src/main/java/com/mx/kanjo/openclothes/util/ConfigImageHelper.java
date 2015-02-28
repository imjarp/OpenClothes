package com.mx.kanjo.openclothes.util;

import android.support.v4.util.Pair;

/**
 * Created by JARP on 26/02/2015.
 */
public final class ConfigImageHelper {

    private final Pair<Integer,Integer> sizeImage ;
    private final Boolean roundImage ;

    public Pair<Integer, Integer> getSizeImage() {
        return sizeImage;
    }

    public Boolean roundImage() {
        return roundImage;
    }

    private ConfigImageHelper(final Pair<Integer, Integer> sizeImage, final Boolean roundImage) {
        this.sizeImage = sizeImage;
        this.roundImage = roundImage;
    }

    public static class ConfigImageHelpBuilder {
        private Pair<Integer,Integer> sizeImage ;
        private Boolean roundImage = false ;

        public ConfigImageHelpBuilder(final Pair<Integer,Integer> sizeImage) {
            this.sizeImage = sizeImage;
        }


        public ConfigImageHelpBuilder withRoundImage(Boolean roundImage) {
            this.roundImage = roundImage;
            return this;
        }

        public ConfigImageHelper build() {
            ConfigImageHelper configImageHelper = new ConfigImageHelper(sizeImage, roundImage);
            return configImageHelper;
        }
    }
}
