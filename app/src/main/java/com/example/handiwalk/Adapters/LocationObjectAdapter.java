package com.example.handiwalk.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handiwalk.Models.LocationModel;
import com.example.handiwalk.R;

import java.util.ArrayList;
import java.util.List;

public class LocationObjectAdapter extends RecyclerView.Adapter<LocationObjectAdapter.ViewHolder> {
    final private LocationObjectAdapter.OnListItemClickListener clickListener;
    private List<LocationModel> objects;

    public LocationObjectAdapter(LocationObjectAdapter.OnListItemClickListener listener) {
        objects = new ArrayList<>();
        clickListener = listener;
    }

    public void update(List<LocationModel> list) {
        System.out.println("Update call");
        if (list != null) {
            objects = list;
            notifyDataSetChanged();
        }
    }


    @NonNull
    public  LocationObjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.list_layout, parent, false);
        return new  LocationObjectAdapter.ViewHolder(view);
    }

    public void onBindViewHolder( LocationObjectAdapter.ViewHolder viewHolder, int position) {
        viewHolder.name.setText(objects.get(position).getName());
        viewHolder.quote.setText(objects.get(position).getDescription());
    }


    public interface OnListItemClickListener {
        void onListItemClick(LocationModel clickedItemIndex);
        void onRateClick(LocationModel clickedItemIndex);
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
            rateButton = itemView.findViewById(R.id.rateButton);
            button.setOnClickListener(this);
            rateButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println(view.getId());
            System.out.println(rateButton.getId());
            System.out.println(button.getId());
            if(view.getId() == rateButton.getId()) {
                clickListener.onRateClick(objects.get(getBindingAdapterPosition()));
            } else {
                clickListener.onListItemClick(objects.get(getBindingAdapterPosition()));
            }
        }

    }
}
