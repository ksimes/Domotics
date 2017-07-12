import { Injectable } from '@angular/core';
import {Http, Response, Headers} from "@angular/http";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import 'rxjs/add/observable/throw';
import {Observable} from "rxjs/Observable";
import {Measurement} from "../models/Measurement";
import {Configuration} from "../models/configuration";
import {Utilities} from "../services/utilities";

@Injectable()
export class DataTemperatureService {

  private temperatureUrl:string;
  private configuration:Configuration;
  private headers:Headers;

  constructor(private _http:Http, private _configuration:Configuration) {
    this.configuration = _configuration;
    this.temperatureUrl = this.configuration.ServerWithApiUrl + this.configuration.Temperature;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    this.headers.append('Accept', 'application/json');
  }

  public GetAllTemperatures = ():Observable<Measurement[]> => {
    return this._http.get(this.temperatureUrl, this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationTemperatures = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.temperatureUrl + station, this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetLatestStationTemperature = (station:number):Observable<Measurement> => {
    return this._http.get(this.temperatureUrl + station + this.configuration.Latest, this.headers)
        .map((response:Response) => <Measurement>response.json())
        .catch(this.handleError);
  };

  public GetStationTemperaturesToday = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.temperatureUrl + station + this.configuration.Date + Utilities.getFormattedTodayDate(), this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationTemperaturesInLastHour = (station:number):Observable<Measurement[]> => {
    return this.GetStationTemperaturesInLastXHours(station, 1)
  };

  public GetStationTemperaturesInLastXHours = (station:number, hours : number):Observable<Measurement[]> => {
    return this._http.get(this.temperatureUrl + station + this.configuration.Range + Utilities.getFormattedHoursAgo(hours) + "/" + Utilities.getFormattedDateNow(), this.headers)
        .map((response:Response) => <Measurement[]>response.json() )
        .catch(this.handleError);
  };

  public GetStationTemperaturesInLastDays = (station:number, days : number):Observable<Measurement[]> => {
    let hours = days * 24;
    return this.GetStationTemperaturesInLastXHours(station, hours)
  };

  /**

   public Add = (itemName: string): Observable<MyTypedItem> => {
        let toAdd = JSON.stringify({ ItemName: itemName });

        return this._http.post(this.actionUrl, toAdd, { headers: this.headers })
            .map((response: Response) => <MyTypedItem>response.json())
            .error(this.handleError);
    }

   public Update = (id: number, itemToUpdate: MyTypedItem): Observable<MyTypedItem> => {
        return this._http.put(this.actionUrl + id, JSON.stringify(itemToUpdate), { headers: this.headers })
            .map((response: Response) => <MyTypedItem>response.json())
            .error(this.handleError);
    }

   public Delete = (id: number): Observable<Response> => {
        return this._http.delete(this.actionUrl + id)
            .error(this.handleError);
    }
   **/

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
