package org.v8LogScanner.webAppControllers;

public class TestProfile implements ITestProfile{
  private int id = 0;

  @Override
  public void setId(int id) {
    this.id = id;
    
  }

  @Override
  public int getId() {
    // TODO Auto-generated method stub
    return id;
  }
  
}
