package com.example.handiwalk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class ChooseFrag extends Fragment implements LocationObjectAdapter.OnListItemClickListener {
    RecyclerView mTestList;

    LocationObjectAdapter mListAdapter;
    ChooseViewModel viewModel;
    View view;

    ListenerChooseMap fragmentInterfacer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.choose_lay, container, false);

        viewModel = new ViewModelProvider(this).get(ChooseViewModel.class);

        mTestList = view.findViewById(R.id.rv);
        mTestList.hasFixedSize();
        mTestList.setLayoutManager(new LinearLayoutManager(getContext()));


        mListAdapter = new LocationObjectAdapter(this);
        viewModel.init().observe(getViewLifecycleOwner(), listObjects -> mListAdapter.update(listObjects));
        mTestList.setAdapter(mListAdapter);


        return view;
    }

    @Override
    public void onListItemClick(LocationObject clickedItemIndex) {
        Navigation.findNavController(view).navigate(R.id.MapFrag);
        fragmentInterfacer.setCoordinates(clickedItemIndex.getCoordinates());
        Toast.makeText(getContext(), "Location: " + clickedItemIndex.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInterfacer = (ListenerChooseMap) context;
    }
}
