import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {Observable} from "rxjs/Observable";
import {Measurement} from "../models/Measurement.ts";
import {Configuration} from "./app.constants";
import {Utilities} from "./app.utilities";

@Injectable()
export class DataTemperatureService {

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

  public GetAllTemperatures = ():Observable<Measurement[]> => {
    return this._http.get(this.actionUrl + this.configuration.Temperature, this.headers)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationTemperatures = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.actionUrl + this.configuration.Temperature + station)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  public GetStationTemperaturesToday = (station:number):Observable<Measurement[]> => {
    return this._http.get(this.actionUrl + this.configuration.Temperature + station + this.configuration.Range + Utilities.getFormattedTodayDate() + this.configuration.ForOneDay)
        .map((response:Response) => <Measurement[]>response.json())
        .catch(this.handleError);
  };

  /**
   public GetSingle = (id: number): Observable<MyTypedItem> => {
        return this._http.get(this.actionUrl + id)
            .map((response: Response) => <MyTypedItem>response.json())
            .error(this.handleError);
    }

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

  private handleError(error:Response) {
    console.error(error);
    return Observable.throw(error.json().error || 'Server error');
  }
}