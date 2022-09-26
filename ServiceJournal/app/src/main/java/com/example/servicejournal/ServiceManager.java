package com.example.servicejournal;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.CallLog;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceManager extends Service {

    static Timer timer;
    static TimerTask mTimerTask;

    static PostRequest request;

    static int lastCountBook;
    static String lastNum;
    static String lastType;
    static Date  lastDate;
    static String lastDuration;

    public ServiceManager() {
    }

    public boolean isLastSame(String num, String type, Date date, String duration, int count){

        if(lastCountBook == count
                && lastNum.equals(num)
                && lastType.equals(type)
                && lastDate.equals(date)
                && lastDuration.equals(duration))
            return true;
        else
            return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        lastCountBook = 0;
        lastNum = null;
        lastType = null;
        lastDate = null;
        lastDuration = null;

        request = new PostRequest("loclahost");
        timer = new Timer();

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Cursor managedCursor = getContentResolver().query(
                        CallLog.Calls.CONTENT_URI, null, null, null, null);

                managedCursor.moveToLast();

                int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
                int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
                int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

                if (isLastSame(managedCursor.getString(number),
                        managedCursor.getString(type),
                        new Date(Long.valueOf(managedCursor.getString(date))),
                        managedCursor.getString(type),
                        managedCursor.getCount())) {
                    return;
                } else {
                    lastCountBook = managedCursor.getCount();
                    lastNum = managedCursor.getString(number);
                    lastType = managedCursor.getString(type);
                    lastDate = new Date(Long.valueOf(managedCursor.getString(date)));
                    lastDuration = managedCursor.getString(type);
                }

                managedCursor.moveToFirst();
                number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
                date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
                duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

                while (managedCursor.moveToNext()) {
                    String phNumber = managedCursor.getString(number);
                    String callType = managedCursor.getString(type);
                    String callDate = managedCursor.getString(date);
                    Date callDayTime = new Date(Long.valueOf(callDate));
                    String callDuration = managedCursor.getString(duration);
                    String dir = null;
                    int dircode = Integer.parseInt(callType);
                    switch (dircode) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            dir = "OUTGOING";
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            dir = "INCOMING";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            dir = "MISSED";
                            break;
                    }
                    request.sendData(phNumber, callType, callDayTime.toString(), callDuration);
                }
                managedCursor.close();
                request.sendFinish();

            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
            timer.schedule(mTimerTask, 0, 180000);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}