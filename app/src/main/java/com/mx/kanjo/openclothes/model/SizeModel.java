package com.mx.kanjo.openclothes.model;

/**
 * Created by JARP on 12/9/14.
 */
public class SizeModel
{
    private int idSize;
    private String sizeDescription;

    public SizeModel(int idSize, String sizeDescription) {
        this.idSize = idSize;
        this.sizeDescription = sizeDescription;
    }

    public SizeModel() {
    }

    public int getIdSize() {
        return idSize;
    }

    public void setIdSize(int idSize) {
        this.idSize = idSize;
    }

    public String getSizeDescription() {
        return sizeDescription;
    }

    public void setSizeDescription(String sizeDescription) {
        this.sizeDescription = sizeDescription;
    }
}