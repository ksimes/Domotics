import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {Station} from "../models/Station.ts";
import {Configuration} from "./app.constants";

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

  private handleError(error:Response) {
    console.error(error);
    return Observable.throw(error.json().error || 'Server error');
  }
}