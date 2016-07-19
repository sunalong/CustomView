package com.itcode.customView.test;

import android.app.Activity;
import android.os.Bundle;

import com.itcode.customView.R;
import com.itcode.customView.view.StartCustomTextView;

/**
 * Created by along on 16/7/18.
 */

public class TextViewTestActivity extends Activity{

    StartCustomTextView mCYTextView;
    String text="台湾（Taiwan）位于中国大陆东南沿海的大陆架上，东临太平洋，[1]东北邻琉球群岛，[2]南界巴士海峡与菲律宾群岛相对，[3]西隔台湾海峡与福建省相望，[4-5]总面积约3"+
".6万平方千米，包括台湾岛及兰屿、绿岛、钓鱼岛等21个附属岛屿和澎湖列岛64个岛屿。台湾岛面积35882.6258平方千米，[6]是中国第一大岛，[7]"+
"7成为山地与丘陵，平原主要集中于西部沿海，地形海拔变化大。由于地处热带及亚热带气候之交界，自然景观与生态资源丰富多元。人口约2350万，逾7成集中于西部5大都会区，[8]其中以首要都市台北为中心的台北都会区最大。"+
"台湾是中国不可分割的一部分。[9-11]原住民族（高山族）在17世纪汉族移入前即已在此定居；自明末清初始有较显著之福建南部和广东东部人民移垦，最终形成以汉族为主体的社会。[12]南宋澎湖属福建路；[13]元、明在澎湖设巡检司；[14-15]"+
"明末被荷兰和西班牙侵占；[16-17]1662年郑成功收复；[18]清代1684年置台湾府，属福建省，[19]1885年建省；[20-21]1895年清政府以《马关条约》割让与日本；[22]1945年抗战胜利后光复；[23]"+
"1949年国民党政府在内战失利中退守台湾，海峡两岸分治至今。[24]"+
"台湾自1960年代起推行出口导向型工业化战略，经济社会发展突飞猛进，缔造了举世瞩目的台湾经济奇迹，[25]名列亚洲四小龙之一，于1990年代跻身发达经济体之列。[26-28]台湾制造业与高新技术产业发达，半导体、IT、通讯、电子精密制造等领域全球领先。"+
"台湾文化以中华文化为主体，是中华文化的重要组成部分，[29-30]原住民族的南岛文化亦有影响，[31]近现代又融合日本和欧美文化，呈现多元风貌。Android提供了精巧和有力的组件化模型构建用户的UI部分。主要是基于布局类：View和ViewGroup。在此基础上，android平台提供了大量的预制的View和xxxViewGroup子类，即布局（layout）和窗口小部件（widget）。可以用它们构建自己的UI。";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_custom_text_view);

        mCYTextView = (StartCustomTextView)findViewById(R.id.mv);
        mCYTextView.SetText(text);
    }
}
