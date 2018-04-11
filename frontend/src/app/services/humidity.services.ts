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
  public GetStationHumidities = (station: string): Observable<Measurement[]> => {
    return this.GetData(this.humidityUrl + station);
  };
  public GetLatestStationHumidity = (station: string): Observable<Measurement> => {
    return this._http.get<Measurement>(this.humidityUrl + station + this.configuration.Latest);
  };
  public GetStationHumiditiesToday = (station: string): Observable<Measurement[]> => {
    return this.GetData(this.humidityUrl + station + this.configuration.Date + Utilities.getFormattedTodayDate());
  };
  public GetStationHumiditiesInLastXHours = (station: string, hours: number): Observable<Measurement[]> => {
    return this.GetData(this.humidityUrl + station + this.configuration.Range + Utilities.getFormattedHoursAgo(hours) + "/" + Utilities.getFormattedDateNow());
  };
  public GetStationHumiditiesInLastHour = (station: string): Observable<Measurement[]> => {
    return this.GetStationHumiditiesInLastXHours(station, 1)
  };
  public GetStationHumiditiesInLastDays = (station: string, days: number): Observable<Measurement[]> => {
    let hours = days * 24;
    return this.GetStationHumiditiesInLastXHours(station, hours)
  };
}
