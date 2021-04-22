package com.blueskykong.gateway.enhanced.test;

import com.blueskykong.gateway.enhanced.security.CustomRemoteTokenServices;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {


    public static void main(String[] args) {

       log.info(CustomRemoteTokenServices.getAuthorizationHeader("gateway","gateway"));

    }



}
