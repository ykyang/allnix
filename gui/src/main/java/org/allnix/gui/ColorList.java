package org.allnix.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import vtk.vtkLookupTable;

public class ColorList {
    static final public List<double[]> matlabColorList;
    static final public List<double[]> formationColorList;
    static final public double[] darkGray;
    static final public double[] red;
    static final public double[] green;
    static final public double[] blue;
    static final public double[] yellow;
    static final public double[] lightBackground;
    static final public Color lightBackgroundColor;
    static final public double[] darkBackground;
    static final public Color darkBackgroundColor;
    static {
        matlabColorList = new ArrayList<>();
        matlabColorList.add(new double[] { 0, 0.447, 0.741 });
        matlabColorList.add(new double[] { 0.85, 0.325, 0.098 });
        matlabColorList.add(new double[] { 0.929, 0.694, 0.125 });
        matlabColorList.add(new double[] { 0.494, 0.184, 0.556 });
        matlabColorList.add(new double[] { 0.466, 0.674, 0.188 });
        matlabColorList.add(new double[] { 0.301, 0.745, 0.933 });
        matlabColorList.add(new double[] { 0.635, 0.078, 0.184 });
        matlabColorList.add(new double[] { 0, 0.447, 0.741 });
        matlabColorList.add(new double[] { 0.85, 0.325, 0.098 });
        matlabColorList.add(new double[] { 0.929, 0.694, 0.125 });
        matlabColorList.add(new double[] { 0.494, 0.184, 0.556 });
        matlabColorList.add(new double[] { 0.466, 0.674, 0.188 });

        formationColorList = new ArrayList<>();
        
//        //formationColorList.add(new double[] { 0.6, 0.6, 1 }); // 153, 153, 255
//        //formationColorList.add(new double[] { 0.902, 0.863, 0.898 }); // 153, 153, 255
//        // Template: formationColorList.add(new double[] {/255., /255., /255.});
//        //: From Roberto
//        formationColorList.add(new double[] {255/255., 201/255., 102/255.});
//        formationColorList.add(new double[] {194/255., 194/255., 255/255.});
//        formationColorList.add(new double[] {255/255., 255/255., 157/255.});
//        formationColorList.add(new double[] {194/255., 224/255., 255/255.});
//        formationColorList.add(new double[] {255/255., 194/255., 224/255.});
//        formationColorList.add(new double[] {194/255., 255/255., 194/255.});
//        formationColorList.add(new double[] {224/255., 194/255., 255/255.});
//        formationColorList.add(new double[] { 0.361, 0.361, 1 }); // 92 92 255
//        formationColorList.add(new double[] { 1, 0.6, 1 }); // 255 153 255
//        formationColorList.add(new double[] {225/255., 225/255., 121/255.});
//        
//        
//        //formationColorList.add(new double[] { 1, 1, 0.36 }); // 255, 255, 92
//        formationColorList.add(new double[] { 0.6, 0.8, 1 }); // 153, 204, 255
//        formationColorList.add(new double[] { 1, 0.6, 0.8 }); // 255, 153, 204
//        formationColorList.add(new double[] { 0.6, 1, 0.6 }); // 153, 255, 153
//        formationColorList.add(new double[] { 0.8, 0.6, 1 }); // 204, 153, 255
//        formationColorList.add(new double[] { 1, 1, 0.122 }); // 255, 255, 31
//        formationColorList.add(new double[] { 0.6, 1, 1 }); // 153 255 255
//        formationColorList.add(new double[] { 1, 0.6, 1 }); // 255 153 255
//        formationColorList.add(new double[] { 0.361, 0.361, 1 }); // 92 92 255
//        
//        formationColorList.add(new double[] { 0.6, 1, 0.8 }); // 153 255 204
//        //formationColorList.add(new double[] { .686, 0.431, 0.306 }); // 92 92 255
//                
//        formationColorList.add(new double[] { 1, 0.6, 0.6 }); // 255 153 153
//        formationColorList.add(new double[] { 0.122, 0.122, 1 }); // 31 31 255
//        formationColorList.add(new double[] { 1, 0.8, 0.6 }); // 255 204 153
//        
//        formationColorList.add(new double[] { 0.6, 0.6, 1 }); // 153, 153, 255
        // Got from FormationPlot.m
        formationColorList.add(new double[] {0.600000000000000,   0.600000000000000,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   1.000000000000000,   0.360784313725490});
        formationColorList.add(new double[] {0.600000000000000,   0.800000000000000,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   0.600000000000000,   0.800000000000000});
        formationColorList.add(new double[] {0.600000000000000,   1.000000000000000,   0.600000000000000});
        formationColorList.add(new double[] {0.800000000000000,   0.600000000000000,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   1.000000000000000,   0.121568627450980});
        formationColorList.add(new double[] {0.600000000000000,   1.000000000000000,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   0.600000000000000,   1.000000000000000});
        formationColorList.add(new double[] {0.360784313725490,   0.360784313725490,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   0.600000000000000,   0.600000000000000});
        formationColorList.add(new double[] {0.600000000000000,   1.000000000000000,   0.800000000000000});
        formationColorList.add(new double[] {0.121568627450980,   0.121568627450980,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   0.800000000000000,   0.600000000000000});
        formationColorList.add(new double[] {0.800000000000000,   1.000000000000000,   0.600000000000000});
        formationColorList.add(new double[] {0.600000000000000,   0.600000000000000,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   1.000000000000000,   0.360784313725490});
        formationColorList.add(new double[] {0.600000000000000,   0.800000000000000,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   0.600000000000000,   0.800000000000000});
        formationColorList.add(new double[] {0.600000000000000,   1.000000000000000,   0.600000000000000});
        formationColorList.add(new double[] {0.800000000000000,   0.600000000000000,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   1.000000000000000,   0.121568627450980});
        formationColorList.add(new double[] {0.600000000000000,   1.000000000000000,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   0.600000000000000,   1.000000000000000});
        formationColorList.add(new double[] {0.360784313725490,   0.360784313725490,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   0.600000000000000,   0.600000000000000});
        formationColorList.add(new double[] {0.600000000000000,   1.000000000000000,   0.800000000000000});
        formationColorList.add(new double[] {0.121568627450980,   0.121568627450980,   1.000000000000000});
        formationColorList.add(new double[] {1.000000000000000,   0.800000000000000,   0.600000000000000});
        formationColorList.add(new double[] {0.800000000000000,   1.000000000000000,   0.600000000000000});
        formationColorList.add(new double[] {1.000000000000000,   0.647058823529412,                   0});
        
        
        Color color = Color.darkGray;
        darkGray = new double[] { color.getRed()/255., color.getGreen()/255., color.getBlue()/255.};
        lightBackground = new double[] {250/255., 250/255., 250/255.};
        lightBackgroundColor = new Color(250/255.f, 250/255.f, 250/255.f);
//        lightBackground = new double[] {220/255., 222/255., 220/255.};
//        lightBackgroundColor = new Color(220/255.f, 222/255.f, 220/255.f);
        // > From Amanda
        red = new double[] {234/255., 85/255., 84/255.};
        green = new double[] {26/255., 170/255., 95/255.};
        blue = new double[] {63/255., 155/255., 255/255.};
        yellow = new double[] {234/255., 190/255., 84/255.};
        darkBackground = new double[] {56/255., 60/255., 63/255.};
        darkBackgroundColor = new Color(56/255.f, 60/255.f, 63/255.f);
        // 204 255 153
        // 153 153 255
        // 255 255 92
        // 153 204 255
        // 255 153 204
        // 153 255 153
        // 204 153 255
        // 255 255 31
        // 153 255 255
        // 255 153 255
        // 92 92 255
        // 255 153 153
        // 153 255 204
        // 31 31 255
        // 255 204 153
        // 204 255 153
        // 255 165 0
    }

