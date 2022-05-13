package com.example.handiwalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
  NavController navController;
  NavigationView navigationView;
  DrawerLayout drawerLayout;
  AppBarConfiguration mAppBarConfiguration;
  Toolbar toolbar;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViews();
    setSupportActionBar(toolbar);
    NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);
    mAuth = FirebaseAuth.getInstance();


    if (mAuth.getCurrentUser() == null) {
      startActivity(new Intent(this, LoginActivity.class));
      finish();
    }

  }

  private void findViews() {
    toolbar = findViewById(R.id.topAppBar);
    drawerLayout = findViewById(R.id.drawer_layout);
    navigationView = findViewById(R.id.nav_view);
    navController = Navigation.findNavController(this, R.id.fragmentContainerView);
    mAppBarConfiguration = new AppBarConfiguration.Builder(
        R.id.MapFragment, R.id.OverviewFragment, R.id.FavouriteFragment)
        .setOpenableLayout(drawerLayout).build();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.top_app_bar, menu);
    return true;
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
        || super.onSupportNavigateUp();
  }

  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == findViewById(R.id.LogOutButton).getId()) {
      AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
      });
    }
    if (item.getItemId() == toolbar.getMenu().findItem(R.id.Tutorial).getItemId()) {
      navController.navigate(R.id.TutorialFragment);
    }
    return super.onOptionsItemSelected(item);
  }
}