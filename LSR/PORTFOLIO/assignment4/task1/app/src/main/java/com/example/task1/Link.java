package com.example.task1;

import android.content.Context;
import android.view.View;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Link {
    private String category;
    private String title;
    private String photo;
    private String URL;

    public Link(String category, String title, String photo, String URL) {
        this.category = category;
        this.title = title;
        this.photo = photo;
        this.URL = URL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return photo;
    }

    public void setImageName(String imageName) {
        this.photo = imageName;
    }

    public static ArrayList<Link> createLinkList(View view){
        ArrayList<Link> links = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> photo = new ArrayList<>();
        ArrayList<String> URL = new ArrayList<>();
        ArrayList<String> category = new ArrayList<>();

        InputStream input = view.getResources().openRawResource(R.raw.news_items);
        Scanner scanner = new Scanner(input);
        while(scanner.hasNext()){
            String line = scanner.nextLine();

            String[] pieces = line.split(":",2);

            switch(pieces[0]){
                case "title":
                    title.add(pieces[1]);
                    break;
                case "photo":
                    String[] temp = pieces[1].split("\\.");
                    photo.add(temp[0]);
                    break;
                case "website":
                    URL.add(pieces[1]);
                    break;
                case "category":
                    category.add(pieces[1]);
                    break;
            }
        }
        //by right the size is 9,since they are 9 items.
        for(int i=0;i<title.size();i++){
            links.add(new Link(category.get(i),title.get(i),photo.get(i),URL.get(i)));
        }

        return links;
    }
}
