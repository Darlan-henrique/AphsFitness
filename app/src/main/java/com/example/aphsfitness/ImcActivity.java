package com.example.aphsfitness;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ImcActivity extends AppCompatActivity {

    private EditText editHeight;
    private EditText editWeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        editHeight = findViewById(R.id.edit_imc_height);
        editWeight = findViewById(R.id.edit_imc_weight);

        Button btnSend = findViewById(R.id.btn_imc_send);

        btnSend.setOnClickListener(view -> {
            if (!validate()) {
                Toast.makeText(ImcActivity.this, R.string.fields_messages, Toast.LENGTH_LONG).show();
                return;
            }

            String sHeight = editHeight.getText().toString();
            String sWeight = editWeight.getText().toString();

            int height = Integer.parseInt(sHeight);
            int weight = Integer.parseInt(sWeight);

            double result = calculateItem(height, weight);
            Log.d("Teste: ", "Resultado: " + result);

            int imcResponseid = imcResponse(result);

            // Criar caixa de dialog
            AlertDialog dialog = new AlertDialog.Builder(ImcActivity.this)
                    .setTitle(getString(R.string.imc_response, result))
                    .setMessage(imcResponseid)
                    .setNegativeButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton(R.string.save, ((dialog1, which) -> {
                        SqlHelper sqlHelper = SqlHelper.getINSTANCE(ImcActivity.this);

                        // Thread criada para evitar travemento na tela
                        new Thread(() -> {
                            int updateId = 0;

                            // verifica se tem ID vindo da tela anterior quando é UPDATE
                            if (getIntent().getExtras() != null)
                                updateId = getIntent().getExtras().getInt("updateId", 0);

                            long calcId;
                                // verifica se é update ou create
                                if(updateId > 0) {
                                    calcId = SqlHelper.getINSTANCE(ImcActivity.this).updateItem("imc",result,updateId);
                                } else {
                                    calcId = SqlHelper.getINSTANCE(ImcActivity.this).addItem("imc",result);
                                }
                            // Thread Principal
                            runOnUiThread(() -> {
                                if (calcId > 0) {
                                    Toast.makeText(ImcActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
                                    openListCalcActivity();
                                }
                            });

                        }).start();
                    }))
                    .create();

            dialog.show();

            // esconder o teclado
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editHeight.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
        });
    }

    // Inflar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    // Evento de click menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_list:
                openListCalcActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // funcao do intent para vincular telas
    public void openListCalcActivity() {
        Intent intent = new Intent(ImcActivity.this, ListCalcActivity.class);
        intent.putExtra("type","imc");
        startActivity(intent);
    }

    @StringRes
    private int imcResponse(double imc) {
        if (imc < 15) {
            return R.string.imc_severely_low_weight;
        } else if (imc < 16) {
            return R.string.imc_very_low_weight;
        } else if (imc < 18.5) {
            return R.string.imc_low_weight;
        } else if (imc < 25) {
            return R.string.normal;
        } else if (imc < 30) {
            return R.string.imc_high_weight;
        } else if (imc < 35) {
            return R.string.imc_so_high_weight;
        } else if (imc < 40) {
            return R.string.imc_severely_high_weight;
        } else {
            return R.string.imc_extreme_weight;
        }
    }

    // peso / altura * altura
    private double calculateItem(int height, int weight) {
        return weight / (((double) height / 100) * ((double) height / 100));
    }

    private boolean validate() {
        return (!editHeight.getText().toString().startsWith("0")
                && !editWeight.getText().toString().startsWith("0")
                && !editHeight.getText().toString().isEmpty()
                && !editWeight.getText().toString().isEmpty()
        );
    }
}