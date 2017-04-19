'use strict';

const port = "8080";
const localhost = "127.0.0.1";

// EXTENDING JAVASCRIPT
Object.clone = function (obj) {
  if (null == obj || "object" != typeof obj) return obj;
  var copy = obj.constructor();
  for (var attr in obj) {
    if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
  }
  return copy;
};

Array.prototype.removeClones = function (element) {
  let result = [];
  for (let i = 0; i < this.length; i++) {
    if (result.indexOf(this[i]) == -1){
      result.push(this[i]);
    }
  }
  return result;
};

window.rest = function(options) {
  
  var settings = $.extend({
    accept: "application/json",
    url: "",
    type: "GET",
    params: new Map(),
    onreadystatechange: null,
  }, options || {});
  
  var request = new XMLHttpRequest();      
  request.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      if (typeof settings.onreadystatechange == 'function') {
        settings.onreadystatechange.call(request, this.responseText);
      }
    }  
  };
  
  if (settings.type == "GET") {
    let parameterString = "";
    let count = 1;
    for (let [ key, val ] of settings.params.entries()) {      
      parameterString = key + "=" + val + (settings.params.size < count ? "&" : ""); 
      count++;      
    }
    parameterString = (parameterString == "" ? "" : "?" + parameterString);
    request.open(settings.type, settings.url + parameterString, true);    
  }  
  request.setRequestHeader("Accept", settings.accept);
  request.send();  
  
}

// EXTENDING JQUERY
$.createLink = function(server, query){
  return `http://${server}:${port}${query}`;
};

$.fn.translate = function(){
  var translate = navigator.mozL10n.get;
  // We're using textContent because inserting content from external sources into your page using innerHTML can be dangerous.
  // https://developer.mozilla.org/Web/API/Element.innerHTML#Security_considerations
  let el = this.attr("data-text");
  let textTranslated = translate(el);

  this.text(textTranslated);
  return this;
};

// Applicable only for <select> tags with 'data-path param'
$.fn.loadOptions = function(queryParams,  callback) {
  
  let path = this.attr("data-path");
  if (typeof path != "string")
    return
    
  $.get(path, queryParams, (data, status) => {
    let optionsList = [];
    for (let i = 0; i < data.length; i++){      
      optionsList[i] = `<option>${data[i]}</option>`;
    }
    let optionsLine = optionsList.join("\n");
    this.html(this.html() + optionsLine);
    callback.call(this, this);
  });
  return this;
}

// EXTENDINGS AUTOCOMPLETE 
$.widget( "ui.autocomplete", $.ui.autocomplete, { 
   
  updateSource(val, isInitialize = false) {
    this.options.source = val;
    this._initSource();
      
    if (typeof this.options.source == "object" && this.options.source.length == 1) {      
      this.element.val(this.options.source[0]);      
    }
    else if (isInitialize){
      this.element.trigger("blur");
    }
      
  },
  updateSourceAsLink(source, request_data, isInitialize = false){
    
    if (typeof source != "string"){
      throw new Error("source cannot be other than string!");
    }
    
    let resp = $.get(source, (data, status) => {
      this.updateSource(data, isInitialize);      
    })
    .fail(() => this.updateSource([], isInitialize))
  },
  
  build: function(tip, source, request_data){
   
    this.element.on("focus", {"tip": tip}, this.focus);
    this.element.on("blur", {"tip": tip}, this.blur);   
    
    if (typeof source == "object") {
      this.updateSource(source, true);
    }
    else if (typeof source == "string") {
      this.updateSourceAsLink(source, request_data, true);
    }
    else {
      //"source is not defined!";
      this.element.trigger("blur");
    }
    
    // turn on searching when element is activated    
    this._on( this.element, {      
			focus: function( event ) {
        if (typeof this.source == "function"){
          this._searchTimeout(event);
        }
    }});    
    this.element.keydown(this.keydown);
  },  
  focus: function(event) {
    
    let field$ = $(this);
    if (field$.hasClass("dtTable-field-inactive")){
      field$.val("");
      field$.removeClass("dtTable-field-inactive");
      this.focus();
    }    
  },
  blur: function(event) {
    let field$ = $(this);
    if (field$.val() == ""){
      field$.addClass("dtTable-field-inactive");
      field$.val(event.data.tip);
    }
  },
  _change: function(event){
    this._super();
    this.element.trigger("change");
  },
  inputMask: function(rgx) {
    this._inputMask = rgx;
  },
  keydown: function() {
    let t1= "";
  }
});

