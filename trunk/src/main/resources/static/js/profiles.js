'use strict'

class ScanProfile {
    
  constructor () {
    this.id          = 0;
    this.name        = "";
    this.dateRange   = "ANY";
    this.limit        = 20;
    this.logType     = "ANY";
    this.sortingProp = "ANY";
    this.groupType   = "BY_PROPS";
    this.rgxList     = [];
    this.rgxExp      = "";
    this.rgxOp       = "CURSOR_OP";
    this.userPeriod  = ["", ""];
    this.logPaths    = [];
  }
  
  // UTIL FUNCTIONS
  
  static fill(profile, profileName, formData) {
    profile.name = profileName;
    for (let prop in formData) {
      profile[prop] = formData[prop];
    }
  }
  
  static create(jsonData, formData ) {     
    let profile = JSON.parse(jsonData);
    let prototypeProfile = new ScanProfile;
    Object.setPrototypeOf(profile, prototypeProfile);
    for (let prop in profile) {
      formData[prop] = profile[prop];
    }   
    return profile;
  }
}



