package com.szy.controller;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wqc")
@DS("es_database")
public class WaterQualityCollectorController extends BaseController{

}