// WIDGET FOR LEFT MENU PANEL

$.widget( "custom.leftMenu", $.ui.menu, { 
  
  buildLeftMenu: function(){
    
    // styles
    this.element.css({
      "background": "transparent",
      "color": "rgb(255,255,255)",
      "border": "0px",
      "font-size": "17px",
      "font-family": "Arial",
    });   
    
    // setting icons and events for left menu
    $("<img>", {
      src: "img/theme/content.png",    
    }).
    addClass("left-menu-icon").
    prependTo(this.element.find( ".ui-menu-item-wrapper")
      .addClass("left-menu-item").
       on("click", this.onClickItem) 
    ); 
    
    this.activateMenuItem($("div#ui-id-1"));
    
    return this;
  },
  
  focus: function( event, item ){
    let instance = this._super(event, item);
    // changing focused color
    this.focused = this.active.children( ".ui-menu-item-wrapper" );
          			
    this._removeClass(this.focused, null, "ui-state-active" );
		this._addClass(this.focused, null, "left-menu-state-active" );
    return instance;  
  },
  
  blur: function( event, fromFocus ){
   if ( !fromFocus ) {
			clearTimeout( this.timer );
		}

		if ( !this.active ) {
			return;
		}

		this._removeClass( this.active.children( ".ui-menu-item-wrapper" ),
			null, "left-menu-state-active" );

		this._trigger( "blur", event, { item: this.active } );
		this.active = null;
  },
  
  onClickItem: function(event){

    $( "#left-menu-list" ).leftMenu().leftMenu("activateMenuItem", this);
    
  },
  
  activateMenuItem: function(element){
       
    $(element).    
      addClass("left-menu-fired").
      parent(".ui-menu-item").
      siblings("li").
      find(".ui-menu-item-wrapper").
      removeClass("left-menu-fired");
    
    window.project.currentPage = $(element).parents(".ui-menu-item");
  }, 
  firedMenuItem: function() {
    let firedDiv$ = this.element.find(".left-menu-fired");
    return firedDiv$.parent();
  },
});

// WIDGET FOR LANG BUTTONS

$.widget( "custom.langButton", $.ui.button, { 
  build: function(){
    this.element.css("padding", "0px");
    this.element.css("width", "50px");
    this.element.css("font-size", "0.8em");
    
    this.element.click(() => {
      this.activate();
    });    
    
    if (window.project.lang == this.element.text()){
      this.activate();    
    }
    
    return this;
  },
  activate: function(){
    
    window.project.lang = this.element.text();   
    
    this.element.
      css({"color": "yellow"}).
      siblings("button").
      css({"color": ""});    
  }
});

// WIDGET FOR PARAGRAPH TITLE

$.widget( "custom.pageParagraph", { 
  
  build: function(){
    
    let caption$ = $(this.element).find("div.paragraph-caption");
       
    let toolTip = $(caption$)
    .find("span.paragraph-tooltip");    
    
    toolTip
    .attr("title", toolTip.text())
    .tooltip()
    .text("?");
          
    caption$
    .hover(
      function(event) {
        $(this).addClass("paragraph-hover");
      },
      function(event) {
        $(this).removeClass("paragraph-hover");
      }
    ) 
    .click(
      function(event) {
        
        $(this).toggleClass("paragraph-fired");

        let thisModule$ = $(this)
        .closest("div.paragraph-module");
                
        thisModule$        
        .find("div.paragraph-text")
        .toggle(300);               
        
        thisModule$
        .siblings()
        .find("div.paragraph-text")
        .hide(300)
        
        thisModule$
        .siblings()
        .find("div.paragraph-caption")
        .removeClass("paragraph-fired");
        
      }
    );
    
    let text$ = $(this.element).find("div.paragraph-text");
    text$.hide();                  
  }
});

// WIDGET FOR TABLE

