import {Injectable} from '@angular/core';
import {DataTemperatureService} from "./temperature.services";
import {DataHumidityService} from "./humidity.services";
import {DataHeatIndexService} from "./heatIndex.services";
import {Measurement} from "../models/Measurement";
import * as moment from 'moment';
import {Moment} from "moment";
import {StationDisplay} from "../models/station-display";
import {Station} from "../models/Station";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/observable/forkJoin';
import {Subject} from "rxjs/Subject";

@Injectable()
export class DisplayStationService {

  constructor(private _dataTemperatureService: DataTemperatureService,
              private _dataHumidityService: DataHumidityService,
              private _dataHeatIndexService: DataHeatIndexService) {
  }

  public getStationData(station: Station): Observable<StationDisplay> {
    const _returnValue: Subject<StationDisplay> = new Subject();
    let result: StationDisplay = new StationDisplay(station);
    const stationKey: string = station._key;
    let readings = [];

    readings.push(this._dataTemperatureService.GetLatestStationTemperature(stationKey));
    readings.push(this._dataHumidityService.GetLatestStationHumidity(stationKey));
    readings.push(this._dataHeatIndexService.GetLatestStationHeatIndex(stationKey));

    Observable.forkJoin(readings).subscribe(([temperature, humidity, heatIndex]) => {

      result.temperature = '-';
      result.humidity = '-';
      result.heatIndex = '-';

      if (temperature) {
        let temp = (temperature as Measurement);
        result.timeStamp = DisplayStationService.processTimeStamp(temp);
        if (!result.timeStamp.startsWith('More')) {
          result.temperature = `${temp.value} \u00B0C`;
          result.humidity = `${(humidity as Measurement).value}%`;
          result.heatIndex = `${(heatIndex as Measurement).value}`;
        }
      }
      _returnValue.next(result);
      _returnValue.complete();
    });

    return _returnValue.asObservable();
  }

  private static processTimeStamp(data: Measurement): string {

    const year: number = +data.timeStamp.substr(0, 4);
    const month: number = +data.timeStamp.substr(4, 2);
    const day: number = +data.timeStamp.substr(6, 2);
    const hour: number = +data.timeStamp.substr(8, 2);
    const minute: number = +data.timeStamp.substr(10, 2);
    const second: number = +data.timeStamp.substr(12, 2);

    const date: Date = new Date(year, month - 1, day, hour, minute, second);

    const timestamp: Moment = moment(date);
    let result: string;

    // console.log('Difference:  ' + timestamp.diff(moment(), 'minutes'));

    if (Math.abs(timestamp.diff(moment(), 'minutes')) > 30) {
      result = 'More than 30 minutes ago';
    }
    else {
      result = moment(date).format('ddd, MMM Do YYYY, HH:mm:ss');
    }

    return result;
  }

  private static handleError(error: any):
    string {
    let errMsg = "No data available for this period";
    if (error.status != 416) {
      let errMsg = (error.message) ? error.message :
        error.status ? `${error.status} - ${error.statusText}` : 'Server error';
      console.log(' ' + error.status + ' : ' + errMsg);     // log to console
    } else {
      console.log(error);
    }

    return errMsg;
  }
}
