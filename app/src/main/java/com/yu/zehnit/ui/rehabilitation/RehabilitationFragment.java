package com.yu.zehnit.ui.rehabilitation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yu.zehnit.R;

public class RehabilitationFragment extends Fragment {

    private RehabilitationViewModel rehabilitationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rehabilitationViewModel =
                new ViewModelProvider(this).get(RehabilitationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rehabilitation, container, false);
        final TextView textView = root.findViewById(R.id.text_rehabilitation);
        rehabilitationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


}