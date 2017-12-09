import {Injectable} from "@angular/core";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {Measurement} from "../models/Measurement";
import {Configuration} from "../models/configuration";
import {Utilities} from "../services/utilities";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class DataHumidityService {
  private humidityUrl: string;
  private configuration: Configuration;

  constructor(private _http: HttpClient, private _configuration: Configuration) {

    this.configuration = _configuration;
    this.humidityUrl = _configuration.ServerWithApiUrl + _configuration.Humidity;
  }

  private GetData = (_url: string): Observable<Measurement[]> => {
    return this._http.get<Measurement[]>(_url);
  };

  public GetAllHumidities = (): Observable<Measurement[]> => {
    return this.GetData(this.humidityUrl);
  };
  public GetStationHumidities = (station: number): Observable<Measurement[]> => {
    return this.GetData(this.humidityUrl + station);
  };
  public GetLatestStationHumidity = (station: number): Observable<Measurement> => {
    return this._http.get<Measurement>(this.humidityUrl + station + this.configuration.Latest);
  };
  public GetStationHumiditiesToday = (station: number): Observable<Measurement[]> => {
    return this.GetData(this.humidityUrl + station + this.configuration.Date + Utilities.getFormattedTodayDate());
  };
  public GetStationHumiditiesInLastXHours = (station: number, hours: number): Observable<Measurement[]> => {
    return this.GetData(this.humidityUrl + station + this.configuration.Range + Utilities.getFormattedHoursAgo(hours) + "/" + Utilities.getFormattedDateNow());
  };
  public GetStationHumiditiesInLastHour = (station: number): Observable<Measurement[]> => {
    return this.GetStationHumiditiesInLastXHours(station, 1)
  };
  public GetStationHumiditiesInLastDays = (station: number, days: number): Observable<Measurement[]> => {
    let hours = days * 24;
    return this.GetStationHumiditiesInLastXHours(station, hours)
  };
}
