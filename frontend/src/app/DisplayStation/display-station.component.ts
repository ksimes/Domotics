import {Component, Input, SimpleChange} from '@angular/core';
import {DataStationService} from '../services/station.services';
import {DataTemperatureService} from '../services/temperature.services';
import {DataHumidityService} from '../services/humidity.services';
import {DataHeatIndexService} from '../services/heatIndex.services';
import {Measurement} from '../models/Measurement';
import {Station} from '../models/Station';
import * as moment from 'moment';

@Component({
  selector: '[display-station]',
  providers: [DataStationService, DataHeatIndexService, DataTemperatureService, DataHumidityService],
  templateUrl: './display-station.component.html',
})

export class DisplayStationComponent {
  @Input('station')
  station: Station;

  temperature: Measurement = new Measurement(1, 1, 0.0, '2016');
  humidity: Measurement = new Measurement(1, 1, 0.0, '2016');
  heatIndex: Measurement = new Measurement(1, 1, 0.0, '2016');
  errorMsg: string = '';
  timeStamp: string = '';

  constructor(private _dataTemperatureService: DataTemperatureService,
              private _dataHumidityService: DataHumidityService,
              private _dataHeatIndexService: DataHeatIndexService) {
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    this.refresh(this.station);
  }

  public refresh(station: Station) {
    this.errorMsg = '';

    this.getLatestTemperature(station.id);
    this.getLatestHumidity(station.id);
    this.getLatestHeatIndex(station.id);
  }

  public details() {
    let station: Station = this.station;
    this.errorMsg = '';

    this.getLatestTemperature(station.id);
    this.getLatestHumidity(station.id);
    this.getLatestHeatIndex(station.id);
  }

  //...

  private getLatestTemperature(station: number): void {
    this._dataTemperatureService.GetLatestStationTemperature(station).subscribe((data: Measurement) => this.temperature = data,
      error => {
        console.log(error);
        this.errorMsg = error;
      },
      () => {
        this.timeStamp = DisplayStationComponent.processTimeStamp(this.temperature);
      }
    );
  }

  private getLatestHumidity(station: number): void {
    this._dataHumidityService.GetLatestStationHumidity(station).subscribe((data: Measurement) => this.humidity = data,
      error => {
        console.log(error);
        this.errorMsg = error;
      },
      () => {
      }
    );
  }

  private getLatestHeatIndex(station: number): void {
    this._dataHeatIndexService.GetLatestStationHeatIndex(station).subscribe((data: Measurement) => this.heatIndex = data,
      error => {
        console.log(error);
        this.errorMsg = error;
      },
      () => {
      }
    );
  }

  private static  processTimeStamp(data: Measurement): string {
    let year: number = +data.timestamp.substr(0, 4);
    let month: number = +data.timestamp.substr(4, 2);
    let day: number = +data.timestamp.substr(6, 2);
    let hour: number = +data.timestamp.substr(8, 2);
    let minute: number = +data.timestamp.substr(10, 2);
    let second: number = +data.timestamp.substr(12, 2);

    let date: Date = new Date(year, month - 1, day, hour, minute, second);

    console.log('date   ' + date);

    return moment(date).format('dddd, MMMM Do YYYY, h:mm:ss a')
  }

  private static showTime(timeStamp: string): string {
    return timeStamp.substr(8, 2) + ':' + timeStamp.substr(10, 2) + ':' + timeStamp.substr(12, 2);
  }
}
