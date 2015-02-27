package com.mx.kanjo.openclothes.util;

import android.support.v4.util.Pair;

/**
 * Created by JARP on 26/02/2015.
 */
public final class ConfigImageHelp {

    private final Pair<Integer,Integer> sizeImage ;
    private final Boolean roundImage ;

    public Pair<Integer, Integer> getSizeImage() {
        return sizeImage;
    }

    public Boolean getRoundImage() {
        return roundImage;
    }

    private  ConfigImageHelp(final Pair<Integer, Integer> sizeImage, final Boolean roundImage) {
        this.sizeImage = sizeImage;
        this.roundImage = roundImage;
    }

    public static class ConfigImageHelpBuilder {
        private Pair<Integer,Integer> sizeImage ;
        private Boolean roundImage ;

        private ConfigImageHelpBuilder(final Pair<Integer,Integer> sizeImage) {
            this.sizeImage = sizeImage;
        }


        public ConfigImageHelpBuilder withRoundImage(Boolean roundImage) {
            this.roundImage = roundImage;
            return this;
        }

        public ConfigImageHelp build() {
            ConfigImageHelp configImageHelp = new ConfigImageHelp(sizeImage, roundImage);
            return configImageHelp;
        }
    }
}
