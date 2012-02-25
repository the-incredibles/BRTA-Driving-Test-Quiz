package net.incredibles.brtaquiz.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.inject.Inject;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.controller.ResultController;
import net.incredibles.brtaquiz.service.Session;
import net.incredibles.brtaquiz.service.TimerService;
import net.incredibles.brtaquiz.util.PieChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 * @author sharafat
 * @Created 2/21/12 9:49 PM
 */
public class ResultActivity extends RoboActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ResultActivity.class);

    @InjectView(R.id.total_questions)
    private TextView totalQuestionsTextView;
    @InjectView(R.id.answered)
    private TextView answeredTextView;
    @InjectView(R.id.correct)
    private TextView correctTextView;
    @InjectView(R.id.incorrect)
    private TextView incorrectTextView;
    @InjectView(R.id.unanswered)
    private TextView unansweredTextView;
    @InjectView(R.id.marks)
    private TextView marksObtainedTextView;
    @InjectView(R.id.total_time)
    private TextView totalTimeTextView;
    @InjectView(R.id.time_taken)
    private TextView timeTakenTextView;
    @InjectView(R.id.detailed_result_btn)
    private Button detailedResultBtn;
    @InjectView(R.id.show_chart_btn)
    private Button showChartBtn;
    @InjectView(R.id.quit_btn)
    private Button quitBtn;

    @InjectResource(R.string.preparing_result)
    private String preparingResult;
    @InjectResource(R.string.result)
    private String result;
    @InjectResource(R.string.correct)
    private String correct;
    @InjectResource(R.string.incorrect)
    private String incorrect;
    @InjectResource(R.string.unanswered)
    private String unanswered;
    @InjectResource(R.string.marks)
    private String marks;

    @Inject
    private NotificationManager notificationManager;
    @Inject
    private ResultController resultController;
    @Inject
    private Session session;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        notificationManager.cancel(TimerService.SERVICE_ID);

        updateUI();
        setButtonClickHandlers();
    }

    private void updateUI() {
        totalQuestionsTextView.setText(Integer.toString(resultController.getTotalQuestions()));
        answeredTextView.setText(Integer.toString(resultController.getAnswered()));
        correctTextView.setText(Integer.toString(resultController.getCorrect()));
        incorrectTextView.setText(Integer.toString(resultController.getIncorrect()));
        unansweredTextView.setText(Integer.toString(resultController.getUnanswered()));
        marksObtainedTextView.setText(Integer.toString(resultController.getObtainedMarks()));
        totalTimeTextView.setText(resultController.getTotalTime());
        timeTakenTextView.setText(resultController.getTimeTaken());
    }

    private void setButtonClickHandlers() {
        detailedResultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, ResultDetailsActivity.class));
            }
        });

        showChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] colors = new int[]{Color.GREEN, Color.RED, Color.YELLOW};
                String[] computingParameters = new String[]{correct, incorrect, unanswered};
                double[] pieValues = {resultController.getCorrect(), resultController.getIncorrect(),
                        resultController.getUnanswered()};

                PieChart pieChart = new PieChart();
                pieChart.setChartValues(pieValues);
                pieChart.setChartColors(colors);
                pieChart.setCOMPUTING_PARAMETER(computingParameters);
                pieChart.setGraphTitle(result);

                startActivity(pieChart.execute(ResultActivity.this));
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


    /**
     * The primary purpose is to prevent systems before android.os.Build.VERSION_CODES.ECLAIR
     * from calling their default KeyEvent.KEYCODE_BACK during onKeyDown.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /** Overrides the default implementation for KeyEvent.KEYCODE_BACK so that all systems call onBackPressed(). */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /** If a Child Activity handles KeyEvent.KEYCODE_BACK. Simply override and add this method. */
    private void onBackPressed() {
        //Do nothing...
    }
}
