import {Injectable} from "@angular/core";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {Measurement} from "../models/Measurement";
import {Configuration} from "../models/configuration";
import {Utilities} from "../services/utilities";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class DataHeatIndexService {

  private heatIndexUrl:string;
  private configuration:Configuration;

  constructor(private _http: HttpClient, private _configuration: Configuration) {

    this.configuration = _configuration;
    this.heatIndexUrl = _configuration.ServerWithApiUrl +  _configuration.HeatIndex;
  }

  private GetData = (_url: string): Observable<Measurement[]> => {
    return this._http.get<Measurement[]>(_url);
  };

  public GetAllHeatIndices = ():Observable<Measurement[]> => {
    return this.GetData(this.heatIndexUrl);
  };

  public GetStationHeatIndices = (station:number):Observable<Measurement[]> => {
    return this.GetData(this.heatIndexUrl + station);
  };

  public GetLatestStationHeatIndex = (station:number):Observable<Measurement> => {
    return this._http.get<Measurement>(this.heatIndexUrl + station + this.configuration.Latest);
  };

  public GetStationHeatIndicesToday = (station:number):Observable<Measurement[]> => {
    return this.GetData(this.heatIndexUrl + station + this.configuration.Date + Utilities.getFormattedTodayDate());
  };

  public GetStationHeatIndicesInLastHour = (station:number):Observable<Measurement[]> => {
    return this.GetStationHeatIndicesInLastXHours(station, 1)
  };

  public GetStationHeatIndicesInLastXHours = (station:number, hours : number):Observable<Measurement[]> => {
    return this._http.get<Measurement[]>(this.heatIndexUrl + station + this.configuration.Range + Utilities.getFormattedHoursAgo(hours) + "/" + Utilities.getFormattedDateNow());
  };

  public GetStationHeatIndicesInLastDays = (station:number, days : number):Observable<Measurement[]> => {
    let hours = days * 24;
    return this.GetStationHeatIndicesInLastXHours(station, hours)
  };
}
