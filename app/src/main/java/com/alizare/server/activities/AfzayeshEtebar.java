package com.alizare.server.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alizare.server.R;

public class AfzayeshEtebar extends AppCompatActivity {

    TextView txtEtebar , txtMeghdarEtebar , txtEtebarnaahaii;
    Button btnPay , btnOnline;
    ToggleButton btn10 , btn20 , btn30, btn50, btn100, btn200 ;
    EditText edtMablagh;
    int s10 , s20 , s30 , s50 , s100 , s200 = 0;
    private int sum =0;
            int etebarfelii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afzayesh_etebar);

        txtEtebar = (TextView) findViewById(R.id.txt_etebar);
        txtMeghdarEtebar = (TextView) findViewById(R.id.txt_etebar_taghiir);
        txtEtebarnaahaii = (TextView) findViewById(R.id.txt_etebar_nahaii);

        btn10 = (ToggleButton) findViewById(R.id.btn_10);
        btn20 = (ToggleButton) findViewById(R.id.btn_20);
        btn30 = (ToggleButton) findViewById(R.id.btn_30);
        btn50 = (ToggleButton) findViewById(R.id.btn_50);
        btn100 = (ToggleButton) findViewById(R.id.btn_100);
        btn200 = (ToggleButton) findViewById(R.id.btn_200);
        btnPay = (Button) findViewById(R.id.btn_pay);
        btnOnline = (Button) findViewById(R.id.btn_online);

        edtMablagh = (EditText) findViewById(R.id.edt_mablagh);

        SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);
        etebarfelii= Integer.parseInt(prefs.getString("credit", "0"));
        txtEtebar.setText("اعتبار فعلی "+etebarfelii+" ریال");

        txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
        txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfzayeshEtebar.this , InsertPayInfo.class);
                startActivity(intent);

            }
        });


        btn10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s10= 100000;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");

                } else {
                    s10= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                }
            }
        });
        btn20.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s20= 200000;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");



                } else {
                    s20= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                }
            }
        });
        btn30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s30= 300000;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                } else {
                    s30= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                }
            }
        });
        btn50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s50= 500000;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                } else {
                    s50= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                }
            }
        });
        btn100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s100= 1000000;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                } else {
                    s100= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                }
            }
        });
        btn200.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s200= 2000000;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                } else {
                    s200= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                }
            }
        });


        TextWatcher inputTextWatcherMatn = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    btn10.setEnabled(false);
                    btn10.setChecked(false);

                    btn20.setEnabled(false);
                    btn20.setChecked(false);

                    btn30.setEnabled(false);
                    btn30.setChecked(false);

                    btn50.setEnabled(false);
                    btn50.setChecked(false);

                    btn100.setEnabled(false);
                    btn100.setChecked(false);

                    btn200.setEnabled(false);
                    btn200.setChecked(false);

                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + Integer.parseInt(s.toString()) + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (Integer.parseInt(s.toString())+etebarfelii) + " ریال");


                }
                if(s.length()==0){
                    btn10.setEnabled(true);
                    btn10.setChecked(false);

                    btn20.setEnabled(true);
                    btn20.setChecked(false);

                    btn30.setEnabled(true);
                    btn30.setChecked(false);

                    btn50.setEnabled(true);
                    btn50.setChecked(false);

                    btn100.setEnabled(true);
                    btn100.setChecked(false);

                    btn200.setEnabled(true);
                    btn200.setChecked(false);

                    txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + sum + " ریال");
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (sum+etebarfelii) + " ریال");


                }



            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        edtMablagh.addTextChangedListener(inputTextWatcherMatn);






    }

}
