
$(window).ready(() => {
  
  function getAttributesForServer() {
    return {
      get logPaths() { return $("#LogPathsTable").dtTable("values", "Path");},
      get servers() { return $("#LogPathsTable").dtTable("values", "Server").removeClones();},
      get rgxList() { 
        let rgxList = [];
        $(".event-filter-block").each(function(){
          rgxList.push(this.eventData.eventType);
        });    
      return rgxList;
      }
    };
  };
  
  // PARAGRAPHS
  $("[id^=Paragraph]").pageParagraph().pageParagraph("build");
  $("#Paragraph3 .paragraph-caption").trigger("click");
 
  // 1. SET LOG LOCATIONS
  // LOG TABLE
  let logPathsTable$ = $("#LogPathsTable").dtTable();  
  logPathsTable$.dtTable("build", 
    {
      id: "Server", 
      width: "200px", 
      title: "IP or computer name", 
      source: [localhost]
    }, 
    {
      id: "Path", 
      width: "500px", 
      title: "directory with *.log files or path to scan",
      source: $.createLink(localhost, "/scanLogPaths"),
    }
  )
  .dtTable("addListener", "Server", "change", function( event ){
    let currRow = logPathsTable$.dtTable("getRowByCell", event.target);    
    $(currRow.Path).autocomplete("updateSourceAsLink", $.createLink(currRow.Server.val(), "/scanLogPaths"),{});    
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
            currRow.Server.val(inputServer);
            currRow.Path.val(data[i]);                       
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
  .click( function(event){
    let divFilter$ = $("<div>", {
      class: "event-filter-block"   
    })
    .appendTo($(this).parents(".paragraph-text"));  
    divFilter$.eventFilter();
  })
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
      click_ok: function(){alert("ok");}        
    });     
  })
  .find("span.ui-icon")  
  .css({"background-image": "url(\"/img/icons/save.png\")"});
  
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
    
  test$ = $("#testButton");
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
});




















