package com.example.macie.mobilecheckout.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macie.mobilecheckout.R;
import com.example.macie.mobilecheckout.program_logic.Product;
import com.example.macie.mobilecheckout.program_logic.VirtualBasket;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter used for arranging information in VirtualBasket.
 */

public class ProductListAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> productList;

    /**
     * Inner class containing Views being used in List items.
     */

    private class ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView priceView;
        ImageButton removeButton;
    }

    /**
     * Constructor for ProductListAdapter. Initializes declared variables.
     * @param context
     * @param resourceId
     * @param productList
     */

    public ProductListAdapter(Context context, int resourceId, List<Product> productList) {
        super(context, resourceId, productList);
        this.context = context;
        this.productList = productList;
    }

    /**
     * Declares ViewHolder, binds its fields to layouts and fills them with appropriate data
     * from Product. Sets OnClickListener to item removal Button.
     * @param position
     * @param contentView
     * @param parent
     * @return View field with Product information.
     */

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.product_list, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)contentView.findViewById(R.id.image_thumbnail_view);
            viewHolder.nameView = (TextView)contentView.findViewById(R.id.product_name_view);
            viewHolder.priceView = (TextView)contentView.findViewById(R.id.list_price_view);
            viewHolder.removeButton = (ImageButton)contentView.findViewById(R.id.remove_button);
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)contentView.getTag();
        }
        final Product product = productList.get(position);
        Picasso.with(context).load(product.getImageUrl()).into(viewHolder.imageView);
        viewHolder.nameView.setText(product.getName());
        viewHolder.priceView.setText(String.format("%s", product.getPrice() + ",-"));
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VirtualBasket.getInstance().remove(product);
                notifyDataSetChanged();
                Toast.makeText(getContext(), "Item removed", Toast.LENGTH_SHORT).show();
            }
        });
        return contentView;
    }
}
