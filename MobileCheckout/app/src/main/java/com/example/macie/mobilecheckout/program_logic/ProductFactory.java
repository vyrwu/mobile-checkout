package com.example.macie.mobilecheckout.program_logic;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Produces new Product object supplied with data fetched from Firebase database.
 */

public class ProductFactory {

    private static String name;
    private static Long price;
    private static String color;
    private static String imageUrl;

    /**
     * Establishes connection with Firebase and fetches data about product identified with barcode
     * String. Creates Product object with this data and propagates it inside an Intent.
     * @param context
     * @param barcode
     */
    public static void downloadProductInformation(final Context context, final String barcode) {
        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference();
        databaseReference.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (barcode.equals(dataSnapshot.getKey())) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        switch (child.getKey()) {
                            case "Name":
                                name = (String) child.getValue();
                                System.out.println(name);
                                break;
                            case "Price":
                                price = (Long) child.getValue();
                                System.out.println(price);
                                break;
                            case "Color":
                                color = (String) child.getValue();
                                System.out.println(color);
                                break;
                            case "Url":
                                imageUrl = (String) child.getValue();
                                System.out.println(imageUrl);
                                break;
                        }
                    }
                    Product newProduct = new Product(barcode, name, price, color, imageUrl);
                    context.sendBroadcast(new Intent("Product Information").putExtra("product", newProduct));
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

