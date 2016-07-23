import {Injectable} from '@angular/core';
import {Option} from "./models/Option.ts";
import {DisplayOptions} from "./models/DisplayOptions.ts";

@Injectable()
export class Configuration {
  public applicationName:string = "Domotics";
  public Server:string = "http://localhost:8080";
  public BaseApiUrl:string = "/domotic/api";
  public Temperature:string = "/temperature/";
  public Humidity:string = "/humidity/";
  public HeatIndex:string = "/heatindex/";
  public Station:string = "/station/";
  public Range:string = "/range/";
  public ForOneDay:string = "/0/";
  
  public ServerWithApiUrl = this.Server + this.BaseApiUrl;

  public display: Option[] = [
  { id: 1, name: 'hour' },
  { id: 2, name: '2 hours' },
  { id: 3, name: '4 hours' },
  { id: 4, name: '6 hours' },
  { id: 5, name: '12 hours' },
  { id: 6, name: '24 hours' },
  { id: 7, name: '1 week' }
  ];

  public initialState: DisplayOptions = {
    displayId: 1,
    stationId: 1,
    showTemp: true,
    showHumidity: false,
    showHeatIndex: false
  };

}