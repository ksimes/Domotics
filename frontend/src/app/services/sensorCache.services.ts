import {Injectable} from '@angular/core';
import {Configuration} from '../models/configuration';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from "@angular/common/http";
import {SensorCache} from "../models/sensorCache";

@Injectable()
export class SensorCacheService {

  private actionUrl: string;
  private configuration: Configuration;

  constructor(private _http: HttpClient, private _configuration: Configuration) {

    this.actionUrl = _configuration.ServerWithApiUrl;
    this.configuration = _configuration;
  }

  public getSensorCache = (): Observable<SensorCache[]> => {
    return this._http.get<SensorCache[]>(this.actionUrl + this.configuration.SensorCache);
  };
}
