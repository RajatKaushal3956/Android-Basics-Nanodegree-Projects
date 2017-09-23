package com.example.god.bookapp;

public class Book {
    private String mTitle;
    private String []mAuthors;
    private String mDescription;
    private String mlink;
    public Book(String Title, String []Authors, String Description, String link){
        mTitle=Title;
        mAuthors=Authors;
        mDescription=Description;
        mlink=link;
    }
    public String getTitle(){return mTitle;}
    public String getAuthorsString(){
        String s = "";
        for(int i=0;i<mAuthors.length;i++) {
            if(i == mAuthors.length-1)
                s += mAuthors[i];
            else
                s += mAuthors[i] + ", ";
        }
        return s;
    }
    public String getDescription(){return mDescription;}
    public String getlink(){return mlink;}
}