$.widget( "custom.dtTable", { 
  
  _col_options: {
    id:"", 
    width:"", 
    title:"", 
    source: [],
    index: 0,
    request_data: undefined,
  },
  
  collumns: [],
  rows: [],
  
  _size: 0, 
  _divShell$: undefined,
  
  build: function(...collumns){
    
    for(let i = 0; i < collumns.length; i++){
      collumns[i].index = i;
      this.collumns[i] = $.extend(Object.clone(this._col_options), collumns[i]);      
    }
    
    this._divShell$ =  this.element.wrap("<div>").closest("div");
    
    this.addCommand("Add row", (event) => {
      event.preventDefault();
      this.addRow();
    });
    this.addCommand("Remove row", (event) => {
      event.preventDefault();
      if (this._size == 1)
        return;
      this.removeRow();      
    });
        
    // Building table structure
    this.element.addClass("dTable");    
    this.element.append(`<colgroup>`).append(`<thead>`).append(`<tbody>`);    
    $(this.element).find(`thead`).append(`<tr>`).addClass(`dtTable-heading`);
        
    this.addColl("", "10px");    
    this.collumns.forEach(col => 
    {
      this.addColl(col.id, col.width)
    });
    
    this.addRow();
  },  
  addCommand: function(name, event){
    $("<a>")
    .click(event)
    .appendTo(this._divShell$)
    .refButton(name);    
  },  
  addColl: function(name, width) {
    
    $(this.element)
    .find("colgroup")
    .append(`<col style="width:${width}">`)
    
    $(`<th class="dtTable-headCell">${name}</th>`)
    .appendTo($(this.element).find("thead tr"));
  },  
  addRow: function (){
     
    this._size++;    
    
    let tr$ = $("<tr>")
    .appendTo($(this.element).find("tbody"));
     
    let currRow = {};
    
    // row number
    $(`<td class="dtTable-cell-index">${this._size}</td>`)
    .appendTo(tr$);
    
    // user collumns
    this.collumns.forEach(col => {
      let cell_element = $(`<td class="dtTable-cell"></td>`)      
      .appendTo(tr$)
      .append(`<span>`)
      .children()
      .append( () => { 
        let input_cell = $("<input>", {
          class: "dtTable-field",
          id: col.id,
          title: col.title,
          "data-rowindex": this._size-1})          
         .autocomplete({          
            minLength: 0,
            options: {minLength: 0},
         })
        .autocomplete("build", col.title, col.source, col.request_data);
        currRow[col.id] = input_cell;
        return input_cell;      
      })});
    this.rows.push(currRow);
    return currRow;
  },  
  removeRow: function() {
    
    this._size--;
    this.rows.pop();
    
    $(this.element).find("tBody tr:last").remove();
  },  
  removeAll: function() {    
    let initSize = this._size;
    for (let i = 0; i < initSize; i++) {
      this.removeRow();
    }
  },  
  currRow: function() {
    let active_cell = this.element.find( ":focus" );        
    let curr_row = this.rows[active_cell.attr("data-rowindex")];
    return curr_row;
  },  
  getRowByCell: function(cell){
    let rowindex = cell.attributes["data-rowindex"].value;
    return this.rows[rowindex];
  },
  getRowByIndex: function(index){
      return this.rows[index];
  },
  size: function() {
    return this._size;
  },
  addListener: function(collumnName, eventName, handler){
    this.element.on(eventName, `#${collumnName}` , handler);
  },
  values: function(col){
    let result = []; 
    $(`[id='${col}']`, this.element)
      .filter(":not([class~='dtTable-field-inactive'])")
      .each(function() {
        result.push(this.value);
       }
    );       
    return result;
  }
});   

// WIDGET FOR REF BUTTON

$.fn.refButton = function(name, width) {  
  this.attr("href", "#").attr("class", "dtTable-button").html(name);  
  if (width) {
    this.css({width: width});
  };
  return this;
}


// WIDGET FOR DROP DOWN MENU

$.fn.dropdownMenu = function(path, width, select_callback) {
  
  let divMenu$ = this.wrap('<div class="dropdown">').parent();
    
  $.get(path, (data, status) => {
    
    let optionsList = [];
    optionsList.push(`<ul style="width:${width};" id="menu">`);
    for (let i = 0; i < data.length; i++){
      optionsList.push(`<li><div>${data[i]}</div></li>`);
    }
    optionsList.push("</ul>")
    let optionsLine = optionsList.join("\n");
    
    $(optionsLine)
      .appendTo(divMenu$)
      .menu({select: select_callback})      
      .addClass("dropdown-content")
      .css({display: "none"});  
  });
  return this;
};


