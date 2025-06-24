package gitactivity.main.charts;


import gitactivity.main.model.UserDailyStat;
import gitactivity.main.model.UserHourlyStat;
import gitactivity.main.services.UserDailyStatService;
import gitactivity.main.services.UserHourlyStatService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;


import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;


import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

import java.io.IOException;
import java.util.List;

@Service
public class PictureManager {

    @Autowired
    private UserDailyStatService userDailyStatService;

    @Autowired
    private UserHourlyStatService userHourlyStatService;

    private static CategoryDataset createBarDataset(List<UserHourlyStat> stats) {

        var dataset = new DefaultCategoryDataset();
        for(UserHourlyStat stat : stats){
            dataset.setValue(stat.getCommits(), "Commits", stat.getLogin());
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
    public void createBarPicture(List<UserHourlyStat> stats, String name)throws IOException {
        CategoryDataset dataset = createBarDataset(stats);

        JFreeChart chart = createBarChart(dataset);
        ChartUtils.saveChartAsPNG(new File(name), chart, 450, 400);
    }




    private static XYDataset createLineDataset(List<UserHourlyStat> stats) {

        var series1 = new XYSeries("Commits");
        for(int i = 0; i < stats.size(); i++){
            series1.add(i+9, stats.get(i).getCommits());
        }

        var series2 = new XYSeries("Lines");
        for(int i = 0; i < stats.size(); i++){
            series2.add(i+9, stats.get(i).getLines());
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
    public void createLinePicture(List<UserHourlyStat> stats, String name, String userName) throws IOException, InterruptedException {
        XYDataset dataset = createLineDataset(stats);
        String resourcesPath = "main/src/main/resources/static/";
        JFreeChart chart = createLineChart(dataset, userName);
        ChartUtils.saveChartAsPNG(new File(resourcesPath + name), chart, 450, 400);
    }




    public void deletePicture(String name)throws IOException {
        File file = new File(name);
        file.delete();
    }

    public void mainMethod(String login) throws IOException, InterruptedException {
        PictureManager Picture = new PictureManager();
        String name = "goida.png";
        Picture.createLinePicture(userHourlyStatService.getUserHourlyStats(login), name, login);
    }
}
