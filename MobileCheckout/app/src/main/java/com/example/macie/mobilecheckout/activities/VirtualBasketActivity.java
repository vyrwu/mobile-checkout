package com.example.macie.mobilecheckout.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.macie.mobilecheckout.R;
import com.example.macie.mobilecheckout.adapters.ProductListAdapter;
import com.example.macie.mobilecheckout.fragments.PopupFragment;
import com.example.macie.mobilecheckout.program_logic.Product;
import com.example.macie.mobilecheckout.program_logic.VirtualBasket;

import java.util.List;

/**
 * Virtual basket activity containing ListView with a list of added items.
 */

public class VirtualBasketActivity extends AppCompatActivity {

    private ListView mListView;
    private ListAdapter mListAdapter;

    /**
     * Sets up the window change animation style, binds fields to layouts. Creates a Product list
     * containing information about scanned items. Initializes custom ListAdapter to be used within
     * the item layout. Sets logic of OnItemClickListener to display Dialog with item's details.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.style.Fade;
        setContentView(R.layout.activity_virtual_basket);

        mListView = (ListView)findViewById(R.id.basket_list_view);

        List<Product> productList = VirtualBasket.getInstance().getProductList();

        mListAdapter = new ProductListAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                productList);

        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product chosenProduct = VirtualBasket.getInstance().findByIndex(position);
                showDialog(chosenProduct);
            }
        });
    }

    /**
     * Creates DialogFragment object and sets it to a new instance of PopupFragment class, which
     * is supplied with a Product object. Displays Dialog on the screen.
     * @param product
     */

    public void showDialog(Product product) {
        DialogFragment newFragment = PopupFragment.newInstance(R.string.app_name, product);
        newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }

}
