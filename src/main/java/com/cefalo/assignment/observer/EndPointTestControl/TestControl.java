package com.cefalo.assignment.observer.EndPointTestControl;

import com.cefalo.assignment.observer.endpoints.Feature;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("endpoint")
public class TestControl {

    @Autowired
    SimpleMeterRegistry compositeMeterRegistry;

    private Log log = LogFactory.getLog(TestControl.class);

    /**
     * to change log level of a logger send post request to http://localhost:9001/actuator/loggers/[loggerfulename]
     * with body
     * {"configuredLevel":"TRACE","effectiveLevel":"INFO"}
     * @return
     */
    @GetMapping("log")
    public String log() {
        log.trace("This is a TRACE level message");
        log.debug("This is a DEBUG level message");
        log.info("This is an INFO level message");
        log.warn("This is a WARN level message");
        log.error("This is an ERROR level message");
        return "See the log for details";
    }

    @GetMapping(value = "boo")
    public Double myName() {

        Counter pong = Counter.builder("is.it.working")
                .tag("no", "none")
                .register(compositeMeterRegistry);

        pong.increment();

        return pong.count();
    }
}
