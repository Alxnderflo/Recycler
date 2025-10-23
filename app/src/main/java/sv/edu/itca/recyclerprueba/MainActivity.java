package sv.edu.itca.recyclerprueba;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import sv.edu.itca.recyclerprueba.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        configViewPager();
        configBottomNavigation();
    }

    private void configViewPager() {
        viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);

        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new ScoreFragment());
        adapter.addFragment(new TeacherFragment());

        viewPager.setAdapter(adapter);
    }

    private void configBottomNavigation() {
        binding.btnNavView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Home) {
                viewPager.setCurrentItem(0, true);
            } else if (item.getItemId() == R.id.Score) {
                viewPager.setCurrentItem(1, true);
            } else if (item.getItemId() == R.id.Teacher) {
                viewPager.setCurrentItem(2, true);
            }
            return true;
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.btnNavView.setSelectedItemId(R.id.Home);
                        break;
                    case 1:
                        binding.btnNavView.setSelectedItemId(R.id.Score);
                        break;
                    case 2:
                        binding.btnNavView.setSelectedItemId(R.id.Teacher);
                        break;
                }
            }
        });
    }
}