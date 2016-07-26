/**
 * 
 * Created by S.King on 22/07/2016.
 */

export class DisplayOptions {
  constructor(public displayId:number, public stationId:number, public showTemp:boolean, public showHumidity:boolean, public showHeatIndex:boolean) {
  }

  public setDisplayId(value:number):DisplayOptions {
    return new DisplayOptions(value, this.stationId, this.showTemp, this.showHumidity, this.showHeatIndex);
  }

  public setTemp(value:boolean):DisplayOptions {
    return new DisplayOptions(this.displayId, this.stationId, value, this.showHumidity, this.showHeatIndex);
  }
}
