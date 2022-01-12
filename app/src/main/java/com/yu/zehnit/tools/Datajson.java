package com.yu.zehnit.tools;

import androidx.annotation.NonNull;

public class Datajson {
    private Float pitch;
    private Float yaw;
    private Float roll;

    public Float getPitch() {
        return pitch;
    }

    public void setPitch(Float pitch) {
        this.pitch = pitch;
    }

    public Float getYaw() {
        return yaw;
    }

    public void setYaw(Float yaw) {
        this.yaw = yaw;
    }

    public Float getRoll() {
        return roll;
    }

    public void setRoll(Float roll) {
        this.roll = roll;
    }

    @NonNull
    @Override
    public String toString() {
        return "pitch:"+pitch+"yaw:"+yaw+"roll:"+roll;
    }
}
