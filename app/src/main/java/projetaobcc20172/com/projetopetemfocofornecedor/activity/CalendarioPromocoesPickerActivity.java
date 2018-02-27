package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.PromocaoDao;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.PromocaoDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Promocao;

public class CalendarioPromocoesPickerActivity extends AppCompatActivity {

    private List<Date> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_promocoes);

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_main);

        toolbar.setTitle("Selecione os dias");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        Calendar current = Calendar.getInstance();

        Calendar currentMinus = DateUtils.getCalendar();
        currentMinus.set(Calendar.MONTH, current.get(Calendar.MONTH)-1);
        currentMinus.set(Calendar.YEAR, current.get(Calendar.YEAR));
        currentMinus.set(Calendar.DATE, currentMinus.getActualMaximum(Calendar.DAY_OF_MONTH)+1);

        Button getDateButton = (Button) findViewById(R.id.getDateButton);

        Promocao promocao = (Promocao) getIntent().getSerializableExtra("promocao");

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        try {
            calendarView.setMinimumDate(currentMinus);
            calendarView.setDisabledDays(getDisabledDays(current));
            calendarView.setDate(current);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        mDatas = new ArrayList<>();

        getDateButton.setOnClickListener(v -> {

            for(Calendar calendar: calendarView.getSelectedDates()){
                mDatas.add(calendar.getTime());
            }

            promocao.setDatas(mDatas);
            PromocaoDao promocaoDao = new PromocaoDaoImpl(this);

            if(getIntent().getBooleanExtra("edicao",false)){
                promocaoDao.atualizar(promocao,promocao.getFornecedorId());
            }else{
                promocaoDao.inserir(promocao,promocao.getFornecedorId());
            }

            abrirTelaPrincipal();
        });
    }

    private List<Calendar> getDisabledDays(Calendar currentDate){
        Calendar inicio = DateUtils.getCalendar();
        inicio.set(Calendar.YEAR,currentDate.get(Calendar.YEAR));
        inicio.set(Calendar.MONTH,currentDate.get(Calendar.MONTH));
        inicio.set(Calendar.DATE,0);

        return DateUtils.getDatesRange(inicio,currentDate);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(CalendarioPromocoesPickerActivity.this, PromocaoActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
