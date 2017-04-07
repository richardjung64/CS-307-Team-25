package com.styln;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ItemActivity extends AppCompatActivity {

    private static final String LOG_TAG = ItemActivity.class.getSimpleName();

    private ImageView itemImage;
    private TextView name,sku,brand,price,description,numLikes,numOwned,rank,availability;
    private String SKUKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        SKUKey = getIntent().getStringExtra("SKU");

        name = (TextView)findViewById(R.id.itemName);
        brand = (TextView)findViewById(R.id.itemBrand);
        sku = (TextView)findViewById(R.id.sku);
        itemImage = (ImageView)findViewById(R.id.itemImage);
        description = (TextView)findViewById(R.id.itemDescription);
        price = (TextView)findViewById(R.id.itemPrice);

        if(SKUKey.equals("1")){
            name.setText("Shirt");
            brand.setText("Adidas");
            sku.setText("SKU: 1");
            itemImage.setImageResource(R.drawable.item_1);
            description.setText("Hey its a black tshirt.");
        }
        if(SKUKey.equals("2")){
            name.setText("Shoe");
            brand.setText("Adidas");
            sku.setText("SKU: 2");
            itemImage.setImageResource(R.drawable.item_2);
            description.setText("Hey its a white shoe.");

        }



    }

    public void openHome(View view) {
        Log.d(LOG_TAG, "Launching Home Activity...");
        startActivity(new Intent(ItemActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openTrend(View view) {
        Log.d(LOG_TAG, "Launching Trend Activity...");
        startActivity(new Intent(ItemActivity.this, TrendActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openPost(View view) {
    }

    public void openBrowse(View view) {
        Log.d(LOG_TAG, "Launching Browse Activity...");
        startActivity(new Intent(ItemActivity.this, BrowseActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openProfile(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(ItemActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openLink(View view) {
        Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void itemAction(View view) {
        showPopupMenu(view);
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.item_action_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new ItemActivity.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_like:
                    Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_add_to_wardrobe:
                    Toast.makeText(getApplicationContext(), "Added to Wardrobe", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_add_to_wishlist:
                    Toast.makeText(getApplicationContext(), "Added to Wishlist", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
}
