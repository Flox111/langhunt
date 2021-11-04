package ru.maltsev.langhunt;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LoginAdapter extends FragmentStateAdapter{
    private final int totalTabs = 2;
    private String[] tabTitles = new String[]{"Login", "SignUp"};

    public LoginAdapter(@NonNull FragmentManager fragmentActivity, @NonNull Lifecycle lifecycle){
        super(fragmentActivity, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1:
                SignupTabFragment signupTabFragment = new SignupTabFragment();
                return signupTabFragment;
            default:
                return new LoginTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}