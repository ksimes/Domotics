import {Injectable} from '@angular/core';
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import 'rxjs/add/observable/throw';
import {Observable} from "rxjs/Observable";
import {Measurement} from "../models/Measurement";
import {Configuration} from "../models/configuration";
import {Utilities} from "../services/utilities";
import {HttpClient} from "@angular/common/http";


@Injectable()
export class DataTemperatureService {

  private temperatureUrl: string;
  private configuration: Configuration;

  constructor(private _http: HttpClient, private _configuration: Configuration) {
    this.configuration = _configuration;
    this.temperatureUrl = this.configuration.ServerWithApiUrl + this.configuration.Temperature;
  }

  private GetData = (_url: string): Observable<Measurement[]> => {
    return this._http.get<Measurement[]>(_url);
  };

  public GetAllTemperatures = (): Observable<Measurement[]> => {
    return this.GetData(this.temperatureUrl);
  };

  public GetStationTemperatures = (station: number): Observable<Measurement[]> => {
    return this.GetData(this.temperatureUrl + station);
  };

  public GetLatestStationTemperature = (station: number): Observable<Measurement> => {
    return this._http.get<Measurement>(this.temperatureUrl + station + this.configuration.Latest);
  };

  public GetStationTemperaturesToday = (station: number): Observable<Measurement[]> => {
    return this.GetData(this.temperatureUrl + station + this.configuration.Date + Utilities.getFormattedTodayDate());
  };

  public GetStationTemperaturesInLastHour = (station: number): Observable<Measurement[]> => {
    return this.GetStationTemperaturesInLastXHours(station, 1)
  };

  public GetStationTemperaturesInLastDays = (station: number, days: number): Observable<Measurement[]> => {
    let hours = days * 24;
    return this.GetStationTemperaturesInLastXHours(station, hours)
  };

  public GetStationTemperaturesInLastXHours = (station: number, hours: number): Observable<Measurement[]> => {
    return this.GetData(this.temperatureUrl + station + this.configuration.Range + Utilities.getFormattedHoursAgo(hours) + "/" + Utilities.getFormattedDateNow());
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
}
