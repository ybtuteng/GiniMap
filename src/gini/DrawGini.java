package gini;


/**
 * Created by tuteng on 2016/4/5.
 * It's a main
 */
public class DrawGini {
    public static void main(String[] args){
        GiniCompute my= new GiniCompute();
        float[] sample = new float[6775];//测试用例
        int length = sample.length;
        float[] ideal = new float[2];
        float[] sortSample = new float[sample.length];//用于存储处理后的数据

        //初始化用例,随机生成0~100的数存入数组
        for(int index = 0;index < sample.length;index++){
            sample[index] =(float)(Math.random() * 100);
        }

        //计算基尼系数
        double result;
        result = my.calcGiniCoefficient(sample);

        sortSample = my.initialNumbers(sample);
        ideal = my.idealGini();
        GenarateMap show = new GenarateMap();
        show.showPic(sortSample,ideal,result,length);
    }
}
