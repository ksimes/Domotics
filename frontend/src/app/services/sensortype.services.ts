import {Injectable} from "@angular/core";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {SensorType} from "../models/SensorType";
import {Configuration} from "../models/configuration";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class DataSensorTypeService {

  private actionUrl:string;
  private configuration:Configuration;

  constructor(private _http: HttpClient, private _configuration: Configuration) {

    this.actionUrl = _configuration.ServerWithApiUrl;
    this.configuration = _configuration;
  }

  public GetAllSensorTypes = ():Observable<SensorType[]> => {
    return this._http.get<SensorType[]>(this.actionUrl + this.configuration.SensorType);
  };

  public GetSensorType = (sensorType:number):Observable<SensorType> => {
    return this._http.get<SensorType>(this.actionUrl + this.configuration.SensorType + sensorType);
  };
}
