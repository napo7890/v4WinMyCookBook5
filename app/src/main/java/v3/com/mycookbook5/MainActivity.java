package v3.com.mycookbook5;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import v3.com.mycookbook5.fragment.ExploreFragment;
import v3.com.mycookbook5.fragment.FavoritesFragment;
import v3.com.mycookbook5.fragment.RecipeFragment;

public class MainActivity extends BaseActivity {

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Initialize Database */
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("recipes");

        final String userId = getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//                if (!snapshot.exists()) {
//                    Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
//                    startActivity(intent);
//                    MainActivity.this.finish();
//                    return;
//
//                } else {
                    //setContentView(R.layout.activity_main);

                    setupToolbar();
                    setupActionbar();
                    setupDrawer();

                    /** Create the adapter that will return a fragment for each section */
                    mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                        private final Fragment[] mFragments = new Fragment[]{
                                new RecipeFragment(),
                                new FavoritesFragment(),
                                new ExploreFragment(),
                        };
                        private final String[] mFragmentNames = new String[]{
                                getString(R.string.heading_my_recipes),
                                getString(R.string.heading_favorites),
                                getString(R.string.heading_explore),
                        };

                        @Override
                        public int getCount() {
                            return mFragmentNames.length;
                        }

                        @Override
                        public Fragment getItem(int position) {
                            return mFragments[position];
                        }

                        @Override
                        public CharSequence getPageTitle(int position) {
                            return mFragmentNames[position];
                        }
                    };

                    /** Set up the ViewPager with the sections adapter */
                    mViewPager = (ViewPager) findViewById(R.id.viewpager);
                    mViewPager.setAdapter(mPagerAdapter);
                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(mViewPager);

                    /** Button launches CreateRecipeActivity */
                    findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity.this, CreateRecipeActivity.class));
                        }
                    });
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }
}
