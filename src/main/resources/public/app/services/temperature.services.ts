import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {Measurement} from "../models/Measurement.ts";
import {Configuration} from "../configuration";
import {Utilities} from "../utilities";

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
    return this._http.get(this.temperatureUrl + station)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationTemperaturesToday = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.temperatureUrl + station + this.configuration.Range + Utilities.getFormattedTodayDate() + this.configuration.ForOneDay)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationTemperaturesInLastHour = (station:number):Observable<Measurement[]> => {
    return this.GetStationTemperaturesInLastXHours(station, 1)
  };

  public GetStationTemperaturesInLastXHours = (station:number, hours : number):Observable<Measurement[]> => {
    return this._http.get(this.temperatureUrl + station + this.configuration.Range + Utilities.getFormattedHoursAgo(hours) + "/" + Utilities.getFormattedDateNow())
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationTemperaturesInLastDays = (station:number, days : number):Observable<Measurement[]> => {
    let hours = days * 24;
    return this.GetStationTemperaturesInLastXHours(station, hours)
  };

  /**
   public GetSingle = (id: number): Observable<MyTypedItem> => {
        return this._http.get(this.actionUrl + id)
            .map((response: Response) => <MyTypedItem>response.json())
            .error(this.handleError);
    }

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

  private handleError(error:Response) {
    console.log(error);
    return Observable.throw(error.json().error || 'Server error');
  }
}