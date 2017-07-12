import {Injectable} from '@angular/core';
import {Headers} from '@angular/http';
import {Http} from '@angular/http';
import {Station} from '../models/Station';
import {Configuration} from '../models/configuration';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class DataStationService {

  private actionUrl: string;
  private configuration: Configuration;
  private headers: Headers;

  constructor(private http: Http, private _configuration: Configuration) {

    this.actionUrl = _configuration.ServerWithApiUrl;
    this.configuration = _configuration;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    this.headers.append('Accept', 'application/json');
  }

  public getAllStations = (): Observable<Station[]> => {
    return this.http.get(this.actionUrl + this.configuration.Station, this.headers)
      .map(response => <Station[]>response.json())
      .catch(this.handleError);
  };

  public getStation = (station: number): Observable<Station> => {
    return this.http.get(this.actionUrl + this.configuration.Station + station, this.headers)
      .map(response => <Station>response.json())
      .catch(this.handleError);
  };

  private handleError(error: any): Promise<any> {
    let errMsg = 'No data available for this station';
    if (error.status != 416) {
      let errMsg = (error.message) ? error.message :
        error.status ? `${error.status} - ${error.statusText}` : 'Server error';
      console.log(' ' + error.status + ' : ' + errMsg);     // log to console
    }
    return Promise.reject(errMsg);
  }
}
