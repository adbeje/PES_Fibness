package com.pes.fibness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchUsersActivity extends AppCompatActivity implements UsersAdapter.SelectedUser{

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView followers, following;

    private List<UserModel> userModelList = new ArrayList<>();
    private ArrayList<Pair<Integer, String>> names = new ArrayList<>();
    private UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        toolbar = findViewById(R.id.toolbar);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        recyclerView = findViewById(R.id.recyclerview);

        /*falta añadir
        ArrayList<Pair<Integer,String>> users = User.getInstance().getShortUsersInfo();
        names = users;
        */
        for(int i=0; i< 10; ++i){
            names.add(i,new Pair<Integer, String>(i, "HOLA"));
        }

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));



        for(int i=0; i < names.size(); ++i){
            UserModel userModel = new UserModel(names.get(i).first, names.get(i).second);
            userModelList.add(userModel);
        }

        usersAdapter = new UsersAdapter(userModelList, this);
        recyclerView.setAdapter(usersAdapter);



    }

    @Override
    public void selectedUser(UserModel userModel) {

        /**hay que cargar los datos del usuario seleccionado*/
        startActivity(new Intent(SearchUsersActivity.this, SelectedUserActivity.class).putExtra("data", userModel));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_view);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usersAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.search_view)
            return  true;

        return super.onOptionsItemSelected(item);
    }
}
