import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {Measurement} from "../models/Measurement.ts";
import {Configuration} from "../configuration";
import {Utilities} from "../utilities";

@Injectable()
export class DataHeatIndexService {

  private heatIndexUrl:string;
  private configuration:Configuration;
  private headers:Headers;

  constructor(private _http:Http, private _configuration:Configuration) {

    this.configuration = _configuration;
    this.heatIndexUrl = _configuration.ServerWithApiUrl +  _configuration.HeatIndex;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    this.headers.append('Accept', 'application/json');
  }

  public GetAllHeatIndices = ():Observable<Measurement[]> => {
    return this._http.get(this.heatIndexUrl, this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationHeatIndices = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.heatIndexUrl + station, this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetLatestStationHeatIndex = (station:number):Observable<Measurement> => {
    return this._http.get(this.heatIndexUrl + station + this.configuration.Latest, this.headers)
        .map((response:Response) => <Measurement>response.json())
        .catch(this.handleError);
  };

  public GetStationHeatIndicesToday = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.heatIndexUrl + station + this.configuration.Date + Utilities.getFormattedTodayDate(), this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationHeatIndicesInLastHour = (station:number):Observable<Measurement[]> => {
    return this.GetStationHeatIndicesInLastXHours(station, 1)
  };

  public GetStationHeatIndicesInLastXHours = (station:number, hours : number):Observable<Measurement[]> => {
    return this._http.get(this.heatIndexUrl + station + this.configuration.Range + Utilities.getFormattedHoursAgo(hours) + "/" + Utilities.getFormattedDateNow(), this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationHeatIndicesInLastDays = (station:number, days : number):Observable<Measurement[]> => {
    let hours = days * 24;
    return this.GetStationHeatIndicesInLastXHours(station, hours)
  };

  private handleError(error:any) {
    let errMsg = "No data available for this period";
    if(error.status != 416) {
      let errMsg = (error.message) ? error.message :
          error.status ? `${error.status} - ${error.statusText}` : 'Server error';
      console.log(' ' + error.status + ' : ' + errMsg);     // log to console
    }
    return Observable.throw(errMsg);
  }
}