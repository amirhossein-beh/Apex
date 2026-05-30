package com.pedasco.apex.api;


import com.pedasco.apex.service.DeploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/test")
public class testController {

    private final DeploymentService deploymentService;

//    @GetMapping
//    public String testDeployment(){
//        deploymentService.createPilotDeployment(
//                2L,
//                List.of(
//                        UUID.fromString("eb9e349f-a4bd-43ad-a04b-10d3e2ab8bc1")
//                ),
//                "amirhossein"
//        );
//        return "ok";
//    }


}
