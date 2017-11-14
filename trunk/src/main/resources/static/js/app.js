// DOMContentLoaded is fired once the document has been loaded and parsed,
// but without waiting for other external resources to load (css/images/etc)
// That makes the app more responsive and perceived as faster.
// https://developer.mozilla.org/Web/Reference/Events/DOMContentLoaded
window.addEventListener('DOMContentLoaded', function() {

  // We'll ask the browser to use strict code to help us catch errors earlier.
  // https://developer.mozilla.org/Web/JavaScript/Reference/Functions_and_function_scope/Strict_mode
  'use strict';

  var translate = navigator.mozL10n.get;

  // We want to wait until the localisations library has loaded all the strings.
  // So we'll tell it to let us know once it's ready.
  navigator.mozL10n.once(start);

  // ---

  function start() {
    
    // Page initialization 
    window.project = new Project();
    window.project.prepareOnResize();    
 
    // Build elements
    window.project.pageFrame.ready(() => {
      $("#left-menu-list").leftMenu().leftMenu("buildLeftMenu");
      $(".w3-lang > button").langButton().langButton("build");
    });
    // Subscription to the events
    $(window).resize(() => window.project.prepareOnResize());
  }
    
  // Common class
  class Project {

    constructor(){
      this._currentPage = "page1.html";
      this._pageFrame = $("#frame-content");    
      this._lang = "en-US";
    }

    set currentPage(firedMenuItem) {
      
      let firedMenuId = firedMenuItem
        .text()
        .replace(/\s/gi, "_")
        .toUpperCase();
      let firedMenuPage = firedMenuItem.attr("data-page");
      
      if(this._currentPage != firedMenuPage){
        this._currentPage = firedMenuPage;
        this._pageFrame.attr("src", firedMenuPage);
        this.translateContent();
      } 
      
      let firedMenuOp = firedMenuItem.attr("data-rgxOp");
      $(this._pageFrame)
        .contents()
         .find("article")
         .attr("data-rgxOp", firedMenuOp);     
    }

    set lang(lang) {
      this._lang = lang; 
      navigator.mozL10n.language.code = lang;     
      this.translateContent();
    }
    get lang() {return this._lang;}
    
    get pageFrame() {return this._pageFrame;} 
    
    translateContent(){
      // Delay firing translation after elements refreshing  
      window.setTimeout(() => {
        let elements$ = $("*[data-text]");
        elements$.translate();      
        $(this._pageFrame).
          contents().
          find("*[data-text]").
          translate();
        },
        500
      );   
    }

    prepareOnResize(){
      if (window.innerWidth <= 950){
        $(".w3schools-logo").css("position", "relative");
        $(".w3schools-logo, .toptext").css("text-align", "center");
        $(".top").css("padding-bottom", "10px");
        $(".top").css("padding-top", "0px");
        $(".w3-right-top").removeClass("w3-right");       
      }
      else{
        $(".w3schools-logo").css("position", "absolute");
        $(".w3schools-logo, .toptext").removeClass("text-align");
        $(".top").css("padding-bottom", "0px");
        $(".top").css("padding-top", "10px");
        $(".w3-right-top").addClass("w3-right");
      }       
    }
  }
});



