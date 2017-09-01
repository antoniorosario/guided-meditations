package com.antoniorosario.guidedmeditation.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antoniorosario.guidedmeditation.MeditationManager;
import com.antoniorosario.guidedmeditation.R;
import com.antoniorosario.guidedmeditation.databinding.FragmentMeditationManagerBinding;
import com.antoniorosario.guidedmeditation.databinding.ListItemMeditationBinding;
import com.antoniorosario.guidedmeditation.model.Meditation;
import com.antoniorosario.guidedmeditation.viewmodel.MeditationViewModel;

import java.util.List;

public class MeditationListFragment extends Fragment {

    private MeditationManager meditationManager;

    public static MeditationListFragment newInstance() {
        return new MeditationListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        meditationManager = new MeditationManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMeditationManagerBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_meditation_manager, container, false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(new MeditationAdapter(meditationManager.getMeditations()));
        binding.recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        meditationManager.release();
    }

    private class MeditationHolder extends RecyclerView.ViewHolder {
        private ListItemMeditationBinding binding;

        private MeditationHolder(ListItemMeditationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setViewModel(new MeditationViewModel(meditationManager));
        }

        public void bind(Meditation meditation) {
            binding.getViewModel().setMeditation(meditation);
            binding.executePendingBindings();
        }
    }

    private class MeditationAdapter extends RecyclerView.Adapter<MeditationHolder> {

        List<Meditation> meditations;

        public MeditationAdapter(List<Meditation> meditations) {
            this.meditations = meditations;
        }

        @Override
        public MeditationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemMeditationBinding binding = DataBindingUtil
                    .inflate(inflater, R.layout.list_item_meditation, parent, false);
            return new MeditationHolder(binding);
        }

        @Override
        public void onBindViewHolder(MeditationHolder holder, int position) {
            Meditation meditation = meditations.get(position);
            holder.bind(meditation);
        }

        @Override
        public int getItemCount() {
            return meditations.size();
        }
    }
}
