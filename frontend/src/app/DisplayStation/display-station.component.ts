import {Component, Input, SimpleChange} from '@angular/core';
import {DataStationService} from '../services/station.services';
import {DataTemperatureService} from '../services/temperature.services';
import {DataHumidityService} from '../services/humidity.services';
import {DataHeatIndexService} from '../services/heatIndex.services';
import {Measurement} from '../models/Measurement';
import {Station} from '../models/Station';
import * as moment from 'moment';
import {Moment} from "moment";

@Component({
  selector: '[display-station]',
  providers: [DataStationService, DataHeatIndexService, DataTemperatureService, DataHumidityService],
  templateUrl: './display-station.component.html',
  styleUrls: ['./display-station.component.css'],
})

export class DisplayStationComponent {
  @Input('station')
  station: Station;

  temperature: Measurement = new Measurement('1', '1', 0.0, '2016');
  humidity: Measurement = new Measurement('1', '1', 0.0, '2016');
  heatIndex: Measurement = new Measurement('1', '1', 0.0, '2016');

  errorMsg: string = '';
  timeStamp: string = '';

  constructor(private _dataTemperatureService: DataTemperatureService,
              private _dataHumidityService: DataHumidityService,
              private _dataHeatIndexService: DataHeatIndexService) {
  }

  private static processTimeStamp(data: Measurement): string {

    let year: number = +data.timeStamp.substr(0, 4);
    let month: number = +data.timeStamp.substr(4, 2);
    let day: number = +data.timeStamp.substr(6, 2);
    let hour: number = +data.timeStamp.substr(8, 2);
    let minute: number = +data.timeStamp.substr(10, 2);
    let second: number = +data.timeStamp.substr(12, 2);

    let date: Date = new Date(year, month - 1, day, hour, minute, second);

    let timestamp: Moment = moment(date);
    let result: string;

    console.log('Difference:  ' + timestamp.diff(moment(), 'minutes'));

    if (Math.abs(timestamp.diff(moment(), 'minutes')) > 30) {
      result = 'More than 30 minutes ago';
    }
    else {
      result = moment(date).format('ddd, MMM Do YYYY, HH:mm:ss');
    }

    return result;
  }

  private static showTime(timeStamp: string): string {
    return timeStamp.substr(8, 2) + ':' + timeStamp.substr(10, 2) + ':' + timeStamp.substr(12, 2);
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    this.refresh(this.station);
  }

  //...

  public refresh(station: Station) {
    this.errorMsg = '';

    this.getLatestTemperature(station._key);
    this.getLatestHumidity(station._key);
    this.getLatestHeatIndex(station._key);
  }

  private getLatestTemperature(station: string): void {
    this._dataTemperatureService.GetLatestStationTemperature(station).subscribe((data: Measurement) => this.temperature = data,
      error => {
        this.errorMsg = this.handleError(error);
      },
      () => {
        this.timeStamp = DisplayStationComponent.processTimeStamp(this.temperature);
      }
    );
  }

  private getLatestHumidity(station: string): void {
    this._dataHumidityService.GetLatestStationHumidity(station).subscribe((data: Measurement) => this.humidity = data,
      error => {
        this.errorMsg = this.handleError(error);
      },
      () => {
      }
    );
  }

  private getLatestHeatIndex(station: string): void {
    this._dataHeatIndexService.GetLatestStationHeatIndex(station).subscribe((data: Measurement) => this.heatIndex = data,
      error => {
        this.errorMsg = this.handleError(error);
      },
      () => {
      }
    );
  }

  private handleError(error: any): string {
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
