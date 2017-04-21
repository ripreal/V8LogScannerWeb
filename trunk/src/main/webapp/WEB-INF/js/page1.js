'use strict'

$(window).ready(() => {
  window.formData = function formData() {
    return {
      get logPathsHib() { 
        return $("#LogPathsTable").dtTable("getValues");
      },
      set logPathsHib(val) {
        $("#LogPathsTable").dtTable("setValues", val);
      },      
      get rgxList() { 
        let rgxList = [];
        $(".event-filter-block").each(function(){
          rgxList.push(this.eventData.eventType);
        });    
      return rgxList;
      },
      set rgxList(val) {},
      get rgxOp() { return $("article").attr("data-rgxOp");},
      set rgxOp(val) {},
      get dateRange() {return $("#LogDateRange").dateRangeSet("getDateRange");},
      set dateRange(val){},
      get userPeriod() {return $("#LogDateRange").dateRangeSet("getUserPeriod");},
      set userPeriod(val) {},
      get limit() {return $("#LogLimit > input").val()},
      set limit(val){},
    };
  };
  
  // PARAGRAPHS
  $("[id^=Paragraph]").pageParagraph().pageParagraph("build");
  $("#Paragraph1 .paragraph-caption").trigger("click");
 
  // 1. SET LOG LOCATIONS
  $("#LogDateRange").dateRangeSet();
  $("#LogLimit").inputField({tip:"100", label: "Log level limit"});
  // LOG TABLE
  let logPathsTable$ = $("#LogPathsTable").dtTable();  
  logPathsTable$.dtTable("build", 
    {
      id: "server", 
      width: "200px", 
      title: "IP or computer name", 
      source: [localhost]
    }, 
    {
      id: "path", 
      width: "500px", 
      title: "directory with *.log files or path to scan",
      source: $.createLink(localhost, "/scanLogPaths"),
    }
  )
  .dtTable("addListener", "server", "change", function( event ){
    let currRow = logPathsTable$.dtTable("getRowByCell", event.target);    
    $(currRow.Path).autocomplete("updateSourceAsLink", $.createLink(currRow.server.val(), "/scanLogPaths"),{});    
  })
  .dtTable("addCommand", "Add All logs from cfg file", () => {	
    $.modalDialog({
      title: "Adding ogs from *.logcfg file",
      inputName: "server",
      inputMenu: ["127.0.0.1"],
      click_ok: function(event, value) {
        event.preventDefault();
        let this$ = $(this);
        let inputServer = value;
        logPathsTable$.dtTable("removeAll");
        $.get($.createLink(inputServer, "/scanLogPaths"), (data, status) => {
          for (let i = 0; i < data.length; i++){
            let currRow = logPathsTable$.dtTable("addRow", i);
            currRow.server.val(inputServer);
            currRow.path.val(data[i]);                       
          };	
        })
        .always(() => this$.dialog("close"));        
			}       
    });
  });

  // 2. BUTTONS
  
  $( "#AddEvent" ).button({
    icon: "ui-icon-plusthick",
    showLabel: true
  })
  .addClass("positiveButton")
  .click(() => $.fn.eventFilter("addEvent"))
  .trigger("click");
  
  $( "#RemoveEvent" ).button({
    icon: "ui-icon-closethick",
    showLabel: true
  })
  .addClass("alertButton")
  .click( function(event){
    $(".event-filter-block:last").remove();    
  });  
  
  //   
  $( "#SaveProfile" ).button({
    icon: "ui-icon-caret-1-n",
    showLabel: true
  })
  .click( function(event){
    $.modalDialog({
      title: "Saving current profile",
      inputName: "profile name",
      inputMenu: ["my profile1"],
      click_ok: function(event, profileName){
        profile.fill(profileName, this.formData());
      }        
    });     
  })
  .find("span.ui-icon")  
  .css({"background-image": "url(\"/img/icons/save.png\")"});
  
  $( "#LoadProfile" ).button({
    icon: "ui-icon-caret-1-n",
    showLabel: true
  })
  .click( function(event){
    $.modalDialog();     
  })
  .find("span.ui-icon")  
  .css({"background-image": "url(\"/img/icons/open.png\")"});
  
  $( "#ResetAll" ).button({
    showLabel: true
  })
  .click( function(event){
    $.modalDialog();     
  })
  
  // 3. START
  
  $("#StartOp")
    .button()
    .addClass("positiveButton")
    .click((event) => {
      let attrs = getAttributesForServer();
      $.ajax({
        url: "/startRgxOp",
        type: "POST",
        dataType: "json",
        data: {"eventType": "ANY", "id":"2342423",},
        contentType: 'application/json; charset=UTF-8', // This is the money shot
        sucess: function(data, status) {
          let t1 = "";}
      })
      .fail(function() {
        let t1 = "";
      });       
    });   
  
  $( "#progressbar" ).progressbar({
    value: 20
  })
  .css("height", "27px");  
    
  let test$ = $("#testButton");
  test$.click(function()  {
    let profile = new ScanProfile();
    profile.id = 2;
    let t2 = JSON.stringify({"id": 345});
    $.ajax({
      url: "/setProfile",
      data: profile,
      contentType: "application/json", 
      complete: function(xhr, status) { 
        alert("ok!");
      },
      method: "POST"
    });
    
  }); 
  
  window.rest({
    url: "/getProfile", 
    onreadystatechange(data) { 
      let t1 = "";
      window.profile = ScanProfile.create(data, window.formData());
  }});
  
});




















