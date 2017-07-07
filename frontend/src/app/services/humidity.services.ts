import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {Measurement} from "../models/Measurement";
import {Configuration} from "../configuration";
import {Utilities} from "../utilities";

@Injectable()
export class DataHumidityService {

  private humidityUrl:string;
  private configuration:Configuration;
  private headers:Headers;

  constructor(private _http:Http, private _configuration:Configuration) {

    this.configuration = _configuration;
    this.humidityUrl = _configuration.ServerWithApiUrl +  _configuration.Humidity;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    this.headers.append('Accept', 'application/json');
  }

  public GetAllHumidities = ():Observable<Measurement[]> => {
    return this._http.get(this.humidityUrl, this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationHumidities = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.humidityUrl + station, this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetLatestStationHumidity = (station:number):Observable<Measurement> => {
    return this._http.get(this.humidityUrl + station + this.configuration.Latest, this.headers)
        .map((response:Response) => <Measurement>response.json())
        .catch(this.handleError);
  };

  public GetStationHumiditiesToday = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.humidityUrl + station + this.configuration.Date + Utilities.getFormattedTodayDate(), this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationHumiditiesInLastHour = (station:number):Observable<Measurement[]> => {
    return this.GetStationHumiditiesInLastXHours(station, 1)
  };

  public GetStationHumiditiesInLastXHours = (station:number, hours : number):Observable<Measurement[]> => {
    return this._http.get(this.humidityUrl + station + this.configuration.Range + Utilities.getFormattedHoursAgo(hours) + "/" + Utilities.getFormattedDateNow(), this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationHumiditiesInLastDays = (station:number, days : number):Observable<Measurement[]> => {
    let hours = days * 24;
    return this.GetStationHumiditiesInLastXHours(station, hours)
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
