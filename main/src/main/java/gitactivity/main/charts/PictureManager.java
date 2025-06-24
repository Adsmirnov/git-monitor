package gitactivity.main.charts;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;


import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;


import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;

import java.io.IOException;

public class PictureManager {
    private static CategoryDataset createBarDataset(int[] comits, String[] users) {

        var dataset = new DefaultCategoryDataset();
        for(int i = 0; i < comits.length; i++){
            dataset.setValue(comits[i], "Commits", users[i]);
        }
        return dataset;
    }
    private static JFreeChart createBarChart(CategoryDataset dataset) {

        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "",
                "Commits",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
        return barChart;
    }
    public void createBarPicture(int[] comits, String[] users, String name)throws IOException {
        CategoryDataset dataset = createBarDataset(comits, users);

        JFreeChart chart = createBarChart(dataset);
        ChartUtils.saveChartAsPNG(new File(name), chart, 450, 400);
    }




    private static XYDataset createLineDataset(int[] comits, int[] Lines) {

        var series1 = new XYSeries("Commits");
        for(int i = 0; i < comits.length; i++){
            series1.add(i+9, comits[i]);
        }

        var series2 = new XYSeries("Lines");
        for(int i = 0; i < Lines.length; i++){
            series2.add(i+9, Lines[i]);
        }

        var dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        return dataset;
    }
    private static JFreeChart createLineChart(XYDataset dataset, String userName) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                userName,
                "Time",
                "Number",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        return chart;
    }
    public void createLinePicture(int[] comits, int[] Lines, String name, String userName)throws IOException {
        XYDataset dataset = createLineDataset(comits, Lines);

        JFreeChart chart = createLineChart(dataset, userName);
        ChartUtils.saveChartAsPNG(new File(name), chart, 450, 400);
    }




    public void delitePicture(String name)throws IOException {
        File file = new File(name);
        file.delete();
    }

    public void mainMethod() throws IOException {

        PictureManager Picture = new PictureManager();
        int[] comits = {11, 46, 28, 33, 22, 13};
        String[] users = {"User 1", "User 2", "User 3", "User 4", "User 5", "User 6"};
        String name = "goida.png";
        int[] comitsUser = {4, 6, 2, 7, 2, 7, 10, 6, 11};
        int[] lines = {43, 56, 77, 44, 73, 471, 56, 575, 43};
        Picture.createLinePicture(comitsUser, lines, name, "user 1");
    }
}
