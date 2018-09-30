package com.example.macie.mobilecheckout.program_logic;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Contains data about scanned item. Also something with parcelling.
 */

public class Product implements Parcelable {
    private String barcode;
    private String name;
    private Long price;
    private String color;
    private String imageUrl;

    /**
     * Generates instance of Product from a Parcel object.
     */

    public static final Parcelable.Creator<Product> CREATOR =
            new Parcelable.Creator<Product>() {
                @Override
                public Product createFromParcel(Parcel in) {
                    return new Product(in);
                }

                @Override
                public Product[] newArray(int size) {
                    return new Product[size];
                }
            };

    /**
     * Constructor for Product. Initializes declared variables.
     * @param barcode
     * @param name
     * @param price
     * @param color
     * @param imageUrl
     */
    public Product(String barcode, String name, Long price, String color, String imageUrl) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.color = color;
        this.imageUrl = imageUrl;
    }

    /**
     * Constructor for reading data from parcelled object.
     * @param in
     */
    private Product(Parcel in) {
        barcode = in.readString();
        name = in.readString();
        price = in.readLong();
        color = in.readString();
        imageUrl = in.readString();
    }

    /**
     * Writes Product information into a Parcel.
     * @param out
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(barcode);
        out.writeString(name);
        out.writeLong(price);
        out.writeString(color);
        out.writeString(imageUrl);
    }

    /**
     *
     * @return HashCode of Product object.
     */
    @Override
    public int describeContents() {
        return hashCode();
    }

    public String getBarcode() { return barcode; }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return name;
    }
}
