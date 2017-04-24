package com.styln;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shanu on 4/24/17.
 */

public class NotificationListFragment extends ListFragment {
    private ArrayList<Notifications> notifications;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Notifications");
        notifications = new ArrayList<Notifications>();
        notifications = NotificationBanner.get(getActivity()).getNotificationList();
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        setListAdapter(adapter);
    }

    private class NotificationAdapter extends ArrayAdapter<Notifications> {
        public NotificationAdapter(ArrayList<Notifications> notifications) {
            super(getActivity(), 0, notifications);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (notifications == null && convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.no_notf_layout,null);
            }
            else {
                if (convertView == null)
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.list_fragment_notify, null);
                Notifications c = getItem(position);
                TextView title_text = (TextView) convertView.findViewById(R.id.notify_title);
                title_text.setText(c.getTitle());

//            TextView date_text = (TextView) convertView.findViewById(R.id.notify_date);
//            date_text.setText(c.getDate().toString());

                return convertView;
            }
            return convertView;
        }
    }

    public void onResume() {
        super.onResume();
        ((NotificationAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
