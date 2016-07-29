import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {Station} from "../models/Station.ts";
import {Configuration} from "../configuration";

@Injectable()
export class DataStationService {

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

  public GetAllStations = ():Observable<Station[]> => {
    return this._http.get(this.actionUrl + this.configuration.Station)
        .map((response:Response) => <Station[]>response.json())
        .catch(this.handleError);
  };

  public GetStation = (station:number):Observable<Station> => {
    return this._http.get(this.actionUrl + this.configuration.Station + station)
        .map((response:Response) => <Station>response.json())
        .catch(this.handleError);
  };

  private handleError(error:any) {
    let errMsg = "No data available for this station";
    if(error.status != 416) {
      let errMsg = (error.message) ? error.message :
          error.status ? `${error.status} - ${error.statusText}` : 'Server error';
      console.log(' ' + error.status + ' : ' + errMsg);     // log to console
    }
    return Observable.throw(errMsg);
  }
}