    static public ColorList matlabColorList() {
        ColorList colorList = new ColorList();
        colorList.colorList = new ArrayList<>(matlabColorList);

        return colorList;
    }

    static public ColorList formationColorList() {
        ColorList colorList = new ColorList();
        colorList.colorList = new ArrayList<>(formationColorList);

        return colorList;
    }
    
    /**
     * Set a starting color index
     * 
     * This allows cube to match HF Modeler color scheme where HF Modeler
     * typically has more formations. 
     * 
     * @param ind
     * @return
     */
    static public ColorList formationColorList(int ind) {
        ColorList colorList = new ColorList();
        colorList.colorList = new ArrayList<>(formationColorList);
        colorList.colorIndex = ind;
        
        return colorList;
    }

    private List<double[]> colorList = new ArrayList<>();
    private int colorIndex = 0;

    public ColorList() {
        
    }
    /**
     * 
     * @param r Red [0,1]
     * @param g Green [0,1]
     * @param b Blue [0,1]
     */
    public void setColor(double[] r, double[] g, double[] b) {
        // Check they are the same size
        if (r.length != g.length) {
            throw new RuntimeException("Red and Green are of different size");
        }
        if (g.length != b.length){
            throw new RuntimeException("Green and Blue are of different size");
        }
        
        int length = r.length;
        
        for (int i = 0; i < length; i++) {
            double[] rgb = new double[3];
            rgb[0] = r[i];
            rgb[1] = g[i];
            rgb[2] = b[i];
            this.colorList.add(rgb);
        }
    }
    
