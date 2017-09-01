package com.antoniorosario.guidedmeditation.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.antoniorosario.guidedmeditation.MeditationManager;
import com.antoniorosario.guidedmeditation.model.Meditation;

public class MeditationViewModel extends BaseObservable {
    private Meditation meditation;
    private MeditationManager meditationManager;

    public MeditationViewModel(MeditationManager meditationManager) {
        this.meditationManager = meditationManager;
    }

    @Bindable
    public String getTitle() {
        return meditation.getTitle();
    }

    @Bindable
    public String getAuthor() {
        return meditation.getAuthor();
    }


    public void setMeditation(Meditation meditation) {
        this.meditation = meditation;
        notifyChange();
    }

    public void onMeditationClicked() {
        meditationManager.release();
        meditationManager.play(meditation);
    }
}
