package com.example.god.tourguide;


public class word {
    private int Heading;
    private int Address;
    private int ImageId;
    private static final int no_image=-1;
    public word(int heading_Name,int Address_of_place,int Image){
        Heading=heading_Name;
        Address=Address_of_place;
        ImageId=Image;
    }
    public int getHeadingId(){
        return Heading;
    }
    public int getAddressId(){
        return Address;
    }
    public int getImageId(){
        return ImageId;
    }
    public boolean hasImage(){
        return ImageId!=no_image;
    }
}
