package com.styln.demo.nosql;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;

import java.util.Set;

public class DemoNoSQLClothingResult implements DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final ClothingDO result;

    DemoNoSQLClothingResult(final ClothingDO result) {
        this.result = result;
    }
    @Override
    public void updateItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        final String originalValue = result.getClothingPhotoLink();
        result.setClothingPhotoLink(DemoSampleDataGenerator.getRandomSampleString("CLothing_PhotoLink"));
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setClothingPhotoLink(originalValue);
            throw ex;
        }
    }

    @Override
    public void deleteItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        mapper.delete(result);
    }

    private void setKeyTextViewStyle(final TextView textView) {
        textView.setTextColor(KEY_TEXT_COLOR);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(5), dp(2), dp(5), 0);
        textView.setLayoutParams(layoutParams);
    }

    /**
     * @param dp number of design pixels.
     * @return number of pixels corresponding to the desired design pixels.
     */
    private int dp(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private void setValueTextViewStyle(final TextView textView) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(15), 0, dp(15), dp(2));
        textView.setLayoutParams(layoutParams);
    }

    private void setKeyAndValueTextViewStyles(final TextView keyTextView, final TextView valueTextView) {
        setKeyTextViewStyle(keyTextView);
        setValueTextViewStyle(valueTextView);
    }

    private static String bytesToHexString(byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02X", bytes[0]));
        for(int index = 1; index < bytes.length; index++) {
            builder.append(String.format(" %02X", bytes[index]));
        }
        return builder.toString();
    }

    private static String byteSetsToHexStrings(Set<byte[]> bytesSet) {
        final StringBuilder builder = new StringBuilder();
        int index = 0;
        for (byte[] bytes : bytesSet) {
            builder.append(String.format("%d: ", ++index));
            builder.append(bytesToHexString(bytes));
            if (index < bytesSet.size()) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public View getView(final Context context, final View convertView, int position) {
        final LinearLayout layout;
        final TextView resultNumberTextView;
        final TextView userIdKeyTextView;
        final TextView userIdValueTextView;
        final TextView cLothingPhotoLinkKeyTextView;
        final TextView cLothingPhotoLinkValueTextView;
        final TextView clothingBrandKeyTextView;
        final TextView clothingBrandValueTextView;
        final TextView clothingColorKeyTextView;
        final TextView clothingColorValueTextView;
        final TextView clothingWebLinkKeyTextView;
        final TextView clothingWebLinkValueTextView;
        final TextView clothingDislikesKeyTextView;
        final TextView clothingDislikesValueTextView;
        final TextView clothingLikesKeyTextView;
        final TextView clothingLikesValueTextView;
        if (convertView == null) {
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            resultNumberTextView = new TextView(context);
            resultNumberTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(resultNumberTextView);


            userIdKeyTextView = new TextView(context);
            userIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userIdKeyTextView, userIdValueTextView);
            layout.addView(userIdKeyTextView);
            layout.addView(userIdValueTextView);

            cLothingPhotoLinkKeyTextView = new TextView(context);
            cLothingPhotoLinkValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(cLothingPhotoLinkKeyTextView, cLothingPhotoLinkValueTextView);
            layout.addView(cLothingPhotoLinkKeyTextView);
            layout.addView(cLothingPhotoLinkValueTextView);

            clothingBrandKeyTextView = new TextView(context);
            clothingBrandValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(clothingBrandKeyTextView, clothingBrandValueTextView);
            layout.addView(clothingBrandKeyTextView);
            layout.addView(clothingBrandValueTextView);

            clothingColorKeyTextView = new TextView(context);
            clothingColorValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(clothingColorKeyTextView, clothingColorValueTextView);
            layout.addView(clothingColorKeyTextView);
            layout.addView(clothingColorValueTextView);

            clothingWebLinkKeyTextView = new TextView(context);
            clothingWebLinkValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(clothingWebLinkKeyTextView, clothingWebLinkValueTextView);
            layout.addView(clothingWebLinkKeyTextView);
            layout.addView(clothingWebLinkValueTextView);

            clothingDislikesKeyTextView = new TextView(context);
            clothingDislikesValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(clothingDislikesKeyTextView, clothingDislikesValueTextView);
            layout.addView(clothingDislikesKeyTextView);
            layout.addView(clothingDislikesValueTextView);

            clothingLikesKeyTextView = new TextView(context);
            clothingLikesValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(clothingLikesKeyTextView, clothingLikesValueTextView);
            layout.addView(clothingLikesKeyTextView);
            layout.addView(clothingLikesValueTextView);
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            userIdKeyTextView = (TextView) layout.getChildAt(1);
            userIdValueTextView = (TextView) layout.getChildAt(2);

            cLothingPhotoLinkKeyTextView = (TextView) layout.getChildAt(3);
            cLothingPhotoLinkValueTextView = (TextView) layout.getChildAt(4);

            clothingBrandKeyTextView = (TextView) layout.getChildAt(5);
            clothingBrandValueTextView = (TextView) layout.getChildAt(6);

            clothingColorKeyTextView = (TextView) layout.getChildAt(7);
            clothingColorValueTextView = (TextView) layout.getChildAt(8);

            clothingWebLinkKeyTextView = (TextView) layout.getChildAt(9);
            clothingWebLinkValueTextView = (TextView) layout.getChildAt(10);

            clothingDislikesKeyTextView = (TextView) layout.getChildAt(11);
            clothingDislikesValueTextView = (TextView) layout.getChildAt(12);

            clothingLikesKeyTextView = (TextView) layout.getChildAt(13);
            clothingLikesValueTextView = (TextView) layout.getChildAt(14);
        }

        resultNumberTextView.setText(String.format("#%d", + position+1));
        userIdKeyTextView.setText("userId");
        userIdValueTextView.setText(result.getUserId());
        cLothingPhotoLinkKeyTextView.setText("CLothing_PhotoLink");
        cLothingPhotoLinkValueTextView.setText(result.getClothingPhotoLink());
        clothingBrandKeyTextView.setText("Clothing_Brand");
        clothingBrandValueTextView.setText(result.getClothingBrand());
        clothingColorKeyTextView.setText("Clothing_Color");
        clothingColorValueTextView.setText(result.getClothingColor());
        clothingWebLinkKeyTextView.setText("Clothing_WebLink");
        clothingWebLinkValueTextView.setText(result.getClothingPhotoLink());
        clothingDislikesKeyTextView.setText("Clothing_dislikes");
        clothingDislikesValueTextView.setText("" + result.getClothingDislikes().longValue());
        clothingLikesKeyTextView.setText("Clothing_likes");
        clothingLikesValueTextView.setText("" + result.getClothingLikes().longValue());
        return layout;
    }
}
