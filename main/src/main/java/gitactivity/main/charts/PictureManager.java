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
import java.util.logging.Logger;

@Service
public class PictureManager {
    Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private UserDailyStatService userDailyStatService;

    @Autowired
    private UserHourlyStatService userHourlyStatService;

    private static CategoryDataset createBarDataset(List<UserDailyStat> stats) {

        var dataset = new DefaultCategoryDataset();
        for (UserDailyStat stat : stats) {
            dataset.setValue(stat.getCommits(), "Commits", stat.getLogin());
        }
        return dataset;
    }

    private static JFreeChart createBarChart(CategoryDataset dataset) {

        return ChartFactory.createBarChart(
                "",
                "",
                "Commits",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
    }

    public void createBarPicture(List<UserDailyStat> stats, String name) throws IOException {
        logger.info("Строю граффик");

        CategoryDataset dataset = createBarDataset(stats);
        String resourcesPath = "main/target/classes/static/";
        JFreeChart chart = createBarChart(dataset);
        ChartUtils.saveChartAsPNG(new File(resourcesPath + name), chart, 450, 400);
    }


    private static XYDataset createLineDataset(UserHourlyStat[] stats) {

        var series1 = new XYSeries("Commits");
        for (int i = 0; i < stats.length; i++) {
            series1.add(i + 9, stats[i].getCommits());
        }

        var series2 = new XYSeries("Lines");
        for (int i = 0; i < stats.length; i++) {
            series2.add(i + 9, stats[i].getLines());
        }

        var dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        return dataset;
    }

    private static JFreeChart createLineChart(XYDataset dataset, String userName) {

        return ChartFactory.createXYLineChart(
                userName,
                "Time",
                "Number",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    public void createLinePicture(UserHourlyStat[] stats, String name, String userName) throws IOException {
        XYDataset dataset = createLineDataset(stats);
        String resourcesPath = "main/target/classes/static/";
        JFreeChart chart = createLineChart(dataset, userName);
        ChartUtils.saveChartAsPNG(new File(resourcesPath + name), chart, 450, 400);
    }


    public void deletePicture(String name) {
        File file = new File(name);
        file.delete();
    }

    public void drawUserHourlyStats(String login) throws IOException {
        PictureManager picture = new PictureManager();
        String name = "goida.png";
        picture.createLinePicture(userHourlyStatService.getUserHourlyStats(login), name, login);
    }

    public void drawDailyStats() throws IOException {
        PictureManager picture = new PictureManager();
        String name = "goida.png";
        picture.createBarPicture(userDailyStatService.getStats(), name);
    }
}
