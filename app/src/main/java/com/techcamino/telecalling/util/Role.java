package com.techcamino.telecalling.util;

public class Role {

    public static String getRole(int flags) throws Exception{

        switch (flags){
            case 1:
                return  Constants.SUPER_ADMIN;
            case 2:
                return Constants.ADMIN;
            case 3:
                return Constants.USER_MEMBER;
        }
        return null;
    }

}
