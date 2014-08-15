package com.terrencewatson;

import com.terrencewatson.services.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by twatson on 8/15/14.
 */

@Controller
@RequestMapping("/import")
public class ImportController {

    @Autowired
    ImportService importService;


    public void importFromUrl(@RequestParam String url){

    }
}
