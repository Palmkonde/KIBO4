package jp.jaxa.iss.kibo.rpc.sampleapk;

import java.util.*;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

public class createPath {
    private static double[][] dist = {
        {0.0000,1.4604,0.7537,2.5055,3.2339,2.3102,1.5708,1.7834,3.2480},
        {1.4604,0.0000,1.7306,2.8853,3.6437,2.1653,1.3342,1.9363,3.4509},
        {0.7537,1.7306,0.0000,1.8490,2.6081,1.7351,1.4561,1.5189,2.5715},
        {2.5055,2.8853,1.8490,0.0000,1.3270,1.5027,1.8290,1.7866,1.2681},
        {3.2339,3.6437,2.6081,1.3270,0.0000,1.6416,2.6376,2.5824,0.8505},
        {2.3102,2.1653,1.7351,1.5027,1.6416,0.0000,1.2871,1.3938,1.4059},
        {1.5708,1.3342,1.4561,1.8290,2.6376,1.2871,0.0000,0.6943,2.2679},
        {1.7834,1.9363,1.5189,1.7866,2.5824,1.3938,0.6943,0.0000,2.2702},
        {3.2480,3.4509,2.5715,1.2681,0.8505,1.4059,2.2679,2.2702,0.0000}
    };

    private static Point[] pos = {
            new Point(10.3000, -9.8080, 4.3290), // 0
            new Point(11.2625, -10.1800, 5.3625), // 1
            new Point(10.5134, -9.0852, 4.3220), // 2
            new Point(10.6031, -7.7101, 4.3209), // 3
            new Point(10.3070, -6.6740, 5.0953), // 4
            new Point(11.1020, -8.0304, 5.5676), // 5
            new Point(11.5430, -8.9890, 4.8305), // 6
            new Point(11.3690, -8.5518, 4.3200), // 7
            new Point(11.1430, -6.7607, 4.9654), // 8
            new Point(10.3000, -8.6000, 4.7200), // 9
            new Point(10.3000, -7.8000, 4.9200), // 10
            new Point(10.5000, -9.0000, 4.7200), // 11
            new Point(10.5000, -8.6000, 4.7200), // 12
            new Point(10.5000, -8.2000, 4.9200), // 13
            new Point(10.5000, -7.8000, 4.9200), // 14
            new Point(10.7000, -8.6000, 4.7200), // 15
            new Point(10.7000, -8.2000, 4.9200), // 16
            new Point(10.9000, -9.6000, 4.3200), // 17
            new Point(10.9000, -8.8000, 4.7200), // 18
            new Point(10.9000, -8.2000, 4.9200), // 19
            new Point(10.9000, -8.2000, 5.1200), // 20
            new Point(10.9000, -7.8000, 4.9200), // 21
            new Point(11.1000, -9.6000, 4.9200), // 22
            new Point(11.1000, -8.4000, 4.9200), // 23
            new Point(11.1000, -8.2000, 4.9200), // 24
            new Point(11.1000, -8.2000, 5.3200), // 25
            new Point(11.3000, -8.4000, 4.9200), // 26
            new Point(11.3000, -8.4000, 5.1200) // 27
    };

    private Quaternion[] q = {
        new Quaternion(0f,0f,0f,0f),
        new Quaternion(-0.085f, -0.098f, -0.751f, 0.647f), // 1
        new Quaternion( 0.413f, 0.482f, -0.587f,  0.503f), // 2
        new Quaternion( -0.111f,	0.632f,	 0.022f, 0.767f), // 3
        new Quaternion( 0.078f,	-0.986f,	 0.012f,-0.147f), // 4
        new Quaternion(-0.528f,	-0.619f,	-0.442f,   0.377f), // 5
        new Quaternion(0.010f,	-0.123f,	-0.078f, 0.989f), // 6 0.012f,	-0.147f,	-0.078f,   0.986f
        new Quaternion(0f, 0.707f, 0, 0.707f), // 7
    };

    private ArrayList<ArrayList<ArrayList<Integer>>> path = new ArrayList<>();

    public createPath()
    {
        this.intialPath();
    }

    public Quaternion getQ(int p)
    {
        return q[p];
    }

    public ArrayList<Point> getPath(int u,int v)
    {
        ArrayList<Integer> tmp = path.get(u).get(v);
        ArrayList<Point> ans = new ArrayList<>();
        for(int i=0;i<tmp.size();i++)
        {
            if(tmp.get(i) == u ) continue;
            ans.add(pos[tmp.get(i)]);
        }

        return ans;
    }

    public double getDist(int u,int v) { return dist[u][v]; }

