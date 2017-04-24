'use strict'

class ScanProfile {
    
  constructor () {
    this._id          = 0;
    this._name        = "";
    this._dateRange   = "ANY";
    this._limit        = 20;
    this._logType     = "ANY";
    this._sortingProp = "ANY";
    this._groupType   = "BY_PROPS";
    this._rgxList     = [];
    this._rgxExp      = "";
    this._rgxOp       = "CURSOR_OP";
    this._userPeriod  = ["", ""];
    this._logPaths    = [];
    this._logPathsHib = [];
  }
  
  set id (id) { this._id = id;}
  get id () {return this._id;}
  
  set name(name) {this._name = name;}
  get name() {return this._name;}
  
  set dateRange(dateRange) {this._dateRange = dateRange}
  get dateRange() {return this._dateRange}
  
  set limit(limit) {this._limit = limit;}
  get limit() {return this._limit;}
  
  set logType(logType) {this._logType = logType;}
  get logType() {return this._logType;}
  
  set sortingProp(sortingProp) {this._sortingProp = sortingProp;}
  get sortingProp() {return this._sortingProp;}
  
  set groupType(groupType) {this._groupType = groupType;}
  get groupType() {return this._groupType;}
  
  set rgxList(rgxList) {this._rgxList = rgxList;}
  get rgxList() {return this._rgxList;}
  
  set rgxExp(rgxExp) {this._rgxExp = rgxExp;}
  get rgxExp() {return this._rgxExp;}
  
  set rgxOp(rgxOp) {this._rgxOp = rgxOp;}
  get rgxOp() {return this._rgxOp;}
  
  set userPeriod(userPeriod) {this._userPeriod = userPeriod;}
  get userPeriod() {return this._userPeriod}
  
  set logPaths(logPaths) {this._logPaths = logPaths;}
  get logPaths() {return this._logPaths;} 
  
  set logPathsHib(logPathsHib) {this._logPathsHib = logPathsHib;} 
  get logPathsHib() {return this._logPathsHib;} 
  
  // UTIL FUNCTIONS
  fillProfile(profileName, formData) {
    this.name         = profileName;
    for (let formProp in formData) {
      this[formProp] = formData;
    }
  }
  
  static create(jsonData, formData ) {     
    let profile = JSON.parse(jsonData);
    profile.prototype = new ScanProfile();
    for (let prop in profile) {
      formData[prop] = profile[prop];
    }    
  }
  
}



