package jp.jaxa.iss.kibo.rpc.sampleapk;


import android.graphics.Bitmap;
import android.util.Log;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.arc.astrobee.Kinematics;
import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */


public class YourService extends KiboRpcService {

    private final String TAG = this.getClass().getSimpleName();
    private final int LOOPMAX = 3;
    private final Point LASER = new Point(0.1302,0.0572,-0.1111);

    int currentPos = 0;
    String qrcode;

    Readbitmap qrcodeReader = new Readbitmap();
    createPath pathFinder = new createPath();
    Piority_queue pq = new Piority_queue();
    Quaternion quaternion;
    ArrayList<Point> path;

    @Override
    protected void runPlan1(){
        Log.i(TAG, "start mission");

        api.startMission();
        Kinematics KIBO = api.getRobotKinematics();

        move(10.3d, -9.808d, 4.329d,0f,0f,0f,0f); // start position

        path = pathFinder.getPath(currentPos,7);
        quaternion = pathFinder.getQ(7);

        for(Point e : path)
        {
            api.moveTo(e,quaternion,false);
        }

        api.flashlightControlFront(0.05f);
        //Mat matImage = api.getMatNavCam();
        //api.saveMatImage(matImage,"matimage.jpg");
        Bitmap bitImage = api.getBitmapNavCam();
        api.saveBitmapImage(bitImage,"bitmap.bmp");
        api.flashlightControlFront(0f);

        qrcode = qrcodeReader.BmpQrProcess(bitImage);
        if(qrcode == null)
        {
            qrcode = "your string";
        }

        Log.i(TAG, "Reading output is: " + qrcode);

        currentPos = 7;

        List<Long> timeRemain = api.getTimeRemaining();
        while(timeRemain.get(1) > 60000) //60000
        {
            Log.i(TAG, "Time Remain: " + timeRemain.get(1).toString());

            List<Integer> activePoint = api.getActiveTargets();

            int near = activePoint.get(0);
            int loopCount = 0;

            for(int i=0;i<activePoint.size();i++) { pq.push(pathFinder.getDist(currentPos, activePoint.get(i)) , activePoint.get(i)); }

            while(!pq.empty() && loopCount < (LOOPMAX + activePoint.size()) && timeRemain.get(1) > 60000)
            {
                Log.i(TAG, "Active Point = " + activePoint.toString());

                near = pq.top();
                pq.pop();

                Log.i(TAG, "from: " + currentPos + " going to " + near);
                path = pathFinder.getPath(currentPos, near);
                quaternion = pathFinder.getQ(near);


                for(Point e:path)
                {
                    Result r = api.moveTo(e,quaternion,false);

                    int cnt = 0;
                    while(!r.hasSucceeded() && cnt < LOOPMAX)
                    {
                        r = api.moveTo(e,quaternion, false);
                        cnt++;
                    }
                }

                api.laserControl(true);
                api.takeTargetSnapshot(near);

                currentPos = near;
                loopCount++;
                timeRemain = api.getTimeRemaining();
            }

            while(!pq.empty()) pq.pop();

            timeRemain = api.getTimeRemaining();
        }

        // 2,1 to goal more than 60 sec
        // 6 to goal more than 32 sec
        // 5,3 to goal more than 25 sec
        // 4 to goal more than 20 sec

        timeRemain = api.getTimeRemaining();
        Log.i(TAG, "Time Reaming: " + timeRemain.get(1).toString());

        if(timeRemain.get(1) < 60000 && (currentPos == 2 || currentPos == 1)) api.reportMissionCompletion(qrcode); //better than get zero
        if(timeRemain.get(1) < 32000 && currentPos == 6) api.reportMissionCompletion(qrcode);
        if(timeRemain.get(1) < 25000 && (currentPos == 5 || currentPos == 3)) api.reportMissionCompletion(qrcode);
        if(timeRemain.get(1) < 20000 && currentPos == 4) api.reportMissionCompletion(qrcode);
        if(timeRemain.get(1) < 10000) api.reportMissionCompletion(qrcode);

        api.notifyGoingToGoal();
        Log.i(TAG, "from: " + currentPos + " to GOAL");
        Quaternion q = new Quaternion(0f,0f,0f,0f);
        path = pathFinder.getPath(currentPos, 8);

        for(Point e : path)
        {
            Result r = api.moveTo(e,q,false);
        }


        api.reportMissionCompletion(qrcode);
        return;
    }

    void move(double x, double y, double z, float qx, float qy, float qz, float qw)
    {
        String pos = String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z) + ", ";
        String qua = "qua: " + String.valueOf(qx) + "," + String.valueOf(qy) + "," + String.valueOf(qz) + "," + String.valueOf(qw);
        Log.i(TAG, pos + qua);

        Point p = new Point(x,y,z);
        Quaternion q = new Quaternion(qx,qy,qz,qw);
        Result r = api.moveTo(p,q,true);

        List<Long> time = api.getTimeRemaining();
        int cnt = 0;
        while(!r.hasSucceeded() && cnt < LOOPMAX && time.get(1) >= 60000)
        {
            time = api.getTimeRemaining();
            r = api.moveTo(p,q, true);
            cnt++;
        }
    }
}

