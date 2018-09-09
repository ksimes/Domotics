import {Injectable} from '@angular/core';
import {DataTemperatureService} from "./temperature.services";
import {DataHumidityService} from "./humidity.services";
import {DataHeatIndexService} from "./heatIndex.services";
import * as moment from 'moment';
import {Moment} from "moment";
import 'rxjs/add/observable/forkJoin';

@Injectable()
export class DisplayStationService {
  public static outOfDateMsg = 'More than 30 minutes';

  constructor(private _dataTemperatureService: DataTemperatureService,
              private _dataHumidityService: DataHumidityService,
              private _dataHeatIndexService: DataHeatIndexService) {
  }

  public static processTimeStamp(timeStamp: string): string {

    const year: number = +timeStamp.substr(0, 4);
    const month: number = +timeStamp.substr(5, 2);
    const day: number = +timeStamp.substr(8, 2);
    const hour: number = +timeStamp.substr(11, 2);
    const minute: number = +timeStamp.substr(14, 2);
    const second: number = +timeStamp.substr(17, 2);

    const date: Date = new Date(year, month - 1, day, hour, minute, second);

    const timestamp: Moment = moment(date);
    let result: string;

    // console.log('Difference:  ' + timestamp.diff(moment(), 'minutes'));

    if (Math.abs(timestamp.diff(moment(), 'minutes')) > 30) {
      result = this.outOfDateMsg;
    }
    else {
      const seconds = Math.abs(timestamp.diff(moment(), 'seconds'));
      if (seconds > 60) {
        result = Math.trunc(seconds / 60) + ' min ' + seconds % 60 + ' secs at ';
      } else {
        result = seconds + ' seconds at ';
      }
      // result += timestamp.format('ddd, MMM Do YYYY, HH:mm:ss');
      result += timestamp.format('HH:mm:ss');
    }

    return result;
  }

  public static handleError(error: any): string {
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

  public static getFormattedDate(): string {
    let date: Date = new Date();
    // console.log('date   ' + date);
    return moment(date).format('dddd, MMMM Do YYYY, HH:mm:ss')
  }
}
