package com.blueskykong.gateway.enhanced;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@Slf4j
@SpringBootApplication
@RestController
public class GatewayOwnApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayOwnApplication.class, args);
    }


    @RequestMapping("/error")
    public List<String> error() {
        log.info("=============================");
        return Arrays.asList("sorry, something went wrong.");
    }

/*    @RequestMapping(value = "/fallbackcontroller")
    public Map<String, String> fallBackController() {
        Map<String, String> res = new HashMap();
        res.put("code", "-100");
        res.put("data", "service not available");
        return res;
    }*/
}
