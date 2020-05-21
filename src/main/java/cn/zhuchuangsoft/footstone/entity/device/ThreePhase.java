package cn.zhuchuangsoft.footstone.entity.device;

import cn.zhuchuangsoft.footstone.utils.StringUtil;
import lombok.Data;

import java.util.Date;

/**
 * 管总三相设备封装
 */
@Data
public class ThreePhase extends PoweredDevice {

    private static final long serialVersionUID = -336435056982640857L;
    //欠压断电值
    private Short errUnder;
    //过压断电值
    private Short errOver;
    //过流断电值
    private Short errCurrent;
    //漏电值
    private Short leakage;
    //漏电断电值
    private Short errLeakValue;
    //过温断电值
    private Short errTempValue;
    //A相故障电弧
    private Short arcA;
    //B相故障电弧
    private Short arcB;
    //C相故障电弧
    private Short arcC;
    //故障电弧报警值
    private Short errArcValue;
    //三相电压平衡度
    private Short voltageBalance;
    //三相电压平衡度报警值
    private Short errVoltageBalance;
    //三相电流平衡度
    private Short currentBalance;
    //三相电流平衡度报警值
    private Short errCurrentBalance;
    //A相频率  50.0Hz
    private Short hzA;
    //B相频率  50.0Hz
    private Short hzB;
    //C相频率  50.0Hz
    private Short hzC;
    //A相电压相位角
    private Short voltagePhaseA;
    //B相电压相位角
    private Short voltagePhaseB;
    //C相电压相位角
    private Short voltagePhaseC;
    //A相电流相位角
    private Short currentPhaseA;
    //B相电流相位角
    private Short currentPhaseB;
    //C相电流相位角
    private Short currentPhaseC;
    //A相电压谐波
    private Short voltageHarmonicA;
    //B相电压谐波
    private Short voltageHarmonicB;
    //C相电压谐波
    private Short voltageHarmonicC;
    //A相电流谐波
    private Short currentHarmonicA;
    //B相电流谐波
    private Short currentHarmonicB;
    //C相电流谐波
    private Short currentHarmonicC;
    //A相功率因数
    private Short factorA;
    //B相功率因数
    private Short factorB;
    //C相功率因数
    private Short factorC;
    //A相无功功率
    private Short noPowerA;
    //B相无功功率
    private Short noPowerB;
    //C相无功功率
    private Short noPowerC;
    //A相有功功率
    private Short powerA;
    //B相有功功率
    private Short powerB;
    //C相有功功率
    private Short powerC;
    //A相视在功率
    private Short apparentA;
    //B相视在功率
    private Short apparentB;
    //C相视在功率
    private Short apparentC;
    //总功率
    private Short powerAll;
    //A相电量
    private Integer energyA;
    //B相电量
    private Integer energyB;
    //C相电量
    private Integer energyC;
    //总电量
    private Integer energy;
    //GPRS信号强度
    private Byte signalG;
    //掉电指示
    private Byte failure;
    //A相短路
    private Byte failureA;
    //B相短路
    private Byte failureB;
    //C相短路
    private Byte failureC;

    public ThreePhase() {
    }