    public synchronized double[] nextColor() {
        // > cyclic
        if (colorIndex == colorList.size()) {
            colorIndex = 0;
        }

        double[] color = colorList.get(colorIndex);
        ++colorIndex;

        return color;
    }
    
    static public void buildLookupTableColor(vtkLookupTable lut, double[] r, double[] g, double[] b) {
        
        int count = r.length;
        
        if (count != g.length){
            throw new RuntimeException("Green color has different length");
        }
        
        if (count != b.length){
            throw new RuntimeException("Blue color has different length");
        }
        
        
        lut.SetNumberOfTableValues(count);
        for (int i = 0; i < count; i++) {
            lut.SetTableValue(i, r[i], g[i], b[i], 1);
        }
        lut.SetNanColor(1, 1, 1, 1);
//        lut.SetNanColor(1, 1, 1, 0); // does not work
//        lut.SetScaleToLog10();
        lut.Build();
    }
    
    static public Color getMatlabColor(int i) {
        double[] rgb = matlabColorList.get(i);
        Color color = new Color((float)rgb[0], (float)rgb[1], (float)rgb[2]);
        return color;
    }
    
    static public void buildGrayColorTable(vtkLookupTable lut) {
        buildGrayColorTable(lut, 0, 1, 402);
    }
    
    static public void buildGrayColorTable(vtkLookupTable lut, double a, double b, int count) {
        double denominator = count - 1; 
        lut.SetNumberOfTableValues(count);
        for (int i = 0; i < count; i++) {
            double value = a + i/denominator*(b-a);
            lut.SetTableValue(i, value, value, value, 1);
        }
        lut.SetNanColor(0, 0, 0, 1);
//        lut.SetNanColor(1, 0, 0, 0); // does not work
        lut.Build();
    }
    static public double[] toDouble(Color color) {
        double[] c = new double[3];
        c[0] = color.getRed()/255.;
        c[1] = color.getGreen()/255.;
        c[2] = color.getBlue()/255.;
        
        return c;
    }
    
