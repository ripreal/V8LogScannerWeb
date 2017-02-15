package org.v8LogScanner.webAppControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PagesController {

  @RequestMapping(value="/index")
  public String index(){
    return "index.html";
  }
  
  @RequestMapping(value="/")
  public String home(){
    return "index.html";
  }
  
  @RequestMapping(value="/page1")
  public String page1(){
    return "page1.html";
  }
    
}
