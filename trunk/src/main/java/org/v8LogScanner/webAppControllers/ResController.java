package org.v8LogScanner.webAppControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ResController {
  //////////////
  // CSS FILES 
  //////////////
  
  @RequestMapping(value="/css/{cssName:.+}.css", method = RequestMethod.GET)
  public String appCSS(@PathVariable String cssName){
    return "/css/" + cssName + ".css";
  }

  //////////////
  // JS FILES 
  //////////////
  @RequestMapping(value="/js/libs/jquery-3.1.1.js", method = RequestMethod.GET)
  public String jqueryJS(){
    return "/js/libs/jquery-3.1.1.js";
  }
  
  @RequestMapping(value="/js/libs/jquery-ui.js", method = RequestMethod.GET)
  public String jqueryUIJS(){
    return "/js/libs/jquery-ui.js";
  }
  
  @RequestMapping(value="/js/app.js", method = RequestMethod.GET)
  public String appJS(){
    return "/js/app.js";
  }
  
  @RequestMapping(value="/js/app_ctrls.js", method = RequestMethod.GET)
  public String appCtrlsJS(){
    return "/js/app_ctrls.js";
  }
  
  @RequestMapping(value="/js/libs/l10n.js", method = RequestMethod.GET)
  public String appl10nJS(){
    return "/js/libs/l10n.js";
  }
  
  @RequestMapping(value="/js/page1.js", method = RequestMethod.GET)
  public String page1JS(){
    return "/js/page1.js";
  }
  //////////////
  // IMG FILES 
  //////////////
  
  @RequestMapping(value="/img/theme/{filename:.+}", method = RequestMethod.GET)
  public String themeImg(@PathVariable String filename){
    return "/img/theme/" + filename;
  }
  
  @RequestMapping(value="/css/images/{filename:.+}", method = RequestMethod.GET)
  public String cssImg(@PathVariable String filename){
    return "/css/images/" + filename;
  }

  //////////////
  // OTHER FILES 
  //////////////
  @RequestMapping(value="/data/{filename:.+}", method = RequestMethod.GET)
  public String locales(@PathVariable String filename){
    return "/data/" + filename;
  }
}
