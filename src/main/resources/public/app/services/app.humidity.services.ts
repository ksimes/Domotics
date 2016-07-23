import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {Measurement} from "../../models/Measurement.ts";
import {Configuration} from "../app.constants";
import {Utilities} from "../app.utilities";

@Injectable()
export class DataHumidityService {

  private actionUrl:string;
  private configuration:Configuration;
  private headers:Headers;

  constructor(private _http:Http, private _configuration:Configuration) {

    this.actionUrl = _configuration.ServerWithApiUrl;
    this.configuration = _configuration;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    this.headers.append('Accept', 'application/json');
  }

  public GetAllHumidities = ():Observable<Measurement[]> => {
    return this._http.get(this.actionUrl + this.configuration.Humidity)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationHumidities = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.actionUrl + this.configuration.Humidity + station)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationHumiditiesToday = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.actionUrl + this.configuration.Humidity + station + this.configuration.Range + Utilities.getFormattedTodayDate() + this.configuration.ForOneDay)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  private handleError(error:Response) {
    console.error(error);
    return Observable.throw(error.json().error || 'Server error');
  }
}