package com.styln;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class ItemActivity extends AppCompatActivity {

    private static final String LOG_TAG = ItemActivity.class.getSimpleName();

    private ImageView itemImage;
    private TextView name,brand,price,description,type,year,color,numLikes,numOwned,rank,availability,storeLink;
    private ImageView availabilityColor;
    private String SKUKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        SKUKey = getIntent().getStringExtra("SKU");

        name = (TextView)findViewById(R.id.itemName);
        type = (TextView)findViewById(R.id.itemType);
        year = (TextView)findViewById(R.id.itemYear);
        brand = (TextView)findViewById(R.id.itemBrand);
        itemImage = (ImageView)findViewById(R.id.itemImage);
        description = (TextView)findViewById(R.id.itemDescription);
        price = (TextView)findViewById(R.id.itemPrice);
        numLikes = (TextView)findViewById(R.id.itemNumLikes);
        numOwned = (TextView)findViewById(R.id.itemNumOwned);
        availability = (TextView)findViewById(R.id.itemAvailability);
        availabilityColor = (ImageView)findViewById(R.id.itemAvailabilityBG);

        grabItem task = new grabItem();
        ClothingDO currentItem = new ClothingDO();
        try {
            task.id = SKUKey;
            currentItem = task.execute("String").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        String address = currentItem.getClothingPhotoLink();
        name.setText(currentItem.getUserId());
        type.setText(currentItem.getClothingType());
        year.setText(currentItem.getClothingYear());
        price.setText("$ "+currentItem.getClothingPrice());
        brand.setText(currentItem.getClothingBrand());
        numLikes.setText(""+currentItem.getClothingLikes());
        numOwned.setText(""+currentItem.getClothingOwned());
        description.setText(currentItem.getClothingDescription());

        if(currentItem.getClothingAvailability()) {
            availabilityColor.setImageDrawable(getResources().getDrawable(R.color.green));
            availability.setText("Available: Yes");
        } else {
            availabilityColor.setImageDrawable(getResources().getDrawable(R.color.red));
            availability.setText("Available: NO");
        }

        Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                thumbnail(0.1f).into(itemImage);


    }

    private class grabItem extends AsyncTask<String, Void, ClothingDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        ClothingDO loadresult = new ClothingDO();
        String id;

        protected ClothingDO doInBackground(String... strings) {
            ClothingDO currentItem;
            String itemID = ""+id;
            currentItem = mapper.load(ClothingDO.class, itemID);
            loadresult = currentItem;
            return loadresult;
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
        Uri uri = Uri.parse("https://www.hollisterco.com/shop/us"); // missing 'http://' will cause crashed
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
            DataAction da = new DataAction();
            switch (menuItem.getItemId()) {
                case R.id.menu_like:
                    da.likeClothing(getIntent().getStringExtra("SKU"));
                    Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();

                    Log.d(LOG_TAG, "Refresh Item");
                    startActivity(new Intent(ItemActivity.this, ItemActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("SKU",getIntent().getStringExtra("SKU")));
                    finish();
                    return true;
                case R.id.menu_add_to_wardrobe:
                    da.ownClothing(getIntent().getStringExtra("SKU"));
                    Toast.makeText(getApplicationContext(), "Added to Wardrobe", Toast.LENGTH_SHORT).show();

                    Log.d(LOG_TAG, "Refresh Item");
                    startActivity(new Intent(ItemActivity.this, ItemActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("SKU",getIntent().getStringExtra("SKU")));
                    finish();

                    return true;
                case R.id.menu_add_to_wishlist:
                    da.wishClothing(getIntent().getStringExtra("SKU"));
                    Toast.makeText(getApplicationContext(), "Added to Wishlist", Toast.LENGTH_SHORT).show();

                    Log.d(LOG_TAG, "Refresh Item");
                    startActivity(new Intent(ItemActivity.this, ItemActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("SKU",getIntent().getStringExtra("SKU")));
                    finish();
                    return true;
                default:
            }
            return false;
        }
    }
}
