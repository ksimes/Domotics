import {Injectable} from '@angular/core';

@Injectable()
export class Configuration {
  public Server:string = "http://localhost:8080";
  public BaseApiUrl:string = "/domotic/api";
  public Temperature:string = "/temperature/";
  public Humidity:string = "/humidity/";
  public HeatIndex:string = "/heatindex/";
  public Station:string = "/station/";
  public Range:string = "/range/";
  public ForOneDay:string = "/0/";
  
  public ServerWithApiUrl = this.Server + this.BaseApiUrl;
}