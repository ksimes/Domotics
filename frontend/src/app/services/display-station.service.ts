import {Injectable} from '@angular/core';
import {DataTemperatureService} from "./temperature.services";
import {DataHumidityService} from "./humidity.services";
import {DataHeatIndexService} from "./heatIndex.services";
import {Measurement} from "../models/Measurement";
import * as moment from 'moment';
import {Moment} from "moment";
import 'rxjs/add/observable/forkJoin';

@Injectable()
export class DisplayStationService {

  constructor(private _dataTemperatureService: DataTemperatureService,
              private _dataHumidityService: DataHumidityService,
              private _dataHeatIndexService: DataHeatIndexService) {
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