    static public void buildParulaColorTable(vtkLookupTable lut) {
        double[][] colors = {
            {0.2422,    0.1504,    0.6603},
            {0.2504,    0.1650,    0.7076},
            {0.2578,    0.1818,    0.7511},
            {0.2647,    0.1978,    0.7952},
            {0.2706,    0.2147,    0.8364},
            {0.2751,    0.2342,    0.8710},
            {0.2783,    0.2559,    0.8991},
            {0.2803,    0.2782,    0.9221},
            {0.2813,    0.3006,    0.9414},
            {0.2810,    0.3228,    0.9579},
            {0.2795,    0.3447,    0.9717},
            {0.2760,    0.3667,    0.9829},
            {0.2699,    0.3892,    0.9906},
            {0.2602,    0.4123,    0.9952},
            {0.2440,    0.4358,    0.9988},
            {0.2206,    0.4603,    0.9973},
            {0.1963,    0.4847,    0.9892},
            {0.1834,    0.5074,    0.9798},
            {0.1786,    0.5289,    0.9682},
            {0.1764,    0.5499,    0.9520},
            {0.1687,    0.5703,    0.9359},
            {0.1540,    0.5902,    0.9218},
            {0.1460,    0.6091,    0.9079},
            {0.1380,    0.6276,    0.8973},
            {0.1248,    0.6459,    0.8883},
            {0.1113,    0.6635,    0.8763},
            {0.0952,    0.6798,    0.8598},
            {0.0689,    0.6948,    0.8394},
            {0.0297,    0.7082,    0.8163},
            {0.0036,    0.7203,    0.7917},
            {0.0067,    0.7312,    0.7660},
            {0.0433,    0.7411,    0.7394},
            {0.0964,    0.7500,    0.7120},
            {0.1408,    0.7584,    0.6842},
            {0.1717,    0.7670,    0.6554},
            {0.1938,    0.7758,    0.6251},
            {0.2161,    0.7843,    0.5923},
            {0.2470,    0.7918,    0.5567},
            {0.2906,    0.7973,    0.5188},
            {0.3406,    0.8008,    0.4789},
            {0.3909,    0.8029,    0.4354},
            {0.4456,    0.8024,    0.3909},
            {0.5044,    0.7993,    0.3480},
            {0.5616,    0.7942,    0.3045},
            {0.6174,    0.7876,    0.2612},
            {0.6720,    0.7793,    0.2227},
            {0.7242,    0.7698,    0.1910},
            {0.7738,    0.7598,    0.1646},
            {0.8203,    0.7498,    0.1535},
            {0.8634,    0.7406,    0.1596},
            {0.9035,    0.7330,    0.1774},
            {0.9393,    0.7288,    0.2100},
            {0.9728,    0.7298,    0.2394},
            {0.9956,    0.7434,    0.2371},
            {0.9970,    0.7659,    0.2199},
            {0.9952,    0.7893,    0.2028},
            {0.9892,    0.8136,    0.1885},
            {0.9786,    0.8386,    0.1766},
            {0.9676,    0.8639,    0.1643},
            {0.9610,    0.8890,    0.1537},
            {0.9597,    0.9135,    0.1423},
            {0.9628,    0.9373,    0.1265},
            {0.9691,    0.9606,    0.1064},
            {0.9769,    0.9839,    0.0805}
        };
        
        
        int count = colors.length;
        lut.SetNumberOfTableValues(count);
        for (int i = 0; i < count; i++) {
            lut.SetTableValue(i, colors[i][0], colors[i][1], colors[i][2], 1);
        }
        lut.SetNanColor(1, 1, 1, 1);
//        lut.SetNanColor(1, 1, 1, 0); // does not work
//        lut.SetScaleToLog10();
        lut.Build();
    }
    
    
    static public void buildJetColorTable(vtkLookupTable lut) {
        double[][] colors = {
            {        0,         0,    0.5625},
            {        0,         0,    0.6250},
            {        0,         0,    0.6875},
            {        0,         0,    0.7500},
            {        0,         0,    0.8125},
            {        0,         0,    0.8750},
            {        0,         0,    0.9375},
            {        0,         0,    1.0000},
            {        0,    0.0625,    1.0000},
            {        0,    0.1250,    1.0000},
            {        0,    0.1875,    1.0000},
            {        0,    0.2500,    1.0000},
            {        0,    0.3125,    1.0000},
            {        0,    0.3750,    1.0000},
            {        0,    0.4375,    1.0000},
            {        0,    0.5000,    1.0000},
            {        0,    0.5625,    1.0000},
            {        0,    0.6250,    1.0000},
            {        0,    0.6875,    1.0000},
            {        0,    0.7500,    1.0000},
            {        0,    0.8125,    1.0000},
            {        0,    0.8750,    1.0000},
            {        0,    0.9375,    1.0000},
            {        0,    1.0000,    1.0000},
            {   0.0625,    1.0000,    0.9375},
            {   0.1250,    1.0000,    0.8750},
            {   0.1875,    1.0000,    0.8125},
            {   0.2500,    1.0000,    0.7500},
            {   0.3125,    1.0000,    0.6875},
            {   0.3750,    1.0000,    0.6250},
            {   0.4375,    1.0000,    0.5625},
            {   0.5000,    1.0000,    0.5000},
            {   0.5625,    1.0000,    0.4375},
            {   0.6250,    1.0000,    0.3750},
            {   0.6875,    1.0000,    0.3125},
            {   0.7500,    1.0000,    0.2500},
            {   0.8125,    1.0000,    0.1875},
            {   0.8750,    1.0000,    0.1250},
            {   0.9375,    1.0000,    0.0625},
            {   1.0000,    1.0000,         0},
            {   1.0000,    0.9375,         0},
            {   1.0000,    0.8750,         0},
            {   1.0000,    0.8125,         0},
            {   1.0000,    0.7500,         0},
            {   1.0000,    0.6875,         0},
            {   1.0000,    0.6250,         0},
            {   1.0000,    0.5625,         0},
            {   1.0000,    0.5000,         0},
            {   1.0000,    0.4375,         0},
            {   1.0000,    0.3750,         0},
            {   1.0000,    0.3125,         0},
            {   1.0000,    0.2500,         0},
            {   1.0000,    0.1875,         0},
            {   1.0000,    0.1250,         0},
            {   1.0000,    0.0625,         0},
            {   1.0000,         0,         0},
            {   0.9375,         0,         0},
            {   0.8750,         0,         0},
            {   0.8125,         0,         0},
            {   0.7500,         0,         0},
            {   0.6875,         0,         0},
            {   0.6250,         0,         0},
            {   0.5625,         0,         0},
            {   0.5000,         0,         0}
        };
        int count = colors.length;
        lut.SetNumberOfTableValues(count);
        for (int i = 0; i < count; i++) {
            lut.SetTableValue(i, colors[i][0], colors[i][1], colors[i][2], 1);
        }
        lut.SetNanColor(1, 1, 1, 1);
//        lut.SetNanColor(1, 1, 1, 0); // does not work
//        lut.SetScaleToLog10();
        lut.Build();
    }
}