// WIDGET FOR EVENT FILTER MENU

$.fn.eventFilter = function() {
  
  // public members
  this.each(function() {
    let outer = this;
    this.eventData = {
      get eventType() {return $("#eventButton", outer).text();},
      
    };    
  });
  
  // private members
  let filterCount = 0;
  
  let clearFilters = function() {
    $(this).find(".filterItem").remove();
    buildDefault();
  };

  let buildDefault = function() {
    addFilter.trigger("click", "where");
    addFilter.trigger("click", "groupby");
    addFilter.trigger("click", "orderby");    
  };
  
  let changeFilterType = function(event) {
    let filterType = $(':selected', this).attr('data-filter-type');
    let filterItem = $(this).closest('.filterItem');
    $('.qualifier',filterItem).remove();
    let filterVariant = $('div.template.'+filterType)
    .children().clone().addClass('qualifier')
    .appendTo(filterItem);

    let _eventName = $(this).closest(".event-filter-block")[0].eventData.eventType;
    
    // property menu
    filterVariant
      .filter('select[name="selectorMatchType"]')         
      .selectmenu({
      width: "10em",
      change: function(){
        $(this).siblings('input[name="term"]').autocomplete("inputMask");      
      }})
      .loadOptions({event: _eventName}, (this$) => {this$.selectmenu().selectmenu("refresh");});
            
    // equality matcher menu
    filterVariant
      .filter('select[name="stringMatchType"]')
      .selectmenu({
      width: "8em",
      change: function(){alert("ok");}});

    // term autocomplete menu
    filterVariant
      .filter('input[name="term"]')
      .autocomplete({
      width: "8em",
      change: function(){let t1 = "";}})
      .autocomplete("build", "input value")
      .addClass("standart-input");

    $('option[value=""]', this).remove();
    
    $(this).selectmenu().selectmenu("refresh");
  };
  
  // constructor's body
  
  let filterForm$ = $(".templates #filtersForm").clone().appendTo(this);
  
  filterForm$
    .find("#eventButton")
    .refButton("ANY", "85px")
    .click(function(event) { event.preventDefault();})
    .dropdownMenu("/eventTypes", "85px", (event) => {
      event.preventDefault();
      filterForm$.find("#eventButton").text($(event.currentTarget).text());
      
      clearFilters.call(filterForm$);      
    }
  );
  
  let addFilter = filterForm$.find("#addFilterButton").button({
    icon: "ui-icon-plusthick",
    showLabel: true
  });
  
  addFilter.click( (event, defName) => {
    
    let filterItem = $('<div>')
    .addClass('filterItem')
    .appendTo(this.find('#filterPane'))    
    .data('suffix','.' + (filterCount++));    
    
    $('div.template.filterChooser')
    .children().clone().appendTo(filterItem);      

    let filterChooser = filterItem.find(".filterChooser").selectmenu(
      {width: "13em",
       change: changeFilterType,
      }
    );    
    
    if (defName) {
      $('option[value=""]', filterChooser).remove();
      $(`option[value="${defName}"]`, filterChooser).attr("selected", "selected");
      filterChooser.selectmenu("refresh");
      changeFilterType.call(filterChooser[0]);
    }  
    
    filterItem.find(".filterRemover").button({
      icon: "ui-icon-closethick",
      showLabel: false
    })
    .on('click',function(){
      $(this).closest('div.filterItem').remove();
    });    
  });
  
  buildDefault();

};

// WIDGET FOR DIALOG

$.modalDialog = function(options) {
  
  var settings = $.extend ({
    title: "Default title",
    inputName: "default input",
    inputMenu: ["default item"],
    tip: "input value",
    click_ok: null,    
  }, options || {});    
  
  var element = $('<div id="modalDialog" title=""></div>');
  
  element
  .attr("title", settings.title)
  .append("<p>")  
  .html(`${settings.inputName}:<input id='dialogInput1'/>`)
  
  element.find("#dialogInput1")  
  .autocomplete({          
    minLength: 0,
    options: {minLength: 0}})
  .autocomplete("build", settings.tip, settings.inputMenu);
    
  var dialog = element.dialog({
	autoOpen: false,
	width: 330,
	buttons: [
		{
			text: "Ok",
			click: function(event) {
        event.preventDefault();
        let this$ = $(this);
        let val = this$.find("#dialogInput1").val();
        if (settings.click_ok != null) {
          settings.click_ok.call(window, event, val);
        }
        dialog.dialog("close");
			}
		},
		{
			text: "Cancel",
			click: function(event) {
        event.preventDefault();
				$( this ).dialog( "close" );
			}
		}
	]})  
  dialog.dialog( "open" ); 
};

