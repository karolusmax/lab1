package com.example.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity implements Fragment1.OnButtonClickListener{

    // private FragmentManager fragmentManager;
    //  private Fragment fragment1, fragment2, fragment3, fragment4;
    private int[] frames;
    private boolean hiden;
    private int[] sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            frames = new int[]{R.id.frame1, R.id.frame2, R.id.frame3, R.id.frame4};
            hiden = false;
            sequence = new int[]{0, 1, 2, 3};
            Fragment[] fragments = new Fragment[]{new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4()};
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (int i = 0; i < 4; i++) {
                transaction.add(frames[i], fragments[i]);
            }
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            frames = savedInstanceState.getIntArray("FRAMES");
            hiden = savedInstanceState.getBoolean("HIDEN");
            sequence = savedInstanceState.getIntArray("SEQUENCE");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("FRAMES", frames);
        outState.putBoolean("HIDEN", hiden);
        outState.putIntArray("SEQUENCE", sequence);
    }

    private void newFragments() {
        Fragment[] newFragments = new Fragment[]{new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4()};
        Fragment[] inSequence = new Fragment[]{newFragments[sequence[0]], newFragments[sequence[1]], newFragments[sequence[2]], newFragments[sequence[3]]};
        newFragments = inSequence;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < 4; i++) {
            transaction.replace(frames[i], newFragments[i]);
            if (hiden && !(newFragments[i] instanceof Fragment1))
                transaction.hide(newFragments[i]);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onButtonClickHide() {
        if(hiden)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (Fragment f : fragmentManager.getFragments()){
            if (f instanceof Fragment1)
                continue;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(f);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        hiden = true;
    }

    public void onButtonClickRestore() {
        if(!hiden)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (Fragment f : fragmentManager.getFragments()){
            if (f instanceof Fragment1)
                continue;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(f);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        hiden = true;
    }

    public void onButtonClickClockwise() {
        int t = frames[0];
        frames[0] = frames[1];
        frames[1] = frames[2];
        frames[2] = frames[3];
        frames[3] = t;
        newFragments();
    }

    public void onButtonClickShuffle() {
        List<Integer> s = new ArrayList<>(Arrays.asList(sequence[0],sequence[1],sequence[2],sequence[3]));
        Collections.shuffle(s);
        for(int i = 0; i < 4; i++)
            sequence[i]= s.get(i);
        newFragments();
    }
    public void onAttachFragment(@NonNull Fragment fragment){
        super.onAttachFragment(fragment);
        if(fragment instanceof Fragment1){
            ((Fragment1)fragment).setOnButtonClickListener(this);
        }
    }
}