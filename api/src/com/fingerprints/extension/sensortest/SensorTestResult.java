/**
 * Copyright (c) 2017 Fingerprint Cards AB <tech@fingerprints.com>
 * All rights are reserved.
 * Proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Any use is subject to an appropriate license granted by Fingerprint Cards AB.
 */

package com.fingerprints.extension.sensortest;

import com.fingerprints.extension.util.ArrayUtils;
import com.fingerprints.extension.util.FpcError;
import com.fingerprints.extension.util.Logger;

public class SensorTestResult {
    private Logger mLogger = new Logger(getClass().getSimpleName());

    public enum ResultCode {
        PASS(0, "PASS"),
        FAIL(1, "FAIL"),
        CANCELLED(2, "CANCELLED"),
        NOT_SUPPORTED(3, "NOT SUPPORTED"),
        ERROR(4, "ERROR");

        private int mValue;
        private String mString;

        private ResultCode(int value, String string) {
            mValue = value;
            mString = string;
        }

        public int getValue() {
            return mValue;
        }

        public String getString() {
            return mString;
        }

        public static ResultCode fromInt(int i) {
            for (ResultCode r : values()) {
                if (r.getValue() == i) {
                    return r;
                }
            }
            return ERROR;
        }
    }

    public FpcError FpcSdkErrorCode;
    public ResultCode resultCode;
    public String resultString;
    public int errorCode;
    public String errorString;
    public boolean imageFetched;
    public byte[] image;
    public String log;

    public SensorTestResult(ResultCode resultCode) {
        this(resultCode, "", 0, "");
    }

    public SensorTestResult(com.fingerprints.extension.V1_0.SensorTestResult result) {
        this(ResultCode.fromInt(result.resultCode),
                result.resultString,
                result.errorCode,
                result.errorString);
        if (result.imageData != null && result.imageData.size() > 0) {
            this.imageFetched = true;
            this.image = ArrayUtils.toByteArray(result.imageData);
        }
        mLogger.d("SensorTestResult, log = " + result.log == null ? "null" : "not null len=" + result.log.length());
        this.log = result.log;
    }

    public SensorTestResult(ResultCode resultCode, String resultString, int errorCode, String errorString) {
        mLogger.enter("SensorTestResult");
        this.resultCode = resultCode;
        this.resultString = resultString;
        this.errorCode = errorCode;
        this.errorString = errorString;
        this.FpcSdkErrorCode = new FpcError(errorCode);
        mLogger.exit("SensorTestResult");
    }

    public String getErrorCode() {
        return this.FpcSdkErrorCode.getExternalErrorCode();
    }

    public String getModuleErrorCode() {
        return this.FpcSdkErrorCode.getModuleInternalError();
    }

    public String getErrorCodeString() {
        return this.FpcSdkErrorCode.toString();
    }
}
