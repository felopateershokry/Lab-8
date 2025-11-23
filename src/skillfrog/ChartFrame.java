/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

/**
 *
 * @author GAMING
 */
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.table.DefaultTableModel;

public class ChartFrame extends JFrame {

    public ChartFrame(CourseAnalyticsResult result, JsonDatabaseManager dbManager) {
        setTitle("Course Analytics - Course #" + result.getCourseId());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // 1) Quiz averages per lesson (Bar chart)
        DefaultCategoryDataset quizDataset = new DefaultCategoryDataset();
        for (Map.Entry<Integer, Double> e : result.getQuizAverages().entrySet()) {
            quizDataset.addValue(e.getValue(), "Average Score", "Lesson " + e.getKey());
        }
        JFreeChart quizChart = ChartFactory.createBarChart(
                "Quiz Average per Lesson",
                "Lesson",
                "Average Score",
                quizDataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        tabs.addTab("Quiz Averages", new ChartPanel(quizChart));

        // 2) Lesson completion rates (Bar chart)
        DefaultCategoryDataset lessonDataset = new DefaultCategoryDataset();
        for (Map.Entry<Integer, Double> e : result.getLessonCompletionRates().entrySet()) {
            lessonDataset.addValue(e.getValue(), "Completion %", "Lesson " + e.getKey());
        }
        JFreeChart lessonChart = ChartFactory.createBarChart(
                "Lesson Completion Rates",
                "Lesson",
                "Completion Percentage",
                lessonDataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        tabs.addTab("Lesson Completion", new ChartPanel(lessonChart));

        // 3) Student completion table + optional pie of average distribution
        JPanel studentPanel = new JPanel(new BorderLayout());

        // table model
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Student ID", "Name", "Completion %"}, 0);

        // Fill table using studentCompletion map and dbManager to fetch names
        for (Map.Entry<Integer, Double> e : result.getStudentCompletion().entrySet()) {
            int sid = e.getKey();
            String name = "Unknown";
            User u = dbManager.getUsers().stream().filter(x -> x.getUserId() == sid).findFirst().orElse(null);
            if (u != null) {
                name = u.getUsername();
            }
            tableModel.addRow(new Object[]{sid, name, String.format("%.2f", e.getValue())});
        }

        JTable table = new JTable(tableModel);
        studentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // simple pie of average completion segments (e.g., completed vs not average)
        double total = 0;
        int count = 0;
        for (double v : result.getStudentCompletion().values()) {
            total += v;
            count++;
        }
        double avg = count == 0 ? 0 : total / count;

        DefaultPieDataset pie = new DefaultPieDataset();
        pie.setValue("Avg Completion", avg);
        pie.setValue("Remaining", Math.max(0, 100 - avg));

        JFreeChart pieChart = ChartFactory.createPieChart("Average Completion vs Remaining", pie, true, true, false);
        JPanel bottom = new JPanel(new GridLayout(1, 1));
        bottom.add(new ChartPanel(pieChart));
        studentPanel.add(bottom, BorderLayout.SOUTH);

        tabs.addTab("Students", studentPanel);

        add(tabs);
        setVisible(true);
    }
}
