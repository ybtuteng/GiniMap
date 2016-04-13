package gini;

/**
 * Created by ybtut on 2016/4/5.
 * Draw the map
 */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GenarateMap {

    private void createQXT(String title, String dataName1, String dataName2, String xtitle, String ytitle, String filePath,float[] data,float[] ideal,int total) {

        try {

            XYSeries xyseries = new XYSeries(dataName1);
            XYSeries xyseries1 = new XYSeries(dataName2);
            XYSeriesCollection xyseriescollection = new XYSeriesCollection();//数据??


            double count = 0;

            //data2是实际的线，data1是理想的线
            for (int i = 0; i < data.length; i++) {//表示该图片有data.length行
                String tempData = "" + data[i];
                //count = GiniCompute.getXAxis(total,i);//实际线点的横坐标值
                xyseries1.add(count, Double.parseDouble(tempData));//添加该点
                count++;
                }

           //理想线，上面只有两个点，一个（0.0） 一个（total.100）
            String data11 = "" + ideal[0];
            xyseries.add(0, Double.parseDouble(data11));
            String data12 = "" + ideal[1];
            xyseries.add(total, Double.parseDouble(data12));

            xyseriescollection.addSeries(xyseries);
            xyseriescollection.addSeries(xyseries1);

            JFreeChart chart = createChart(xyseriescollection, title, xtitle, ytitle); // 标题，x轴标题,y轴标题

            chart.setBackgroundPaint(Color.white); // 设置背景??
            chart.setBorderVisible(false); // 设置不边??
            XYPlot plot = (XYPlot) chart.getPlot();

            //saveChartAsJPEG：表示保存为jpeg格式的图
            ChartUtilities.saveChartAsJPEG(new File(filePath), chart, 600, 600);//500X500的表格

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JFreeChart createChart(XYDataset xydataset, String title, String xtitle, String ytitle) {
        JFreeChart jfreechart = ChartFactory.createXYLineChart(title, xtitle, ytitle, xydataset,
                PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer();

        xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{6F, 6F}, 0.0F));
        xylineandshaperenderer.setSeriesStroke(1, new BasicStroke(2.0F, 1, 1, 1.0F, null, 0.0F));
        xylineandshaperenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());

        xylineandshaperenderer.setItemLabelFont(new Font("黑体", Font.TRUETYPE_FONT, 24));// 设置字体

        //设置拐点不可见
        xylineandshaperenderer.setBaseShapesVisible(false);
        xyplot.setRenderer(xylineandshaperenderer);

        //设置y轴刻度
        NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
        numberaxis .setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis .setLowerBound(0);
        numberaxis .setUpperBound(104);
        return jfreechart;
    }

    public void showPic(float[] data,float[] ideal,double gini,int total){

        GenarateMap jcm = new GenarateMap();

        //让gini系数保留两位小数
        DecimalFormat doubleDigit= new DecimalFormat("######0.00");
        String giniString;
        giniString = doubleDigit.format(gini);
        String title = "The Sum:" + GiniCompute.getSum() + "      The Gini Index:" + giniString;

        String xtitle = "Positions/trait";// X轴标
        String ytitle = "Sum/%";// Y轴标
        String dataName1 = "Ideal";//红色线数据代表的含义
        String dataName2 = "Realistic";//蓝色线数据代表的含义
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String time;
        time = df.format(new Date());
        String filePath = "C:/Users/ybtut/IdeaProjects/test/image/" + time +".jpeg";

        jcm.createQXT(title, dataName1, dataName2, xtitle, ytitle, filePath,data,ideal,total);
        System.out.println("	------生成图片完成" + filePath + "------");

        //在面板上打开刚刚生成的图片
        final JFrame frame = new JFrame();
        ImageIcon mypic = new ImageIcon("C:/Users/ybtut/IdeaProjects/test/image/"+ time +".jpeg");

        frame.setTitle("The Gini Map");
        frame.setContentPane(new ImagePanel(mypic.getImage()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, mypic.getIconWidth() + 16, mypic.getIconHeight() + 39);//因为获取的长宽不足以显示整个图片，因此将显示宽度增加16，显示长度增加39
        frame.setVisible(true);

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                frame.repaint();
            }
        }).start();
    }

    static class ImagePanel extends JPanel {
        BufferedImage image;

        public ImagePanel(Image image) {
            // Not really need a BufferedImage, just a requirement
            this.image = new BufferedImage(image.getWidth(null), image.getHeight(null),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = this.image.getGraphics();
            g.drawImage(image, 0, 0, null);
        }

        public void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, null);
        }
    }
}



