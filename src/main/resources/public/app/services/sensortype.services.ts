import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {SensorType} from "../models/SensorType.ts";
import {Configuration} from "../configuration";

@Injectable()
export class DataSensorTypeService {

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

  public GetAllSensorType = ():Observable<SensorType[]> => {
    return this._http.get(this.actionUrl + this.configuration.SensorType, this.headers)
        .map((response:Response) => <SensorType[]>response.json())
        .catch(this.handleError);
  };

  public GetSensorType = (sensorType:number):Observable<SensorType> => {
    return this._http.get(this.actionUrl + this.configuration.SensorType + sensorType, this.headers)
        .map((response:Response) => <SensorType>response.json())
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