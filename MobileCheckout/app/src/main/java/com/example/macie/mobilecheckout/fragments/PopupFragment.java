package com.example.macie.mobilecheckout.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.macie.mobilecheckout.R;
import com.example.macie.mobilecheckout.activities.ScannerActivity;
import com.example.macie.mobilecheckout.activities.VirtualBasketActivity;
import com.example.macie.mobilecheckout.adapters.ProductDetailsAdapter;
import com.example.macie.mobilecheckout.program_logic.Product;
import com.example.macie.mobilecheckout.program_logic.VirtualBasket;

/**
 * Creates DialogFragment popup window customized for product information.
 */

public class PopupFragment extends DialogFragment {
    private ListView productInfoListView;

    /**
     * Builds AlertDialog on Activity this was called from. Sets different PositiveButtons and NegativeButtons
     * depending on which Activity called for PopupDialog instance. Binds views with corresponding layouts.
     * Creates ProductDetailsAdapter used to arrange data inside a popup and binds it to ListView.
     * @param savedInstanceState
     * @return Created AlertDialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Product product = getArguments().getParcelable("product");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(getActivity() instanceof ScannerActivity) {
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    VirtualBasket.getInstance().add(product);
                    Toast.makeText(getContext(), "Added to basket", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
        }

        if(getActivity() instanceof VirtualBasketActivity){
            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_popup, null);

        productInfoListView = (ListView) view.findViewById(R.id.product_information);
        ProductDetailsAdapter productDetailsAdapter =
                new ProductDetailsAdapter(getContext(), R.layout.product_details, product);
        productInfoListView.setAdapter(productDetailsAdapter);

        builder.setView(view);

        return builder.create();
    }

    /**
     * Sends Intent when Popup is being closed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent closePopup = new Intent("Close Popup");
        this.getContext().sendBroadcast(closePopup);
    }

    /**
     * Static method creating new instance of PopupFragment, which contains arguments put into
     * Bundle. Should be called whenever new PopupFragment instance is needed.
     * @param title
     * @param product
     * @return Instance of PopupFragment with custom data.
     */
    public static PopupFragment newInstance(int title, Product product) {
        PopupFragment frag = new PopupFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putParcelable("product", product);
        frag.setArguments(args);
        return frag;
    }
}

