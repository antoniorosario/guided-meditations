package com.antoniorosario.guidedmeditation.model;

public class Meditation {

    private String assetPath;
    private String title;
    private String author;

    public Meditation(String assetPath) {
        this.assetPath = assetPath;
        String[] components = assetPath.split("/");
        String fileName = components[components.length - 1];
        if (fileName.contains("TaraBrach")) {
            this.author = "Tara Brach";
        }
        title = fileName.replace(".mp3", "").replace("-", " ").replace("TaraBrach", "");
    }

    public String getAssetPath() {
        return assetPath;
    }

    public String getTitle() {
        return title;
    }


    public String getAuthor() {
        return author;
    }
}
