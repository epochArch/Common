package com.epocharch.common.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by archer on 07/03/2017.
 */
public enum DeployLevel {

    ZONE("zone"),IDC("idc"),SITE("site");

    private String code;

    private DeployLevel(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DeployLevel getByCode(String c) {
        DeployLevel level = DeployLevel.ZONE;
        if(!StringUtils.isBlank(c)){
            for (DeployLevel d : DeployLevel.values()) {
                if (c.equalsIgnoreCase(d.getCode())) {
                    level = d;
                    break;
                }
            }
        }
        return level;
    }
}
