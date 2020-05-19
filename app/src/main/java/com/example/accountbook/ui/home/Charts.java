package com.example.accountbook.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accountbook.R;
import com.example.accountbook.model.Percentage;
import com.example.accountbook.service.UserService;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Charts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Charts extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView date, cost, income;
    private View view;
    private LinearLayout linearLayout;
    private ImageButton imageButton;
    private PieChart mPieChart;
    private Boolean isCost = true;
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver mReceiver;
    public static final String ACTION_TAG = "Details-Data-Update";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Charts() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Charts newInstance(String param1, String param2) {
        Charts fragment = new Charts();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_charts, container, false);
        linearLayout = view.findViewById(R.id.linearLayout_date);
        date = view.findViewById(R.id.chart_date);
        cost = view.findViewById(R.id.chart_cost);
        income = view.findViewById(R.id.chart_income);
        imageButton = view.findViewById(R.id.chart_button);

        //register broadcast receiver
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_TAG);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                isCost = true;
                readyToDisplayPieChart();
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);

        final Calendar calendar= Calendar.getInstance();
        date.setText(calendar.get(Calendar.YEAR)+"-"+String.format("%02d",calendar.get(Calendar.MONTH)+1));
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String text = "You have selected：" + year + "-" + String.format("%02d", month + 1);
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT ).show();
                        date.setText(year + "-" + String.format("%02d", month+1));

                        isCost = true;
                        readyToDisplayPieChart();
                    }
                }
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH));
                mDialog.show();
                ((ViewGroup) ((ViewGroup) mDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                //((ViewGroup) ((ViewGroup) mDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            }
        });
        mPieChart = view.findViewById(R.id.pie_chart);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyToDisplayPieChart();
            }
        });
        readyToDisplayPieChart();
        return view;
    }

    private void readyToDisplayPieChart(){
        if (isCost){
            showPieChart(mPieChart, getPieChartData("1", date.getText().toString().trim()));
            isCost = false;
        }else{
            showPieChart(mPieChart, getPieChartData("0", date.getText().toString().trim()));
            isCost = true;
        }
    }

    private List<PieEntry> getPieChartData(String isCost, String date) {
        UserService user = new UserService(getActivity());
        List<Percentage> dataList = user.getPieChartData(isCost, date);
//        List<String> dataList = "数据库或网络获取数据"
        List<PieEntry> mPie = new ArrayList<>();
        Double total=0.0;
        if(dataList == null){
            Toast.makeText(getContext(), "No data in this month", Toast.LENGTH_LONG).show();
            cost.setText("0.0");
            income.setText("0.0");
        }else{
            for (Percentage temp : dataList) {
                PieEntry pieEntry = new PieEntry(temp.getPercentage(), temp.getLabel());
                total += temp.getValue();
                //pieEntry.setX();
                mPie.add(pieEntry);
            }
            if (isCost.equals("1")){
                cost.setText(total.toString());
                income.setText("0.0");
            }else{
                cost.setText("0.0");
                income.setText(total.toString());
            }
        }
        return mPie;
    }

    private void showPieChart(PieChart pieChart, List<PieEntry> pieList) {
        PieDataSet dataSet = new PieDataSet(pieList,"Label");

        // 设置颜色list，让不同的块显示不同颜色，下面是我觉得不错的颜色集合，比较亮
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int[] MATERIAL_COLORS = {
                Color.rgb(200, 172, 255)
        };
        for (int c : MATERIAL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);

        // 设置描述，我设置了不显示，因为不好看，你也可以试试让它显示，真的不好看
        Description description = new Description();
        description.setEnabled(false);
        pieChart.setDescription(description);
        //设置半透明圆环的半径, 0为透明
        pieChart.setTransparentCircleRadius(0f);

        //设置初始旋转角度
        pieChart.setRotationAngle(-15);

        //数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1OffsetPercentage(80f);

        //设置连接线的颜色
        dataSet.setValueLineColor(Color.LTGRAY);
        // 连接线在饼状图外面
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // 设置饼块之间的间隔
        dataSet.setSliceSpace(1f);
        dataSet.setHighlightEnabled(true);
        // 不显示图例
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        // 和四周相隔一段距离,显示数据
        pieChart.setExtraOffsets(26, 5, 26, 5);

        // 设置pieChart图表是否可以手动旋转
        pieChart.setRotationEnabled(false);
        // 设置piecahrt图表点击Item高亮是否可用
        pieChart.setHighlightPerTapEnabled(true);
        // 设置pieChart图表展示动画效果，动画运行1.4秒结束
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        //设置pieChart是否只显示饼图上百分比不显示文字
        pieChart.setDrawEntryLabels(true);
        //是否绘制PieChart内部中心文本
        pieChart.setDrawCenterText(false);
        // 绘制内容value，设置字体颜色大小
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.DKGRAY);

        pieChart.setData(pieData);
        // 更新 piechart 视图
        pieChart.postInvalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
    }
}
