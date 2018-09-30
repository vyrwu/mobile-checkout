package com.example.macie.mobilecheckout.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macie.mobilecheckout.program_logic.Product;
import com.example.macie.mobilecheckout.R;
import com.squareup.picasso.Picasso;

/**
 * Adapter used for arranging information in PopupFragment.
 */

public class ProductDetailsAdapter extends ArrayAdapter<Product> {
    Context context;
    Product product;

    /**
     * Constructor for ProductDetailsAdapter. Initializes declared variables.
     * @param context
     * @param resourceId
     * @param product
     */
    public ProductDetailsAdapter(Context context, int resourceId, Product product) {
        super(context, resourceId);
        this.context = context;
        this.product = product;
    }

    /**
     * Inner class containing Views being used in PopupFragment Dialog.
     */
    private class ViewHolder {
        ImageView imageView;
        TextView barcodeView;
        TextView nameView;
        TextView priceView;
        TextView colorView;
    }

    /**
     * Declares ViewHolder, binds its fields to layouts and fills them with appropriate data
     * from Product.
     * @param position
     * @param contentView
     * @param parent
     * @return a View field with Product information
     */
    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.product_details, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)contentView.findViewById(R.id.image);
            viewHolder.barcodeView = (TextView)contentView.findViewById(R.id.barcode_text) ;
            viewHolder.nameView = (TextView)contentView.findViewById(R.id.name_text);
            viewHolder.colorView = (TextView)contentView.findViewById(R.id.color_text);
            viewHolder.priceView = (TextView)contentView.findViewById(R.id.price_text);
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)contentView.getTag();
        }
        Picasso.with(context).load(product.getImageUrl()).into(viewHolder.imageView);
        viewHolder.barcodeView.setText(product.getBarcode());
        viewHolder.nameView.setText(product.getName());
        viewHolder.colorView.setText(product.getColor());
        viewHolder.priceView.setText(String.format("%s", product.getPrice() + ",-"));
        return contentView;
    }

    /**
     * Restricts Adapter to fill only 1 View with information.
     * @return
     */
    @Override
    public int getCount() {
        return 1;
    }
}
