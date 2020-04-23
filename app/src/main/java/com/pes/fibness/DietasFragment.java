package com.pes.fibness;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DietasFragment extends Fragment {

    private ListView listViewT;
    private ArrayList<String> dietList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dietas, container, false);

        /**Substituir por conexion con la BD**/
        dietList = User.getInstance().getDietList();
        /**End**/

        listViewT = (ListView)view.findViewById(R.id.listViewDiet);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row, dietList);

        refreshList();

        listViewT.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dietPage = new Intent(getActivity(), CreateDietActivity.class);
                dietPage.putExtra("new", false);
                dietPage.putExtra("title", dietList.get(position));
                startActivity(dietPage);
            }
        } );

        listViewT.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showEditBox(position);
                return true;
            }
        } );

        Button button = (Button) view.findViewById(R.id.FakeFloatingButton2);

        button.setOnClickListener( new AdapterView.OnClickListener() {
            public void onClick(View v){
                showInputBox();
            }
        });

        return view;
    }

    private void refreshList() {
        listViewT.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.row, dietList));
    }

    private void showEditBox(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.input_edit_diet);
        builder.setTitle("Edit " + dietList.get(position));
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText editNameText = (EditText) dialog.findViewById(R.id.editTitleDietInput);
        editNameText.setText(dietList.get(position));
        final EditText editDescText = (EditText) dialog.findViewById(R.id.editDescDietInput);

        /**Substituir por conexion con la BD**/
        editDescText.setText(User.getInstance().getDietDesc(dietList.get(position)));
        /**End**/

        Button btDelete = (Button) dialog.findViewById(R.id.btdeleteEditDiet);
        Button btDone = (Button) dialog.findViewById(R.id.btdoneEditDiet);
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNameText.getText().toString().trim().length() == 0) {
                    editNameText.setError("Please, add a name");
                } else {
                    /**Substituir por conexion con la BD**/
                    User.getInstance().setDietName(dietList.get(position), editNameText.getText().toString());
                    User.getInstance().setDietDesc(editNameText.getText().toString(), editDescText.getText().toString());
                    /**End**/

                    dietList.set(position, editNameText.getText().toString());
                    refreshList();
                    dialog.dismiss();
                }
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**Substituir por conexion con la BD**/
                User.getInstance().deleteDiet(dietList.get(position));
                /**End**/

                dietList.remove(position);
                refreshList();
                dialog.dismiss();
            }
        });

    }


    public void showInputBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.input_new_diet);
        builder.setTitle("Diet");
        final AlertDialog dialog = builder.create();
        dialog.show();
        TextView txt = (TextView) dialog.findViewById(R.id.TitleNewDiet);
        txt.setText("Add a name");
        final EditText editNameText = (EditText) dialog.findViewById(R.id.TitleDietInput);
        final EditText editDescText = (EditText) dialog.findViewById(R.id.DescNewDiet);
        Button bt = (Button) dialog.findViewById(R.id.btdoneDiet);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNameText.getText().toString().trim().length() == 0) {
                    editNameText.setError("Please, add a name");
                } else {
                    String titleDiet = editNameText.getText().toString();
                    String desc = editDescText.getText().toString();

                    /**Substituir por conexion con la BD**/
                    Diet d = new Diet();
                    d.name = titleDiet;
                    d.desc = desc;
                    d.id = -1;
                    User.getInstance().addDiet(d);
                    /**End**/

                    dietList.add(titleDiet);
                    refreshList();
                    dialog.dismiss();
                    Intent dietPage = new Intent(getActivity(), CreateDietActivity.class);
                    dietPage.putExtra("new", true);
                    dietPage.putExtra("title", titleDiet);
                    startActivity(dietPage);
                }
            }
        });
    }
}