    protected void intialPath()
    {
        path = new ArrayList<>(9);
        for(int i=0;i<9;i++)
        {
            path.add(new ArrayList<>());
        }
        path.get(0).add(new ArrayList<>(Arrays.asList(0,0)));
        path.get(0).add(new ArrayList<>(Arrays.asList(0,1)));
        path.get(0).add(new ArrayList<>(Arrays.asList(0,2)));
        path.get(0).add(new ArrayList<>(Arrays.asList(0,13,3)));
        path.get(0).add(new ArrayList<>(Arrays.asList(0,9,10,4)));
        path.get(0).add(new ArrayList<>(Arrays.asList(0,5)));
        path.get(0).add(new ArrayList<>(Arrays.asList(0,6)));
        path.get(0).add(new ArrayList<>(Arrays.asList(0,17,7)));
        path.get(0).add(new ArrayList<>(Arrays.asList(0,15,21,8)));
        path.get(1).add(new ArrayList<>(Arrays.asList(1,0)));
        path.get(1).add(new ArrayList<>(Arrays.asList(1,1)));
        path.get(1).add(new ArrayList<>(Arrays.asList(1,22,2)));
        path.get(1).add(new ArrayList<>(Arrays.asList(1,16,3)));
        path.get(1).add(new ArrayList<>(Arrays.asList(1,4)));
        path.get(1).add(new ArrayList<>(Arrays.asList(1,5)));
        path.get(1).add(new ArrayList<>(Arrays.asList(1,6)));
        path.get(1).add(new ArrayList<>(Arrays.asList(1,7)));
        path.get(1).add(new ArrayList<>(Arrays.asList(1,26,8)));
        path.get(2).add(new ArrayList<>(Arrays.asList(2,0)));
        path.get(2).add(new ArrayList<>(Arrays.asList(2,22,1)));
        path.get(2).add(new ArrayList<>(Arrays.asList(2,2)));
        path.get(2).add(new ArrayList<>(Arrays.asList(2,13,3)));
        path.get(2).add(new ArrayList<>(Arrays.asList(2,12,14,4)));
        path.get(2).add(new ArrayList<>(Arrays.asList(2,5)));
        path.get(2).add(new ArrayList<>(Arrays.asList(2,11,6)));
        path.get(2).add(new ArrayList<>(Arrays.asList(2,11,18,7)));
        path.get(2).add(new ArrayList<>(Arrays.asList(2,15,21,8)));
        path.get(3).add(new ArrayList<>(Arrays.asList(3,13,0)));
        path.get(3).add(new ArrayList<>(Arrays.asList(3,16,1)));
        path.get(3).add(new ArrayList<>(Arrays.asList(3,13,2)));
        path.get(3).add(new ArrayList<>(Arrays.asList(3,3)));
        path.get(3).add(new ArrayList<>(Arrays.asList(3,4)));
        path.get(3).add(new ArrayList<>(Arrays.asList(3,20,5)));
        path.get(3).add(new ArrayList<>(Arrays.asList(3,24,6)));
        path.get(3).add(new ArrayList<>(Arrays.asList(3,19,23,7)));
        path.get(3).add(new ArrayList<>(Arrays.asList(3,8)));
        path.get(4).add(new ArrayList<>(Arrays.asList(4,10,9,0)));
        path.get(4).add(new ArrayList<>(Arrays.asList(4,1)));
        path.get(4).add(new ArrayList<>(Arrays.asList(4,14,12,2)));
        path.get(4).add(new ArrayList<>(Arrays.asList(4,3)));
        path.get(4).add(new ArrayList<>(Arrays.asList(4,4)));
        path.get(4).add(new ArrayList<>(Arrays.asList(4,5)));
        path.get(4).add(new ArrayList<>(Arrays.asList(4,6)));
        path.get(4).add(new ArrayList<>(Arrays.asList(4,23,7)));
        path.get(4).add(new ArrayList<>(Arrays.asList(4,8)));
        path.get(5).add(new ArrayList<>(Arrays.asList(5,0)));
        path.get(5).add(new ArrayList<>(Arrays.asList(5,1)));
        path.get(5).add(new ArrayList<>(Arrays.asList(5,2)));
        path.get(5).add(new ArrayList<>(Arrays.asList(5,20,3)));
        path.get(5).add(new ArrayList<>(Arrays.asList(5,4)));
        path.get(5).add(new ArrayList<>(Arrays.asList(5,5)));
        path.get(5).add(new ArrayList<>(Arrays.asList(5,6)));
        path.get(5).add(new ArrayList<>(Arrays.asList(5,25,7)));
        path.get(5).add(new ArrayList<>(Arrays.asList(5,8)));
        path.get(6).add(new ArrayList<>(Arrays.asList(6,0)));
        path.get(6).add(new ArrayList<>(Arrays.asList(6,1)));
        path.get(6).add(new ArrayList<>(Arrays.asList(6,11,2)));
        path.get(6).add(new ArrayList<>(Arrays.asList(6,24,3)));
        path.get(6).add(new ArrayList<>(Arrays.asList(6,4)));
        path.get(6).add(new ArrayList<>(Arrays.asList(6,5)));
        path.get(6).add(new ArrayList<>(Arrays.asList(6,6)));
        path.get(6).add(new ArrayList<>(Arrays.asList(6,7)));
        path.get(6).add(new ArrayList<>(Arrays.asList(6,8)));
        path.get(7).add(new ArrayList<>(Arrays.asList(7,17,0)));
        path.get(7).add(new ArrayList<>(Arrays.asList(7,1)));
        path.get(7).add(new ArrayList<>(Arrays.asList(7,18,11,2)));
        path.get(7).add(new ArrayList<>(Arrays.asList(7,23,19,3)));
        path.get(7).add(new ArrayList<>(Arrays.asList(7,23,4)));
        path.get(7).add(new ArrayList<>(Arrays.asList(7,25,5)));
        path.get(7).add(new ArrayList<>(Arrays.asList(7,6)));
        path.get(7).add(new ArrayList<>(Arrays.asList(7,7)));
        path.get(7).add(new ArrayList<>(Arrays.asList(7,26,8)));
        path.get(8).add(new ArrayList<>(Arrays.asList(8,21,15,0)));
        path.get(8).add(new ArrayList<>(Arrays.asList(8,26,1)));
        path.get(8).add(new ArrayList<>(Arrays.asList(8,21,15,2)));
        path.get(8).add(new ArrayList<>(Arrays.asList(8,3)));
        path.get(8).add(new ArrayList<>(Arrays.asList(8,4)));
        path.get(8).add(new ArrayList<>(Arrays.asList(8,5)));
        path.get(8).add(new ArrayList<>(Arrays.asList(8,6)));
        path.get(8).add(new ArrayList<>(Arrays.asList(8,26,7)));
        path.get(8).add(new ArrayList<>(Arrays.asList(8,8)));
    }
}
