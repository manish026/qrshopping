package com.demo.qrshopping;

/**
 * Created by manish on 26/04/18.
 */

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final Context context;
    private List<Product> values;
    Boolean showDelete = false;
    DataChanged dataChanged;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public Button delete;
        public View layout;


        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            delete = v.findViewById(R.id.delete);
            if(!showDelete){
                delete.setVisibility(View.GONE);
            }
        }
    }

    public void remove(int position) {
        if( values.size()>position) {
            values.remove(position);
//            CustomerActivity.input.remove(position);
        }
        notifyDataSetChanged();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductAdapter(List<Product> myDataset,Context context,Boolean showDelete) {
        values = myDataset;
        this.context = context;
        this.showDelete = showDelete;
    }

    public ProductAdapter(List<Product> myDataset,Context context,Boolean showDelete,DataChanged dataChanged) {
        values = myDataset;
        this.context = context;
        this.showDelete = showDelete;
        this.dataChanged = dataChanged;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Product product = values.get(position);
        if(showDelete){
            holder.txtHeader.setText(product.name);
        }else
        holder.txtHeader.setText(product.name+" ("+product.quantity+")");
        holder.delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(product,position);

            }
        });

        holder.txtFooter.setText("₹ "+product.price);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

    private void deleteProduct(final Product product, final int position){
        JSONObject json = new JSONObject();
        try {
            json.put("id",product.id);
            json.put("price",product.price.toString());
            json.put("name",product.name.toString());
            json.put("shopid",Utility.shopId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url =  context.getString(R.string.baseURL)+"product.php?AddProduct="+json;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Utility.showToast("Item removed",context);
                try {
                    String s = new String(responseBody,"UTF-8");
                    Utility.total -= Float.parseFloat(product.price);
                    remove(position);
                    if(dataChanged!= null) {
                        dataChanged.totalChanged();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utility.showToast("Please try again",context);
            }
        });
    }

}