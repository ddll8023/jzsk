package com.szy.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/riverc")
@DS("collector")
public class RiverCollectorController extends BaseController{
}
