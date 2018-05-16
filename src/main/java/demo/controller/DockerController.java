package demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuwei
 * @date 2018/5/8 17:04
 */
@RestController
@RequestMapping("/test")
public class DockerController {

    @RequestMapping("/index")
    public String index() {
        return "Hello Docker!";
    }
}
