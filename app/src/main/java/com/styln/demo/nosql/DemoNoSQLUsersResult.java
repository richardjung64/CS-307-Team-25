package com.styln.demo.nosql;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;

import java.util.Set;

public class DemoNoSQLUsersResult implements DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final UsersDO result;

    DemoNoSQLUsersResult(final UsersDO result) {
        this.result = result;
    }
    @Override
    public void updateItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        final double originalValue = result.getUserAge();
        result.setUserAge(DemoSampleDataGenerator.getRandomSampleNumber());
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setUserAge(originalValue);
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
        final TextView userAgeKeyTextView;
        final TextView userAgeValueTextView;
        final TextView userGenderKeyTextView;
        final TextView userGenderValueTextView;
        final TextView userPhotoKeyTextView;
        final TextView userPhotoValueTextView;
        final TextView userPostKeyTextView;
        final TextView userPostValueTextView;
        final TextView userPrivacyKeyTextView;
        final TextView userPrivacyValueTextView;
        final TextView usersFollowersKeyTextView;
        final TextView usersFollowersValueTextView;
        final TextView usersFollowingKeyTextView;
        final TextView usersFollowingValueTextView;
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

            userAgeKeyTextView = new TextView(context);
            userAgeValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userAgeKeyTextView, userAgeValueTextView);
            layout.addView(userAgeKeyTextView);
            layout.addView(userAgeValueTextView);

            userGenderKeyTextView = new TextView(context);
            userGenderValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userGenderKeyTextView, userGenderValueTextView);
            layout.addView(userGenderKeyTextView);
            layout.addView(userGenderValueTextView);

            userPhotoKeyTextView = new TextView(context);
            userPhotoValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userPhotoKeyTextView, userPhotoValueTextView);
            layout.addView(userPhotoKeyTextView);
            layout.addView(userPhotoValueTextView);

            userPostKeyTextView = new TextView(context);
            userPostValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userPostKeyTextView, userPostValueTextView);
            layout.addView(userPostKeyTextView);
            layout.addView(userPostValueTextView);

            userPrivacyKeyTextView = new TextView(context);
            userPrivacyValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userPrivacyKeyTextView, userPrivacyValueTextView);
            userPrivacyValueTextView.setTypeface(Typeface.MONOSPACE);
            layout.addView(userPrivacyKeyTextView);
            layout.addView(userPrivacyValueTextView);

            usersFollowersKeyTextView = new TextView(context);
            usersFollowersValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(usersFollowersKeyTextView, usersFollowersValueTextView);
            layout.addView(usersFollowersKeyTextView);
            layout.addView(usersFollowersValueTextView);

            usersFollowingKeyTextView = new TextView(context);
            usersFollowingValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(usersFollowingKeyTextView, usersFollowingValueTextView);
            layout.addView(usersFollowingKeyTextView);
            layout.addView(usersFollowingValueTextView);
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            userIdKeyTextView = (TextView) layout.getChildAt(1);
            userIdValueTextView = (TextView) layout.getChildAt(2);

            userAgeKeyTextView = (TextView) layout.getChildAt(3);
            userAgeValueTextView = (TextView) layout.getChildAt(4);

            userGenderKeyTextView = (TextView) layout.getChildAt(5);
            userGenderValueTextView = (TextView) layout.getChildAt(6);

            userPhotoKeyTextView = (TextView) layout.getChildAt(7);
            userPhotoValueTextView = (TextView) layout.getChildAt(8);

            userPostKeyTextView = (TextView) layout.getChildAt(9);
            userPostValueTextView = (TextView) layout.getChildAt(10);

            userPrivacyKeyTextView = (TextView) layout.getChildAt(11);
            userPrivacyValueTextView = (TextView) layout.getChildAt(12);

            usersFollowersKeyTextView = (TextView) layout.getChildAt(13);
            usersFollowersValueTextView = (TextView) layout.getChildAt(14);

            usersFollowingKeyTextView = (TextView) layout.getChildAt(15);
            usersFollowingValueTextView = (TextView) layout.getChildAt(16);
        }

        resultNumberTextView.setText(String.format("#%d", + position+1));
        userIdKeyTextView.setText("userId");
        userIdValueTextView.setText(result.getUserId());
        userAgeKeyTextView.setText("User_Age");
        userAgeValueTextView.setText("" + result.getUserAge().longValue());
        userGenderKeyTextView.setText("User_Gender");
        userGenderValueTextView.setText(result.getUserGender());
        userPhotoKeyTextView.setText("User_Photo");
        userPhotoValueTextView.setText(result.getUserPhoto());
        userPostKeyTextView.setText("User_Post");
        //userPostValueTextView.setText(result.getUserPost());
        userPrivacyKeyTextView.setText("User_Privacy");
        userPrivacyValueTextView.setText(bytesToHexString(result.getUserPrivacy()));
        usersFollowersKeyTextView.setText("Users_Followers");
        usersFollowersValueTextView.setText(result.getUsersFollowers().toString());
        usersFollowingKeyTextView.setText("Users_Following");
        //usersFollowingValueTextView.setText(result.getUsersFollowing().toString());
        return layout;
    }
}
