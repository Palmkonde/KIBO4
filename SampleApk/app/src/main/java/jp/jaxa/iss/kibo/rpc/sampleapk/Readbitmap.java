package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.Result;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Readbitmap {

    public String MatQrProcess(Mat mat) {
        Rect roi = new Rect(600, 550, 200, 200);
        Mat cropped = new Mat(mat, roi);
        Bitmap bMap = Bitmap.createBitmap(cropped.width(), cropped.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(cropped, bMap);

        return BmpQrProcess(bMap);
    }

    public String BmpQrProcess(Bitmap bMap) {
        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(),intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = null;
        try {
            result = new MultiFormatReader().decode(bitmap);
        } catch (NotFoundException ex) {
            Logger.getLogger(Readbitmap.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(result == null) return null;

        List<String> check = Arrays.asList("JEM", "COLUMBUS", "RACK1", "ASTROBEE", "INTBALL", "BLANK");
        List<String> ans = Arrays.asList("STAY_AT_JEM", "GO_TO_COLUMBUS", "CHECK_RACK_1", "I_AM_HERE", "LOOKING_FORWARD_TO_SEE_YOU", "NO_PROBLEM");
        for (int i = 0; i <= 5; i++)
        {
            if (Objects.equals(check.get(i), result.toString()))
            {
                System.out.println(ans.get(i));
                return ans.get(i);
            }
        }
        return null;
    }
}
