package com.example.aphsfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMain  = findViewById(R.id.main_rv);

        List<MainItem> mainItems = new ArrayList<>();
        mainItems.add(new MainItem(1,R.drawable.imc,R.string.label_imc, Color.YELLOW));
        mainItems.add(new MainItem(2,R.drawable.tmb,R.string.label_tmb,Color.BLUE));

        // 1 -> Definir o comportamento de exibicao do layout da recyclerview
        // mosaic
        // grid
        // linear (horizontal | vertical )
        rvMain.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
        MainAdapter adapter = new MainAdapter(mainItems);
        adapter.setListener(id -> {
            switch (id){
                case 1:
                    startActivity(new Intent(MainActivity.this, ImcActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(MainActivity.this, TmbActivity.class));
                    break;
            }
        });
        rvMain.setAdapter(adapter);

    }

    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHold> {

        private List<MainItem> mainItems;
        private OnItemClickListener listener;

        public MainAdapter(List<MainItem> mainItems){
            this.mainItems = mainItems;
        }

        public void setListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        // Espera como retorna a nosso metodo MainViewHold o retorno precisar fazer um inflate para layout main_item
        public MainViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainViewHold(getLayoutInflater().inflate(R.layout.main_item, parent,false));
        }

        @Override
        // Garante que nossa Viewhold so execute uma vez somente trocando o conteudo.
        public void onBindViewHolder(@NonNull MainViewHold holder, int position) {
            MainItem mainItemCurrent = mainItems.get(position);
            holder.bind(mainItemCurrent);
        }

        // Quantidade de item da recycleview
        @Override
        public int getItemCount() {
            return mainItems.size();
        }

        // Entenda como sendo a VIEW DA CELULA que esta denro do Recyclerview
        private class MainViewHold extends RecyclerView.ViewHolder {

            public MainViewHold(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(MainItem item) {
                TextView textName = itemView.findViewById(R.id.item_txt_name);
                ImageView imgIcon = itemView.findViewById(R.id.item_img_icon);
                LinearLayout btnImc = (LinearLayout) itemView.findViewById(R.id.btn_imc);

                // evento de botao
                btnImc.setOnClickListener(view -> {
                    listener.onClick(item.getId());
                });
                textName.setText(item.getTextStringId());
                imgIcon.setImageResource(item.getDrawabledId());
                btnImc.setBackgroundColor(item.getColor());
            }
        }
    }
}