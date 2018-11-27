package org.giwi.networkgraph;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private InputFragment inputFragment;
    private GraphFragment graphFragment;
    private AnswerFragment answerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout mMainFrame = findViewById(R.id.main_frame);
        BottomNavigationView mMainNav = findViewById(R.id.navigation);

        homeFragment = new HomeFragment();
        inputFragment = new InputFragment();
        graphFragment = new GraphFragment();
        answerFragment = new AnswerFragment();

        setFragment(homeFragment);


        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.navigation_home){
                    if(HomeFragment.socket==null){
                        setFragment(homeFragment);
                        return true;
                    }
                    else{
                        setFragment(graphFragment);
                        return true;
                    }
                }
                else if(menuItem.getItemId() == R.id.navigation_input && HomeFragment.socket!=null){
                    setFragment(inputFragment);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (HomeFragment.socket != null) {
            try {
                HomeFragment.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}