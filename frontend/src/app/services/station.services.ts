import {Injectable} from '@angular/core';
import {Station} from '../models/Station';
import {Configuration} from '../models/configuration';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class DataStationService {

  private actionUrl: string;
  private configuration: Configuration;

  constructor(private _http: HttpClient, private _configuration: Configuration) {

    this.actionUrl = _configuration.ServerWithApiUrl;
    this.configuration = _configuration;
  }

  public getAllStations = (): Observable<Station[]> => {
    return this._http.get<Station[]>(this.actionUrl + this.configuration.Station);
  };

  public getStation = (station: string): Observable<Station> => {
    return this._http.get<Station>(this.actionUrl + this.configuration.Station + station);
  };
}
