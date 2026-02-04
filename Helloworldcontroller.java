package io.endeavour.stocks.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
public class Helloworldcontroller {
    @GetMapping(value="/hello")
    public String firstmethod(){
        return"Hello World";
    }
    @GetMapping(value = "/helloAdityasir")
public String sayHello(){
        return "Hello Aditya Sir";

}
@GetMapping(value = "/sir/{inputString}") // this becomes a path parameter
public String hellosir(@PathVariable(value = "inputString") String str){
        return str.concat(str);

}
@GetMapping(value = "/concatParam/{inputdate}/{InputString}")
public String concatParameters(@PathVariable(value = "InputString")String str,
                               @PathVariable(value = "inputdate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate somedate) {
    return str + somedate.minusMonths(3);
}
    @GetMapping(value = "/concatquerryParam/{InputString}")
public String queryparm(@PathVariable(value = "InputString")String str,
            @RequestParam(value = "inputdate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate somedate){
        return str + somedate.minusMonths(6);

}
@PostMapping(value = "/sortlist/{inputString}")
public List<String> sortlist(@RequestBody List<String> list,
                             @PathVariable(value = "inputString") String str){
    Collections.sort(list);
    return list;
}
}
