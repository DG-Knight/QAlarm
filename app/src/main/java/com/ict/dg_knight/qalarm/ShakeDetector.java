package com.ict.dg_knight.qalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by DG-Knight on 13/10/2559.
 */

public class ShakeDetector implements SensorEventListener {
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F; //การเริ่มต้น
    private static final int SHAKE_SLOP_TIME_MS = 500; //ค่าความลาดเอียง
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000; //รีเซ็ตเมื่อหยุด 3 วินาที

    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public interface OnShakeListener {
        void onShake(int count);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mListener != null) {
            float x = event.values[0]; // ค่าจาก Sensor event
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce จะกลับไปเริ่ม 1 ใหม่
            float gForce = (float)Math.sqrt( gX * gX + gY * gY + gZ * gZ );//ฟังก์ชันหาค่ารากที่ 2 ใช้หาค่ารากที่สอง

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();

                //ไม่สนใจ event หรือค่าความเอียงของโทรศัพท์ที่สั้นเกินเกิน(500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset การเขย่าหลังจากไม่เข่าไป 3 วิ
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }
                mShakeTimestamp = now;
                mShakeCount++;

                mListener.onShake(mShakeCount);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //ใช้ตรวจสอบ ความถูกต้อง ไม่ใช้งาน
    }
}
