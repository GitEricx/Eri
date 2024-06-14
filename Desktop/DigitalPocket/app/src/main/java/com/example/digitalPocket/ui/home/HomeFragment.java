package com.example.digitalPocket.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.digitalPocket.ui.Dialog.ChangeDialog;
import com.example.digitalPocket.ui.login.LoggedUser;
import com.example.digitalPocket.util.NimiqHashAvatarGenerator;
import com.example.digitalPocket.R;
import com.example.digitalPocket.data.model.User;
import com.example.digitalPocket.dao.UserDao;
import com.example.digitalPocket.ui.SwapActivity;
import com.example.digitalPocket.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ImageView manageView;
    private ImageView searchview;
    private DrawerLayout drawerLayout;
    private NavigationView manageDrawer1;
    private NavigationView manageDrawer2;
    private ListView searchListView;
    public static Map<String, BigDecimal> userBalanceMap;
    private TextView myusername;
    private TextView myaddress;
    private TextView myemail;
    private TextView mypubKey;
    private ImageView changePwd;
    private ImageView changeUsername;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        initializeUIComponents(root);

        // Observe ViewModel data
        observeViewModelData(root);

        // Set click listeners for manage and search views
        setManageViewClickListener(root);

        setSearchViewClickListener(root);

        // Initialize the balance list view
        ListView balanceListView = root.findViewById(R.id.balanceListView);
        setToExchange(balanceListView);
        GetNimiqHashAvatar(root);
        return root;
    }

    private void GetNimiqHashAvatar(View root) {
        if(LoggedUser.loggedUserAddress==null){
            Toast.makeText(getActivity(), "Can't get Avatar", Toast.LENGTH_SHORT).show();
        }else{
            NimiqHashAvatarGenerator avatarGenerator = new NimiqHashAvatarGenerator();
            Bitmap avatar = avatarGenerator.generateAvatar(LoggedUser.loggedUserAddress, 52);
            ImageView userAvatar=root.findViewById(R.id.userIcon);
            userAvatar.setImageBitmap(avatar);
        }
    }

    private void setToExchange(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map.Entry<String, BigDecimal> clickedEntry = (Map.Entry<String, BigDecimal>) adapterView.getItemAtPosition(i);
                String cryptoName = clickedEntry.getKey();
                BigDecimal balance = clickedEntry.getValue();
                Intent intent = new Intent(getActivity(),SwapActivity.class);
                intent.putExtra("cryptoName", cryptoName);
                intent.putExtra("balance", balance.toString());
                startActivity(intent);
            }
        });
    }

    private void initializeUIComponents(View root) {
        drawerLayout = root.findViewById(R.id.drawer_layout);
        manageView = root.findViewById(R.id.mangeview); // Corrected the typo from mangeview to manageView
        searchview=root.findViewById(R.id.searchview);
        manageDrawer1 = root.findViewById(R.id.mange_draw1); // Corrected the typo from mange_draw1 to manage_draw1
        manageDrawer2 = root.findViewById(R.id.mange_draw2); // Corrected the typo from mange_draw2 to manage_draw2
    }

    private void observeViewModelData(View root) {
        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        homeViewModel.getUserBalanceLiveData().observe(getViewLifecycleOwner(), userBalance -> {
            if (userBalance != null) {
                ListView balanceListView = root.findViewById(R.id.balanceListView);
                homeViewModel.adapterSet(balanceListView, R.layout.balance_item, userBalance,false);
                userBalanceMap =userBalance;
//                UserDao.LoggedUserBalance=userBalance;
            }
        });
        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
    }

    private void setManageViewClickListener(View root) {
        manageView.setOnClickListener(v -> {
            // Clear header and menu of the first drawer
            if (manageDrawer1.getHeaderView(0) != null) {
                manageDrawer1.removeHeaderView(manageDrawer1.getHeaderView(0));
            }
            manageDrawer1.getMenu().clear();

            // Open the first drawer and set up its header and menu
            drawerLayout.openDrawer(GravityCompat.START);
            manageDrawer1.inflateHeaderView(R.layout.mange_header);
            manageDrawer1.inflateMenu(R.menu.mange_main);

            // Set the back button click listener for the first drawer
            root.findViewById(R.id.backicon).setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.START));

            // Set up menu item click listeners
            setupManageDrawer1MenuClickListeners(root);
        });
    }

    private void setupManageDrawer1MenuClickListeners(View root) {
        Menu menu = manageDrawer1.getMenu();

        MenuItem purseItem = menu.findItem(R.id.nav_purse);
        purseItem.setOnMenuItemClickListener(item -> {
            // Clear header and menu of the second drawer
            if (manageDrawer2.getHeaderView(0) != null) {
                manageDrawer2.removeHeaderView(manageDrawer2.getHeaderView(0));
            }
            manageDrawer2.getMenu().clear();
            // Open the second drawer and set up its header
            drawerLayout.openDrawer(GravityCompat.END);
            manageDrawer2.inflateHeaderView(R.layout.mange_header2);
            String username=LoggedUser.getLoggedUser().getUsername();
            String email=LoggedUser.getLoggedUserEmail();
            String address=LoggedUser.getLoggedUserAddress();
            String pubKey=LoggedUser.getLoggedUser().getPubKey();
            myusername=root.findViewById(R.id.myusernameView);
            myemail=root.findViewById(R.id.myemailView);
            myaddress=root.findViewById(R.id.myaddressView);
            mypubKey=root.findViewById(R.id.mypubkeyView);
            changePwd=root.findViewById(R.id.changePasswdView);
            changeUsername=root.findViewById(R.id.changUserNameView);
            myusername.setText(username);
            myemail.setText(email);
            myaddress.setText(address);
            mypubKey.setText(pubKey);
            root.findViewById(R.id.backicon2).setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.END));
            changePwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChangeDialog.showChangeDialog(getContext(), email, "passwd", newUsername -> {
                    });
                }
            });
            changeUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChangeDialog.showChangeDialog(getContext(), email, "username", newUsername -> {
                        myusername.setText(newUsername);
                    });
                }
            });
            return false;
        });

        MenuItem quitItem = menu.findItem(R.id.nav_quit);
        quitItem.setOnMenuItemClickListener(menuItem -> {
            // Clear shared preferences and navigate to the login activity
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("DigitalPursePreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String lastLoggedUser = sharedPreferences.getString("username", null);
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("lastLoggedUser", lastLoggedUser);
            startActivity(intent);
            requireActivity().finish();
            return false;
        });
    }

    private void setSearchViewClickListener(View root) {
        searchview.setOnClickListener(v -> {
            // Clear header and menu of the first drawer
            if (manageDrawer1.getHeaderView(0) != null) {
                manageDrawer1.removeHeaderView(manageDrawer1.getHeaderView(0));
            }
            manageDrawer1.getMenu().clear();
            // Open the first drawer and set up its header
            drawerLayout.openDrawer(GravityCompat.START);
            manageDrawer1.inflateHeaderView(R.layout.seachr_header);
            // Set up the search functionality
            setupSearchFunctionality(root);
        });
    }

    private void setupSearchFunctionality(View root) {
        searchListView = root.findViewById(R.id.search_list);
        homeViewModel.adapterSet(searchListView, R.layout.balance_item, userBalanceMap, true);
        SearchView innerSearchView = root.findViewById(R.id.searchView);
        innerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                homeViewModel.filter(s, searchListView);
                return false;
            }
        });
        setToExchange(searchListView);

        // Set the cancel button click listener
        root.findViewById(R.id.cancelView).setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.START));
    }
}
