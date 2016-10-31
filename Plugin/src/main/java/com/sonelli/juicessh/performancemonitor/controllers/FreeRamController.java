package com.sonelli.juicessh.performancemonitor.controllers;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.sonelli.juicessh.performancemonitor.R;
import com.sonelli.juicessh.pluginlibrary.exceptions.ServiceNotConnectedException;
import com.sonelli.juicessh.pluginlibrary.listeners.OnSessionExecuteListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FreeRamController extends BaseController {

    public static final String TAG = "FreeRamController";

    public FreeRamController(Context context) {
        super(context);
    }

    @Override
    public BaseController start() {
        super.start();

        // Compile the regex patterns outside of the menu_main loop (cpu heavy)
        // Mem: 326M Active, 320M Inact, 506M Wired, 7404K Cache, 2744M Free
        // Mem: 385M Active, 255M Inact, 3121M Wired, 142M Free
        final Pattern freePattern = Pattern.compile("^Mem:\\s*.+, ([0-9]+.) Free");


        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                try {

                    getPluginClient().executeCommandOnSession(getSessionId(), getSessionKey(), "top -d1 -n 0  | grep Mem", new OnSessionExecuteListener() {

                        private int buffers = -1;
                        private int free = -1;
                        private int cached = -1;

                        @Override
                        public void onCompleted(int exitCode) {
                            switch(exitCode){
                                case 127:
                                    setText(getString(R.string.error));
                                    Log.d(TAG, "Tried to run a command but the command was not found on the server");
                                    break;
                            }
                        }
                        @Override
                        public void onOutputLine(String line) {
                            Log.v("Memory returns",line);

                            Matcher freeMatcher = freePattern.matcher(line);


                            if(freeMatcher.find()){
                                setText(freeMatcher.group(1));
                            }

                        }

                        @Override
                        public void onError(int error, String reason) {
                            toast(reason);
                        }

                    });
                } catch (ServiceNotConnectedException e){
                    Log.d(TAG, "Tried to execute a command but could not connect to JuiceSSH plugin service");
                }

                if(isRunning()){
                    handler.postDelayed(this, INTERVAL_SECONDS * 1000L);
                }
            }
        });
        return this;
    }

}