    public ThreePhase(byte[] bytes) {
        this.deviceCode = String.valueOf(StringUtil.byteFour(bytes[4], bytes[5], bytes[6], bytes[7]));
        this.voltageA = StringUtil.byteTwo(bytes[8], bytes[9]);
        this.voltageB = StringUtil.byteTwo(bytes[10], bytes[11]);
        this.voltageC = StringUtil.byteTwo(bytes[12], bytes[13]);
        this.errUnder = StringUtil.byteTwo(bytes[14], bytes[15]);
        this.errOver = StringUtil.byteTwo(bytes[16], bytes[17]);
        this.currentA = StringUtil.byteTwo(bytes[18], bytes[19]);
        this.currentB = StringUtil.byteTwo(bytes[20], bytes[21]);
        this.currentC = StringUtil.byteTwo(bytes[22], bytes[23]);
        this.errCurrent = StringUtil.byteTwo(bytes[24], bytes[25]);
        this.leakage = StringUtil.byteTwo(bytes[26], bytes[27]);
        this.errLeakValue = StringUtil.byteTwo(bytes[28], bytes[29]);
        this.tempA = StringUtil.byteTwo(bytes[30], bytes[31]);
        this.tempB = StringUtil.byteTwo(bytes[32], bytes[33]);
        this.tempC = StringUtil.byteTwo(bytes[34], bytes[35]);
        this.tempN = StringUtil.byteTwo(bytes[36], bytes[37]);
        this.errTempValue = StringUtil.byteTwo(bytes[38], bytes[39]);
        this.arcA = StringUtil.byteTwo(bytes[40], bytes[41]);
        this.arcB = StringUtil.byteTwo(bytes[42], bytes[43]);
        this.arcC = StringUtil.byteTwo(bytes[44], bytes[45]);
        this.errArcValue = StringUtil.byteTwo(bytes[46], bytes[47]);
        this.voltageBalance = StringUtil.byteTwo(bytes[48], bytes[49]);
        this.errVoltageBalance = StringUtil.byteTwo(bytes[50], bytes[51]);
        this.currentBalance = StringUtil.byteTwo(bytes[52], bytes[53]);
        this.errCurrentBalance = StringUtil.byteTwo(bytes[54], bytes[55]);
        this.hzA = StringUtil.byteTwo(bytes[56], bytes[57]);
        this.hzB = StringUtil.byteTwo(bytes[58], bytes[59]);
        this.hzC = StringUtil.byteTwo(bytes[60], bytes[61]);
        this.voltagePhaseA = StringUtil.byteTwo(bytes[62], bytes[63]);
        this.voltagePhaseB = StringUtil.byteTwo(bytes[64], bytes[65]);
        this.voltagePhaseC = StringUtil.byteTwo(bytes[66], bytes[67]);
        this.currentPhaseA = StringUtil.byteTwo(bytes[68], bytes[69]);
        this.currentPhaseB = StringUtil.byteTwo(bytes[70], bytes[71]);
        this.currentPhaseC = StringUtil.byteTwo(bytes[72], bytes[73]);
        this.voltageHarmonicA = StringUtil.byteTwo(bytes[74], bytes[75]);
        this.voltageHarmonicB = StringUtil.byteTwo(bytes[76], bytes[77]);
        this.voltageHarmonicC = StringUtil.byteTwo(bytes[78], bytes[79]);
        this.currentHarmonicA = StringUtil.byteTwo(bytes[80], bytes[81]);
        this.currentHarmonicB = StringUtil.byteTwo(bytes[82], bytes[83]);
        this.currentHarmonicC = StringUtil.byteTwo(bytes[84], bytes[85]);
        this.factorA = StringUtil.byteTwo(bytes[86], bytes[87]);
        this.factorB = StringUtil.byteTwo(bytes[88], bytes[89]);
        this.factorC = StringUtil.byteTwo(bytes[90], bytes[91]);
        this.noPowerA = StringUtil.byteTwo(bytes[92], bytes[93]);
        this.noPowerB = StringUtil.byteTwo(bytes[94], bytes[95]);
        this.noPowerC = StringUtil.byteTwo(bytes[96], bytes[97]);
        this.powerA = StringUtil.byteTwo(bytes[98], bytes[99]);
        this.powerB = StringUtil.byteTwo(bytes[100], bytes[101]);
        this.powerC = StringUtil.byteTwo(bytes[102], bytes[103]);
        this.apparentA = StringUtil.byteTwo(bytes[104], bytes[105]);
        this.apparentB = StringUtil.byteTwo(bytes[106], bytes[107]);
        this.apparentC = StringUtil.byteTwo(bytes[108], bytes[109]);
        this.powerAll = StringUtil.byteTwo(bytes[110], bytes[111]);
        this.energyA = StringUtil.byteFour(bytes[112], bytes[113], bytes[114], bytes[115]);
        this.energyB = StringUtil.byteFour(bytes[116], bytes[117], bytes[118], bytes[119]);
        this.energyC = StringUtil.byteFour(bytes[120], bytes[121], bytes[122], bytes[123]);
        this.energy = StringUtil.byteFour(bytes[124], bytes[125], bytes[126], bytes[127]);
        this.signalG = bytes[128];
        System.out.println("signalG-----" + bytes[128]);
        this.failure = bytes[129];
        this.failureA = bytes[130];
        this.failureB = bytes[131];
        this.failureC = bytes[132];
        this.createTime = new Date();
    }


}
