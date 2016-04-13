package gini;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by ybtut on 2016/4/5.
 * To compute some index for other class
 */
public class GiniCompute {

    public static float sumPosition = 0;//传递总持仓量的一个参数，方便各个方法调用
    public float[] initialNumbers(float[] sample){
        float[] groupData = new float[sample.length+1];//实际只有10个有效数据，groupData[0]被填上了0，以确保曲线过原点;
        groupData[0] = 0;

        //对数组里的数从小到大排序
        groupData = sample.clone();
        Arrays.sort(groupData);

        //计算累计量
        for(int index = 1;index < groupData.length;index++){
            groupData[index] += groupData[index - 1];
        }

        float sum = 0;//求和时用到的一个临时变量
        sum = groupData[groupData.length - 1];
        setSum(sum);

        //得出每个点之前持有量所占的百分比
        BigDecimal doubleDigit;
        for(int index = 0;index < groupData.length;index++) {
            groupData[index] = (groupData[index] / sum) * 100;
            doubleDigit =   new BigDecimal(groupData[index]);
            groupData[index] = doubleDigit.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();//保留两位小数
        }
        return groupData;
    }

    //计算一个数组的基尼系数
    public double calcGiniCoefficient(float[] values)
    {
        if (values.length < 1) return 0;  //not computable
        if (values.length == 1) return 0;
        double relVars = 0;

        float descMean = this.Stat(values);
        if (descMean == 0.0) return 0; // only possible if all data is zero
        for (int i = 0; i < values.length; i++)
        {
            for (int j = 0; j < values.length; j++)
            {
                if (i == j) continue;
                relVars += (Math.abs(values[i] - values[j]));
            }
        }
        relVars = relVars / (2.0 * values.length * values.length);
        return (relVars / descMean); // return gini value
    }

    //计算数组的平均值
    public float Stat(float[] seriesNumber){
        float sum = 0;
        for(int index = 0;index < seriesNumber.length;index++){
            sum+=seriesNumber[index];
        }
        float mean;
        mean = sum / seriesNumber.length;
        return mean;
    }

    //初始化理想的Gini系数，即一条斜率为1的直线
    public float[] idealGini(){
        float[] ideal = new float[2];
        ideal[0] = 0;
        ideal[1] = 100;
        return ideal;
    }

    //获得总持仓量并存在一个常量里
    public static void setSum(float sum){
        sumPosition = sum;
    }

    //取得总量
    public static float getSum(){
        return sumPosition;
    }
}
