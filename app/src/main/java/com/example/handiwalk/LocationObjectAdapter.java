package com.example.handiwalk;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LocationObjectAdapter extends RecyclerView.Adapter<LocationObjectAdapter.ViewHolder> {
    final private LocationObjectAdapter.OnListItemClickListener clickListener;
    private List<LocationObject> objects;

    LocationObjectAdapter(LocationObjectAdapter.OnListItemClickListener listener) {
        objects = new ArrayList<>();
        clickListener = listener;
    }

    public void update(List<LocationObject> list) {
        System.out.println("Update call");
        if (list != null) {
            objects = list;
            notifyDataSetChanged();
        }
    }


    public  LocationObjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.list_layout, parent, false);
        return new  LocationObjectAdapter.ViewHolder(view);
    }

    public void onBindViewHolder( LocationObjectAdapter.ViewHolder viewHolder, int position) {
        viewHolder.name.setText(objects.get(position).getName());
        viewHolder.character.setText("LOLO");
        viewHolder.quote.setText(objects.get(position).getDescription());
    }


    public interface OnListItemClickListener {
        void onListItemClick(LocationObject clickedItemIndex);
       // void onRateClick(LocationObject clickedItemIndex);
    }


    public int getItemCount() {
        return objects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView character;
        TextView quote;
        Button button;
        Button rateButton;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.locationName);
            character = itemView.findViewById(R.id.geoPoint);
            quote = itemView.findViewById(R.id.desciption);
            button=itemView.findViewById(R.id.showOnMap);
            rateButton=itemView.findViewById(R.id.rateButton);
            rateButton.setOnClickListener(this);
            button.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rateButton:
                    // TODO add function to rate button

                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.review_window, null);

                    boolean focusable = true;
                    final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getWidth(), popupView.getHeight(), focusable);
                    popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });

                    break;
                default:
                    System.out.println("User clicked on unregistered button");
                    break;
            }
        }

    }
}