// WIDGET FOR DATE RANGE SET
$.fn.dateRangeSet = function(functionName) {

  let func = $.fn.dateRangeSet[functionName];  
  if (typeof func == 'function' ) {
    return func.call(this);
  }  
  
  var this$ = this;
  this.css("display", "ruby");
  
  var dateRange = function(range) {
    // START & END DATE FIELDS
   var dateRangesInput$ = this$.find("div.dateRangesInput");
   var dateRange1 = this$.find('input[name="dateRange1"]');
   var dateRange2 = this$.find('input[name="dateRange2"]');
  
   if (range == "SET_OWN") {
     dateRangesInput$.removeClass("forbidden");
     dateRange1.removeAttr("disabled");
     dateRange2.removeAttr("disabled");
   }
    else {
      dateRangesInput$.addClass("forbidden");
      dateRange1.attr("disabled", "true");
      dateRange2.attr("disabled", "true"); 
      
      let params = new Map();
      params.set("dateRange", range);
      window.rest({
        url: `/startDateByDateRange`,
        params: params,
        onreadystatechange: function(data) {
         dateRange1.val(data);
      }});       
      window.rest({
        url: `/endDateByDateRange`,
        params: params,
        onreadystatechange: function(data) {
          dateRange2.val(data);
      }});  
    }    
  }
  
  $("<span>Period filter:</span>")
    .addClass("ui-widget")
    .addClass("betweenSpace")
    .css("vertical-align", "middle")
    .appendTo(this$);

  // date range select
  $('div.template.dateRangeMatcher')
    .children()
    .clone()
    .appendTo(this$)
    .filter('select[name="selectorMatchType"]')         
    .selectmenu({
    width: "10em",
    change: function(element){
      dateRange(element.currentTarget.textContent);
    }})
    .loadOptions(null, (this$) => {
      dateRange(this$.val());
      this$.selectmenu().selectmenu("refresh");
    }) 
    .next()
    .css("margin-right", "10px");

  // start date select
  $('div.template.dateRange')
    .children()
    .clone()
    .appendTo(this$);   
  
  this$.find('div[name="dateRange1"]').inputField({tip : "yyyy.mm.dd"});
  this$.find('div[name="dateRange2"]').inputField({tip : "yyyy.mm.dd"});
  
  $.fn.dateRangeSet.getDateRange = function() {
    return $("select :selected", this$).val();
  };
  
  $.fn.dateRangeSet.getUserPeriod = function() {    
    let startDate = this$.find('input[name="dateRange1"]').val();
    let endDate = this$.find('input[name="dateRange2"]').val();
    return [startDate, endDate];    
  };
  
  return this$;
  
};

$.fn.inputField = function(options) {
    
  var settings = $.extend({
    tip: "input value",
    label: null,
  }, options || {});
  
  let this$ = this;
  
  this$
  .addClass("standartPadding");
    
  let input$ = $('<input>')
  .attr("type", "text")
  .addClass("betweenSpace")
  .addClass("standart-input")  
  .addClass("dtTable-field-inactive")
  .appendTo(this$);
  
  input$.on("focus", {"tip" : settings.tip}, function(event) {  
    if (input$.hasClass("dtTable-field-inactive")){
      this$.val("");
      this$.removeClass("dtTable-field-inactive");
      this$.trigger("focus");
    }    
  });
  
  if (settings.label != null) {
    $(`<span>${settings.label}</span>`)
      .prependTo(this$);    
  }
  
  this$.on("blur",{"tip" : settings.tip}, function(event) {
    if (this$.val() == ""){
      this$.addClass("dtTable-field-inactive");
      this$.val(event.data.tip);
    }
  });
  
  this$.trigger("blur");
  
  return this$;
  
}







