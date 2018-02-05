package com.amex.tp.common;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.saucerest.SauceREST;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class TestSupport {

    @AfterMethod(alwaysRun = true)
    public static void afterTest (ITestResult testResult) {
        System.out.println("afterTest is running");

        String username = System.getenv("SAUCE_USER_NAME") != null ? System.getenv("SAUCE_USER_NAME") : System.getenv("SAUCE_USERNAME");
        String accesskey = System.getenv("SAUCE_API_KEY") != null ? System.getenv("SAUCE_API_KEY") : System.getenv("SAUCE_ACCESS_KEY");

        if(username != null && accesskey != null) {

            SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

            System.out.println(testResult.getInstanceName());
            SauceOnDemandSessionIdProvider sessionIdProvider = (SauceOnDemandSessionIdProvider) testResult.getInstance();
            String sessionId = sessionIdProvider.getSessionId();

            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("passed", testResult.isSuccess());

            SauceREST sauceREST = new SauceREST(authentication.getUsername(), authentication.getAccessKey());
            sauceREST.updateJobInfo(sessionId, updates);
        }
    }
}
