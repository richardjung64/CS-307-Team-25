package com.styln;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {


    private static final String LOG_TAG = FollowActivity.class.getSimpleName();
    private String pageKey;

    private List<Item> Wardrobe = new ArrayList<>();
    private List<Item> Wishlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private CollectionItemsAdapter iAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        pageKey = getIntent().getStringExtra("KEY");
        Log.d(LOG_TAG, "Opened from " + pageKey);


        if(pageKey.equals("wardrobe")){
            iAdapter = new CollectionItemsAdapter(this, Wardrobe);
            prepareWardrobeData();
        } else {
            iAdapter = new CollectionItemsAdapter(this,Wishlist);
            prepareWishlistData();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

    }


    public void back(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(CollectionActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        // finish should always be called on the main thread.
        finish();
    }


    private void prepareWardrobeData() {
        Item item = new Item("Tshirt 1", "Adidas",1,R.drawable.item_1);
        Wardrobe.add(item);

        item = new Item("Tshirt 2", "Adidas",1,R.drawable.item_1);
        Wardrobe.add(item);
        item = new Item("Tshirt 3", "Adidas",1,R.drawable.item_1);
        Wardrobe.add(item);
        item = new Item("Tshirt 4", "Adidas",1,R.drawable.item_1);
        Wardrobe.add(item);
        item = new Item("Tshirt 5", "Adidas",1,R.drawable.item_1);
        Wardrobe.add(item);
        item = new Item("Tshirt 6", "Adidas",1,R.drawable.item_1);
        Wardrobe.add(item);
        item = new Item("Tshirt 7", "Adidas",1,R.drawable.item_1);
        Wardrobe.add(item);
        item = new Item("Tshirt 8", "Adidas",1,R.drawable.item_1);
        Wardrobe.add(item);
        item = new Item("Shoe 1", "Adidas",2,R.drawable.item_2);
        Wardrobe.add(item);

        iAdapter.notifyDataSetChanged();
    }


    private void prepareWishlistData() {
        Item item = new Item("Shoe 1", "Adidas",2, R.drawable.item_2);
        Wishlist.add(item);
        iAdapter.notifyDataSetChanged();
    }

    public void refreshWardrobe(View view) {
        startActivity(new Intent(CollectionActivity.this, CollectionActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","wardrobe"));
        finish();
    }
    public void refreshWishlist(View view) {
        startActivity(new Intent(CollectionActivity.this, CollectionActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","wishlist"));
        finish();
    }
}
